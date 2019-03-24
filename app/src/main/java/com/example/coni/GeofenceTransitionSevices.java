package com.example.coni;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

public class GeofenceTransitionSevices extends IntentService {
    public GeofenceTransitionSevices(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if(geofencingEvent.hasError()) {
            String error = String.valueOf(geofencingEvent.getErrorCode());
            Toast.makeText(this, "Error Code: " +error, Toast.LENGTH_SHORT).show();
            return;
        }

        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        if(geofenceTransition== Geofence.GEOFENCE_TRANSITION_ENTER||geofenceTransition==Geofence.GEOFENCE_TRANSITION_EXIT){

            List<Geofence> triggeringGeofence = geofencingEvent.getTriggeringGeofences();
            String geofenceDetails = getGeofenceTransitionDetails(geofenceTransition,triggeringGeofence);
        }
    }

    private String getGeofenceTransitionDetails(int geofenceTransition, List<Geofence> triggeringGeofence) {
        ArrayList<String> triggerfencelist = new ArrayList<>();

        for (Geofence geofence: triggeringGeofence) {
            triggerfencelist.add(geofence.getRequestId());
        }

        String status = null;

        if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER){
            status = "ENTERING";
        } else if(geofenceTransition ==  Geofence.GEOFENCE_TRANSITION_EXIT) {
            status = "OUT OF RANGE";
        }

        return status + TextUtils.join(", ",triggerfencelist);
    }
}
