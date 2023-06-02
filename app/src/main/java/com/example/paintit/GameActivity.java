package com.example.paintit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
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
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.paintit.HelperDB;
import com.example.paintit.R;
import com.example.paintit.StringToArrayAdapter;
import com.example.paintit.Timer;
import com.example.paintit.TouchDetector;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class GameActivity extends TouchDetector {

    private GridLayout buttonGrid;
    private int numRows;
    private int numColumns;
    private int buttonSize;
    private int[][] pixels;
    Button previousButton = null;
    private int[][] isColored;
    private String[] colors;
    private HelperDB helperDB;
    private HorizontalScrollView colorsBar;
    private int chosenColor;
    private LinearLayout scrollBar;
    private Timer timer;
    private Date time;
    private int id;
    private int pixelNum;
    private int clickedNum;
    private Intent intent;
    private boolean soundsEnabled;
    private SharedPreferences preferences;
    HashMap<Button, Integer> originalColors = new HashMap<>();
    private MediaPlayer mediaPlayer;
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
    private float scaleFactor = 1.0f;
    private float lastScrollX, lastScrollY;

    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        intent = getIntent();
        id = intent.getIntExtra("id", 0);
        helperDB = new HelperDB(getApplicationContext());
        String pixelData = helperDB.getPaintingPixels(id);
        String colorsData = helperDB.getPaintingColors(id);
        preferences = getSharedPreferences("maPrefs", Context.MODE_PRIVATE);
        userId = preferences.getLong("connectedId", 0);
        Development devData = helperDB.getDevelopment(id, userId);
        String isColoredData = devData.getColoredData();
        clickedNum = devData.getNumClicked();
        pixels = StringToArrayAdapter.stringToArray(pixelData);
        colors = StringToArrayAdapter.stringToColorArray(colorsData);
        isColored = StringToArrayAdapter.stringToArray(isColoredData);
        numRows = pixels.length;
        numColumns = pixels[0].length;
        buttonGrid = findViewById(R.id.gridLayout);
        colorsBar = findViewById(R.id.colorsBar);
        scrollBar = new LinearLayout(this);
        chosenColor = 0;

        for (int i = 1; i < colors.length; i++) {
            final Button color = new Button(this);
            final GradientDrawable circle = new GradientDrawable();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(10, 10, 10, 10);
            color.setLayoutParams(params);

            ViewTreeObserver vto = color.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    color.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int width = color.getWidth();
                    int height = color.getHeight();
                    int size = Math.max(width, height);
                    size = (int) (size * 0.75);
                    color.getLayoutParams().width = size;
                    color.getLayoutParams().height = size;
                    color.requestLayout();
                    int cornerRadius = size / 2;
                    circle.setCornerRadius(cornerRadius);
                    circle.setStroke(3, Color.BLACK);
                    color.setBackground(circle);
                    int textSize = size / 8;
                    color.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                }
            });
            int originalColor = Color.parseColor(colors[i]);
            circle.setColor(originalColor);
            color.setPadding(10, 10, 10, 10);
            color.setText("" + (i));
            float[] hsv = new float[3];
            Color.colorToHSV(originalColor, hsv);
            hsv[2] = Math.min(1.0f, hsv[2] + 0.2f);
            int adjustedColor = Color.HSVToColor(hsv);
            circle.setColor(adjustedColor);
            if (hsv[2] < 0.5) {
                color.setTextColor(Color.WHITE);
            }
            color.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mediaPlayer != null) {
                        mediaPlayer.start();
                    }
                    int numColor = Integer.parseInt((String) color.getText());
                    if (numColor != chosenColor) {
                        chosenColor = numColor;
                        float[] hsv = new float[3];
                        int buttonColor = circle.getColor().getDefaultColor();
                        Color.colorToHSV(buttonColor, hsv);
                        float value = hsv[2];
                        if (value < 0.5) {
                            hsv[2] = Math.min(1.0f, value + 0.2f);
                        } else {
                            hsv[2] = Math.max(0.0f, value - 0.2f);
                        }
                        int newColor = Color.HSVToColor(hsv);
                        circle.setColor(newColor);
                        color.setBackground(circle);
                        if (previousButton != null) {
                            int previousColor = originalColors.get(previousButton);
                            ((GradientDrawable) previousButton.getBackground()).setColor(previousColor);
                        }
                        if (!originalColors.containsKey(color)) {
                            originalColors.put(color, buttonColor);
                        }
                        previousButton = color;
                    }
                }
            });

            scrollBar.addView(color);
        }

        colorsBar.addView(scrollBar);

        preferences = getSharedPreferences("maPrefs", Context.MODE_PRIVATE);
        soundsEnabled = preferences.getBoolean("SoundEffects", true);

        if (soundsEnabled) {
            mediaPlayer = MediaPlayer.create(this, R.raw.button_click);
        } else {
            mediaPlayer = new MediaPlayer();
        }
        buttonGrid.setRowCount(numRows);
        buttonGrid.setColumnCount(numColumns);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        buttonSize = screenWidth / 15;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                final Button button = new Button(this);
                button.setText("" + pixels[i][j]);
                button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                button.setPadding(0, 0, 0, 0);
                GradientDrawable shape = new GradientDrawable();
                if (isColored[i][j] == 0) {
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
                    shape.setColor(Color.parseColor(colors[pixels[i][j]]));
                    button.setBackground(shape);
                }
                button.setLayoutParams(new GridLayout.LayoutParams(new ViewGroup.LayoutParams(buttonSize, buttonSize)));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int text = Integer.parseInt((String) button.getText());
                        if (chosenColor == text) {
                            shape.setShape(GradientDrawable.RECTANGLE);
                            shape.setStroke(1, Color.BLACK);
                            button.setBackground(shape);
                            clickedNum++;
                            button.setBackgroundColor(Color.parseColor(colors[Integer.parseInt((String) button.getText())]));
                            button.setTextColor(Color.parseColor(colors[Integer.parseInt((String) button.getText())]));
                            button.setClickable(false);
                            if (mediaPlayer != null){
                                mediaPlayer.start();
                            }
                            Toast.makeText(GameActivity.this, ""+clickedNum, Toast.LENGTH_SHORT).show();
                            if (clickedNum == pixelNum){
                                Toast.makeText(GameActivity.this, "WOW OMG", Toast.LENGTH_SHORT).show();
                                Intent in = new Intent(GameActivity.this , StatisticsActivity.class);
                                startActivity(in);
                            }
                        }
                    }
                });
                buttonGrid.addView(button);
            }
        }
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleGestureListener());
        gestureDetector = new GestureDetector(this, new GestureListener());

            if (id == 1){
                pixelNum = 146;
            }
            else if (id == 2){
                pixelNum = 554;
            }
            else if (id == 3){
                pixelNum = 114;
            }
            else if (id == 4){
                pixelNum = 4105;
            }
            else if (id == 5){
                pixelNum = 1117;
            }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back_and_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuBack:
                saveDevelopmentStatus();
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                }
            return true;
            case R.id.menuReset:
                resetDialog();
                return true;
            default:
            return super.onOptionsItemSelected(item);
    }
    }

    @Override
    public void onBackPressed() {
        saveDevelopmentStatus();
        super.onBackPressed();
        Intent intent1 = new Intent(this, GalleryActivity.class);
        startActivity(intent1);
        if (soundsEnabled && mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onUserLeaveHint() {
        saveDevelopmentStatus();
        if (soundsEnabled && mediaPlayer != null) {
            mediaPlayer.start();
        }
        super.onUserLeaveHint();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            saveDevelopmentStatus();
            if (soundsEnabled && mediaPlayer != null) {
                mediaPlayer.start();
            }
        }
    }

    private void saveDevelopmentStatus() {
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
        helperDB.updateDevelopment(idPainting, userId, isColored, clickedNum, 0);
    }

    private class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private float scaleFactor = 1.0f;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 10.0f));
            buttonGrid.setScaleX(scaleFactor);
            buttonGrid.setScaleY(scaleFactor);
            return true;
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            buttonGrid.scrollBy(Math.round(distanceX), Math.round(distanceY));
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        return true;
    }
    private void resetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset Painting");
        builder.setMessage("Do you want to reset the painting?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                helperDB.deleteDevelopment(id);
                recreate();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancel dialog
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}

