package net.ddns.zimportant.lovingpeople.service.common.model;

import android.support.annotation.Nullable;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;
import com.stfalcon.chatkit.commons.models.MessageContentType;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Message extends RealmObject implements IMessage {
	@Required
	@PrimaryKey
	private String id;
	@Required
	private String body;
	@Required
	private Date createdAt;
	private String authorId;

	public Message() {
	}

	public Message(String body, String authorId) {
		this.id = UUID.randomUUID().toString();
		this.body = body;
		this.createdAt = Calendar.getInstance().getTime();
		this.authorId = authorId;
	}

	public String getId() {
		return id;
	}

	@Override
	public String getText() {
		return body;
	}

	@Override
	public IUser getUser() {
		return getRealm()
				.where(UserChat.class)
				.equalTo("id", getAuthorId())
				.findFirst();
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

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
}
