package net.ddns.zimportant.lovingpeople.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import net.ddns.zimportant.lovingpeople.R;
import net.ddns.zimportant.lovingpeople.service.common.model.UserChat;
import net.ddns.zimportant.lovingpeople.service.helper.RealmHelper;
import net.ddns.zimportant.lovingpeople.service.interfaces.OnLoadedCallback;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class PrepareActivity extends AppCompatActivity {

	Realm realm;
	OnLoadedCallback callback;

	public static void open(Context context) {
		RealmHelper.setUpDefaultRealm();
		context.startActivity(new Intent(context, PrepareActivity.class));
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prepare);
		setUpRealm();
		setUpData(() -> {
			MainActivity.open(this);
			finish();
		});
	}

	private void setUpRealm() {
		realm = Realm.getDefaultInstance();
	}

	private void setUpData(OnLoadedCallback callback) {
		this.callback = callback;
		realm
				.where(UserChat.class)
				.findAllAsync()
				.addChangeListener(realmChangeListener);
	}

	RealmChangeListener<RealmResults<UserChat>> realmChangeListener =
			new RealmChangeListener<RealmResults<UserChat>>() {
		@Override
		public void onChange(RealmResults<UserChat> userChats) {
			if (userChats.isLoaded()) {
				userChats.removeChangeListener(this);
				callback.onSuccess();
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		realm.close();
	}
}
