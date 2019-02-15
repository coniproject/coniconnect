package com.example.coni;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Guardianreg extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    DBHelper mydb;
    SQLiteDatabase sqLiteDatabase;

    EditText edtlastname, edtfirstname,
            edtmidname, edtage,
            edtbday, edtgender,
            edtcontactno, edtemail,
            edtuname, edtpass,
            confirmpword;


    Button btnsignup;
    RadioButton rdbmale, rdbfemale;
    RadioGroup rdbgender;
    TextView txtbday;

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardianreg);


        mydb = new DBHelper(this);

        edtlastname =  findViewById(R.id.editlastname);
        edtlastname.requestFocus();
        edtfirstname = findViewById(R.id.editfirstname);
        edtmidname = findViewById(R.id.editmidname);
        edtage = findViewById(R.id.editage);
        edtcontactno = findViewById(R.id.editcontactnumber);
        edtemail = findViewById(R.id.editemail);
        edtuname = findViewById(R.id.editusername);
        edtpass = findViewById(R.id.editpassword);
        confirmpword = findViewById(R.id.editconfirmpass);
        btnsignup = findViewById(R.id.btn_signup);
        rdbmale = findViewById(R.id.rdmale);
        rdbfemale = findViewById(R.id.rdfem);
        txtbday=findViewById(R.id.txtbday);
        rdbgender = findViewById(R.id.rdbgroupgender);



        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });



        //DateTimePicker


        txtbday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Guardianreg.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                txtbday.setText(date);
            }
        };

    }

    public void validation() {

        String valusername = edtuname.getText().toString();
        String valpass = edtpass.getText().toString();
        String valconfirmpass = confirmpword.getText().toString();
        String valemail = edtemail.getText().toString();
//        String valcontactno = edtcontactno.getText().toString();

        sqLiteDatabase = mydb.getReadableDatabase();
        Cursor res = mydb.usernameValidation(valusername, sqLiteDatabase);
        Cursor emailres = mydb.emailValidation(valemail, sqLiteDatabase);

        //Username
        if(!res.moveToFirst()) {
           res.close();
        }
        else {
            edtuname.setError("Username Taken.");
            edtuname.setText("");
        }
        //Password

        if(valpass.length() > 6) {

            if(!valpass.equals(valconfirmpass)) {
                edtpass.setError("Passwords do not match.");
                edtpass.setText("");
            }
            else {

                //Radio Button

                int checkRadioID = rdbgender.getCheckedRadioButtonId();
                RadioButton selectedGender = findViewById(checkRadioID);

                boolean isInserted = mydb.addguardian(
                        edtlastname.getText().toString(),
                        edtfirstname.getText().toString(),
                        edtmidname.getText().toString(),
                        edtage.getText().toString(),
                        txtbday.getText().toString(),
                        selectedGender.getText().toString(),
                        edtcontactno.getText().toString(),
                        edtemail.getText().toString(),
                        edtuname.getText().toString(),
                        edtpass.getText().toString());


                if (isInserted) {
                    Toast.makeText(Guardianreg.this, "Registered", Toast.LENGTH_SHORT).show();
                    Intent toLogin = new Intent(Guardianreg.this, UserLogin.class);
                    startActivity(toLogin);

                    edtlastname.setText("");
                    edtfirstname.setText("");
                    edtmidname.setText("");
                    edtemail.setText("");
                    edtuname.setText("");
                    edtcontactno.setText("");
                    edtpass.setText("");
                    edtbday.setText("");
                }
                else{
                    Toast.makeText(Guardianreg.this, "Please fill up all the required fields.", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else {
            edtpass.setError("Minimum 6 Characters");
        }


        //Email

        if(!emailres.moveToFirst()) {
           emailres.close();
        } else {
            edtemail.setError("Email Address already exists.");
            edtemail.setText("");
        }

    }
}
