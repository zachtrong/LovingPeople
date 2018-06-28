package net.ddns.zimportant.lovingpeople.activity;

import android.support.v7.app.AppCompatActivity;

import net.ddns.zimportant.lovingpeople.service.utils.AppUtils;

import java.util.HashSet;
import java.util.Set;

public abstract class BaseConnectActivity extends AppCompatActivity {
	private static Set<AppCompatActivity> instances = new HashSet<>();

	public static void addInstance(AppCompatActivity activity) {
		instances.add(activity);
	}

	public static void removeInstance(AppCompatActivity activity) {
		instances.remove(activity);
	}

	public static void stopAllInstances() {
		AppUtils.d("STOPPPPPPPP");
		for (AppCompatActivity activity : instances) {
			activity.finish();
			instances.remove(activity);
		}
	}
}
