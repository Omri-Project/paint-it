package com.example.paintit;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class HelperDB extends SQLiteOpenHelper {

    // database finals
    static final String DB_NAME = "Database";
    static final int DB_VERSION = 1;

    // Table finals
    public static final String USERS = "Users";
    public static final String USER_ID = "id";
    public static final String USERNAME = "UserName";
    public static final String PASSWORD = "Password";
    public static final String EMAIL = "Email";

    private static final String USER_TABLE = "CREATE TABLE IF NOT EXISTS " + USERS + " (" + USER_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + USERNAME + " TEXT, "
            + PASSWORD + " TEXT, "
            + EMAIL + " TEXT " + ") ";

    private static final String[] USER_COLUMN = {USER_ID, USERNAME, PASSWORD, EMAIL};

    // Statistics

    private static final String STATISTICS = "statistics";
    private static final String STATISTICS_ID = "id";
    private static final String STATISTICS_TIME = "time";
    private static final String STATISTICS_SQUARES_PAINTED = "squares_painted";

    private static final String STATISTICS_TABLE = " CREATE TABLE IF NOT EXISTS " + STATISTICS + " (" +
            STATISTICS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            STATISTICS_TIME + " INTEGER NOT NULL, " +
            STATISTICS_SQUARES_PAINTED + " INTEGER NOT NULL);";

    private static final String[] STATISTICS_COLUMN = {STATISTICS_ID, STATISTICS_TIME, STATISTICS_SQUARES_PAINTED};

    private static final String PAINTING = "painting";
    private static final String PAINTING_ID = "paintingId";
    private static final String PAINTING_NAME = "paintingName";
    private static final String PAINTING_PIXELS = "paintingPixels";
    private static final String PAINTING_COLORS = "paintingColors";

    private static final String PAINTING_TABLE = " CREATE TABLE IF NOT EXISTS " + PAINTING + " (" +
            PAINTING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PAINTING_NAME + " TEXT NOT NULL, " +
            PAINTING_PIXELS + " TEXT NOT NULL, " +
            PAINTING_COLORS + " TEXT NOT NULL);";

    private static final String[] PAINTING_COLUMN = {PAINTING_ID, PAINTING_NAME, PAINTING_PIXELS, PAINTING_COLORS};

    private static final String DEVELOPMENT = "development";
    private static final String DEVELOPMENT_PAINTING = "developmentPainting";
    private static final String DEVELOPMENT_USER = "developmentUser";
    private static final String DEVELOPMENT_COLORED = "developmentColored";

    private static final String DEVELOPMENT_TABLE = " CREATE TABLE IF NOT EXISTS " + DEVELOPMENT + "("
            + DEVELOPMENT_PAINTING + " INTEGER,"
            + DEVELOPMENT_USER + " INTEGER,"
            + DEVELOPMENT_COLORED + " TEXT,"
            + "FOREIGN KEY (" + DEVELOPMENT_PAINTING + ") REFERENCES PAINTING(PAINTING_ID),"
            + "FOREIGN KEY (" + DEVELOPMENT_USER + ") REFERENCES USERS(USER_ID)"
            + ");";

    private static final String[] DEVELOPMENT_COLUMN = {DEVELOPMENT_PAINTING, DEVELOPMENT_USER, DEVELOPMENT_COLORED};


    public HelperDB(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_TABLE);
        db.execSQL(STATISTICS_TABLE);
        db.execSQL(PAINTING_TABLE);
        db.execSQL(DEVELOPMENT_TABLE);
    }

    public void addPainting(String paintingName, String paintingPixels, String paintingColors) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PAINTING_NAME, paintingName);
        values.put(PAINTING_PIXELS, paintingPixels);
        values.put(PAINTING_COLORS, paintingColors);
        db.insert(PAINTING, null, values);

        db.close();
    }
    public void addDevelopment (int paintingId, int userId){
        int[][] paintingPixels = StringToArrayAdapter.stringToArray(getPaintingPixels(paintingId));
        String colored = "";
        for (int i = 0; i < paintingPixels.length; i++){
            for (int j = 0; j < paintingPixels[i].length; j++){
                if (paintingPixels[i][j]==0){
                    colored = colored + "1,";
                } else {
                    colored = colored + "0,";
                }
            } colored = colored + ";";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DEVELOPMENT_PAINTING, paintingId);
        values.put(DEVELOPMENT_USER, userId);
        values.put(DEVELOPMENT_COLORED, colored);
        db.insert(DEVELOPMENT, null, values);
        db.close();
    }
    public String getDevelopment(int paintingId, int userId) {
        String pixels = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {DEVELOPMENT_COLORED};
        String selection = DEVELOPMENT_PAINTING + " =? AND "+ DEVELOPMENT_USER + " =? ";
        String[] selectionArgs = {""+paintingId, ""+userId};

        Cursor cursor = db.query(DEVELOPMENT, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            pixels = cursor.getString(cursor.getColumnIndexOrThrow(DEVELOPMENT_COLORED));
            cursor.close();
        }
        db.close();
        if (pixels==null){
            addDevelopment(paintingId, userId);
        }
        return pixels;
    }

    public void addPredefinedPainting() {
        addPainting("Duck", "0,0,0,0,0,1,1,1,1,0,0,0,0,0,0,0,;0,0,0,0,1,2,2,2,2,1,0,0,0,0,0,0,;0,0,0,1,2,2,2,2,2,2,1,0,0,0,0,0,;0,1,1,3,3,2,1,2,2,2,1,0,0,0,0,0,;1,3,3,3,3,2,2,2,2,2,1,0,0,0,0,0,;0,1,3,3,3,3,2,2,2,1,1,1,0,0,1,0,;0,0,1,1,1,2,2,2,1,2,2,2,1,1,2,1,;0,0,0,1,2,2,2,2,2,2,2,2,2,2,2,1,;0,0,1,2,2,2,2,1,2,2,2,2,1,2,2,1,;0,0,1,2,2,2,2,2,1,2,2,1,2,2,2,1,;0,0,1,2,2,2,2,2,1,1,1,2,2,2,1,0,;0,0,0,1,2,2,2,2,2,2,2,2,2,1,0,0,;0,0,0,0,1,2,2,2,2,2,2,2,1,0,0,0,;0,0,0,0,0,1,1,1,1,1,1,1,0,0,0,0,;", "#FFFFFF,#000000,#ffce01,#ff8101,");
        addPainting("Logo", "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,3,3,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,3,3,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,3,3,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,1,4,4,4,4,4,4,4,4,4,1,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,1,1,1,1,1,1,1,1,1,4,4,4,4,4,4,4,4,4,1,1,1,1,1,1,1,1,1,0,0,0,;0,0,1,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,5,5,5,5,5,5,5,5,5,1,0,0,;0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,5,5,5,5,5,5,5,5,5,5,5,1,0,0,;0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,5,5,5,5,5,5,5,5,1,0,0,;0,0,1,0,0,0,0,0,0,0,0,0,0,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,1,0,0,;0,0,1,0,0,0,0,0,0,0,0,0,0,0,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,1,0,0,;0,0,1,0,0,0,0,0,0,0,0,0,0,0,6,0,0,0,6,5,5,5,5,5,5,5,5,5,5,5,1,0,0,;0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,5,5,5,5,5,5,5,5,5,5,5,5,1,0,0,;0,0,1,0,0,0,0,0,0,0,0,0,0,6,0,0,0,0,0,6,5,5,5,5,5,5,5,5,5,5,1,0,0,;0,0,1,0,0,0,0,0,0,0,0,0,0,0,6,6,6,6,6,0,0,0,5,5,5,5,5,5,5,5,1,0,0,;0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,5,5,5,5,5,5,5,5,5,5,5,5,5,5,1,0,0,;0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,1,0,0,;0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,5,5,5,5,5,5,5,5,5,5,5,1,0,0,;0,0,1,0,0,0,0,0,0,0,0,0,0,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,1,0,0,;0,1,4,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,4,1,0,;1,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,1,;0,1,1,1,1,4,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,4,1,1,1,1,0,;0,0,0,0,0,1,3,2,1,0,0,0,0,0,1,2,3,3,1,0,0,0,0,0,1,2,3,1,0,0,0,0,0,;0,0,0,0,0,1,3,2,1,0,0,0,0,0,1,2,3,3,1,0,0,0,0,0,1,2,3,1,0,0,0,0,0,;0,0,0,0,0,1,3,2,1,0,0,0,0,0,1,2,3,3,1,0,0,0,0,0,1,2,3,1,0,0,0,0,0,;0,0,0,0,1,3,2,2,7,1,1,1,1,1,7,7,7,7,7,1,1,1,1,1,7,2,2,3,1,0,0,0,0,;0,0,0,0,1,3,2,7,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,7,2,3,1,0,0,0,0,;0,0,0,0,1,3,2,7,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,7,2,3,1,0,0,0,0,;0,0,0,1,3,2,2,1,1,1,1,1,1,1,7,7,7,7,7,1,1,1,1,1,1,1,2,2,3,1,0,0,0,;0,0,0,1,3,2,1,0,0,0,0,0,0,0,1,2,2,3,1,0,0,0,0,0,0,0,1,2,3,1,0,0,0,;0,0,0,1,3,2,1,0,0,0,0,0,0,0,1,2,2,3,1,0,0,0,0,0,0,0,1,2,3,1,0,0,0,;0,0,0,1,3,2,1,0,0,0,0,0,0,0,1,2,2,3,1,0,0,0,0,0,0,0,1,2,3,1,0,0,0,;0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,;", "#FFFFFF,#000000,#875326,#5A3F26,#A16F44,#A1A1A1,#FFF200,#6E4D30,");
        addPainting("Mushroom", "0,0,0,0,0,1,1,1,1,1,1,0,0,0,0,0,;0,0,0,1,1,2,2,2,2,0,0,1,1,0,0,0,;0,0,1,0,0,2,2,2,2,0,0,0,0,1,0,0,;0,1,0,0,2,2,2,2,2,2,0,0,0,0,1,0,;0,1,0,2,2,0,0,0,0,2,2,0,0,0,1,0,;1,2,2,2,0,0,0,0,0,0,2,2,2,2,2,1,;1,2,2,2,0,0,0,0,0,0,2,2,0,0,2,1,;1,0,2,2,0,0,0,0,0,0,2,0,0,0,0,1,;1,0,0,2,2,0,0,0,0,2,2,0,0,0,0,1,;1,0,0,2,2,2,2,2,2,2,2,2,0,0,2,1,;1,0,2,2,1,1,1,1,1,1,1,1,2,2,2,1,;0,1,1,1,0,0,1,0,0,1,0,0,1,1,1,0,;0,0,1,0,0,0,1,0,0,1,0,0,0,1,0,0,;0,0,1,0,0,0,0,0,0,0,0,0,0,1,0,0,;0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,;0,0,0,0,1,1,1,1,1,1,1,1,0,0,0,0,;", "#FFFFFF,#000000,#FE0000,");
        addPainting("Frog", "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,3,3,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,4,4,4,3,3,3,3,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,3,3,3,3,4,4,3,3,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,3,4,4,3,3,3,4,4,4,3,3,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,3,3,3,3,4,4,1,1,4,4,4,4,4,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,3,3,3,3,3,3,4,4,4,1,1,1,1,1,1,1,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,3,3,3,3,3,3,3,4,4,4,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,3,3,3,3,3,3,4,4,4,4,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,3,3,3,3,3,3,4,4,4,4,4,1,0,0,0,0,0,0,0,0,1,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,3,3,3,3,3,3,3,4,4,4,4,4,1,0,0,0,0,0,0,0,0,0,5,5,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,3,3,3,3,3,3,4,4,4,4,4,4,1,0,0,0,0,0,0,0,0,5,6,6,6,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,3,3,3,3,3,3,4,4,4,4,4,4,4,1,0,0,0,0,0,0,0,0,5,6,6,6,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,3,3,3,3,3,3,4,4,4,4,4,4,4,1,0,0,0,0,0,0,0,0,5,6,6,6,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,3,3,3,3,3,3,4,4,4,4,4,4,4,4,1,0,0,0,0,0,0,0,0,5,5,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,3,3,3,3,3,3,4,4,4,4,4,4,4,4,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,3,3,3,3,3,4,4,4,4,4,4,4,4,4,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,3,3,3,3,4,3,3,3,4,4,4,4,4,4,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,4,4,3,4,3,3,3,3,3,4,4,4,4,4,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,4,4,3,3,3,3,3,3,3,3,4,4,4,4,4,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,3,3,1,0,0,0,0,1,1,1,1,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,4,3,3,3,3,3,3,3,3,4,4,4,4,4,4,1,4,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,3,3,3,3,1,0,0,1,7,7,7,7,1,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,4,3,3,3,3,3,4,4,4,4,4,4,4,4,4,1,4,4,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,3,3,1,1,1,1,1,7,7,5,5,7,1,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,4,3,3,3,4,4,4,4,4,4,4,4,4,4,4,4,1,4,4,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,3,3,1,7,7,8,8,8,8,7,8,5,7,7,1,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,9,2,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,1,4,4,4,4,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,3,3,1,7,8,8,8,8,8,8,10,10,7,7,7,1,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,0,0,1,6,2,9,6,2,9,6,2,9,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,3,3,3,1,10,10,10,10,10,7,7,7,10,10,7,7,7,1,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,1,1,1,1,3,3,3,3,3,3,3,3,3,3,3,1,1,1,1,9,2,6,9,2,6,9,2,6,9,2,9,4,4,4,4,4,4,4,4,4,4,4,4,4,2,2,1,1,0,0,0,0,0,0,1,1,1,3,3,3,1,7,10,10,10,5,5,5,5,5,7,7,7,7,7,7,1,0,0,0,0,0,;0,0,0,0,0,0,0,0,1,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,1,1,1,1,1,1,2,9,6,2,9,2,6,2,9,6,9,2,6,2,9,6,9,2,6,2,9,6,9,1,1,1,1,1,1,3,3,3,3,3,3,1,7,10,10,5,0,0,0,0,0,5,7,7,7,7,7,7,1,1,1,1,0,;0,0,0,0,0,0,1,1,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,1,1,1,1,1,9,6,9,2,6,2,9,6,9,2,6,2,9,6,9,2,6,2,9,1,1,1,3,3,3,3,3,3,3,1,7,7,7,1,0,0,0,0,0,0,0,5,5,7,7,7,7,7,7,7,7,1,;0,0,0,0,0,1,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,3,3,3,3,3,3,3,3,3,3,1,7,10,2,5,0,0,0,0,0,0,0,0,0,5,7,7,7,7,5,5,7,1,;0,0,0,0,0,1,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,1,1,1,1,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,1,2,10,2,5,0,0,0,0,0,0,0,0,0,0,5,2,2,7,8,5,7,1,;0,0,0,0,1,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,1,1,11,11,11,11,1,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,1,2,10,2,2,5,0,0,0,0,0,0,0,0,0,0,1,2,7,7,7,7,1,;0,0,0,1,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,1,1,1,1,3,3,3,3,3,3,3,3,3,3,3,3,3,3,1,11,12,12,12,12,12,12,1,3,3,3,3,3,3,3,3,3,3,3,3,3,3,1,2,2,2,2,5,0,0,0,0,0,0,0,0,0,0,1,2,2,7,7,7,1,;0,0,0,1,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,1,1,11,11,11,11,1,1,3,3,3,3,3,3,3,3,3,3,3,1,11,12,12,12,12,12,12,12,12,1,3,3,3,3,3,3,3,3,3,3,3,3,3,1,2,2,2,2,2,5,0,0,0,0,0,0,0,0,0,0,1,2,7,7,1,0,;0,0,1,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,1,11,11,12,12,12,12,12,11,1,3,3,1,1,1,1,1,1,1,1,5,11,12,12,5,5,5,12,12,12,1,3,3,3,3,3,3,3,3,3,3,3,3,3,3,1,2,2,2,2,2,5,5,0,0,0,0,0,0,0,0,1,2,8,7,1,0,;0,1,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,1,11,12,12,11,5,5,5,12,12,1,1,1,2,2,2,2,2,8,8,8,8,11,12,12,5,5,5,5,5,12,11,1,3,3,3,3,3,3,3,3,3,3,3,3,3,5,2,2,2,10,2,2,10,5,0,0,0,0,0,0,0,1,2,8,7,1,0,;0,1,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,1,11,12,5,5,5,5,5,12,12,11,11,8,8,8,8,8,8,8,8,8,8,11,12,12,12,12,12,5,5,12,11,1,3,3,3,3,3,3,3,3,3,3,3,3,1,0,5,2,2,2,8,8,2,10,5,5,0,0,0,0,0,1,2,8,7,1,0,;1,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,1,11,12,12,5,5,5,11,12,12,12,11,11,8,8,8,8,8,8,8,8,8,8,11,12,12,12,12,12,12,12,12,11,1,3,3,3,3,3,3,3,3,3,3,3,1,0,0,0,5,2,7,7,8,8,2,2,10,5,0,0,0,1,2,2,7,1,0,0,;1,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,1,11,12,12,12,12,12,12,12,12,12,11,11,11,8,8,8,8,8,8,8,8,11,11,11,12,12,12,12,12,12,12,11,1,3,3,3,3,3,3,3,3,3,3,1,0,0,0,1,5,7,7,10,7,7,8,8,7,7,1,1,1,2,2,8,7,1,0,0,;1,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,1,11,12,12,12,12,12,12,12,12,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,12,12,12,12,12,11,11,1,3,3,3,3,3,3,3,1,1,1,0,0,0,1,2,5,5,7,5,10,8,8,8,8,7,7,10,10,10,10,7,7,1,0,0,;1,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,1,11,11,12,12,12,12,12,12,11,11,11,11,11,11,11,5,5,5,5,5,5,5,5,11,11,11,11,11,11,11,11,11,11,1,3,3,3,3,1,1,0,0,0,0,0,1,5,2,2,2,5,1,1,10,10,10,10,10,10,10,10,10,7,7,1,0,0,0,;0,1,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,1,11,11,11,12,12,12,12,11,11,11,11,11,11,5,5,11,13,13,13,13,13,13,11,5,5,11,11,11,11,11,11,11,11,11,1,1,1,1,0,0,0,0,0,0,1,14,2,5,2,2,5,5,0,1,1,1,1,10,10,10,10,10,10,1,0,0,0,0,;0,1,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,1,11,11,11,11,11,11,11,11,11,11,5,5,5,13,13,13,13,13,13,13,13,13,13,13,13,5,5,11,11,11,11,11,11,11,11,1,0,0,0,0,0,0,0,1,14,14,2,2,5,5,5,0,5,0,0,0,0,1,1,1,1,1,1,0,0,0,0,0,;0,0,1,1,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,1,8,11,11,11,11,11,11,11,11,11,5,11,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,11,5,5,5,11,11,5,5,6,6,1,1,0,0,0,0,1,14,14,2,2,1,1,0,5,0,0,5,5,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,1,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,1,8,11,11,11,11,11,11,5,5,5,11,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,11,5,5,5,5,6,6,6,6,1,0,0,1,14,14,2,2,1,0,0,0,5,0,0,0,0,5,5,0,0,1,5,0,0,0,0,0,0,0,;0,0,0,0,0,1,1,3,3,3,3,3,3,3,3,3,3,3,3,3,3,1,8,11,11,11,11,11,5,11,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,11,5,5,11,6,6,2,2,5,1,14,14,2,2,1,0,0,0,0,0,5,0,0,0,0,0,5,5,6,6,5,0,0,0,0,0,0,;0,0,0,0,0,0,0,1,1,3,3,3,3,3,3,3,3,3,3,3,1,8,8,5,5,11,5,5,11,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,11,11,11,11,2,2,2,5,14,14,10,2,1,0,0,0,0,0,0,5,0,0,0,0,0,0,1,6,6,6,5,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,1,1,1,3,3,3,3,3,3,3,3,1,8,8,5,5,5,11,11,11,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,11,11,13,13,11,11,11,11,2,2,5,8,8,10,2,1,0,0,0,0,0,0,0,0,5,5,1,0,0,0,1,6,6,6,5,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,8,2,8,5,5,11,11,11,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,11,11,11,11,13,13,11,11,6,11,5,8,8,8,2,1,0,0,0,0,0,0,0,0,5,6,6,6,1,0,0,0,1,5,5,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,1,8,8,8,11,11,11,11,11,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,11,11,11,11,13,13,11,11,5,5,14,8,8,2,5,0,0,0,0,0,0,0,0,0,5,6,6,6,1,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,8,8,8,8,8,11,11,11,13,13,13,13,13,13,13,11,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,11,11,13,13,13,11,5,14,8,8,8,10,5,5,0,0,0,0,0,0,0,0,0,5,6,6,6,1,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,8,8,8,2,2,11,11,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,11,11,5,14,8,14,10,10,5,2,2,1,0,0,0,0,0,0,0,0,0,5,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,2,8,8,2,8,8,13,13,13,13,13,11,11,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,11,5,14,8,14,10,10,5,2,2,2,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,8,2,2,8,8,8,8,13,13,13,13,13,13,11,11,13,13,13,13,13,13,13,13,13,12,13,13,13,13,13,13,13,13,13,13,11,5,14,10,14,10,5,5,2,2,2,2,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,8,8,8,8,8,8,8,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,12,12,13,13,13,13,13,13,5,14,10,14,14,5,11,2,6,6,6,2,2,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,6,6,8,2,8,8,11,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,12,12,13,13,13,13,13,5,14,10,14,10,5,11,11,2,6,6,6,2,2,5,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,2,8,8,8,8,11,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,13,5,14,14,14,10,5,11,11,11,2,2,6,6,2,2,5,8,8,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,2,2,2,2,8,11,13,11,13,13,13,13,13,13,13,13,13,13,13,12,12,12,12,12,12,12,12,12,13,13,11,5,14,14,14,5,5,11,11,11,11,11,2,2,2,2,2,5,6,8,8,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,2,2,2,2,8,11,13,13,13,13,13,13,13,13,13,13,12,12,12,12,12,12,12,12,12,12,12,12,12,11,5,5,5,5,5,11,11,11,11,11,11,11,8,2,2,2,5,8,8,8,2,2,8,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,2,2,2,2,2,11,13,13,13,13,13,13,13,13,13,12,12,12,12,12,12,12,12,12,12,12,12,15,5,5,11,11,11,11,11,11,11,11,11,11,11,11,8,8,2,2,8,8,8,2,2,2,2,6,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,2,2,2,2,11,11,13,13,13,12,13,13,13,12,12,12,12,12,12,12,12,12,12,12,12,12,12,5,11,11,11,11,11,11,11,11,11,11,11,11,12,11,11,8,8,8,2,8,8,2,2,2,2,6,6,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,2,2,6,11,11,11,13,13,13,13,13,13,12,12,12,12,12,12,12,12,12,12,12,12,12,12,5,5,11,11,11,11,11,11,11,11,11,11,11,12,12,11,11,8,8,8,8,8,8,8,2,6,8,8,8,8,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,2,2,2,11,11,11,13,13,13,13,13,12,12,12,12,12,12,12,12,12,12,12,12,12,12,11,5,11,5,11,11,11,11,11,11,11,11,11,5,12,12,11,11,8,8,8,8,11,11,11,8,8,8,8,8,8,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,2,2,2,11,11,11,13,13,13,13,12,12,12,12,12,12,12,12,12,12,12,12,12,12,11,5,11,11,11,11,11,11,11,11,11,11,11,5,12,12,11,11,8,8,11,11,11,11,11,2,11,8,8,2,2,8,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,6,6,2,11,11,11,11,13,13,13,12,12,12,12,12,12,12,12,12,12,12,12,12,11,5,11,5,5,11,11,11,11,11,11,11,5,5,12,12,12,11,5,8,11,11,11,2,2,11,11,11,11,8,2,2,8,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,6,6,2,11,11,11,11,11,13,12,12,12,12,12,12,12,12,12,12,12,12,12,11,5,5,11,11,11,11,11,11,11,11,11,5,12,12,12,12,12,5,5,11,11,11,11,2,2,11,11,11,11,8,8,8,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,2,2,11,11,11,11,11,13,12,12,12,12,12,12,12,12,12,12,12,12,11,5,10,16,5,11,11,11,11,11,11,5,5,12,12,12,12,12,5,11,11,11,11,11,11,11,11,11,11,11,11,8,8,8,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,2,2,11,11,11,11,11,13,12,12,12,12,12,12,12,12,12,12,12,12,5,10,10,10,16,5,5,5,5,5,5,12,12,12,12,12,13,5,11,11,11,11,11,11,11,11,11,11,11,2,11,11,2,2,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,2,11,11,11,11,11,11,12,12,12,12,12,12,12,12,12,12,12,5,10,10,10,10,5,11,11,11,11,12,12,12,12,12,12,11,5,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,2,2,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,2,11,11,11,11,11,11,11,12,12,12,12,12,12,12,12,12,5,14,10,10,10,5,11,11,11,12,12,12,12,12,12,12,12,5,11,11,11,11,11,11,11,11,11,11,11,11,8,8,8,11,2,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,5,2,2,11,11,11,11,11,11,12,12,12,12,12,12,12,12,5,14,10,14,10,5,11,11,12,12,12,12,12,12,12,12,12,12,5,11,11,11,11,11,11,11,11,11,11,11,8,8,2,2,2,2,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,5,2,2,11,11,11,8,11,11,12,12,12,12,12,12,5,14,10,14,14,5,11,12,12,12,12,12,12,12,12,12,12,12,12,5,11,11,11,11,11,11,11,11,11,11,11,8,2,2,2,2,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,2,5,2,2,2,11,11,11,11,11,8,12,12,12,12,5,14,10,14,10,5,12,12,12,12,12,12,12,12,12,12,12,12,11,5,11,11,11,11,11,11,11,11,11,11,11,11,11,2,2,2,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,2,2,5,5,2,2,11,11,8,8,8,8,11,12,12,5,14,14,10,5,12,12,12,12,12,12,12,12,12,12,12,12,12,11,5,11,11,12,12,11,11,11,11,11,11,11,11,11,2,2,2,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,6,6,2,6,2,5,2,2,2,8,8,8,8,8,11,12,12,5,5,5,12,12,12,12,12,12,12,12,12,12,12,12,12,11,11,5,12,12,12,12,12,12,11,11,11,11,11,11,11,2,2,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,2,6,6,2,2,2,2,5,5,2,6,6,8,2,2,8,8,11,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,11,11,5,12,12,12,12,12,12,12,12,11,11,11,11,11,11,2,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,2,2,2,11,11,11,11,11,2,1,1,6,8,2,2,8,8,11,11,8,12,12,12,12,12,12,12,12,12,12,12,11,11,11,8,5,12,12,12,12,12,12,12,12,11,11,11,11,11,2,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,2,2,2,11,11,11,11,11,8,1,0,0,1,1,8,8,8,11,11,11,8,8,8,8,8,11,11,11,11,11,11,11,8,11,2,2,5,12,12,12,12,12,12,12,12,11,11,11,11,2,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,2,2,2,11,11,11,11,8,8,1,0,0,0,0,0,1,1,11,11,11,11,8,8,8,8,8,8,11,11,11,11,11,8,8,8,2,2,5,12,12,12,12,12,12,12,12,11,11,11,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,2,2,2,11,11,11,11,8,8,8,1,0,0,0,0,1,1,2,1,1,11,11,11,8,8,8,8,2,8,8,8,8,8,8,2,11,11,11,5,11,12,12,12,12,12,12,12,11,11,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,1,5,2,2,2,2,11,11,8,8,8,8,1,0,0,0,0,1,2,2,2,2,2,1,1,1,1,1,1,11,2,11,11,11,11,8,8,11,11,11,11,5,5,12,12,12,12,12,12,12,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,1,2,2,1,5,5,11,11,11,8,8,1,0,0,0,0,5,1,1,2,2,2,2,2,2,2,1,1,1,1,1,1,1,1,1,1,1,5,11,5,5,11,12,12,12,12,12,12,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,5,5,1,8,11,11,8,8,1,0,0,0,0,0,0,0,1,2,2,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,5,11,11,11,11,12,12,12,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,11,11,11,11,8,1,0,0,0,0,0,0,0,5,5,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,11,11,11,11,11,11,2,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,11,11,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,11,1,5,11,11,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,11,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;", "#FFFFFF,#142632,#69E65C,#346933,#2D4F2A,#213C0F,#FAE578,#A6A94E,#639522,#CED553,#777431,#AAD454,#D6E97D,#C7DE69,#9BA95A,#629721,#46744F,");
        addPainting("Nyan Cat", "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,0,0,0,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,0,0,0,0,0,0,0,;1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,2,0,0,0,0,0,0,0,;1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,3,3,3,4,4,4,4,4,4,4,4,4,4,4,4,4,4,3,3,3,2,0,0,0,0,0,0,;5,5,5,1,1,1,1,1,1,1,1,5,5,5,5,5,5,5,5,1,1,1,1,1,1,1,1,5,5,5,5,5,5,5,5,1,2,3,3,4,4,4,4,4,4,4,6,4,4,6,4,4,4,4,4,3,3,2,0,0,0,0,0,0,;5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,2,3,4,4,6,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,3,2,0,0,0,0,0,0,;5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,2,3,4,4,4,4,4,4,4,4,4,4,4,2,2,4,4,6,4,4,3,2,0,2,2,2,0,0,;7,7,7,5,5,5,5,5,5,5,5,7,7,7,7,7,7,7,7,5,5,5,5,5,5,5,5,7,7,7,7,7,7,7,7,5,2,3,4,4,4,4,4,4,4,4,4,4,2,8,8,2,4,4,4,4,3,2,2,8,8,2,0,0,;7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,2,2,2,2,7,7,2,3,4,4,4,4,4,4,4,6,4,4,2,8,8,8,2,4,4,4,3,2,8,8,8,2,0,0,;7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,2,8,8,2,2,7,2,3,4,4,4,4,4,4,4,4,4,4,2,8,8,8,8,2,2,2,2,8,8,8,8,2,0,0,;9,9,9,7,7,7,7,7,7,7,7,9,9,9,9,9,9,9,9,7,7,7,7,7,7,7,7,9,9,9,2,2,8,8,2,2,2,3,4,4,4,6,4,4,4,4,4,4,2,8,8,8,8,8,8,8,8,8,8,8,8,2,0,0,;9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,2,2,8,8,2,2,3,4,4,4,4,4,4,4,4,6,2,8,8,8,8,8,8,8,8,8,8,8,8,8,8,2,0,;9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,2,2,8,8,2,3,4,6,4,4,4,4,4,4,4,2,8,8,8,0,2,8,8,8,8,8,0,2,8,8,2,0,;10,10,10,9,9,9,9,9,9,9,9,10,10,10,10,10,10,10,10,9,9,9,9,9,9,9,9,10,10,10,10,10,10,2,2,2,2,3,4,4,4,4,4,4,4,4,4,2,8,8,8,2,2,8,8,8,2,8,2,2,8,8,2,0,;10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,2,2,3,4,4,4,4,4,6,4,4,4,2,8,11,11,8,8,8,8,8,8,8,8,8,11,11,2,0,;10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,2,3,3,4,6,4,4,4,4,4,4,2,8,11,11,8,2,8,8,2,8,8,2,8,11,11,2,0,;12,12,12,10,10,10,10,10,10,10,10,12,12,12,12,12,12,12,12,10,10,10,10,10,10,10,10,12,12,12,12,12,12,12,12,10,2,3,3,3,4,4,4,4,4,4,4,4,2,8,8,8,2,2,2,2,2,2,2,8,8,2,0,0,;12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,2,2,2,3,3,3,3,3,3,3,3,3,3,3,2,8,8,8,8,8,8,8,8,8,8,2,0,0,0,;12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,2,8,8,8,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,0,0,0,;0,0,0,12,12,12,12,12,12,12,12,0,0,0,0,0,0,0,0,12,12,12,12,12,12,12,12,0,0,0,0,0,0,0,2,8,8,2,2,0,2,8,8,2,0,0,0,0,0,0,2,8,8,2,0,2,8,8,2,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,0,0,0,2,2,2,0,0,0,0,0,0,0,0,2,2,2,0,0,2,2,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,;", "#FFFFFF,#fe0000,#000000,#fecd9b,#ff99ff,#fe9800,#ff2e9a,#ffff01,#999999,#33ff00,#0099ff,#fe9a9a,#6734ff");
    }
    public String getPaintingPixels(int paintingId) {
        String pixels = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {PAINTING_PIXELS};
        String selection = PAINTING_ID + " =? ";
        String[] selectionArgs = {""+paintingId};

        Cursor cursor = db.query(PAINTING, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            pixels = cursor.getString(cursor.getColumnIndexOrThrow(PAINTING_PIXELS));
            cursor.close();
        }
        db.close();
        return pixels;
    }
    public String getPaintingColors(int paintingId) {
        String colors = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {PAINTING_COLORS};
        String selection = PAINTING_ID + "=?";
        String[] selectionArgs = {String.valueOf(paintingId)};
        Cursor cursor = db.query(PAINTING, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            colors = cursor.getString(cursor.getColumnIndexOrThrow(PAINTING_COLORS));
            cursor.close();
        }
        db.close();
        return colors;
    }
    public void updateDevelopment (int paintingId, int userId, int[][] isColored){
        SQLiteDatabase db = this.getWritableDatabase();
        String colored = StringToArrayAdapter.arrayToString(isColored);
        ContentValues values = new ContentValues();
        values.put(DEVELOPMENT_COLORED, colored);
        db.update(this.DEVELOPMENT, values, DEVELOPMENT_PAINTING + " = ? AND "+ DEVELOPMENT_USER + " = ? ", new String[]{""+paintingId, ""+userId});
        db.close();
    }



    public void addNewUser(String name, String pass, String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USERNAME, name);
        values.put(PASSWORD, pass);
        values.put(EMAIL, email);
        db.insert(USERS, null, values);
        db.close();
    }


    public boolean ifExist(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM users WHERE username=? AND password=?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.rawQuery(query, selectionArgs);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }



    public String getAllUserDetails(){
        String details = "";
        String allDetails = "";
        String order = USER_ID + " ASC ";
        Cursor cursor = getReadableDatabase().query(USERS, USER_COLUMN, null, null, null, null, order);
        if (cursor.getCount() > 0){
            while (cursor.moveToNext()){
                String username = cursor.getString(cursor.getColumnIndexOrThrow(USERNAME));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(PASSWORD));
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(USER_ID));
                details = " ID: " + id + " Username: " + username + " Password: " + password + " ";
                allDetails += details;
            }
        }

        return allDetails;
    }

    public void deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = "id = ?";
        String[] selectionArgs = {String.valueOf(userId)};
        db.delete("users", selection, selectionArgs);
        db.close();
    }

    public void addNewStatistic(long time, int squaresPainted) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HelperDB.STATISTICS_TIME, time);
        values.put(HelperDB.STATISTICS_SQUARES_PAINTED, squaresPainted);
        db.insert(HelperDB.STATISTICS, null, values);
        db.close();
    }

    public void deleteAllStats() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(HelperDB.STATISTICS, null, null);
        db.close();
    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + USERS);
        db.execSQL("DROP TABLE IF EXISTS " + STATISTICS);
        db.execSQL("DROP TABLE IF EXISTS " + PAINTING);
        db.execSQL("DROP TABLE IF EXISTS " + DEVELOPMENT);
        onCreate(db);
    }
}
