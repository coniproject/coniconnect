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
    TextView txtcage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_list);

        imgchild = findViewById(R.id.imageChild);
        txtcname = findViewById(R.id.txtChildName);
        txtcage = findViewById(R.id.txtCurrentLoc);


//        Bundle extra = getIntent().getExtras();
//        String KeyName = extra.getString("KeyName");
//        byte[] KeyImg = extra.getByteArray("KeyImg");


//        txtcname.setText(KeyName);
//        Bitmap bm = BitmapFactory.decodeByteArray(KeyImg, 0, KeyImg.length);
//        imgchild.setImageBitmap(bm);
    }
}
