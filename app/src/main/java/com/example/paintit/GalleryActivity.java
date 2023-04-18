package com.example.paintit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class GalleryActivity extends AppCompatActivity {

    Intent intent;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        TabLayout tabLayout = findViewById(R.id.tabs);
        ViewPager2 viewPager2 = findViewById(R.id.view_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(adapter);
        mediaPlayer = MediaPlayer.create(this, R.raw.button_click);

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
            intent = new Intent(Intent.ACTION_SEND);
            mediaPlayer.start();
            releaseInstance();
            intent.setType("text/plain");
            if (intent != null){
                intent.putExtra(Intent.EXTRA_TEXT, "Try this cool app!");
                startActivity(Intent.createChooser(intent,"Share with"));
            }
        } else {
            mediaPlayer.start();
            releaseInstance();
            Toast.makeText(GalleryActivity.this,"This option is unavailable right now",Toast.LENGTH_SHORT).show();
        }
        return true;
    }


}