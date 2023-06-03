package com.example.paintit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;


public class SharedPrefsAtt extends TouchDetector {

    private static final String KEY_SOUND_EFFECTS = "SoundEffects";
    private static final String KEY_DARK_MODE = "DarkMode";

    private Switch soundEffectsSwitch;
    private Switch darkModeSwitch;

    private boolean soundEffectsEnabled;
    boolean soundsEnabled;
    private boolean darkModeEnabled;
    MediaPlayer mediaPlayer;
    private SharedPreferences preferences;
    private void updateTheme() {
        if (darkModeEnabled) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences = getSharedPreferences("maPrefs", Context.MODE_PRIVATE);
        darkModeEnabled = preferences.getBoolean("DarkMode", false);
        if (darkModeEnabled) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_prefs_att);
        soundEffectsEnabled = preferences.getBoolean(KEY_SOUND_EFFECTS, true);
        soundEffectsSwitch = findViewById(R.id.switch_sound_effects);
        darkModeSwitch = findViewById(R.id.switch_dark_mode);
        soundEffectsSwitch.setChecked(soundEffectsEnabled);
        darkModeSwitch.setChecked(darkModeEnabled);
        soundEffectsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                soundEffectsEnabled = isChecked;
                preferences.edit().putBoolean(KEY_SOUND_EFFECTS, soundEffectsEnabled).apply();

            }
        });
        if (soundsEnabled) {
            mediaPlayer = MediaPlayer.create(this, R.raw.button_click);
        } else {
            mediaPlayer = new MediaPlayer();
        }

        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                darkModeEnabled = isChecked;
                preferences.edit().putBoolean(KEY_DARK_MODE, darkModeEnabled).apply();
                updateTheme();
                recreate();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuBack) {
            Intent in = new Intent(SharedPrefsAtt.this , GalleryActivity.class);
            startActivity(in);
            this.finish();

        }
        return super.onOptionsItemSelected(item);
    }
}

