package com.example.paintit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StatisticsActivity extends AppCompatActivity {

    Button button;
    MediaPlayer mediaPlayer;
    Intent intent;
    TextView squares_value, time_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        button = findViewById(R.id.home_button);
        intent = new Intent(StatisticsActivity.this, GalleryActivity.class);
        squares_value = findViewById(R.id.squares_value);
        time_value = findViewById(R.id.time_value);
        mediaPlayer = MediaPlayer.create(this, R.raw.button_click);

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