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
import android.view.Menu;
import android.view.MenuItem;
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

import java.util.Calendar;
import java.util.Date;

public class GameActivity extends TouchDetector {

    private GridLayout buttonGrid;
    MediaPlayer mediaPlayer;
    private int numRows;
    private int numColumns;
    private int buttonSize;
    private int[][] pixels;
    private int[][] isColored;
    private String[] colors;
    HelperDB helperDB;
    HorizontalScrollView colorsBar;
    int chosenColor;
    LinearLayout scrollBar;
    Timer timer;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        helperDB = new HelperDB(getApplicationContext());
        String pixelData = helperDB.getPaintingPixels(id);
        String colorsData = helperDB.getPaintingColors(id);
        String isColoredData = helperDB.getDevelopment(id, 1);
        pixels = StringToArrayAdapter.stringToArray(pixelData);
        colors = StringToArrayAdapter.stringToColorArray(colorsData);
        isColored = StringToArrayAdapter.stringToArray(isColoredData);
        numRows = pixels.length;
        numColumns = pixels[0].length;
        buttonGrid = findViewById(R.id.gridLayout);
        colorsBar = findViewById(R.id.colorsBar);
        scrollBar = new LinearLayout(this);
        chosenColor = 0;
        for (int i = 1; i < colors.length; i++){
            final Button color = new Button(this);
            color.setText(""+(i));
            color.setBackground(getResources().getDrawable(R.drawable.circle));
            color.setBackgroundColor(Color.parseColor(colors[i]));
            color.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    if (timer.getStart() == null){
//                        timer.setStart(Calendar.getInstance().getTime());
//                    }
                    int numColor = Integer.parseInt((String) color.getText());
                    if (numColor != chosenColor) {
                        chosenColor = numColor;
                    }
                }
            });
            scrollBar.addView(color);
        }colorsBar.addView(scrollBar);
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
                button.setText(""+pixels[i][j]);
                button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                button.setPadding(0, 0, 0, 0);
                if (isColored[i][j] == 0){
                    button.setBackgroundColor(Color.WHITE);
                    button.setTextColor(Color.BLACK);
                } else {
                    button.setBackgroundColor(Color.parseColor(colors[pixels[i][j]]));
                    button.setTextColor(Color.parseColor(colors[pixels[i][j]]));
                    button.setClickable(false);
                }
                GradientDrawable shape = new GradientDrawable();
                shape.setShape(GradientDrawable.RECTANGLE);
                shape.setStroke(2, Color.BLACK);
                button.setBackground(shape);
                button.setLayoutParams(new GridLayout.LayoutParams(new ViewGroup.LayoutParams(buttonSize, buttonSize)));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int text = Integer.parseInt((String) button.getText());
                        if (chosenColor == text){
                            shape.setShape(GradientDrawable.RECTANGLE);
                            shape.setStroke(2, Color.BLACK);
                            button.setBackground(shape);
                            button.setBackgroundColor(Color.parseColor(colors[Integer.parseInt((String) button.getText())]));
                            button.setTextColor(Color.parseColor(colors[Integer.parseInt((String) button.getText())]));
                            button.setClickable(false);
                            mediaPlayer.start();
                            releaseInstance();
                        }
                    }
                }); if (!button.isClickable()){
                    isColored[i][j] = 1;
                }
                buttonGrid.addView(button);
            }
        }
    } @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuBack) {
            helperDB = new HelperDB(getApplicationContext());
            intent = getIntent();
            int idPainting = intent.getIntExtra("id", 0);
            helperDB.updateDevelopment(idPainting, 1, isColored);
            this.finish();
            mediaPlayer.start();
        }
        return true;
    }

}