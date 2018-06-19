package net.ddns.zimportant.lovingpeople.service.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/*
 * Created by troy379 on 06.04.17.
 */
public final class FormatUtils {
	private FormatUtils() {
		throw new AssertionError();
	}

	public static String getDurationString(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		return formatter.format(date);
	}
}
