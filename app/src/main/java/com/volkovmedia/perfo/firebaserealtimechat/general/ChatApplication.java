package com.volkovmedia.perfo.firebaserealtimechat.general;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class ChatApplication extends Application {

    private static String mCurrentUserId;

    public static String getCurrentUserId() {
        return mCurrentUserId;
    }

    public static void setCurrentUserId(String id) {
        mCurrentUserId = id;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}