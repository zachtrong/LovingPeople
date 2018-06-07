package net.ddns.zimportant.lovingpeople.service.persistence;

import net.ddns.zimportant.lovingpeople.service.common.model.ChatRoom;
import net.ddns.zimportant.lovingpeople.service.common.model.Message;
import net.ddns.zimportant.lovingpeople.service.common.model.UserChat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DialogData {
	private DialogData() {
		throw new AssertionError();
	}

	public static ArrayList<ChatRoom> getDialogs() {
		ArrayList<ChatRoom> chats = new ArrayList<>();
		for (int i = 0; i < 20; ++i) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -(i * i));
			calendar.add(Calendar.MINUTE, -(i * i));

			chats.add(getDialog(i, calendar.getTime()));
		}
		return chats;
	}

	private static ChatRoom getDialog(int i, Date lastMessageCreatedAt) {
		ArrayList<UserChat> users = getUsers();
		return null;
	}

	private static ArrayList<UserChat> getUsers() {
		ArrayList<UserChat> users = new ArrayList<>();
		int usersCount = 1;

		for (int i = 0; i < usersCount; i++) {
			users.add(getUser());
		}

		return users;
	}

	private static UserChat getUser() {
		return null;
	}

	private static Message getMessage(final Date date) {
		return null;
	}
}
