package com.example.ztrong.lovingpeople.service.model;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.RealmField;
import io.realm.annotations.Required;

public class User extends RealmObject {
    @PrimaryKey
    private String username;
    @Required
    private String password;
    private RealmList<Message> messages;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RealmList<Message> getMessages() {
        return messages;
    }

    public void setMessages(RealmList<Message> messages) {
        this.messages = messages;
    }
}
