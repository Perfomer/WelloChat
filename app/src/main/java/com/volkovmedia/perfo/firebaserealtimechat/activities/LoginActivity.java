package com.volkovmedia.perfo.firebaserealtimechat.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.volkovmedia.perfo.firebaserealtimechat.R;
import com.volkovmedia.perfo.firebaserealtimechat.firebase.auth.FirebaseAuthCallback;
import com.volkovmedia.perfo.firebaserealtimechat.firebase.auth.google.FirebaseGoogleAuthManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements FirebaseAuthCallback {

    private FirebaseGoogleAuthManager mAuthManager;

    @BindView(R.id.act_login_nickname)
    EditText lNicknameEditText;

    @BindView(R.id.act_login_button)
    Button lSignInButton;

    @BindView(R.id.act_login_progressbar)
    ProgressBar lProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int a = metrics.densityDpi;

        mAuthManager = new FirebaseGoogleAuthManager(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private boolean checkField() {
        String nickname = lNicknameEditText.getText().toString();

        if (nickname.isEmpty()) {
            lNicknameEditText.setError(getString(R.string.error_field_empty));
            return false;
        } else if (nickname.length() > 20) {
            lNicknameEditText.setError(getString(R.string.error_long_nickname));
            return false;
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case FirebaseGoogleAuthManager.RC_SIGN_IN_GOOGLE:
                mAuthManager.checkAuthResult(data);
                break;
        }
    }

    @Override
    public void onAuthError() {
        switchUI(true);
        Toast.makeText(this, R.string.warning_sign_up_failed, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthSuccess() {
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
        finish();
    }

    private void switchUI(boolean active) {
        int visibility = active ? View.GONE : View.VISIBLE;

        lProgressBar.setVisibility(visibility);
        lNicknameEditText.setEnabled(active);
        lSignInButton.setEnabled(active);
    }

    @OnClick(R.id.act_login_button)
    public void onSignInButtonClick() {
        if (checkField()) {
            String nickname = lNicknameEditText.getText().toString();
            switchUI(false);
            mAuthManager.signIn(nickname);
        }
    }
}