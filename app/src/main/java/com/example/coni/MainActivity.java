package com.example.coni;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Button btnlogin, btnsignup;
    private Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new Session(this);


        btnlogin = findViewById(R.id.btn_login);
        btnsignup = findViewById(R.id.btn_signup);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toUserLogin = new Intent(MainActivity.this, UserLogin.class);
                startActivity(toUserLogin);
            }
        });

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toRegistration = new Intent(MainActivity.this, Guardianreg.class);
                startActivity(toRegistration);

            }
        });
    }
}
