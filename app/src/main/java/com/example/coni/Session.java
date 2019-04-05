package com.example.coni;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;

    public Session(Context ctx){
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences("coni", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setLoggedin(boolean logggedin){
        editor.putBoolean("loggedInmode",logggedin);
        editor.commit();
    }

    public boolean loggedin(){

        return prefs.getBoolean("loggedInmode", false);
    }

    public void setLogout(boolean logout) {
        editor.putBoolean("logout",logout);
        editor.commit();
    }
    public boolean logout(){
        return prefs.getBoolean("loggedInmode", false);
    }
}