package net.ddns.zimportant.lovingpeople.service.helper;

import io.realm.Realm;
import io.realm.SyncConfiguration;

public class RealmHelper {
	public static void setUpDefaultRealm() {
		Realm.setDefaultConfiguration(SyncConfiguration.automatic());
	}
}
