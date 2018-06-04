package com.example.ztrong.lovingpeople.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.example.ztrong.lovingpeople.R;
import com.example.ztrong.lovingpeople.service.messenger.Constant;
import com.example.ztrong.lovingpeople.service.utils.AppUtils;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

public class SplashScreenActivity extends BaseActivity {
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO test
		if (SyncUser.current() != null) {
			MainActivity.open(this);
		} else {
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
