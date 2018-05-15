package com.example.ztrong.lovingpeople.activity;

import android.app.Application;

import io.realm.Realm;

public class LovingPeopleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
