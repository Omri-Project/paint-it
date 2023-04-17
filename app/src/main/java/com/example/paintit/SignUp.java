package com.example.paintit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    Button start;
    EditText username, password, email;
    TextView login;
    HelperDB helperDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        start = findViewById(R.id.start_btn);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        email = findViewById(R.id.email);
        helperDB = new HelperDB(getApplicationContext());
        Intent in = new Intent(SignUp.this , LoginActivity.class);
        Intent in1 = new Intent(SignUp.this , GalleryActivity.class);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(in);
                finish();
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helperDB.ifExist(username.getText().toString(), password.getText().toString()) != (long) -1){
                    helperDB.addNewUser(username.getText().toString() , password.getText().toString() , email.getText().toString());
                    Toast.makeText(SignUp.this, "Successfully Added New User", Toast.LENGTH_SHORT).show();
                    startActivity(in1);
                    finish();
                }
                else {
                    Toast.makeText(SignUp.this, "Username already exists, Please Try with a different name", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}