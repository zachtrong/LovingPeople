package net.ddns.zimportant.lovingpeople.service.helper;

import android.annotation.SuppressLint;
import android.content.Context;

import net.ddns.zimportant.lovingpeople.service.common.model.UserChat;
import net.ddns.zimportant.lovingpeople.service.interfaces.OnResponse;

import io.reactivex.disposables.Disposable;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.SyncUser;

import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.USER_ONLINE;

public class ResponseHelper {
	public static ResponseHelper responseHelper = new ResponseHelper();
	public static ResponseHelper getInstance() {
		return responseHelper;
	}

	private Realm realm;
	private UserChat user;
	private Disposable checkUser, checkRequestUser;
	private OnResponse listener;
	private boolean isClearedRequestOnStartup = false;

	@SuppressLint("CheckResult")
	public void register(Context context, Realm realm) {
		this.realm = realm;
		try {
			this.listener = (OnResponse) context;
		} catch (Exception e) {
			throw new ClassCastException("Must implement OnResponse");
		}

		setUpCurrentUser();
	}

	private void setUpCurrentUser() {
		checkUser = realm
				.where(UserChat.class)
				.equalTo("id", SyncUser.current().getIdentity())
				.findAllAsync()
				.asFlowable()
				.filter(RealmResults::isLoaded)
				.subscribe(realmResults -> {
					user = realmResults.first();
					if (!isClearedRequestOnStartup) {
						isClearedRequestOnStartup = true;
						clearRequest();
					}
					setUpNotification();
				});
	}

	private void clearRequest() {
		realm.executeTransaction(bgRealm -> {
			user.setUserRequestId("");
		});
	}

	private void setUpNotification() {
		if (isOnline() && isRequesting()) {
			if (!isConnected()) {
				checkRequestUser();
			}
			else {
				clearRequest();
			}
		}
	}

	private void checkRequestUser() {
		checkRequestUser = realm
				.where(UserChat.class)
				.equalTo("id", user.getUserRequestId())
				.findAllAsync()
				.asFlowable()
				.filter(RealmResults::isLoaded)
				.subscribe(realmResults -> {
					UserChat requestUser = realmResults.first();
					if (!requestUser.getUserRequestId().equals(user.getId())) {
						clearRequest();
					} else {
						listener.onResponse(requestUser.getId());
					}
				});
	}

	private boolean isOnline() {
		return user.getStatus().equals(USER_ONLINE);
	}

	private boolean isRequesting() {
		return user.getUserRequestId().length() != 0;
	}

	private boolean isConnected() {
		return user.getConnectedRoom().length() != 0;
	}
}
