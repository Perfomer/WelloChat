package com.volkovmedia.perfo.firebaserealtimechat.firebase.database;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDatabaseManager {

    public static final String RF_MESSAGES = "messages", RF_USERS = "users";

    protected FirebaseDatabase mDatabase;

    protected FirebaseDatabaseManager() {
        this.mDatabase = FirebaseDatabase.getInstance();
    }

}