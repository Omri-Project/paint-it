package com.example.paintit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends TouchDetector {
    Intent intent;
    MediaPlayer mediaPlayer;
    private long isLoggedIn = -1;
    private SharedPreferences preferences;
    private static final String PREFS_NAME = "maPrefs";
    private long loggedIn = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HelperDB helperDB = new HelperDB(getApplicationContext());
//        getApplicationContext().deleteDatabase("Database");


        preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean soundsEnabled = preferences.getBoolean("SoundEffects", true);
        boolean codeExecuted = preferences.getBoolean("CodeExecuted", false);
        loggedIn = preferences.getLong("connectedId", -1);


        if (!codeExecuted) {
            helperDB.addPredefinedPainting();
//            helperDB.addDevelopment(1,1);
            preferences.edit().putBoolean("CodeExecuted", true).apply();
        }

        if (soundsEnabled) {
            mediaPlayer = MediaPlayer.create(this, R.raw.button_click);
        }

        isLoggedIn = preferences.getLong("connectedId", -1);
    }

    public void goToGallery(View view) {
            if (isLoggedIn != -1) {
                intent = new Intent(this, GalleryActivity.class);
            } else {
                intent = new Intent(this, LoginActivity.class);
            }

            if (mediaPlayer != null) {
                mediaPlayer.start();
            }

            startActivity(intent);
            finish();

    }
}
