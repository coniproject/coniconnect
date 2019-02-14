package com.example.coni;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ChildRegistration extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    DBHelper mydb;
    SQLiteDatabase sqLiteDatabase;

    ImageView imgchild;

    EditText edtclastname, edtcfirstname,
            edtcmidname, edtcage, edtcgender, edtcbday,
            edtdeviceno;


    Button btnregister;
    RadioButton rdbcmale, rdbcfemale, rdtermscon;
    RadioGroup rdbgender, rdbterms;
    TextView txtbday;

    Context context = this;

    private static int SELECT_IMAGE = 1;
    private static int CAPTURE_IMAGE = 2;
    Uri selectedimage;
    Bitmap camImg;
    private String mImageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_registration);

        imgchild = findViewById(R.id.imgchild);
        edtclastname = findViewById(R.id.editclastname);
        edtcfirstname = findViewById(R.id.editcfirstname);
        edtcmidname = findViewById(R.id.editcmidname);
        edtcage = findViewById(R.id.editcage);
        txtbday = findViewById(R.id.txtcbday);
        edtdeviceno = findViewById(R.id.editdeviceno);
        rdbterms = findViewById(R.id.rdbTermsCon);

        btnregister = findViewById(R.id.btn_reg);

        imgchild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mbuilder = new AlertDialog.Builder(ChildRegistration.this);
                View mview = getLayoutInflater().inflate(R.layout.dialog_add_image, null);
                Button mbtnUseCam = mview.findViewById(R.id.btnUseCam);
                Button mbtnUseGal = mview.findViewById(R.id.btnUseGal);
                mbuilder.setView(mview);
                final AlertDialog dialog = mbuilder.create();

                mbtnUseCam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(i, CAPTURE_IMAGE);
                        dialog.dismiss();
                    }

                });

                mbtnUseGal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(ChildRegistration.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                SELECT_IMAGE);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChildDataValidation();
            }
        });
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == SELECT_IMAGE){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/");
                startActivityForResult(intent, SELECT_IMAGE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imgdata) {
        super.onActivityResult(requestCode, resultCode, imgdata);
        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK && imgdata != null) {
            selectedimage = imgdata.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedimage);
                Bitmap yourselectedimage = BitmapFactory.decodeStream(inputStream);
                imgchild.setImageBitmap(yourselectedimage);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == CAPTURE_IMAGE && resultCode == RESULT_OK && imgdata != null) {
            camImg =(Bitmap) imgdata.getExtras().get("img");
            try {
                imgchild.setImageBitmap(camImg);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //ADD CHILD DATA

    public void ChildDataValidation(){
        final String vallastname = edtclastname.getText().toString();
        final String valfirstname = edtcfirstname.getText().toString();
        final String valmidname = edtcmidname.getText().toString();
        final String valage = edtcage.getText().toString();
        final String valbday = edtcbday.getText().toString();
        final String valgender = edtcgender.getText().toString();
        final String valdeviceno = edtdeviceno.getText().toString();
        final String valtermscon = rdtermscon.getText().toString();

        final String imgdata = getBase64String(imgchild);

        sqLiteDatabase = mydb.getReadableDatabase();
        Cursor devres = mydb.deviceValidation(valdeviceno, sqLiteDatabase);


        //TO CODE VALIDATION NG KIDS

        if(vallastname.isEmpty() && valfirstname.isEmpty() && valage.isEmpty() && valbday.isEmpty() && valgender.isEmpty()
        && valdeviceno.isEmpty() && valtermscon.isEmpty() && imgdata.isEmpty()) {
            Toast.makeText(ChildRegistration.this, "Please fill up all the required fields", Toast.LENGTH_SHORT).show();
            edtclastname.requestFocus();
            return;
        }

        if(devres.moveToFirst()) {
            devres.close();
        }
        else {
            edtdeviceno.setError("Device Number not found.");
            edtdeviceno.setText("");
        }


        byte[] photo = getimagebyte(imgchild);

        mydb.addchild(photo,vallastname,valfirstname, valmidname,valage,valbday,valgender);
        Toast.makeText(ChildRegistration.this, "Child registered.", Toast.LENGTH_SHORT).show();
        Intent toMap = new Intent(ChildRegistration.this, MapView.class);
        startActivity(toMap);


    }

    private String getBase64String(ImageView imageView) {

        // give your image file url in mCurrentPhotoPath
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // In case you want to compress your image, here it's at 40%
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static byte[] getimagebyte (ImageView imageView){
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte [] bytearray = stream.toByteArray();
        return bytearray;
    }
}