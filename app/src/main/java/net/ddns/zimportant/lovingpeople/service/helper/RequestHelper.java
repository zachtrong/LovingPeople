package net.ddns.zimportant.lovingpeople.service.helper;

import android.annotation.SuppressLint;
import android.content.Context;

import net.ddns.zimportant.lovingpeople.service.common.model.UserChat;
import net.ddns.zimportant.lovingpeople.service.interfaces.OnRequest;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.SyncUser;

public class RequestHelper {
	public static RequestHelper requestHelper = new RequestHelper();
	public static RequestHelper getInstance() {
		return requestHelper;
	}

	private Realm realm;
	private UserChat currentUser;
	private RealmResults<UserChat> currentUsers;
	private OnRequest listener;

	@SuppressLint("CheckResult")
	public void register(Context context, Realm realm) {
		this.realm = realm;
		try {
			this.listener = (OnRequest) context;
		} catch (Exception e) {
			throw new ClassCastException("Must implement OnRequest");
		}
		setUpCurrentUser();
	}

	private void setUpCurrentUser() {
		currentUsers = realm
				.where(UserChat.class)
				.equalTo("id", SyncUser.current().getIdentity())
				.findAllAsync();
		currentUsers.addChangeListener(realmChangeListener);
	}

	private RealmChangeListener<RealmResults<UserChat>> realmChangeListener =
			new RealmChangeListener<RealmResults<UserChat>>() {
		@Override
		public void onChange(RealmResults<UserChat> userChats) {
			if (userChats.isLoaded()) {
				currentUser = userChats
						.where()
						.equalTo("id", SyncUser.current().getIdentity())
						.findFirst();

				setUpNotification();
			}
		}
	};

	private void setUpNotification() {
		if (isRequesting()) {
			if (!isConnected()) {
				UserChat requestUser = realm
						.where(UserChat.class)
						.equalTo("id", currentUser.getUserRequestId())
						.findFirst();

				if (!requestUser.getUserRequestId().equals(currentUser.getId())) {
					// TODO
				}

				listener.onRequest(requestUser);
			} else {
				// TODO
			}
		}
	}

	private boolean isRequesting() {
		return currentUser.getUserRequestId().length() != 0;
	}

	private boolean isConnected() {
		return currentUser.getConnectedRoom().length() != 0;
	}

	public void close() {
		currentUsers.removeChangeListener(realmChangeListener);
	}
}
