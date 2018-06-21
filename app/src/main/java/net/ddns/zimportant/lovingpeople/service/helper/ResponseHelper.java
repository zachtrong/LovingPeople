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
	private UserChat user, partner;
	private OnResponse listener;
	private Disposable checkUser, checkPartner;

	@SuppressLint("CheckResult")
	public void register(Context context, Realm realm) {
		this.realm = realm;
		try {
			this.listener = (OnResponse) context;
		} catch (Exception e) {
			throw new ClassCastException("Must implement OnResponse");
		}

		setUpUser();
		setUpPartner();
	}

	private void setUpUser() {
		user = realm
				.where(UserChat.class)
				.equalTo("id", SyncUser.current().getIdentity())
				.findFirst();
		checkUser = realm
				.where(UserChat.class)
				.equalTo("id", SyncUser.current().getIdentity())
				.findAllAsync()
				.asFlowable()
				.filter(RealmResults::isLoaded)
				.subscribe(realmResults -> {
					user = realmResults.first();
				});
	}

	private void setUpPartner() {
		checkPartner = realm
				.where(UserChat.class)
				.notEqualTo("id", SyncUser.current().getIdentity())
				.and()
				.notEqualTo("id", user.getUserRequestId())
				.and()
				.equalTo("userRequestId", SyncUser.current().getIdentity())
				.findAllAsync()
				.asFlowable()
				.filter(RealmResults::isLoaded)
				.filter(realmResults -> realmResults.size() != 0)
				.subscribe(realmResults -> {
					partner = realmResults.first();
					onNotification();
				});
	}

	private void onNotification() {
		if (user.getStatus().equals(USER_ONLINE)) {
			if (user.getUserRequestId().length() == 0) {
				realm.executeTransaction(bgRealm -> {
					user.setUserRequestId(partner.getId());
				});
				listener.onResponse(partner.getId());
			}
		}
	}

	public void unregister() {
		checkUser.dispose();
		checkPartner.dispose();
		realm.close();
	}
}
