package com.example.ztrong.lovingpeople.service.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class ChatData extends RealmObject {
    private Date chatTimeDate;
    private User user;
    @Required
    private String message;

    public Date getChatTimeDate() {
        return chatTimeDate;
    }

    public void setChatTimeDate(Date chatTimeDate) {
        this.chatTimeDate = chatTimeDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
