package com.example.coni;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class FamilyList extends AppCompatActivity {

    RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_list);

        ArrayList<Place> pos = new ArrayList<>();
        Place F1 = new Place (1,R.drawable.img1,"Rean","San Jose, Cavite");
        pos.add(F1);
        Place F2 = new Place(2, R.drawable.img2,"Ezi","Putatan, Muntinlupa");
        pos.add(F2);
        Place F3 = new Place (3,R.drawable.img3,"Kaye","Sta. Mesa, Manila");
        pos.add(F3);

        recyclerView = findViewById(R.id.recyclerView);
        RecyclerAdapter adapter = new RecyclerAdapter(pos, this);
        recyclerView.setAdapter(adapter);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);

    }
}
