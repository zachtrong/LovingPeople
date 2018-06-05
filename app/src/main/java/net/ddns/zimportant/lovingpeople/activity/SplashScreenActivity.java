package net.ddns.zimportant.lovingpeople.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import net.ddns.zimportant.lovingpeople.service.Constant;
import net.ddns.zimportant.lovingpeople.service.utils.AppUtils;

import io.realm.ObjectServerError;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

public class SplashScreenActivity extends BaseActivity {
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (SyncUser.current() != null) {
			MainActivity.open(this);
		} else {
			WelcomeActivity.open(this);
			SyncCredentials credentials = SyncCredentials.nickname("user", false);
			SyncUser.logInAsync(credentials, Constant.AUTH_URL, new SyncUser.Callback<SyncUser>() {
				@Override
				public void onSuccess(SyncUser result) {
					MainActivity.open(SplashScreenActivity.this);
				}

				@Override
				public void onError(ObjectServerError error) {
					AppUtils.showToast(SplashScreenActivity.this, "FAIL", true);
				}
			});
		}

		/*
		TaskStackBuilder.create(this)
				.addNextIntentWithParentStack(new Intent(this, MainActivity.class))
				.addNextIntent(new Intent(this, IntroActivity.class))
				.startActivities();
				*/
	}
}
