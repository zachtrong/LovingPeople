package com.example.ztrong.lovingpeople.service.common.model;
import com.stfalcon.chatkit.commons.models.IUser;

public class User implements IUser {
	private String id;
	private String name;
	private String avatar;
	private String status;

	public User(String id, String name, String avatar, String status) {
		this.id = id;
		this.name = name;
		this.avatar = avatar;
		this.status = status;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getAvatar() {
		return avatar;
	}

	public String getStatus() {
		return status;
	}
}
