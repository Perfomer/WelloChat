package com.volkovmedia.perfo.firebaserealtimechat.firebase.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.volkovmedia.perfo.firebaserealtimechat.general.ChatApplication.setCurrentUserId;

public class FirebaseAuthManager {

    protected FirebaseAuth mAuth;
    protected FirebaseUser mFirebaseUser;

    public FirebaseAuthManager(FirebaseAuthCallback callback) {
        init();

        if (mFirebaseUser == null) callback.onAuthError();
        else {
            callback.onAuthSuccess();
            setCurrentUserId(mFirebaseUser.getUid());
        }
    }

    protected FirebaseAuthManager() {
        init();
    }

    private void init() {
        this.mAuth = FirebaseAuth.getInstance();
        this.mFirebaseUser = mAuth.getCurrentUser();
    }

    public void signOut() {
        mAuth.signOut();
    }

    public FirebaseUser getCurrentUser() {
        return mFirebaseUser;
    }
}