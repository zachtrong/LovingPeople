package net.ddns.zimportant.lovingpeople.service.common.model;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;

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
	private Integer likeCount;
	@Required
	private Boolean isLiked;
	@Required
	private Date timestamp;

	public HomeItem() {

	}

	public HomeItem(String itemId, String body) {
		this.itemId = itemId;
		this.body = body;
		this.likeCount = new SecureRandom().nextInt(100);
		this.isLiked = new SecureRandom().nextBoolean();
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
		return likeCount;
	}

	public void setLikeCount(Integer likeCount) {
		this.likeCount = likeCount;
	}

	public Boolean getIsLiked() {
		return isLiked;
	}

	public void setIsLiked(Boolean isLiked) {
		this.isLiked = isLiked;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
