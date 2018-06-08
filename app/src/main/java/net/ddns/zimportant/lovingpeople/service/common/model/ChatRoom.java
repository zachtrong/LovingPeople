package net.ddns.zimportant.lovingpeople.service.common.model;

import com.stfalcon.chatkit.commons.models.IDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.SyncUser;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;
import io.realm.sync.permissions.Permission;

public class ChatRoom extends RealmObject {
	@Required
	@PrimaryKey
	private String id;
	@Required
	private String storytellerId;
	@Required
	private String counselorId;
	private boolean isExpired;
	private RealmList<Message> messages;

	public ChatRoom() {
	}

	public ChatRoom(String storytellerId, String counselorId) {
		this.id = UUID.randomUUID().toString();
		this.isExpired = false;
		this.storytellerId = storytellerId;
		this.counselorId = counselorId;
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

	public RealmList<Message> getMessages() {
		return messages;
	}

	public void setMessages(RealmList<Message> messages) {
		this.messages = messages;
	}

	public String getStorytellerId() {
		return storytellerId;
	}

	public void setStorytellerId(String storytellerId) {
		this.storytellerId = storytellerId;
	}

	public String getCounselorId() {
		return counselorId;
	}

	public void setCounselorId(String counselorId) {
		this.counselorId = counselorId;
	}

	public String getUserId() {
		if (!SyncUser.current().getIdentity().equals(storytellerId)) {
			return storytellerId;
		} else {
			return counselorId;
		}
	}
}

