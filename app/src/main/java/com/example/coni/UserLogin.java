package com.example.coni;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserLogin extends AppCompatActivity {

    Button btnlogin;
    EditText txtusername, txtpassword;

    DBHelper mydb;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        btnlogin = findViewById(R.id.btn_login);
        txtusername = findViewById(R.id.usernamelogin);
        txtpassword = findViewById(R.id.passwordlogin);

        //Database

        mydb = new DBHelper(this);
        login();

    }

    public void login() {

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sqLiteDatabase = mydb.getReadableDatabase();
                String loginname = txtusername.getText().toString();
                String loginpword = txtpassword.getText().toString();
                Cursor res = mydb.userlogin(loginname, loginpword, sqLiteDatabase);

                if (res.moveToFirst()) {
                    Intent intent = new Intent(UserLogin.this, MapView.class);
                    startActivity(intent);

                    Toast.makeText(UserLogin.this, "Signed in.", Toast.LENGTH_SHORT).show();
                    txtusername.setText("");
                    txtpassword.setText("");
                } else {
                    Toast.makeText(UserLogin.this, "Username and password do not match.", Toast.LENGTH_LONG).show();
                    txtusername.setText("");
                    txtpassword.setText("");
                    txtusername.requestFocus();
                }

            }
        });
    }

}
