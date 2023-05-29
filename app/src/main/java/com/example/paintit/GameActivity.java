package com.example.paintit;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
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
    boolean soundsEnabled;
    private ScaleGestureDetector scaleGestureDetector;
    private boolean isModeOne = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        helperDB = new HelperDB(getApplicationContext());
        String pixelData = helperDB.getPaintingPixels(id);
        String colorsData = helperDB.getPaintingColors(id);
        String isColoredData = helperDB.getDevelopment(id, 2);
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
            GradientDrawable circle = new GradientDrawable();
            circle.setColor(Color.parseColor(colors[i]));
//            circle.setShape(GradientDrawable.OVAL);
            circle.setCornerRadius(0f);
            color.setBackground(circle);
            color.setPadding(5,5,5,5);
            color.setText(""+(i));
            if (colors[i].equals("#000000")){
                color.setTextColor(Color.WHITE);
            }
            color.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    if (timer.getStart() == null){
//                        timer.setStart(Calendar.getInstance().getTime());
//                    }
                    mediaPlayer.start();
                    int numColor = Integer.parseInt((String) color.getText());
                    if (numColor != chosenColor) {
                        chosenColor = numColor;
                    }
                }
            });
            scrollBar.addView(color);
        }colorsBar.addView(scrollBar);
//        SharedPreferences preferences = getSharedPreferences("my_prefs", MODE_PRIVATE);
//        soundsEnabled = preferences.getBoolean("sounds_enabled", true);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(GameActivity.this);
        boolean soundsEnabled = preferences.getBoolean("SoundEffects", true);


        if (soundsEnabled) {
            mediaPlayer = MediaPlayer.create(this, R.raw.button_click);
        } else {
            mediaPlayer =  new MediaPlayer();
        }

        // Update the grid layout to use the specified number of rows and columns
        buttonGrid.setRowCount(numRows);
        buttonGrid.setColumnCount(numColumns);

        // Calculate the button size based on the screen width and number of columns
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        buttonSize = screenWidth / 15;

        // Create the buttons and add them to the grid
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                final Button button = new Button(this);
                button.setText(""+pixels[i][j]);
                button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                button.setPadding(0, 0, 0, 0);
                GradientDrawable shape = new GradientDrawable();
                if (isColored[i][j] == 0){
                    button.setBackgroundColor(Color.WHITE);
                    button.setTextColor(Color.BLACK);
                    shape.setStroke(1, Color.BLACK);
                } else {
                    button.setTextColor(Color.parseColor(colors[pixels[i][j]]));
                    button.setClickable(false);
                }
                shape.setShape(GradientDrawable.RECTANGLE);
                button.setBackground(shape);
                if (!button.isClickable()) {
                    //shape.setStroke(1, Color.BLACK);
                    shape.setColor(Color.parseColor(colors[pixels[i][j]]));
                    button.setBackground(shape);
                }
                button.setLayoutParams(new GridLayout.LayoutParams(new ViewGroup.LayoutParams(buttonSize, buttonSize)));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int text = Integer.parseInt((String) button.getText());
                        if (chosenColor == text){
                            shape.setShape(GradientDrawable.RECTANGLE);
                            shape.setStroke(1, Color.BLACK);
                            button.setBackground(shape);
                            button.setBackgroundColor(Color.parseColor(colors[Integer.parseInt((String) button.getText())]));
                            button.setTextColor(Color.parseColor(colors[Integer.parseInt((String) button.getText())]));
                            button.setClickable(false);
                            mediaPlayer.start();
                            releaseInstance();
                        }
                    }
                });
                buttonGrid.addView(button);
            }
        }
    } @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back_and_save,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            helperDB = new HelperDB(getApplicationContext());
            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numColumns; j++) {
                    Button button = (Button) buttonGrid.getChildAt(i * numColumns + j);
                    if (!button.isClickable()) {
                        isColored[i][j] = 1;
                    }
                }
            }
            intent = getIntent();
            int idPainting = intent.getIntExtra("id", 0);
            helperDB.updateDevelopment(idPainting, 2, isColored);
            this.finish();
            if (mediaPlayer != null){
                mediaPlayer.start();
            }
        return true;
    }
    @Override
    public void onBackPressed() {
        // Update the development status in the database
        helperDB = new HelperDB(getApplicationContext());
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                Button button = (Button) buttonGrid.getChildAt(i * numColumns + j);
                if (!button.isClickable()) {
                    isColored[i][j] = 1;
                }
            }
        }
        intent = getIntent();
        int idPainting = intent.getIntExtra("id", 0);
        helperDB.updateDevelopment(idPainting, 2, isColored);

        // Finish the activity
        super.onBackPressed();

        // Start the media player
        if (soundsEnabled && mediaPlayer != null) {
            mediaPlayer.start();
        }
    }
    @Override
    protected void onUserLeaveHint() {
        // Update the development status in the database
        helperDB = new HelperDB(getApplicationContext());
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                Button button = (Button) buttonGrid.getChildAt(i * numColumns + j);
                if (!button.isClickable()) {
                    isColored[i][j] = 1;
                }
            }
        }
        intent = getIntent();
        int idPainting = intent.getIntExtra("id", 0);
        helperDB.updateDevelopment(idPainting, 2, isColored);

        // Start the media player
        if (soundsEnabled && mediaPlayer != null) {
            mediaPlayer.start();
        }

        super.onUserLeaveHint();
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (!hasFocus) {
            // The window lost focus (Recent Apps button pressed)

            // Update the development status in the database
            helperDB = new HelperDB(getApplicationContext());
            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numColumns; j++) {
                    Button button = (Button) buttonGrid.getChildAt(i * numColumns + j);
                    if (!button.isClickable()) {
                        isColored[i][j] = 1;
                    }
                }
            }
            intent = getIntent();
            int idPainting = intent.getIntExtra("id", 0);
            helperDB.updateDevelopment(idPainting, 2, isColored);

            // Start the media player
            if (soundsEnabled && mediaPlayer != null) {
                mediaPlayer.start();
            }
        }
    }

}