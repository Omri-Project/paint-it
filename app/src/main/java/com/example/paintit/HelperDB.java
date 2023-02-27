package com.example.paintit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HelperDB extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "Users";
    public static final String ID = "id";
    public static final String USERNAME = "UserName";
    public static final String PASSWORD = "Password";
   public static final String EMAIL = "Email";
    static final String DB_NAME = "USERS.DB";
    static final int DB_VERSION = 1;
    private static final String USER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME+ " ("+ ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + USERNAME
            + " TEXT NOT NULL, " + PASSWORD + " TEXT NOT NULL" +
            ", " + EMAIL + " TEXT NOT NULL " + ") ";
    private static final String[] USER_COLUMN = {ID , USERNAME , PASSWORD};


    public HelperDB(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(USER_TABLE);

    }

    public void addNewUser(String name, String pass, String email) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USERNAME, name);
        values.put(PASSWORD, pass);
        values.put(EMAIL, email);
        db.insert(USER_TABLE, null, values);
        db.close();
    }


    public long ifExist(String name, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        String order = ID + " ASC ";
        String[] args = new String[] {name, password};
        Cursor cursor = db.query(USER_TABLE, USER_COLUMN, USERNAME + " = ? AND " + PASSWORD + " = ? " , args, null, null, order);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(ID));
            return id;
        } return -1;
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVer, int newVer) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
