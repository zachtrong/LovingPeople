package net.ddns.zimportant.lovingpeople.service.common.model;

import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class ResourceType extends RealmObject {
	@Required
	@PrimaryKey
	private String id;
	@Required
	private String title;
	@Required
	private String content;

	private RealmList<String> postIds;

	public ResourceType() {

	}

	public ResourceType(String title, String content) {
		this.id = UUID.randomUUID().toString();
		this.title = title;
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public RealmList<String> getPostIds() {
		return postIds;
	}

	public void setPostIds(RealmList<String> postIds) {
		this.postIds = postIds;
	}
}
