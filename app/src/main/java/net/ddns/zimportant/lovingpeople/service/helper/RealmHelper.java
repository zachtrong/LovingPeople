package net.ddns.zimportant.lovingpeople.service.helper;

import io.realm.Realm;
import io.realm.SyncConfiguration;
import io.realm.SyncUser;

import static net.ddns.zimportant.lovingpeople.service.Constant.DEFAULT_REALM_URL;

public class RealmHelper {
	public static void setUpDefaultRealm() {
		SyncConfiguration syncConfiguration = new SyncConfiguration
				.Builder(SyncUser.current(), DEFAULT_REALM_URL)
				.partialRealm()
				.build();
		Realm.setDefaultConfiguration(syncConfiguration);
	}
}
