package com.example.paintit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.IDN;

public class LoginActivity extends TouchDetector {

    Button start;
    EditText username, password;
    TextView create_acc, show_lv;
    HelperDB helperDB;
    Intent intent;
    MediaPlayer mediaPlayer;
    boolean isLoggedIn;
    SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        start = findViewById(R.id.start_btn);
        preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        show_lv = findViewById(R.id.show_lv);
        create_acc = findViewById(R.id.create_acc);
//        isLoggedIn = false;
        helperDB = new HelperDB(getApplicationContext());
        Intent in = new Intent(LoginActivity.this , GalleryActivity.class);
        Intent in1 = new Intent(LoginActivity.this , SignUp.class);
//        boolean soundsEnabled = preferences.getBoolean("sounds_enabled", true);
        boolean soundsEnabled = preferences.getBoolean("SoundEffects", true);


        if (soundsEnabled) {
            mediaPlayer = MediaPlayer.create(this, R.raw.button_click);
        } else {
            mediaPlayer =  new MediaPlayer();
        }
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mediaPlayer.start();
                releaseInstance();
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mediaPlayer.start();
                releaseInstance();
            }
        });


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
                releaseInstance();
                String checkUser = String.valueOf(username.getText());
                String checkPass = String.valueOf(password.getText());
                long id = helperDB.userIndex(checkUser, checkPass);
                if (id!=-1) {
                    Toast.makeText(LoginActivity.this, "Entered Successfully", Toast.LENGTH_SHORT).show();
//                    SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putLong("idLoggedIn", id);
//                    editor.apply();
                    preferences.edit().putLong("connectedId", id).apply();
                    startActivity(in);
                    finish();
                }
                else{
                    Toast.makeText(LoginActivity.this, "Wrong Details Or User Doesn't Exist", Toast.LENGTH_SHORT).show();
                }

            }
        });

        show_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
                releaseInstance();
                Toast.makeText(LoginActivity.this, helperDB.getAllUserDetails(), Toast.LENGTH_SHORT).show();
            }
        });


        create_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
                releaseInstance();

                startActivity(in1);
                finish();
            }
        });


    }

}