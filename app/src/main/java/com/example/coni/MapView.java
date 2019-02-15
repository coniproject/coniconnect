package com.example.coni;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapView extends AppCompatActivity {


    private static final String TAG = "UserProfile";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    //vars
    private boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mfusedlocationproviderclient;

    //Map essentials
    GoogleMap mGoogleMap;
    private static final float DEFAULT_ZOOM = 15f;

    //layout
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;

    //Floating Action Button

    boolean isOpen = false;

    FloatingActionButton fab_menu, fab_nearby,
                        fab_home,fab_hotline,fab_zones,
                        fab_reg,fab_logout, fab_update;

    Animation FabOpen, FabClose, FabRotateCW, FabRotateAntiCW;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setLogo(R.drawable.conilogo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_map_view);



        if (checkGoogleServices()) {
            init();
        }

        getLocationPermission();

        fab_menu = findViewById(R.id.fab_nav);
        fab_nearby = findViewById(R.id.fab_nearbyplaces);
        fab_home = findViewById(R.id.fab_maphome);
        fab_hotline = findViewById(R.id.fab_hotline);
        fab_zones = findViewById(R.id.fab_safezone);
        fab_reg = findViewById(R.id.fab_addmember);
//        fab_logout = findViewById(R.id.fab_logout);
        fab_update = findViewById(R.id.fab_update);

        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        FabRotateCW = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        FabRotateAntiCW = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlock);

        fab_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isOpen) {
                    fab_nearby.startAnimation(FabClose);
                    fab_home.startAnimation(FabClose);
                    fab_hotline.startAnimation(FabClose);
                    fab_zones.startAnimation(FabClose);
                    fab_reg.startAnimation(FabClose);
                    fab_update.startAnimation(FabClose);
                    fab_menu.startAnimation(FabRotateAntiCW);

                    fab_nearby.setClickable(false);
                    fab_home.setClickable(false);
                    fab_hotline.setClickable(false);
                    fab_zones.setClickable(false);
                    fab_reg.setClickable(false);
                    fab_update.setClickable(false);
                    isOpen = false;

                } else {
                    fab_nearby.startAnimation(FabOpen);
                        fab_home.startAnimation(FabOpen);
                        fab_hotline.startAnimation(FabOpen);
                        fab_zones.startAnimation(FabOpen);
                        fab_reg.startAnimation(FabOpen);
//                        fab_logout.startAnimation(FabOpen);
                        fab_update.startAnimation(FabOpen);
                        fab_menu.startAnimation(FabRotateCW);

                    fab_nearby.setClickable(true);
                    fab_home.setClickable(true);
                    fab_hotline.setClickable(true);
                    fab_zones.setClickable(true);
                    fab_reg.setClickable(true);
//                    fab_logout.setClickable(true);
                    fab_update.setClickable(true);
                    isOpen = true;
                }

            }
        });

        fab_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toChildReg = new Intent(MapView.this, ChildRegistration.class);
                startActivity(toChildReg);
            }
        });

        fab_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toHome = new Intent(MapView.this, MapView.class);
                startActivity(toHome);
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item){

//        if(mToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);

        int id = item.getItemId();

        switch (id) {
            case R.id.menu_acctset:
                //INsert Intent
                break;

            case R.id.menu_logout:
                Intent toMain = new Intent(MapView.this,MainActivity.class);
                startActivity(toMain);
                Toast.makeText(MapView.this, "Disconnected.", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    private void init() {
        Toast.makeText(MapView.this, "Connected", Toast.LENGTH_SHORT).show();

    }

    public boolean checkGoogleServices() {
        Log.d(TAG, "checkGoogleServices: Checking Google Services Version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MapView.this);

        if (available == ConnectionResult.SUCCESS) {
            //Map Request
            Log.d(TAG, "checkGoogleServices: Google Play Services Available");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //Error Occured
            Log.d(TAG, "checkGoogleServices: An error occured. We'll fix it for you");
            Dialog errordialog = GoogleApiAvailability.getInstance().getErrorDialog(MapView.this, available, ERROR_DIALOG_REQUEST);
            errordialog.show();
        } else {
            Toast.makeText(MapView.this, "Unavailable Map Requests. Please check your internet connection.", Toast.LENGTH_SHORT).show();
        }
        return false;

    }


    //Application Map on Callback

    private void getLocationPermission() {
        Log.d(TAG, "Getting Location Permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                mLocationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {

                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;

                    //Map Initialization
                    initMap();
                }
            }
        }
    }

    private void initMap() {
        Log.d(TAG, "Initializing Map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapview);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                mGoogleMap = googleMap;

                if (mLocationPermissionGranted) {
                    getDeviceLocation();
                    if (ActivityCompat.checkSelfPermission(MapView.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                            (MapView.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mGoogleMap.setMyLocationEnabled(true);
                }

            }
        });
    }

    private void getDeviceLocation(){
        Log.d(TAG, "Getting current location");

        mfusedlocationproviderclient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionGranted){
                Task location = mfusedlocationproviderclient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()) {
                            Log.d(TAG,"getDeviceLocation: Location Found");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()), DEFAULT_ZOOM);
                        }
                        else {
                            Log.d(TAG,"No Location Found. Please allow location services.");
                            Toast.makeText(MapView.this, "Location not found.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }
        catch (SecurityException e){
            Log.d(TAG,"getDeviceLocation: Security Exception :" + e.getMessage());
        }
    }

    private void moveCamera(LatLng latlng, float zoom) {
        Log.d(TAG,"Moving Location to lng : " + latlng.latitude + ", lng : " + latlng.longitude);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));

    }

    //MENU NAVBAR


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

}