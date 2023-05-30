package com.example.paintit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_prefs_att);

        // Retrieve the SharedPreferences instance
        preferences = getSharedPreferences("maPrefs", Context.MODE_PRIVATE);

        // Initialize the preference values
        soundEffectsEnabled = preferences.getBoolean(KEY_SOUND_EFFECTS, true);
        vibrationsEnabled = preferences.getBoolean(KEY_VIBRATIONS, true);
        darkModeEnabled = preferences.getBoolean(KEY_DARK_MODE, false);

        // Retrieve the Switch views from the layout
        soundEffectsSwitch = findViewById(R.id.switch_sound_effects);
        vibrationsSwitch = findViewById(R.id.switch_vibrations);
        darkModeSwitch = findViewById(R.id.switch_dark_mode);

        // Set the initial states of the switches based on the preference values
        soundEffectsSwitch.setChecked(soundEffectsEnabled);
        vibrationsSwitch.setChecked(vibrationsEnabled);
        darkModeSwitch.setChecked(darkModeEnabled);

        // Set the listeners for the switches to update the preference values
        soundEffectsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                soundEffectsEnabled = isChecked;
                // Save the updated preference value
                preferences.edit().putBoolean(KEY_SOUND_EFFECTS, soundEffectsEnabled).apply();

            }
        });

        vibrationsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vibrationsEnabled = isChecked;
                // Save the updated preference value
                preferences.edit().putBoolean(KEY_VIBRATIONS, vibrationsEnabled).apply();
            }
        });

        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                darkModeEnabled = isChecked;
                // Save the updated preference value
                preferences.edit().putBoolean(KEY_DARK_MODE, darkModeEnabled).apply();

                // Apply the dark/light mode to the activity
                if (darkModeEnabled) {
                    // Set dark mode
                    setTheme(R.style.Theme_PaintIt_Dark);
                } else {
                    // Set light mode
                    setTheme(R.style.Theme_PaintIt);
                }
                recreate(); // Recreate the activity to apply the new theme
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
            this.finish();
//            if (soundsEnabled && mediaPlayer != null) {
//                mediaPlayer.start();
//            }
//            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

