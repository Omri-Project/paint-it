package com.example.paintit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
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

public class LoginActivity extends AppCompatActivity {

    Button start;
    EditText username, password;
    TextView create_acc, show_lv;
    HelperDB helperDB;
    Intent intent;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        start = findViewById(R.id.start_btn);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        show_lv = findViewById(R.id.show_lv);
        create_acc = findViewById(R.id.create_acc);
        helperDB = new HelperDB(getApplicationContext());
        Intent in = new Intent(LoginActivity.this , GalleryActivity.class);
        Intent in1 = new Intent(LoginActivity.this , SignUp.class);
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


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
                releaseInstance();
                String checkUser = String.valueOf(username.getText());
                String checkPass = String.valueOf(password.getText());
                if (helperDB.ifExist(checkUser, checkPass)) {
                    Toast.makeText(LoginActivity.this, "Entered Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(in);
                    finish();
                }
                else{
                    Toast.makeText(LoginActivity.this, "Wrong Username Or Password Or User Doesn't Exist", Toast.LENGTH_SHORT).show();
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
                intent.putExtra(Intent.EXTRA_TEXT, "Try this cool app!");
                startActivity(Intent.createChooser(intent,"Share with"));
            }
        } else {
            mediaPlayer.start();
            releaseInstance();
            Toast.makeText(LoginActivity.this,"This option is unavailable right now",Toast.LENGTH_SHORT).show();
        }
        return true;
    }

}