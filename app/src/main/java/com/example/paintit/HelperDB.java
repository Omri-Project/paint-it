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

    private static final String DEVELOPMENT_TABLE = "CREATE TABLE " + DEVELOPMENT + "("
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

    public void addPredefinedPainting() {
        SQLiteDatabase db = this.getWritableDatabase();
        int id = 0;

        String pixels1 = "0,0,0,0,0,1,1,1,1,0,0,0,0,0,0,0,;0,0,0,0,1,2,2,2,2,1,0,0,0,0,0,0,;0,0,0,1,2,2,2,2,2,2,1,0,0,0,0,0,;0,1,1,3,3,2,1,2,2,2,1,0,0,0,0,0,;1,3,3,3,3,2,2,2,2,2,1,0,0,0,0,0,;0,1,3,3,3,3,2,2,2,1,1,1,0,0,1,0,;0,0,1,1,1,2,2,2,1,2,2,2,1,1,2,1,;0,0,0,1,2,2,2,2,2,2,2,2,2,2,2,1,;0,0,1,2,2,2,2,1,2,2,2,2,1,2,2,1,;0,0,1,2,2,2,2,2,1,2,2,1,2,2,2,1,;0,0,1,2,2,2,2,2,1,1,1,2,2,2,1,0,;0,0,0,1,2,2,2,2,2,2,2,2,2,1,0,0,;0,0,0,0,1,2,2,2,2,2,2,2,1,0,0,0,;0,0,0,0,0,1,1,1,1,1,1,1,0,0,0,0,;";
        String name1 = "Duck";
        String colors1 = "#000000,#ffce01,#ff8101";
        ContentValues values1 = new ContentValues();
        values1.put("id", id);
        values1.put(PAINTING_NAME, name1);
        values1.put(PAINTING_PIXELS, pixels1);
        values1.put(PAINTING_COLORS, colors1);
        db.insert(PAINTING_TABLE, null, values1);

        db.close();
    }
    public String getPaintingPixels(int paintingId) {
        String pixels = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {PAINTING_PIXELS};
        String selection = PAINTING_ID + "=?";
        String[] selectionArgs = {String.valueOf(paintingId)};

        Cursor cursor = db.query(PAINTING_TABLE, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            pixels = cursor.getString(cursor.getColumnIndexOrThrow(PAINTING_PIXELS));
            cursor.close();
        }

        db.close();

        return pixels;
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
