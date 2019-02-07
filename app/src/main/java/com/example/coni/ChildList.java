package com.example.coni;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ChildList extends AppCompatActivity {

    ImageView imgchild;
    TextView txtcname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_list);

        imgchild = findViewById(R.id.imageChild);
        txtcname = findViewById(R.id.txtChildName);
//        txtclocation = findViewById(R.id.txtCurrentLoc);
//        txtbattstatus = findViewById(R.id.txtBattStatus);

        Bundle extra = getIntent().getExtras();
        String KeyName = extra.getString("KeyName");
//        String KeyLoc = extra.getString("KeyLoc");
//        String KeyBatt = extra.getString("KeyBatt");
        byte[] KeyImg = extra.getByteArray("KeyImg");


        txtcname.setText(KeyName);
//        txtclocation.setText(KeyLoc);
//        txtbattstatus.setText(KeyBatt);
        Bitmap bm = BitmapFactory.decodeByteArray(KeyImg, 0, KeyImg.length);
        imgchild.setImageBitmap(bm);
    }
}
