package net.ddns.zimportant.lovingpeople.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.ddns.zimportant.lovingpeople.R;
import net.ddns.zimportant.lovingpeople.service.common.model.ChatRoom;
import net.ddns.zimportant.lovingpeople.service.common.model.Request;
import net.ddns.zimportant.lovingpeople.service.common.model.UserChat;
import net.ddns.zimportant.lovingpeople.service.helper.UserViewLoader;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.disposables.Disposable;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.SyncUser;

import static net.ddns.zimportant.lovingpeople.service.Constant.COUNSELOR_ID;
import static net.ddns.zimportant.lovingpeople.service.Constant.ERR_USER_CANCEL;
import static net.ddns.zimportant.lovingpeople.service.Constant.ERR_USER_NOT_AVAILABLE;
import static net.ddns.zimportant.lovingpeople.service.Constant.ERR_USER_STOP_REQUEST;
import static net.ddns.zimportant.lovingpeople.service.Constant.PARTNER;
import static net.ddns.zimportant.lovingpeople.service.Constant.REQUEST;
import static net.ddns.zimportant.lovingpeople.service.Constant.STORYTELLER_ID;
import static net.ddns.zimportant.lovingpeople.service.Constant.TIMEOUT;
import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.COUNSELOR;
import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.STORYTELLER;
import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.USER_BUSY;

public class ResponseActivity extends AppCompatActivity {

	@BindView(R.id.civ_avatar)
	CircleImageView avatar;
	@BindView(R.id.tv_name)
	TextView nameTextView;
	@BindView(R.id.bt_accept)
	Button acceptButton;
	@BindView(R.id.bt_deny)
	Button denyButton;
	@BindView(R.id.tv_timeout_second)
	TextView timeout;

	private Realm realm;
	private String partnerId;
	private String requestId;
	private String userId;
	private UserChat partner, user;
	private Request request;
	private Disposable checkPartner, checkChatRoom;
	private CountDownTimer timer;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setUpLayout();
		setUpRealm();
		prepareRealmData();
		setUpTimeout();
		setUpButton();
	}

	private void setUpLayout() {
		setContentView(R.layout.activity_response_conversation);
		ButterKnife.bind(this);
	}

	private void setUpRealm() {
		realm = Realm.getDefaultInstance();
	}

	private void prepareRealmData() {
		prepareRequest();
		prepareUser();
		preparePartner();
	}

	private void prepareRequest() {
		requestId = getIntent().getExtras().getString(REQUEST);
		request = realm
				.where(Request.class)
				.equalTo("id", requestId)
				.findFirst();
	}

	private void prepareUser() {
		userId = request.getPartnerId();
		user = realm
				.where(UserChat.class)
				.equalTo("id", userId)
				.findFirst();
	}

	private void preparePartner() {
		partnerId = request.getUserId();
		partner = realm
				.where(UserChat.class)
				.equalTo("id", partnerId)
				.findFirst();
		RealmResults<UserChat> partnerRealmResults = realm
				.where(UserChat.class)
				.equalTo("id", partnerId)
				.findAllAsync();
		UserViewLoader userViewLoader = new UserViewLoader
				.Builder(partnerRealmResults)
				.setAvatarView(avatar)
				.setNameView(nameTextView)
				.build();
		userViewLoader.startListening();
	}

	private void setUpTimeout() {
		long diff = Calendar.getInstance().getTimeInMillis() - request.getCreateAt().getTime();
		if (diff/1000 <= TIMEOUT) {
			long timeLeft = TIMEOUT - diff/1000;
			timeout.setText(String.valueOf(timeLeft));

			timer = new CountDownTimer(timeLeft*1000, 1000) {
				@Override
				public void onTick(long millisUntilFinished) {
					timeout.setText(String.valueOf(millisUntilFinished/1000).concat("s"));
				}

				@Override
				public void onFinish() {
					cancelRequest(ERR_USER_CANCEL);
				}
			}.start();
		} else {
			cancelRequest(ERR_USER_CANCEL);
		}
	}

	private void cancelRequest(@Nullable String error) {
		if (timer != null) {
			timer.cancel();
		}
		if (request.isValid()) {
			realm.executeTransaction(bgRealm -> {
				request.setExpired(true);
			});
		}
		Intent intent = new Intent();
		intent.putExtra("error", error);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	private void setUpButton() {
		acceptButton.setOnClickListener((v) -> acceptRequest());
		denyButton.setOnClickListener((v) -> denyRequest());
	}

	private void acceptRequest() {
		if (timer != null) {
			timer.cancel();
		}
		acceptButton.setVisibility(View.GONE);
		denyButton.setVisibility(View.GONE);
		setUpChatRoom();
	}

	private void denyRequest() {
		acceptButton.setVisibility(View.GONE);
		denyButton.setVisibility(View.GONE);
		cancelRequest(ERR_USER_CANCEL);
	}

	private void setUpChatRoom() {
		checkChatRoom = realm
				.where(ChatRoom.class)
				.equalTo(COUNSELOR_ID, getCounselorId())
				.and()
				.equalTo(STORYTELLER_ID, getStorytellerId())
				.findAllAsync()
				.asFlowable()
				.filter(RealmResults::isLoaded)
				.subscribe(realmResults -> {
					ChatRoom chatRoom = null;
					if (realmResults.size() != 0) {
						chatRoom = realmResults.first();
					}

					if (chatRoom != null) {
						checkChatRoom.dispose();
						responseChatRoom(chatRoom.getId());
					} else {
						createChatRoom();
					}
				});
	}

	private void responseChatRoom(String chatRoomId) {
		realm.executeTransaction(bgRealm -> {
			request.setConnectedRoom(chatRoomId);
			request.setExpired(true);
			user.setConnectedRoom(chatRoomId);
		});

		checkPartner = realm
				.where(UserChat.class)
				.equalTo("id", partnerId)
				.findAllAsync()
				.asFlowable()
				.filter(RealmResults::isLoaded)
				.subscribe(realmResults -> {
					partner = realmResults.first();
					if (partner.getConnectedRoom().equals(chatRoomId)) {
						finishActivitySuccess();
						checkPartner.dispose();
					}
				});
	}

	private void createChatRoom() {
		realm.executeTransaction(bgRealm -> {
			bgRealm.insert(new ChatRoom(getStorytellerId(), getCounselorId()));
		});
	}

	private void finishActivitySuccess() {
		if (timer != null) {
			timer.cancel();
		}
		Intent intent = new Intent();
		intent.putExtra(COUNSELOR_ID, getCounselorId());
		intent.putExtra(STORYTELLER_ID, getStorytellerId());
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	private String getStorytellerId() {
		if (partner.getCurrentUserType().equals(STORYTELLER)) {
			return partnerId;
		} else {
			return userId;
		}
	}

	private String getCounselorId() {
		if (partner.getCurrentUserType().equals(COUNSELOR)) {
			return partnerId;
		} else {
			return userId;
		}
	}

	@Override
	public void onBackPressed() {
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		realm.close();
	}
}
