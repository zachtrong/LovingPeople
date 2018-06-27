package net.ddns.zimportant.lovingpeople.service.helper;

import android.annotation.SuppressLint;
import android.content.Context;

import net.ddns.zimportant.lovingpeople.service.common.model.Request;
import net.ddns.zimportant.lovingpeople.service.common.model.UserChat;
import net.ddns.zimportant.lovingpeople.service.interfaces.OnResponse;

import java.util.Calendar;
import java.util.Date;

import io.reactivex.disposables.Disposable;
import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.SyncUser;

import static net.ddns.zimportant.lovingpeople.service.Constant.TIMEOUT;
import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.USER_ONLINE;

public class RequestHelper {
	public static RequestHelper requestHelper = new RequestHelper();
	public static RequestHelper getInstance() {
		return requestHelper;
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
		setUpResponse();
	}

	private void setUpUser() {
		user = realm
				.where(UserChat.class)
				.equalTo("id", SyncUser.current().getIdentity())
				.findFirst();
		checkUser = realm
				.where(UserChat.class)
				.equalTo("id", user.getId())
				.findAllAsync()
				.asFlowable()
				.filter(RealmResults::isLoaded)
				.subscribe(realmResults -> {
					user = realmResults.first();
				});
	}

	private void setUpResponse() {
		Disposable disposable = realm
				.where(Request.class)
				.equalTo("partnerId", SyncUser.current().getIdentity())
				.and()
				.equalTo("isExpired", false)
				.sort("createAt", Sort.DESCENDING)
				.findAllAsync()
				.asFlowable()
				.filter(RealmResults::isLoaded)
				.filter(realmResults -> realmResults.size() != 0)
				.subscribe(realmResults -> {
					Request request = realmResults.first();

					Date createAt = request.getCreateAt();
					long diff = Calendar.getInstance().getTimeInMillis() - createAt.getTime();
					if (diff/1000 <= TIMEOUT) {
						onNotification(request.getId());
					}
				});
	}

	private void onNotification(String requestId) {
		if (user.getStatus().equals(USER_ONLINE)) {
			if (user.getConnectedRoom().length() == 0) {
				listener.onResponse(requestId);
			}
		}
	}

	public void unregister() {
		checkUser.dispose();
		checkPartner.dispose();
		realm.close();
	}
}
