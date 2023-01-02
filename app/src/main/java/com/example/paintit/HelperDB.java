package com.example.paintit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HelperDB extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "Users";
    public static final String ID = "id";
    public static final String USERNAME = "UserName";
    public static final String PASSWORD = "Password";
//    public static final String EMAIL = "Email";
    static final String DB_NAME = "USERS.DB";
    static final int DB_VERSION = 1;
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+ " ("+ ID + "INTEGER PRIMARY KEY AUTOINCREMENT, "+ USERNAME + " TEXT NOT NULL, "+ PASSWORD + " TEXT NOT NULL"
            //", "+ EMAIL + " TEXT NOT NULL
        +");";

    public HelperDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVer, int newVer) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
