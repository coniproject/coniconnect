package com.example.coni;

import android.content.Context;
import android.content.SharedPreferences;

public class LocationPrefs {

    SharedPreferences locprefs;
    SharedPreferences.Editor editor;
    Context context;


    public LocationPrefs(Context context) {
        this.context = context;
        locprefs = context.getSharedPreferences("locationcoords", Context.MODE_PRIVATE);
        editor = locprefs.edit();

    }

    public void putLocPrefs() {

    }
}
