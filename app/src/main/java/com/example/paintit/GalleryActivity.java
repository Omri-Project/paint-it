package com.example.paintit;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class GalleryActivity extends TouchDetector {
    Intent intent;
    MediaPlayer mediaPlayer;
    SharedPreferences preferences;
    boolean soundsEnabled;
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
        setContentView(R.layout.activity_gallery);
        TabLayout tabLayout = findViewById(R.id.tabs);
        ViewPager2 viewPager2 = findViewById(R.id.view_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(adapter);
        preferences = getSharedPreferences("maPrefs", Context.MODE_PRIVATE);
        soundsEnabled = preferences.getBoolean("SoundEffects", true);
        if (soundsEnabled) {
            mediaPlayer = MediaPlayer.create(this, R.raw.button_click);
        } else {
            mediaPlayer = new MediaPlayer();
        }
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0){
                    tab.setText("New Drawings");
                } else {
                    tab.setText("My drawings");
                }
            }
        }).attach();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.first_menu,menu);
        return true;
    }
    @Override
    public boolean onMenuOpened(int featureId, Menu menu){
        mediaPlayer.start();
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuSettings){
            intent = new Intent(this, SharedPrefsAtt.class);
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
            intent = new Intent(Intent.ACTION_SEND);
            mediaPlayer.start();
            releaseInstance();
            intent.setType("text/plain");
            if (intent != null){
                intent.putExtra(Intent.EXTRA_TEXT, "You have to check this new App! It lets you paint art by pixels!! https://tinyurl.com/PaintItApp");
                startActivity(Intent.createChooser(intent,"Share with"));
            }
        } else if (id == R.id.logout) {
            mediaPlayer.start();
            releaseInstance();
            preferences = getSharedPreferences("maPrefs", Context.MODE_PRIVATE);
            preferences.edit().putLong("connectedId", -1).apply();
            Intent in = new Intent(GalleryActivity.this , LoginActivity.class);
            startActivity(in);
        } else {
            mediaPlayer.start();
            releaseInstance();
            Toast.makeText(GalleryActivity.this,"This app is perfect. Shame on you!",Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}