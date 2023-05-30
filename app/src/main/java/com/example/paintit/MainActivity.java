package com.example.paintit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends TouchDetector {
    Intent intent;
    MediaPlayer mediaPlayer;
    private boolean isLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HelperDB helperDB = new HelperDB(getApplicationContext());

        SharedPreferences preferences1 = getSharedPreferences("my_prefs", MODE_PRIVATE);
        boolean soundsEnabled = preferences1.getBoolean("sounds_enabled", true);

        SharedPreferences preferencess = getSharedPreferences("my_prefs", MODE_PRIVATE);
        boolean codeExecuted = preferencess.getBoolean("code_executed", false);

        if (!codeExecuted) {
            SharedPreferences.Editor editor = preferencess.edit();
            editor.putBoolean("code_executed", true);
            helperDB.addPredefinedPainting();
            editor.apply();
        }

        if (soundsEnabled) {
            mediaPlayer = MediaPlayer.create(this, R.raw.button_click);
        }

        SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        isLoggedIn = preferences.getBoolean("isLoggedIn", false);
    }

    public void goToGallery(View view) {
        if (isLoggedIn) {
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
