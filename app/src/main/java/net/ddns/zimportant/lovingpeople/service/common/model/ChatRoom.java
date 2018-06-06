package net.ddns.zimportant.lovingpeople.service.common.model;

import com.stfalcon.chatkit.commons.models.IDialog;

import java.util.ArrayList;

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
	private String photo;
	@Required
	private String name;
	private RealmList<Message> messages;
	private RealmList<Permission> permissions;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RealmList<Message> getMessages() {
		return messages;
	}

	public void setMessages(RealmList<Message> messages) {
		this.messages = messages;
	}

	public RealmList<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(RealmList<Permission> permissions) {
		this.permissions = permissions;
	}
}

