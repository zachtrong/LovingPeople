package net.ddns.zimportant.lovingpeople.service.persistence;

import net.ddns.zimportant.lovingpeople.service.common.model.Dialog;
import net.ddns.zimportant.lovingpeople.service.common.model.Message;
import net.ddns.zimportant.lovingpeople.service.common.model.User;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import static net.ddns.zimportant.lovingpeople.service.persistence.FixturesData.getRandomAvatar;
import static net.ddns.zimportant.lovingpeople.service.persistence.FixturesData.getRandomId;
import static net.ddns.zimportant.lovingpeople.service.persistence.FixturesData.getRandomMessage;
import static net.ddns.zimportant.lovingpeople.service.persistence.FixturesData.getRandomName;
import static net.ddns.zimportant.lovingpeople.service.persistence.FixturesData.getRandomStatus;

public class DialogData {
	private DialogData() {
		throw new AssertionError();
	}

	public static ArrayList<Dialog> getDialogs() {
		ArrayList<Dialog> chats = new ArrayList<>();
		for (int i = 0; i < 20; ++i) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -(i * i));
			calendar.add(Calendar.MINUTE, -(i * i));

			chats.add(getDialog(i, calendar.getTime()));
		}
		return chats;
	}

	private static Dialog getDialog(int i, Date lastMessageCreatedAt) {
		ArrayList<User> users = getUsers();
		return new Dialog(
				getRandomId(),
				users.get(0).getName(),
				getRandomAvatar(),
				users,
				getMessage(lastMessageCreatedAt),
				i < 3 ? 3 - i : 0);
	}

	private static ArrayList<User> getUsers() {
		ArrayList<User> users = new ArrayList<>();
		int usersCount = 1;

		for (int i = 0; i < usersCount; i++) {
			users.add(getUser());
		}

		return users;
	}

	private static User getUser() {
		return new User(
				getRandomId(),
				getRandomName(),
				getRandomAvatar(),
				getRandomStatus());
	}

	private static Message getMessage(final Date date) {
		return new Message(
				getRandomId(),
				getUser(),
				getRandomMessage(),
				date);
	}
}
