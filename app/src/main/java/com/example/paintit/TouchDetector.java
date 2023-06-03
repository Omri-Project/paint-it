package com.example.paintit;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class TouchDetector extends AppCompatActivity implements View.OnTouchListener {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View view = findViewById(android.R.id.content);
        view.setOnTouchListener(this);
        SharedPreferences preferences = getSharedPreferences("maPrefs", Context.MODE_PRIVATE);
        boolean soundsEnabled = preferences.getBoolean("SoundEffects", true);
        if (soundsEnabled) {
            mediaPlayer = MediaPlayer.create(this, R.raw.button_click);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            SharedPreferences preferences = getSharedPreferences("maPrefs", Context.MODE_PRIVATE);
            boolean soundEffectsEnabled = preferences.getBoolean("SoundEffects", true);
            if (soundEffectsEnabled && mediaPlayer != null){
                mediaPlayer.start();
            }
        }
        return true;
    }

}
