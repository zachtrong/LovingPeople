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
	private boolean isExpired;
	private String userId;
	private RealmList<Message> messages;

	public ChatRoom() {
	}

	public ChatRoom(String userId) {
		this.id = UUID.randomUUID().toString();
		this.userId = userId;
		this.isExpired = false;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public RealmList<Message> getMessages() {
		return messages;
	}

	public void setMessages(RealmList<Message> messages) {
		this.messages = messages;
	}
}

