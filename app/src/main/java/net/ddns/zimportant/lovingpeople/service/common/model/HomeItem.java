package net.ddns.zimportant.lovingpeople.service.common.model;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class HomeItem extends RealmObject {
	@PrimaryKey
	@Required
	private String itemId;
	@Required
	private String body;
	@Required
	private RealmList<String> listLikeIdentity;
	@Required
	private Date timestamp;

	public HomeItem() {

	}

	public HomeItem(String body) {
		this.itemId = UUID.randomUUID().toString();
		this.body = body;
		this.listLikeIdentity = new RealmList<>();
		this.timestamp = Calendar.getInstance().getTime();
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Integer getLikeCount() {
		return listLikeIdentity.size();
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public RealmList<String> getListLikeIdentity() {
		return listLikeIdentity;
	}

	public void setListLikeIdentity(RealmList<String> listLikeIdentity) {
		this.listLikeIdentity = listLikeIdentity;
	}
}
