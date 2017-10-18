package com.volkovmedia.perfo.firebaserealtimechat.firebase.auth.google;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.volkovmedia.perfo.firebaserealtimechat.R;
import com.volkovmedia.perfo.firebaserealtimechat.activities.LoginActivity;
import com.volkovmedia.perfo.firebaserealtimechat.firebase.auth.FirebaseAuthManager;
import com.volkovmedia.perfo.firebaserealtimechat.firebase.database.FirebaseDatabaseManager;
import com.volkovmedia.perfo.firebaserealtimechat.firebase.database.types.basis.FirebaseDatabaseObjectManager;
import com.volkovmedia.perfo.firebaserealtimechat.objects.User;

import static com.volkovmedia.perfo.firebaserealtimechat.utils.ResourceManager.getString;

public class FirebaseGoogleAuthManager extends FirebaseAuthManager implements GoogleApiClient.OnConnectionFailedListener {

    public static final int RC_SIGN_IN_GOOGLE = 500;

    private GoogleApiClient mGoogleApiClient;
    private LoginActivity mLoginActivity;

    private FirebaseDatabaseObjectManager<User> mUsersManager;

    private String mUserNickname;

    public FirebaseGoogleAuthManager(LoginActivity loginActivity) {
        super();
        this.mLoginActivity = loginActivity;
        this.mUsersManager = new FirebaseDatabaseObjectManager<>(FirebaseDatabaseManager.RF_USERS, User.class);
        initGoogleAuth();
    }

    private void initGoogleAuth() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(mLoginActivity, R.string.default_web_client_id))
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(mLoginActivity)
                .enableAutoManage(mLoginActivity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public void signIn(String name) {
        mUserNickname = name;
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        mLoginActivity.startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
    }

    public void checkAuthResult(Intent data) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

        if (result.isSuccess()) {
            final GoogleSignInAccount acct = result.getSignInAccount();
            AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

            mAuth.signInWithCredential(credential).addOnCompleteListener(mLoginActivity, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        User currentUser = new User(mUserNickname, getUserPhotoUrl(acct.getPhotoUrl()));
                        mUsersManager.send(currentUser, mAuth.getCurrentUser().getUid());
                        mLoginActivity.onAuthSuccess();
                    } else {
                        mLoginActivity.onAuthError();
                    }
                }
            });
        } else {
            mLoginActivity.onAuthError();
        }
    }

    private String getUserPhotoUrl(Uri photoUri) {
        String result = null;
        if (photoUri != null) {
            result = photoUri.toString();
        }
        return result;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mLoginActivity.onAuthError();
    }
}
