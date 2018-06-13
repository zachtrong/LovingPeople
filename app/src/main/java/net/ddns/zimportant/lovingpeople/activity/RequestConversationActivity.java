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
import net.ddns.zimportant.lovingpeople.service.utils.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.SyncUser;

import static net.ddns.zimportant.lovingpeople.service.Constant.PARTNER;
import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.USER_ONLINE;

public class RequestConversationActivity extends AppCompatActivity {

	@BindView(R.id.bt_cancel)
	Button buttonCancel;
	@BindView(R.id.tv_name)
	TextView nameTextView;
	@BindView(R.id.civ_avatar)
	CircleImageView avatar;

	private String partnerId;
	private String userId;
	private UserChat partnerUser;
	private UserChat currentUser;
	private RealmResults<UserChat> partnerUsers;
	Realm realm;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_conversation);
		ButterKnife.bind(this);
		setUpRealm();
		prepareUser();
		setUpView();
		startRequest();
	}

	private void setUpView() {
		setUpButton();
		setUpUserView();
	}

	private void setUpButton() {
		buttonCancel.setOnClickListener(v -> {
			resetState();
			finish();
		});
	}

	private void setUpUserView() {
		Picasso.get()
				.load(partnerUser.getAvatarUrl())
				.into(avatar);
		nameTextView.setText(partnerUser.getName());
	}

	private void setUpRealm() {
		realm = Realm.getDefaultInstance();
	}

	private void prepareUser() {
		partnerId = getIntent().getExtras().getString(PARTNER);
		userId = SyncUser.current().getIdentity();

		partnerUser = realm
				.where(UserChat.class)
				.equalTo("id", partnerId)
				.findFirst();
		currentUser = realm
				.where(UserChat.class)
				.equalTo("id", userId)
				.findFirst();
	}

	private void startRequest() {
		if (partnerUser.getConnectedRoom().length() != 0) {
			finishActivity("User Is Chatting With Other");
		} else if (!partnerUser.getStatus().equals(USER_ONLINE)) {
			finishActivity("User Is Not Online");
		} else if (partnerUser.getUserRequestId().length() != 0) {
			finishActivity("User Is Being Requested From Other");
		} else if (currentUser.getUserRequestId().length() != 0) {
			finishActivity("You Cannot Request Two Users At One Time");
		} else if (currentUser.getConnectedRoom().length() != 0) {
			finishActivity("You Cannot Request While Connecting With Other");
		} else {
			requestPartner();
		}
	}

	private void finishActivity(@Nullable String error) {
		resetState();
		Intent intent = new Intent();
		intent.putExtra("error", error);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	private void resetState() {
		if (partnerUsers != null) {
			partnerUsers.removeChangeListener(changeListener);
		}
		realm.executeTransaction(bgRealm -> {
			currentUser.setUserRequestId("");
			partnerUser.setUserRequestId("");
		});
	}

	private void requestPartner() {
		realm.executeTransaction(bgRealm -> {
			currentUser.setUserRequestId(partnerUser.getId());
			partnerUser.setUserRequestId(currentUser.getId());
		});

		partnerUsers = realm
				.where(UserChat.class)
				.equalTo("id", partnerId)
				.findAllAsync();
		partnerUsers.addChangeListener(changeListener);
	}

	RealmChangeListener<RealmResults<UserChat>> changeListener = userChats -> {
		partnerUser = userChats
				.where()
				.equalTo("id", partnerId)
				.findFirst();
		checkStatus();
	};

	private void checkStatus() {
		if (partnerUser.getUserRequestId().equals(userId)) {
			if (partnerUser.getConnectedRoom().length() != 0) {
				finishActivity(null);
			}
		} else {
			finishActivity("Something Went Wrong!");
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		realm.close();
	}
}
