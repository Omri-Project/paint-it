package com.example.paintit;

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
    public static final String ID = "id";
    public static final String USERNAME = "UserName";
    public static final String PASSWORD = "Password";
    public static final String EMAIL = "Email";

    private static final String USER_TABLE = "CREATE TABLE IF NOT EXISTS " + USERS + " (" + ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + USERNAME + " TEXT, "
            + PASSWORD + " TEXT, "
            + EMAIL + " TEXT " + ") ";

    private static final String[] USER_COLUMN = {ID, USERNAME, PASSWORD, EMAIL};

    // Statistics

    private static final String TABLE_STATISTICS = "statistics";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_SQUARES_PAINTED = "squares_painted";

    private static final String STATS_TABLE = " CREATE TABLE IF NOT EXISTS " + TABLE_STATISTICS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TIME + " INTEGER NOT NULL, " +
            COLUMN_SQUARES_PAINTED + " INTEGER NOT NULL);";

    public HelperDB(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_TABLE);
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
        String order = ID + " ASC ";
        Cursor cursor = getReadableDatabase().query(USERS, USER_COLUMN, null, null, null, null, order);
        if (cursor.getCount() > 0){
            while (cursor.moveToNext()){
                String username = cursor.getString(cursor.getColumnIndexOrThrow(USERNAME));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(PASSWORD));
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(ID));
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
        values.put(HelperDB.COLUMN_TIME, time);
        values.put(HelperDB.COLUMN_SQUARES_PAINTED, squaresPainted);
        db.insert(HelperDB.TABLE_STATISTICS, null, values);
        db.close();
    }

    public void deleteAllStats() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(HelperDB.TABLE_STATISTICS, null, null);
        db.close();
    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + USERS);
        onCreate(db);
    }
}
