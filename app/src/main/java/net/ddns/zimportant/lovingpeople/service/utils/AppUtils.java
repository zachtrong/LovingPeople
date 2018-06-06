package net.ddns.zimportant.lovingpeople.service.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.Log;
import android.widget.Toast;

/*
 * Created by troy379 on 04.04.17.
 */
public class AppUtils {

	public static final boolean DEBUG = true;
	public static final String TAG = "Debug";

	public static void showToast(Context context, @StringRes int text, boolean isLong) {
		showToast(context, context.getString(text), isLong);
	}

	public static void showToast(Context context, String text, boolean isLong) {
		Toast.makeText(context, text, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
	}

	public static void d(String msg) {
		if (DEBUG) {
			Log.d(TAG, msg);
		}
	}
}