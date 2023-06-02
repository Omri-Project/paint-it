package com.example.paintit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StatisticsActivity extends TouchDetector {

    Button button;
    MediaPlayer mediaPlayer;
    Intent intent;
    TextView squares_value, time_value;
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
        setContentView(R.layout.activity_statistics);

        button = findViewById(R.id.home_button);
        intent = new Intent(StatisticsActivity.this, GalleryActivity.class);
        squares_value = findViewById(R.id.squares_value);
        time_value = findViewById(R.id.time_value);

        boolean soundsEnabled = preferences.getBoolean("SoundEffects", true);

        if (soundsEnabled) {
            mediaPlayer = MediaPlayer.create(this, R.raw.button_click);
        } else {
            mediaPlayer =  new MediaPlayer();
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
                releaseInstance();
                startActivity(intent);
                finish();
            }
        });

    }
}