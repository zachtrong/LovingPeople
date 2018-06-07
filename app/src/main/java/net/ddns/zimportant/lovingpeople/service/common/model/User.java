package net.ddns.zimportant.lovingpeople.service.common.model;
import com.stfalcon.chatkit.commons.models.IUser;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class User extends RealmObject {
	public static final String STORY_TELLER = "StoryTeller";
	public static final String COUNSELOR = "Counselor";
	public static final String USER_OFFLINE = "Offline";
	public static final String USER_BUSY = "Busy";
	public static final String USER_ONLINE = "Online";
	public static final String DEFAULT_USER_IMAGE = "https://i.imgur.com/pnMv3iK.png";

	@Required
	@PrimaryKey
	private String id;
	@Required
	private String name;
	@Required
	private String avatarUrl;
	@Required
	private String status;
	@Required
	private String userType;
	private User userRequest;

	public User() {
	}

	public User(String id) {
		this.id = id;
		this.name = "Anonymous";
		this.avatarUrl = DEFAULT_USER_IMAGE;
		this.status = USER_ONLINE;
		this.userType = STORY_TELLER;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
}
