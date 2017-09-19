package com.miramicodigo.notesbeta;

import android.app.Application;
import android.content.SharedPreferences;

import com.miramicodigo.notesbeta.helper.RealmManager;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RealmManager.initRealmConfiguration(this);
    }
}
