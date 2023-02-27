package com.example.paintit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.IDN;

public class LoginActivity extends AppCompatActivity {

    Button start;
    EditText username, password;
    TextView create_acc;
    HelperDB helperDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        start = findViewById(R.id.start_btn);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        create_acc = findViewById(R.id.create_acc);
        helperDB = new HelperDB(getApplicationContext());
        Intent in = new Intent(LoginActivity.this , GalleryActivity.class);
        Intent in1 = new Intent(LoginActivity.this , SignUp.class);
        String checkUser = username.getText().toString();
        String checkPass = password.getText().toString();
        int exist = Integer.valueOf(String.valueOf(helperDB.ifExist(checkUser , checkPass)));

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exist != -1) {
                    startActivity(in);
                    finish();
                }
                else if (username == null || helperDB.ifExist(username.getText().toString() , password.getText().toString()) == -1 && password == null || helperDB.ifExist(username.getText().toString() , password.getText().toString()) == -1){
                    Toast.makeText(LoginActivity.this, "Wrong Username Or Password Or User Doesn't Exist", Toast.LENGTH_SHORT).show();
                }
            }
        });


        create_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(in1);
                finish();
            }
        });


    }
}




//    Button login = findViewById(R.id.login);
//    Button createAcc = findViewById(R.id.createAcc);
//    EditText username = findViewById(R.id.username);
//    EditText password = findViewById(R.id.password);
//    HelperDB hlp;
//    SQLiteDatabase db;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//        hlp = new HelperDB(LoginActivity.this);
//        db = hlp.getWritableDatabase();
//        db.close();
//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if ((!username.getText().equals(""))&(!password.getText().equals(""))){
// //                   db.rawQuery("SELECT * FROM "+ hlp.TABLE_NAME + " WHERE " + hlp.USERNAME + " = "+ username.getText(),);
//                    if (1 ==1){
//                    } else {
//                        Toast.makeText(LoginActivity.this, "Incorrect details, fix them or create a new account", Toast.LENGTH_LONG).show();
//                    }
//                } else {
//                    Toast.makeText(LoginActivity.this, "Insert details or create a new account", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//        createAcc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ContentValues cv = new ContentValues();
//                Cursor c = db.query("Users", null, null, null, null, null, null);
//                c.moveToFirst();
//                int i = 0;
//                while (!c.isAfterLast()){
//                    i++;
//                    c.moveToNext();
//                }
//                cv.put(hlp.ID, ""+i);
//                cv.put(hlp.USERNAME, String.valueOf(username.getText()));
//                cv.put(hlp.PASSWORD, String.valueOf(password.getText()));
//                db.insert(hlp.TABLE_NAME, null, cv);
//                db.close();
//
//            }
//        });
//    }