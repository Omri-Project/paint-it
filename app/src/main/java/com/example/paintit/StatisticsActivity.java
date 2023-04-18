package com.example.paintit;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class StatisticsActivity extends AppCompatActivity {

    Button button;
    MediaPlayer mediaPlayer;

    TextView squares_value, time_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        squares_value = findViewById(R.id.squares_value);
        time_value = findViewById(R.id.time_value);
        mediaPlayer = MediaPlayer.create(this, R.raw.button_click);
    }
}