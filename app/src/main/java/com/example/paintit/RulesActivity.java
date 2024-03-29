package com.example.paintit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class RulesActivity extends TouchDetector {
    Intent intent, itnt;
    MediaPlayer mediaPlayer;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences = getSharedPreferences("maPrefs", Context.MODE_PRIVATE);
        boolean darkModeEnabled = preferences.getBoolean("DarkMode", false);
        if (darkModeEnabled) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        intent  = new Intent(this, MainActivity.class);
        itnt = new Intent(this, GalleryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        boolean soundsEnabled = preferences.getBoolean("SoundEffects", true);

        if (soundsEnabled) {
            mediaPlayer = MediaPlayer.create(this, R.raw.button_click);
        } else {
            mediaPlayer =  new MediaPlayer();
        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuBack) {
            mediaPlayer.start();
            startActivity(itnt);

        }
        return true;
    }
}