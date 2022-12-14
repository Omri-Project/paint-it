package com.example.paintit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    Intent intent;
    private Switch switchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Switch s1 = findViewById(R.id.switchSound);
        Switch s2 = findViewById(R.id.switchVibrate);
        Switch s3 = findViewById(R.id.switchDark);
        Switch s4 = findViewById(R.id.switchTimer);
        Switch s5 = findViewById(R.id.switchHighlight);
        Switch s6 = findViewById(R.id.switchChange);

        s1.setChecked(true);
        s3.setChecked(true);
        s5.setChecked(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuBack) {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }
}