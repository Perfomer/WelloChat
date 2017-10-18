package com.volkovmedia.perfo.firebaserealtimechat.activities;

import android.annotation.SuppressLint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;
import com.volkovmedia.perfo.firebaserealtimechat.R;
import com.volkovmedia.perfo.firebaserealtimechat.general.GlideApp;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoActivity extends AppCompatActivity {

    private static final int UI_ANIMATION_DELAY = 300;

    public static final String KEY_PHOTO = "photo";

    private final Handler pHideHandler = new Handler();

    private Runnable pHidePart2Runnable;
    private Runnable pShowPart2Runnable;
    private Runnable pHideRunnable;

    private boolean pVisible;

    @BindView(R.id.fullscreen_content)
    ImageView pContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String photoUrl = getIntent().getStringExtra(KEY_PHOTO);

        GlideApp.with(this)
                .load(photoUrl)
                .into(pContentView);

        pHidePart2Runnable = new Runnable() {
            @SuppressLint("InlinedApi")
            @Override
            public void run() {
                pContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
        };

        pShowPart2Runnable = new Runnable() {
            @Override
            public void run() {
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    actionBar.show();
                }
            }
        };

        pHideRunnable = new Runnable() {
            @Override
            public void run() {
                hide();
            }
        };

        pVisible = true;

        pContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        delayedHide(100);
    }

    private void toggle() {
        if (pVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.hide();
        }

        pVisible = false;

        pHideHandler.removeCallbacks(pShowPart2Runnable);
        pHideHandler.postDelayed(pHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        pContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        pVisible = true;

        pHideHandler.removeCallbacks(pHidePart2Runnable);
        pHideHandler.postDelayed(pShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    private void delayedHide(int delayMillis) {
        pHideHandler.removeCallbacks(pHideRunnable);
        pHideHandler.postDelayed(pHideRunnable, delayMillis);
    }
}
