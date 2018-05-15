package com.example.ztrong.lovingpeople.service.model;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Message extends RealmObject implements IMessage {
    @PrimaryKey
    private long id;
    private RealmList<User> userRealmList;
    private RealmList<ChatData> chatDataRealmList;

    public void setId(long id) {
        this.id = id;
    }

    public RealmList<User> getUserRealmList() {
        return userRealmList;
    }

    public void setUserRealmList(RealmList<User> userRealmList) {
        this.userRealmList = userRealmList;
    }

    public RealmList<ChatData> getChatDataRealmList() {
        return chatDataRealmList;
    }

    public void setChatDataRealmList(RealmList<ChatData> chatDataRealmList) {
        this.chatDataRealmList = chatDataRealmList;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public IUser getUser() {
        return null;
    }

    @Override
    public Date getCreatedAt() {
        return null;
    }
}
