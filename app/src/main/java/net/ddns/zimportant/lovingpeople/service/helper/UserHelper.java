package net.ddns.zimportant.lovingpeople.service.helper;

import net.ddns.zimportant.lovingpeople.R;

import io.realm.RealmList;

import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.USER_BUSY;
import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.USER_OFFLINE;
import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.USER_ONLINE;

public class UserHelper {
	public static int getOnlineIndicatorResource(String status) {
		switch (status) {
			case USER_ONLINE:
				return R.drawable.shape_bubble_online;
			case USER_OFFLINE:
				return R.drawable.shape_bubble_offline;
			case USER_BUSY:
				return R.drawable.shape_bubble_busy;
		}
		throw new Error("No such status");
	}

	public static String joinRealmListString(RealmList<String> fields) {
		String res = "";
		for (String field : fields) {
			if (res.length() != 0) {
				res = res.concat(", ");
			}
			res = res.concat(field);
		}
		return res;
	}
}
