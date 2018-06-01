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

import io.realm.Realm;
import io.realm.SyncUser;

public class SplashScreenActivity extends BaseActivity {
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO check login
		TaskStackBuilder.create(this)
				.addNextIntentWithParentStack(new Intent(this, MainActivity.class))
				.addNextIntent(new Intent(this, IntroActivity.class))
				.startActivities();
	}
}
