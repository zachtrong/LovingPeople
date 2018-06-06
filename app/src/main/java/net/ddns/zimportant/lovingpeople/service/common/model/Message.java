package net.ddns.zimportant.lovingpeople.service.common.model;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.MessageContentType;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Message extends RealmObject {
	@Required
	@PrimaryKey
	private String id;
	@Required
	private String body;
	@Required
	private Date createdAt;
	private User author;
}
