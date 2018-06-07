package net.ddns.zimportant.lovingpeople.service.common.model;

import com.stfalcon.chatkit.commons.models.IDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;
import io.realm.sync.permissions.Permission;

public class ChatRoom extends RealmObject {
	@Required
	@PrimaryKey
	private String id;
	@Required
	private boolean isExpired;
	private User user;
	private RealmList<Message> messages;

	public ChatRoom() {
	}

	public ChatRoom(String userId) {
		this.id = UUID.randomUUID().toString();
		this.user = user;
		this.isExpired = false;
		this.messages = new RealmList<>();
		this.messages.add(new Message("", new User()));
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isExpired() {
		return isExpired;
	}

	public void setExpired(boolean expired) {
		isExpired = expired;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public RealmList<Message> getMessages() {
		return messages;
	}

	public void setMessages(RealmList<Message> messages) {
		this.messages = messages;
	}
}

