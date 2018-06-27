package net.ddns.zimportant.lovingpeople.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import net.ddns.zimportant.lovingpeople.R;
import net.ddns.zimportant.lovingpeople.service.common.model.Request;
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
import static net.ddns.zimportant.lovingpeople.service.Constant.ERR_USER_NOT_REQUEST_MORE;
import static net.ddns.zimportant.lovingpeople.service.Constant.ERR_USER_STOP_REQUEST;
import static net.ddns.zimportant.lovingpeople.service.Constant.PARTNER;
import static net.ddns.zimportant.lovingpeople.service.Constant.TIMEOUT;

public class RequestActivity extends AppCompatActivity {

	@BindView(R.id.tv_name)
	TextView nameTextView;
	@BindView(R.id.civ_avatar)
	CircleImageView avatar;
	@BindView(R.id.tv_timeout_second)
	TextView timeout;

	private Realm realm;
	private String userId, partnerId;
	private Request request;
	private CountDownTimer timer;
	private Disposable checkRequest;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_conversation);
		ButterKnife.bind(this);
		setUpRealm();
		prepareInformation();
		prepareRealmData();
		clearRequest();
		startRequest();
	}

	private void setUpRealm() {
		realm = Realm.getDefaultInstance();
	}

	private void prepareInformation() {
		userId = SyncUser.current().getIdentity();
		partnerId = getIntent().getExtras().getString(PARTNER);
	}

	private void prepareRealmData() {
		RealmResults<UserChat> partner = realm
				.where(UserChat.class)
				.equalTo("id", partnerId)
				.findAllAsync();
		UserViewLoader userViewLoader = new UserViewLoader.Builder(partner)
				.setAvatarView(avatar)
				.setNameView(nameTextView)
				.build();
		userViewLoader.startListening();
	}

	private void startRequest() {
		realm.executeTransaction(bgRealm -> {
			request = new Request(userId, partnerId);
			realm.insert(request);
		});

		startListeningChange();
		setUpTimeout();
	}

	private void clearRequest() {
		RealmResults<Request> requests = realm
				.where(Request.class)
				.equalTo("userId", userId)
				.and()
				.equalTo("isExpired", false)
				.findAll();

		realm.executeTransaction(bgRealm -> {
			for (Request request : requests) {
				request.setExpired(true);
			}
		});
	}

	private void startListeningChange() {
		checkRequest = realm
				.where(Request.class)
				.equalTo("id", request.getId())
				.findAllAsync()
				.asFlowable()
				.filter(RealmResults::isLoaded)
				.filter(realmResults -> realmResults.size() != 0)
				.subscribe(realmResults -> {
					request = realmResults.first();

					if (request.isExpired()) {
						checkRequest.dispose();
						checkRequestResult();
					}
				});
	}

	private void checkRequestResult() {
		String connectedRoomId = request.getConnectedRoom();
		if (connectedRoomId.length() == 0) {
			cancelRequest(ERR_USER_STOP_REQUEST);
		} else {
			realm.executeTransaction(bgRealm -> {
				UserChat user = realm
						.where(UserChat.class)
						.equalTo("id", SyncUser.current().getIdentity())
						.findFirst();
				user.setConnectedRoom(connectedRoomId);
			});
			finishActivitySuccess();
		}
	}

	private void cancelRequest(String error) {
		if (timer != null) {
			timer.cancel();
		}
		if (checkRequest != null && !checkRequest.isDisposed()) {
			checkRequest.dispose();
		}
		if (request != null && request.isValid()) {
			realm.executeTransaction(bgRealm -> {
				request.setExpired(true);
			});
		}
		Intent i = new Intent();
		i.putExtra("error", error);
		setResult(Activity.RESULT_OK, i);
		finish();
	}

	private void setUpTimeout() {
		timeout.setText(String.valueOf(TIMEOUT));
		timer = new CountDownTimer(TIMEOUT * 1000, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				timeout.setText(String.valueOf(millisUntilFinished/1000).concat("s"));
			}

			@Override
			public void onFinish() {
				cancelRequest(ERR_USER_CANCEL);
			}
		}.start();
	}

	private void finishActivitySuccess() {
		Intent i = new Intent();
		i.putExtra("error", (String) null);
		setResult(Activity.RESULT_OK, i);
		finish();
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
