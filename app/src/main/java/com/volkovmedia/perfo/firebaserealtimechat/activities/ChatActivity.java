package com.volkovmedia.perfo.firebaserealtimechat.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.volkovmedia.perfo.firebaserealtimechat.R;
import com.volkovmedia.perfo.firebaserealtimechat.firebase.auth.FirebaseAuthCallback;
import com.volkovmedia.perfo.firebaserealtimechat.firebase.auth.FirebaseAuthManager;
import com.volkovmedia.perfo.firebaserealtimechat.firebase.database.FirebaseDatabaseManager;
import com.volkovmedia.perfo.firebaserealtimechat.firebase.database.types.basis.FirebaseDatabaseLoadingCallback;
import com.volkovmedia.perfo.firebaserealtimechat.firebase.database.types.basis.FirebaseDatabaseObjectCallback;
import com.volkovmedia.perfo.firebaserealtimechat.firebase.database.types.basis.FirebaseDatabaseObjectManager;
import com.volkovmedia.perfo.firebaserealtimechat.objects.Message;
import com.volkovmedia.perfo.firebaserealtimechat.objects.User;
import com.volkovmedia.perfo.firebaserealtimechat.utils.GeneralMethods;
import com.volkovmedia.perfo.firebaserealtimechat.utils.UniqueList;
import com.volkovmedia.perfo.firebaserealtimechat.view.adapters.MessageAdapter;

import java.util.Calendar;
import java.util.LinkedHashSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends AppCompatActivity implements FirebaseAuthCallback {

    private final static int RC_SIGN_IN = 131;

    private final static int MENU_LOGOUT = 100;

    @BindView(R.id.act_main_recycler)
    RecyclerView mRecyclerView;

    @BindView(R.id.act_main_text_field)
    EditText mMessageTextField;

    @BindView(R.id.act_main_progressbar)
    ProgressBar mProgressBar;

    private FirebaseAuthManager mAuthManager;

    private FirebaseDatabaseObjectManager<Message> mMessagesManager;
    private FirebaseDatabaseObjectManager<User> mUsersManager;

    private UniqueList<Message> mMessages;
    private UniqueList<User> mUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);
        mAuthManager = new FirebaseAuthManager(this);
        ButterKnife.bind(this);

        showLoading(true);

        mMessages = new UniqueList<>();
        mUsers = new UniqueList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(new MessageAdapter(mMessages, mUsers, new MessageAdapter.OnImageClickListener() {
            @Override
            public void onImageClick(View view, Message data) {
                Intent intent = new Intent(ChatActivity.this, PhotoActivity.class);
                intent.putExtra(PhotoActivity.KEY_PHOTO, data.getText());
                startActivity(intent);
            }
        }));

        mMessagesManager = new FirebaseDatabaseObjectManager<>(new MessagesListener(), FirebaseDatabaseManager.RF_MESSAGES, Message.class);
        mUsersManager = new FirebaseDatabaseObjectManager<>(FirebaseDatabaseManager.RF_USERS, User.class);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMessagesManager.switchListener(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMessagesManager.switchListener(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_LOGOUT, Menu.NONE, R.string.menu_sign_out);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case MENU_LOGOUT:
                mAuthManager.signOut();
                startSignInActivity();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAuthError() {
        startSignInActivity();
    }

    @Override
    public void onAuthSuccess() {

    }

    @OnClick(R.id.act_main_send_button)
    public void onSendButtonClick() {
        String text = mMessageTextField.getText().toString();
        if (text.isEmpty()) return;

        Message message = new Message(
                mAuthManager.getCurrentUser().getUid(),
                Calendar.getInstance().getTime().getTime(),
                text);

        mMessagesManager.send(message);

        mMessageTextField.setText("");
    }

    private void startSignInActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, RC_SIGN_IN);
        finish();
    }

    private void showLoading(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        mProgressBar.setVisibility(visibility);
    }

    private class MessagesListener implements FirebaseDatabaseObjectCallback<Message> {

        private LinkedHashSet<Message> mLoadingQuery;

        MessagesListener() {
            mLoadingQuery = new LinkedHashSet<>();
        }

        @Override
        public void onObjectAdded(final Message object) {
            showLoading(false);
            String authorId = object.getAuthorId();

            if (!mUsers.contains(authorId)) {
                mUsersManager.query(authorId, new FirebaseDatabaseLoadingCallback<User>() {
                    @Override
                    public void onLoadingSuccess(User user) {
                        mUsers.add(user);

                        LinkedHashSet<Message> temp = new LinkedHashSet<>(mLoadingQuery);

                        for (Message message : temp) {
                            if (user.getId().equals(message.getAuthorId())) {
                                displayMessage(message);
                                mLoadingQuery.remove(message);
                            }
                        }
                    }

                    @Override
                    public void onLoadingError(String message) {
                    }
                });

                mLoadingQuery.add(object);
            } else {
                displayMessage(object);
            }
        }

        @Override
        public void onObjectLoadingFailed(String error) {
            showLoading(false);
        }

        private synchronized void displayMessage(final Message message) {
            boolean result = mMessages.add(message);

            if (result) {
                GeneralMethods.sortMessages(mMessages.toArrayList());

                int position = mMessages.indexOf(message);
                mRecyclerView.getAdapter().notifyItemInserted(position);
                mRecyclerView.scrollToPosition(position);
            }
        }
    }

}