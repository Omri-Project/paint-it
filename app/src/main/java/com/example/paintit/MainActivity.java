package com.example.paintit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaPlayer = MediaPlayer.create(this, R.raw.button_click);
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
            intent = new Intent(this, SettingsActivity.class);
            mediaPlayer.start();
            releaseInstance();
            startActivity(intent);
            finish();
        } else  if (id == R.id.menuRules){
            intent = new Intent(this, RulesActivity.class);
            mediaPlayer.start();
            releaseInstance();
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
            Toast.makeText(MainActivity.this,"This option is unavailable right now",Toast.LENGTH_SHORT).show();
        }
        return true;
    }
    public void goToGallery (View view){
        intent = new Intent(this, LoginActivity.class);
        mediaPlayer.start();
        startActivity(intent);
        finish();
        releaseInstance();
    }
}