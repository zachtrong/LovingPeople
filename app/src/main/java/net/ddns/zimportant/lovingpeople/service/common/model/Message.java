package net.ddns.zimportant.lovingpeople.service.common.model;

import android.support.annotation.Nullable;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.MessageContentType;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Message extends RealmObject {
	@Required
	@PrimaryKey
	private String id;
	@Required
	private String body;
	@Required
	private Date createdAt;
	private User author;

	public Message() {
	}

	public Message(String body, User author) {
		this.id = UUID.randomUUID().toString();
		this.body = body;
		this.createdAt = Calendar.getInstance().getTime();
		this.author = author;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}
}
