package net.ddns.zimportant.lovingpeople.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import net.ddns.zimportant.lovingpeople.R;
import net.ddns.zimportant.lovingpeople.service.common.model.HomeItem;
import net.ddns.zimportant.lovingpeople.service.common.model.UserChat;
import net.ddns.zimportant.lovingpeople.service.helper.RealmHelper;
import net.ddns.zimportant.lovingpeople.service.interfaces.OnLoadedCallback;

import io.realm.Realm;
import io.realm.RealmResults;

import static net.ddns.zimportant.lovingpeople.service.Constant.SUBSCRIPTION_USER_CHAT;

public class PrepareActivity extends AppCompatActivity {

	Realm realm;

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
		realm
				.where(UserChat.class)
				.findAllAsync(SUBSCRIPTION_USER_CHAT)
				.addChangeListener((realmModel) -> {
					if (realmModel.isLoaded()) {
						realmModel.removeAllChangeListeners();
						callback.onSuccess();
					}
				});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		realm.close();
	}
}
