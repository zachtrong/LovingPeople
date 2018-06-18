package net.ddns.zimportant.lovingpeople.service.common.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class ResourcePost extends RealmObject {
	@Required
	@PrimaryKey
	private String id;

	@Required
	private String title;

	private RealmList<String> symptoms;
	private RealmList<String> solutions;
	private RealmList<String> links;

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

	public RealmList<String> getSymptoms() {
		return symptoms;
	}

	public void setSymptoms(RealmList<String> symptoms) {
		this.symptoms = symptoms;
	}

	public RealmList<String> getSolutions() {
		return solutions;
	}

	public void setSolutions(RealmList<String> solutions) {
		this.solutions = solutions;
	}

	public RealmList<String> getLinks() {
		return links;
	}

	public void setLinks(RealmList<String> links) {
		this.links = links;
	}
}
