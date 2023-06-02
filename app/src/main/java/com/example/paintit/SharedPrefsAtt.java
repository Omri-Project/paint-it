package com.example.paintit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ComponentName;
import android.content.pm.PackageManager;


public class SharedPrefsAtt extends TouchDetector {

    private static final String KEY_SOUND_EFFECTS = "SoundEffects";
    private static final String KEY_VIBRATIONS = "Vibrations";
    private static final String KEY_DARK_MODE = "DarkMode";

    private Switch soundEffectsSwitch;
    private Switch vibrationsSwitch;
    private Switch darkModeSwitch;

    private boolean soundEffectsEnabled;
    private boolean vibrationsEnabled;
    private boolean darkModeEnabled;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_prefs_att);

        preferences = getSharedPreferences("maPrefs", Context.MODE_PRIVATE);
        soundEffectsEnabled = preferences.getBoolean(KEY_SOUND_EFFECTS, true);
        vibrationsEnabled = preferences.getBoolean(KEY_VIBRATIONS, true);
        darkModeEnabled = preferences.getBoolean(KEY_DARK_MODE, false);
        soundEffectsSwitch = findViewById(R.id.switch_sound_effects);
        vibrationsSwitch = findViewById(R.id.switch_vibrations);
        darkModeSwitch = findViewById(R.id.switch_dark_mode);
        soundEffectsSwitch.setChecked(soundEffectsEnabled);
        vibrationsSwitch.setChecked(vibrationsEnabled);
        darkModeSwitch.setChecked(darkModeEnabled);
        soundEffectsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                soundEffectsEnabled = isChecked;
                preferences.edit().putBoolean(KEY_SOUND_EFFECTS, soundEffectsEnabled).apply();

            }
        });

        vibrationsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vibrationsEnabled = isChecked;
                preferences.edit().putBoolean(KEY_VIBRATIONS, vibrationsEnabled).apply();
            }
        });

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

