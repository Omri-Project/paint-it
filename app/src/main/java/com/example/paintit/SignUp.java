package com.example.paintit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp extends TouchDetector {

    Button start;
    EditText username, password, email;
    TextView login;
    HelperDB helperDB;
    Intent intent;
    MediaPlayer mediaPlayer;
    SharedPreferences preferences;
    Boolean isLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        start = findViewById(R.id.start_btn);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        email = findViewById(R.id.email);
        helperDB = new HelperDB(getApplicationContext());
        Intent in = new Intent(SignUp.this , LoginActivity.class);
        Intent in1 = new Intent(SignUp.this , GalleryActivity.class);
//        SharedPreferences preferences = getSharedPreferences("my_prefs", MODE_PRIVATE);
//        boolean soundsEnabled = preferences.getBoolean("sounds_enabled", true);
        preferences = PreferenceManager.getDefaultSharedPreferences(SignUp.this);
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

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(in);
                finish();
                mediaPlayer.start();
                releaseInstance();
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (username.getText().toString().equals("") || username.getText().toString().equals(null)
                        || password.getText().toString().equals("") || password.getText().toString().equals(null)
                        || email.getText().toString().equals("") || email.getText().toString().equals(null)) {
                    mediaPlayer.start();
                    releaseInstance();
                    Toast.makeText(SignUp.this, "Please Fill All Required Tabs", Toast.LENGTH_SHORT).show();
                }

                else if (helperDB.userIndex(username.getText().toString(), password.getText().toString()) == -1){
                    helperDB.addNewUser(username.getText().toString() , password.getText().toString() , email.getText().toString());
                    Toast.makeText(SignUp.this, "Successfully Added New User", Toast.LENGTH_SHORT).show();
//                    isLoggedIn = true;
//                    SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putBoolean("isLoggedIn", isLoggedIn);
//                    editor.apply();
                    long id = helperDB.userIndex(username.getText().toString(), password.getText().toString());
                    preferences = PreferenceManager.getDefaultSharedPreferences(SignUp.this);
                    preferences.edit().putLong("connectedId", id).apply();


                    startActivity(in1);
                    finish();
                    mediaPlayer.start();
                    releaseInstance();
                }
                else {
                    mediaPlayer.start();
                    releaseInstance();
                    Toast.makeText(SignUp.this, "Username Already Exists, Please Try With a Different Name", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}