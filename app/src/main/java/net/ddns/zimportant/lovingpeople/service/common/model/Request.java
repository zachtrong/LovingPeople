package net.ddns.zimportant.lovingpeople.service.common.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Request extends RealmObject {
	@Required
	@PrimaryKey
	private String id;
	@Required
	private String userId;
	@Required
	private String partnerId;
	@Required
	private Date createAt;
	@Required
	private boolean isExpired;
	@Required
	private String connectedRoom;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public boolean isExpired() {
		return isExpired;
	}

	public void setExpired(boolean expired) {
		isExpired = expired;
	}

	public String getConnectedRoom() {
		return connectedRoom;
	}

	public void setConnectedRoom(String connectedRoom) {
		this.connectedRoom = connectedRoom;
	}
}
