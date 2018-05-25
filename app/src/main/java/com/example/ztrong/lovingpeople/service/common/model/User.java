package com.example.ztrong.lovingpeople.service.common.model;

import com.example.ztrong.lovingpeople.service.common.Status;
import com.stfalcon.chatkit.commons.models.IUser;

public class User implements IUser {
	private String id;
	private String name;
	private String avatar;
	private Status status;

	public User(String id, String name, String avatar, Status status) {
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

	public Status getStatus() {
		return status;
	}
}
