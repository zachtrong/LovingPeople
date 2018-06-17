package net.ddns.zimportant.lovingpeople.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.ddns.zimportant.lovingpeople.R;
import net.ddns.zimportant.lovingpeople.service.common.model.ChatRoom;
import net.ddns.zimportant.lovingpeople.service.common.model.UserChat;
import net.ddns.zimportant.lovingpeople.service.helper.UserViewLoader;

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
import static net.ddns.zimportant.lovingpeople.service.Constant.STORYTELLER_ID;
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

	private Realm realm;
	private String partnerId;
	private String userId;
	private UserChat partner, user;
	private Disposable checkPartner, checkChatRoom;
	private boolean isLoadedView = false;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setUpLayout();
		setUpRealm();
		prepareRealmData();
	}

	private void setUpLayout() {
		setContentView(R.layout.activity_response_conversation);
		ButterKnife.bind(this);
	}

	private void setUpRealm() {
		realm = Realm.getDefaultInstance();
	}

	private void prepareRealmData() {
		prepareCurrent();
		preparePartner();
	}

	private void prepareCurrent() {
		userId = SyncUser.current().getIdentity();
		user = realm
				.where(UserChat.class)
				.equalTo("id", userId)
				.findFirst();
	}

	private void preparePartner() {
		partnerId = getIntent().getExtras().getString(PARTNER);
		setUpPartnerView();
		setUpCheckPartner();
	}

	private void setUpPartnerView() {
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

	private void setUpCheckPartner() {
		checkPartner = realm
				.where(UserChat.class)
				.equalTo("id", partnerId)
				.findAllAsync()
				.asFlowable()
				.filter(RealmResults::isLoaded)
				.subscribe(realmResults -> {
					partner = realmResults.first();
					checkPartnerAvailability();

					if (!isLoadedView) {
						isLoadedView = true;
						setUpButtons();
					}
				});
	}

	private void checkPartnerAvailability() {
		if (partner == null) {
			cancelRequest(ERR_USER_NOT_AVAILABLE);
		} else if (!partner.getUserRequestId().equals(userId)) {
			cancelRequest(ERR_USER_STOP_REQUEST);
		}
	}

	private void setUpButtons() {
		acceptButton.setOnClickListener(v -> acceptRequest());
		denyButton.setOnClickListener(v -> denyRequest());
	}

	private void acceptRequest() {
		acceptButton.setOnClickListener(v -> {});
		denyButton.setVisibility(View.GONE);
		setUpChatRoom();
	}

	private void denyRequest() {
		denyButton.setOnClickListener(v -> {});
		acceptButton.setVisibility(View.GONE);
		cancelRequest(ERR_USER_CANCEL);
	}

	private void cancelRequest(@Nullable String error) {
		if (!checkPartner.isDisposed()) {
			checkPartner.dispose();
		}
		realm.executeTransaction(bgRealm -> {
			user.setUserRequestId("");
			partner.setUserRequestId("");
		});
		Intent intent = new Intent();
		intent.putExtra("error", error);
		setResult(Activity.RESULT_OK, intent);
		finish();
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
						responseChatRoom(chatRoom.getId());
					} else {
						createChatRoom();
					}
				});
	}

	private void responseChatRoom(String chatRoomId) {
		checkChatRoom.dispose();
		if (!checkPartner.isDisposed()) {
			checkPartner.dispose();
		}

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
		realm.executeTransaction(bgRealm -> {
			user.setStatus(USER_BUSY);
			user.setConnectedRoom(chatRoomId);
		});
	}

	private void finishActivitySuccess() {
		realm.executeTransaction(bgRealm -> user.setStatus(USER_BUSY));
		Intent intent = new Intent();
		intent.putExtra(COUNSELOR_ID, getCounselorId());
		intent.putExtra(STORYTELLER_ID, getStorytellerId());
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	private void createChatRoom() {
		checkPartner.dispose();
		realm.executeTransaction(bgRealm -> {
			bgRealm.insert(new ChatRoom(getStorytellerId(), getCounselorId()));
		});
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
	protected void onDestroy() {
		super.onDestroy();
		realm.close();
	}
}
