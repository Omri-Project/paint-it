package com.example.paintit;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class TouchDetector extends AppCompatActivity implements View.OnTouchListener {

    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;

    long timeV = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View view = findViewById(android.R.id.content);
        view.setOnTouchListener(this);
        //SharedPreferences preferences = getSharedPreferences("my_prefs", MODE_PRIVATE);
        //boolean soundsEnabled = preferences.getBoolean("sounds_enabled", true);
        SharedPreferences preferences = getSharedPreferences("maPrefs", Context.MODE_PRIVATE);
        boolean soundsEnabled = preferences.getBoolean("SoundEffects", true);
        if (soundsEnabled) {
            mediaPlayer = MediaPlayer.create(this, R.raw.button_click);
        }



//        if (soundsEnabled) {
//            mediaPlayer = MediaPlayer.create(this, R.raw.button_click);
//        } else {
//            mediaPlayer = null;
//        }
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
