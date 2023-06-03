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
    Intent in;
    int time;
    TextView squares_value, time_value;
    int id;
    long userId;
    SharedPreferences preferences;
    HelperDB helperDB;

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
        helperDB = new HelperDB(getApplicationContext());
        button = findViewById(R.id.home_button);
        intent = new Intent(StatisticsActivity.this, GalleryActivity.class);
        squares_value = findViewById(R.id.squares_value);
        time_value = findViewById(R.id.time_value);
        in = getIntent();
        id = in.getIntExtra("id", 0);
        userId = preferences.getLong("connectedId", 0);
        Development dev = helperDB.getDevelopment(id, userId);
        time = dev.getTime();
        int[] times = {(time/1000)%60,(time/60000)%60, time/3600000};
        time_value.setText(""+times[2]+":"+times[1]+":"+times[0]);
        int clickedSquares = dev.getNumClicked();
        squares_value.setText(""+clickedSquares);
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