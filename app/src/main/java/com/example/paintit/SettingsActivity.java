package com.example.paintit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.paintit.TouchDetector;

public class SettingsActivity extends TouchDetector {

    private Switch soundSwitch;
    private MediaPlayer mediaPlayer;
    private boolean soundsEnabled = true; // Default value if not found in SharedPreferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        soundSwitch = findViewById(R.id.sound_effects_switch);
        SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        soundSwitch.setChecked(preferences.getBoolean("sound_effects", true));
        SharedPreferences.Editor editor = preferences.edit();
        soundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putBoolean("sound_effects", soundSwitch.isChecked());
            }
        });

//
//        switchView = findViewById(R.id.sound_effects_switch);
//
//        switchView.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            soundsEnabled = isChecked;
//            saveSoundEffectsState(isChecked);
//        });
//
//        getSupportFragmentManager().beginTransaction()
//                .replace(android.R.id.content, new SettingsFragment())
//                .commit();
//
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        soundsEnabled = preferences.getBoolean("sound_effects", true);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putBoolean("sound_effects", false);
//        editor.apply();
//
//        if (soundsEnabled) {
//            mediaPlayer = MediaPlayer.create(this, R.raw.button_click);
//        } else {
//            mediaPlayer = MediaPlayer.create(this, null);
//        }
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
            this.finish();
            if (soundsEnabled && mediaPlayer != null) {
                mediaPlayer.start();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveSoundEffectsState(boolean enabled) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("sound_effects", enabled);
        editor.apply();
    }
}
