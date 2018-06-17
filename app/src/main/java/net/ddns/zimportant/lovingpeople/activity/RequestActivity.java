package net.ddns.zimportant.lovingpeople.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.ddns.zimportant.lovingpeople.R;
import net.ddns.zimportant.lovingpeople.service.common.model.UserChat;
import net.ddns.zimportant.lovingpeople.service.helper.UserViewLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.disposables.Disposable;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.SyncUser;

import static net.ddns.zimportant.lovingpeople.service.Constant.ERR_USER_CANCEL;
import static net.ddns.zimportant.lovingpeople.service.Constant.ERR_USER_NOT_AVAILABLE;
import static net.ddns.zimportant.lovingpeople.service.Constant.ERR_USER_NOT_REQUEST_MORE;
import static net.ddns.zimportant.lovingpeople.service.Constant.PARTNER;

public class RequestActivity extends AppCompatActivity {

	public static final int TIMEOUT = 30;

	@BindView(R.id.bt_cancel)
	Button buttonCancel;
	@BindView(R.id.tv_name)
	TextView nameTextView;
	@BindView(R.id.civ_avatar)
	CircleImageView avatar;

	private String partnerId;
	private String userId;
	private Disposable checkPartner;
	private UserChat partner, user;
	private Realm realm;
	private boolean isLoadedView = false;
	private boolean isRequested = false;

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
		checkUserAvailable();
	}

	private void checkUserAvailable() {
		if (user.getUserRequestId().length() != 0) {
			cancelRequest(ERR_USER_NOT_REQUEST_MORE);
		} else if (user.getConnectedRoom().length() != 0) {
			cancelRequest(ERR_USER_NOT_REQUEST_MORE);
		}
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
					checkPartnerAvailable();
					if (isRequested) {
						checkPartnerResponse();
					}
					if (!isLoadedView) {
						isLoadedView = true;
						setUpButtonCancel();
						startRequestPartner();
					}
				});
	}

	private void checkPartnerAvailable() {
		if (partner == null) {
			cancelRequest(ERR_USER_NOT_AVAILABLE);
		} else if (isPartnerRequestOther()) {
			cancelRequest(ERR_USER_NOT_AVAILABLE);
		}
	}

	private boolean isPartnerRequestOther() {
		return partner.getUserRequestId().length() != 0
				&& !partner.getUserRequestId().equals(userId);
	}

	private void cancelRequest() {
		cancelRequest(ERR_USER_CANCEL);
	}

	private void cancelRequest(String error) {
		realm.executeTransaction(bgRealm -> {
			user.setUserRequestId("");
			partner.setUserRequestId("");
		});
		Intent i = new Intent();
		i.putExtra("error", error);
		setResult(Activity.RESULT_OK, i);
		finish();
	}

	private void setUpButtonCancel() {
		final Handler handler = new Handler();
		handler.postDelayed(() -> {
			cancelRequest(ERR_USER_CANCEL);
		}, TIMEOUT * 1000);
		buttonCancel.setOnClickListener(v -> cancelRequest());
	}

	private void startRequestPartner() {
		realm.executeTransaction(bgRealm -> {
			user.setUserRequestId(partnerId);
		});
		isRequested = true;
	}

	private void checkPartnerResponse() {
		if (partner.getUserRequestId().equals(userId)) {
			String partnerConnectedRoom = partner.getConnectedRoom();
			if (partnerConnectedRoom.length() != 0) {
				realm.executeTransaction(bgRealm -> {
					user.setConnectedRoom(partnerConnectedRoom);
					user.setStatus(UserChat.USER_BUSY);
				});
				finishActivitySuccess();
			}
		}
	}

	private void finishActivitySuccess() {
		Intent i = new Intent();
		i.putExtra("error", (String) null);
		setResult(Activity.RESULT_OK, i);
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		realm.close();
	}
}
