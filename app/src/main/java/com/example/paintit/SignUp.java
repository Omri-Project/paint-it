package com.example.paintit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    Button start;
    EditText username, password, email;
    TextView login;
    HelperDB helperDB;
    Intent intent;
    MediaPlayer mediaPlayer;

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
        mediaPlayer = MediaPlayer.create(this, R.raw.button_click);

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
                if (helperDB.ifExist(username.getText().toString(), password.getText().toString()) == false){
                    helperDB.addNewUser(username.getText().toString() , password.getText().toString() , email.getText().toString());
                    Toast.makeText(SignUp.this, "Successfully Added New User", Toast.LENGTH_SHORT).show();
                    startActivity(in1);
                    finish();
                    mediaPlayer.start();
                    releaseInstance();
                }
                else {
                    mediaPlayer.start();
                    releaseInstance();
                    Toast.makeText(SignUp.this, "Username already exists, Please Try with a different name", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.first_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuSettings){
            mediaPlayer.start();
            releaseInstance();
            intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            finish();
        } else  if (id == R.id.menuRules){
            mediaPlayer.start();
            releaseInstance();
            intent = new Intent(this, RulesActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menuShare){
            mediaPlayer.start();
            releaseInstance();
            intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            if (intent != null){
                mediaPlayer.start();
                releaseInstance();
                intent.putExtra(Intent.EXTRA_TEXT, "Try this cool app!");
                startActivity(Intent.createChooser(intent,"Share with"));
            }
        } else {
            mediaPlayer.start();
            releaseInstance();
            Toast.makeText(SignUp.this,"This option is unavailable right now",Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}