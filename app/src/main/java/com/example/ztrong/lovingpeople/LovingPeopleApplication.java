package com.example.ztrong.lovingpeople;

import android.app.Application;

import io.realm.Realm;

public class LovingPeopleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
