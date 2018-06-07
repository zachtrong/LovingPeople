package net.ddns.zimportant.lovingpeople;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.app.AppCompatActivity;

import net.ddns.zimportant.lovingpeople.service.Constant;
import net.ddns.zimportant.lovingpeople.service.utils.AppUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.realm.ObjectServerError;
import io.realm.PermissionManager;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;
import io.realm.permissions.AccessLevel;
import io.realm.permissions.PermissionRequest;
import io.realm.permissions.UserCondition;

import static net.ddns.zimportant.lovingpeople.service.Constant.AUTH_URL;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class RealmTest {
	private Context context;
	@Test
	public void setUp() {
		context = InstrumentationRegistry.getTargetContext();
		Realm.init(context);
	}

	@Test
	public void setup() {
	}
}
