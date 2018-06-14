package net.ddns.zimportant.lovingpeople.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.ddns.zimportant.lovingpeople.R;
import net.ddns.zimportant.lovingpeople.service.common.model.UserChat;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.disposables.Disposable;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.SyncUser;

import static net.ddns.zimportant.lovingpeople.service.Constant.ERR_USER_CANCEL;
import static net.ddns.zimportant.lovingpeople.service.Constant.ERR_USER_NOT_REQUEST_MORE;
import static net.ddns.zimportant.lovingpeople.service.Constant.ERR_USER_CHAT_OTHER;
import static net.ddns.zimportant.lovingpeople.service.Constant.ERR_USER_NOT_AVAILABLE;
import static net.ddns.zimportant.lovingpeople.service.Constant.PARTNER;
import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.USER_BUSY;
import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.USER_ONLINE;

public class RequestActivity extends AppCompatActivity {

	@BindView(R.id.bt_cancel)
	Button buttonCancel;
	@BindView(R.id.tv_name)
	TextView nameTextView;
	@BindView(R.id.civ_avatar)
	CircleImageView avatar;

	private String partnerId;
	private String userId;
	private Disposable checkPartner, responsePartner;
	private UserChat partner, user;
	private Realm realm;
	private boolean isLoadedView = false;

	// TODO: user request then offline :)

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_conversation);
		ButterKnife.bind(this);
		setUpRealm();
		prepareRealmData();
	}

	private void setUpRealm() {
		realm = Realm.getDefaultInstance();
	}

	private void prepareRealmData() {
		prepareUser();
		preparePartner();
	}

	private void prepareUser() {
		userId = SyncUser.current().getIdentity();
		user = realm
				.where(UserChat.class)
				.equalTo("id", userId)
				.findFirst();
	}

	private void preparePartner() {
		partnerId = getIntent().getExtras().getString(PARTNER);
		checkPartner = realm
				.where(UserChat.class)
				.equalTo("id", partnerId)
				.findAllAsync()
				.asFlowable()
				.filter(RealmResults::isLoaded)
				.subscribe(realmResults -> {
					partner = realmResults.first();
					checkPartnerAvailable();
					if (!isLoadedView) {
						isLoadedView = true;
						setUpView();
						setUpRequestPartner();
					}
				});
	}

	private void setUpView() {
		setUpButton();
		setUpUserView();
	}

	private void setUpButton() {
		buttonCancel.setOnClickListener(v -> {
			finishActivityError(ERR_USER_CANCEL);
		});
	}

	private void setUpUserView() {
		Picasso.get()
				.load(partner.getAvatarUrl())
				.into(avatar);
		nameTextView.setText(partner.getName());
	}

	private void setUpRequestPartner() {
		startRequestPartner();
		listenChangePartner();
	}

	private void checkPartnerAvailable() {
		if (partner.getConnectedRoom().length() != 0) {
			finishActivityError(ERR_USER_CHAT_OTHER);
		} else if (!partner.getStatus().equals(USER_ONLINE)) {
			finishActivityError(ERR_USER_NOT_AVAILABLE);
		} else if (partner.getUserRequestId().length() != 0
				&& !partner.getUserRequestId().equals(userId)) {
			finishActivityError(ERR_USER_NOT_AVAILABLE);
		} else if (user.getUserRequestId().length() != 0
				&& !user.getUserRequestId().equals(partnerId)) {
			finishActivityError(ERR_USER_NOT_REQUEST_MORE);
		} else if (user.getConnectedRoom().length() != 0) {
			finishActivityError(ERR_USER_NOT_REQUEST_MORE);
		}
	}

	private void finishActivityError(@Nullable String error) {
		clearListener();
		clearRequestError();
		Intent intent = new Intent();
		intent.putExtra("error", error);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	private void clearListener() {
		if (checkPartner != null && !checkPartner.isDisposed()) {
			checkPartner.dispose();
		}
		if (responsePartner != null && !responsePartner.isDisposed()) {
			responsePartner.dispose();
		}
	}

	private void clearRequestError() {
		realm.executeTransaction(bgRealm -> {
			user.setUserRequestId("");
			user.setStatus(USER_ONLINE);
			partner.setUserRequestId("");
		});
	}

	private void clearRequestSuccess() {
		realm.executeTransaction(bgRealm -> {
			user.setUserRequestId("");
			partner.setUserRequestId("");
		});
	}

	private void startRequestPartner() {
		realm.executeTransaction(bgRealm -> {
			user.setStatus(USER_BUSY);
			user.setUserRequestId(partner.getId());
			partner.setUserRequestId(user.getId());
		});
	}

	private void listenChangePartner() {
		responsePartner = realm
				.where(UserChat.class)
				.equalTo("id", partnerId)
				.findAllAsync()
				.asFlowable()
				.filter(RealmResults::isLoaded)
				.subscribe(realmResults -> {
					partner = realmResults.first();

					if (partner.getUserRequestId().equals(userId)) {
						String chatRoomId = partner.getConnectedRoom();
						if (chatRoomId.length() != 0) {
							clearListener();
							connectUserWithChatRoom(partner.getConnectedRoom());
						}
					} else {
						finishActivityError(ERR_USER_NOT_AVAILABLE);
					}
				});
	}

	private void connectUserWithChatRoom(String chatRoomId) {
		realm.executeTransactionAsync(bgRealm -> {
			user.setConnectedRoom(chatRoomId);
		}, this::finishActivitySuccess);
	}

	private void finishActivitySuccess() {
		clearListener();
		clearRequestSuccess();
		Intent intent = new Intent();
		intent.putExtra("error", (String) null);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		realm.close();
	}
}
