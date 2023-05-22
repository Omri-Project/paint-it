package com.example.paintit;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.paintit.R;
import com.example.paintit.StringToArrayAdapter;

public class GameActivity extends TouchDetector {

    private GridLayout buttonGrid;
    MediaPlayer mediaPlayer;
    private int numRows = 17;
    private int numColumns = 10;
    private int buttonSize = 100;
    private int[][] pixels;
    private String[] colors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        HelperDB helperDB = new HelperDB(getApplicationContext());
        String pixelData = helperDB.getPaintingPixels(id);
        String colorsData = helperDB.getPaintingColors(id);
        pixels = StringToArrayAdapter.stringToArray(pixelData);
        colors = StringToArrayAdapter.stringToColorArray(colorsData);
        numRows = pixels.length;
        numColumns = pixels[0].length;
        buttonGrid = findViewById(R.id.gridLayout);
        SharedPreferences preferences = getSharedPreferences("my_prefs", MODE_PRIVATE);
        boolean soundsEnabled = preferences.getBoolean("sounds_enabled", true);

        if (soundsEnabled) {
            mediaPlayer = MediaPlayer.create(this, R.raw.button_click);
        } else {
            mediaPlayer = null;
        }

        // Update the grid layout to use the specified number of rows and columns
        buttonGrid.setRowCount(numRows);
        buttonGrid.setColumnCount(numColumns);

        // Calculate the button size based on the screen width and number of columns
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        buttonSize = screenWidth / numColumns;

        // Create the buttons and add them to the grid
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                final Button button = new Button(this);
                button.setText(String.valueOf(pixels[i][j]));
                button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                button.setPadding(0, 0, 0, 0);
                button.setBackgroundColor(Color.WHITE);
                button.setTextColor(Color.BLACK);
                GradientDrawable shape = new GradientDrawable();
                shape.setShape(GradientDrawable.RECTANGLE);
                shape.setStroke(2, Color.BLACK);
                button.setBackground(shape);
                button.setLayoutParams(new GridLayout.LayoutParams(
                        new ViewGroup.LayoutParams(buttonSize, buttonSize)));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        button.setBackgroundColor(Color.parseColor(colors[Integer.parseInt((String) button.getText())-1]));
                        button.setTextColor(Color.parseColor(colors[Integer.parseInt((String) button.getText())-1]));
                        mediaPlayer.start();
                        releaseInstance();
                    }
                });
                buttonGrid.addView(button);
            }
        }
    }

}