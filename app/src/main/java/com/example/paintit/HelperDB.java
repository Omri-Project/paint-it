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

    public long ifExist(String name, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String order = ID + " ASC ";
        String[] args = new String[]{name, password};
        Cursor cursor = db.query(USERS, USER_COLUMN, USERNAME + " = ? AND " + PASSWORD + " = ? ", args, null, null, order);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(ID));
            return id;
        }
        return -1;
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

//    public long ifExist(String name, String password) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        String order = ID + " ASC ";
//        String[] args = new String[]{name, password};
//        Cursor cursor = db.query(TABLE_NAME, USER_COLUMN, USERNAME + " = ? AND " + PASSWORD + " = ? AND " + EMAIL + " = ? ", args, null, null, order);
//        cursor.moveToFirst();
//        Log.d("Evermore", "check 1");
//        while (!cursor.isAfterLast()) {
//            Log.d("Evermore", "check 2");
//            long id = cursor.getLong(cursor.getColumnIndexOrThrow(ID));
//            return id;
//        }
//        Log.d("Evermore", "check 3");
//        return -1;
//    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + USERS);
        onCreate(db);
    }
}
