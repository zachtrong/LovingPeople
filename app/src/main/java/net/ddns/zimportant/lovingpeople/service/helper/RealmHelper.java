package net.ddns.zimportant.lovingpeople.service.helper;

import android.util.Log;

import io.realm.Realm;
import io.realm.SyncConfiguration;
import io.realm.SyncUser;
import io.realm.log.RealmLog;

import static net.ddns.zimportant.lovingpeople.service.Constant.DEFAULT_REALM_URL;

public class RealmHelper {
	public static void setUpDefaultRealm() {
		SyncConfiguration syncConfiguration = SyncUser.current()
				.createConfiguration(DEFAULT_REALM_URL)
				.build();
		Realm.setDefaultConfiguration(syncConfiguration);
	}
}
