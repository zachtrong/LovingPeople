package net.ddns.zimportant.lovingpeople.service.common.model;

import com.stfalcon.chatkit.commons.models.IUser;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class UserChat extends RealmObject implements IUser {
	public static final String STORYTELLER = "Storyteller";
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
	@Required
	private String currentUserType;

	private RealmList<String> fields;
	private String connectedRoom;

	private String introduce;
	private String birth;
	private String address;
	private String experience;

	public UserChat() {
	}

	public UserChat(String id) {
		this.id = id;
		this.name = "Anonymous".concat(id);
		this.avatarUrl = DEFAULT_USER_IMAGE;
		this.status = USER_ONLINE;
		this.userType = STORYTELLER;
		this.currentUserType = STORYTELLER;
		this.connectedRoom = "";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		if (currentUserType.equals(COUNSELOR)) {
			return name;
		} else {
			return "Anonymous".concat(id);
		}
	}

	@Override
	public String getAvatar() {
		return avatarUrl;
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

	public RealmList<String> getFields() {
		return fields;
	}

	public void setFields(RealmList<String> fields) {
		this.fields = fields;
	}

	public String getCurrentUserType() {
		return currentUserType;
	}

	public void setCurrentUserType(String currentUserType) {
		this.currentUserType = currentUserType;
	}

	public String getConnectedRoom() {
		return connectedRoom;
	}

	public void setConnectedRoom(String connectedRoom) {
		this.connectedRoom = connectedRoom;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}
}
