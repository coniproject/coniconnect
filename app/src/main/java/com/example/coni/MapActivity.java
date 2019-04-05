package com.example.coni;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.graphics.Color.rgb;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        LocationListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnCircleClickListener
{

    //Map Access
    private GoogleMap mMap;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mChild;
    Marker marker;
    private static final String TAG = "UserProfile";
    public static final String MyPREFERENCES = "myprefs";
    private final Handler handler = new Handler();
    private static final float DEFAULT_ZOOM = 15f;
    SmsReceiver smsReceiver = new SmsReceiver();
//    private HashMap<String, Double> coordinates = new HashMap<String, Double>();
    HashMapList coordslist = new HashMapList();
    SharedPreferences sharedPreferences;
    private Session session;

    private GoogleApiClient client;
    private GeofencingClient geofencingClient;
    private LocationRequest locationRequest;
    private Marker currentLocationmMarker;
    private Location lastlocation;

    public static final int REQUEST_LOCATION_CODE=99;
    int PROXIMITY_RADIUS=10000;

    Circle circle,entered,exit;
    DatabaseReference circleBound;

    //SMS
    private static final int SMS_PERMISSION_CODE = 0;

    //Floating Action Button

    boolean isOpen = false;

    FloatingActionButton fab_menu, fab_nearby,
            fab_home, fab_hotline, fab_zones,
            fab_reg, fab_logout, fab_update;

    Animation FabOpen, FabClose, FabRotateCW, FabRotateAntiCW;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        if (!hasReadSmsPermission()) {
            showRequestPermissionsInfoAlertDialog();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapdisplay);
        mapFragment.getMapAsync(this);

        ChildEventListener mChildEventListener;
        mChild = FirebaseDatabase.getInstance().getReference("conilocationdata/conilocationdata/conilocationdata");
        mChild.push().setValue(marker);

        //FLOATING ACTION MENU

        fab_menu = findViewById(R.id.fab_nav);
//        fab_nearby = findViewById(R.id.fab_nearbyplaces);
        fab_home = findViewById(R.id.fab_maphome);
//        fab_hotline = findViewById(R.id.fab_hotline);
//        fab_zones = findViewById(R.id.fab_safezone);
        fab_reg = findViewById(R.id.fab_addmember);

//        fab_update = findViewById(R.id.fab_update);

        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        FabRotateCW = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        FabRotateAntiCW = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlock);

        //Floating Action Button

        fab_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isOpen) {
//                    fab_nearby.startAnimation(FabClose);
                    fab_home.startAnimation(FabClose);
//                    fab_hotline.startAnimation(FabClose);
//                    fab_zones.startAnimation(FabClose);
                    fab_reg.startAnimation(FabClose);
//                    fab_update.startAnimation(FabClose);
                    fab_menu.startAnimation(FabRotateAntiCW);

//                    fab_nearby.setClickable(false);
                    fab_home.setClickable(false);
//                    fab_hotline.setClickable(false);
//                    fab_zones.setClickable(false);
                    fab_reg.setClickable(false);
//                    fab_update.setClickable(false);
                    isOpen = false;

                } else {
//                    fab_nearby.startAnimation(FabOpen);
                    fab_home.startAnimation(FabOpen);
//                    fab_hotline.startAnimation(FabOpen);
//                    fab_zones.startAnimation(FabOpen);
                    fab_reg.startAnimation(FabOpen);
//                    fab_update.startAnimation(FabOpen);
                    fab_menu.startAnimation(FabRotateCW);

//                    fab_nearby.setClickable(true);
                    fab_home.setClickable(true);
//                    fab_hotline.setClickable(true);
//                    fab_zones.setClickable(true);
                    fab_reg.setClickable(true);
//                    fab_update.setClickable(true);
                    isOpen = true;
                }

            }
        });

        fab_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toChildReg = new Intent(MapActivity.this, ChildRegistration.class);
                startActivity(toChildReg);
            }
        });

        fab_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//        fab_update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent toNearby = new Intent(MapActivity.this, NearbyPlaces.class);
//                startActivity(toNearby);
//            }
//        });
//
//        fab_hotline.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent toHotline = new Intent(MapActivity.this, Hotline.class);
//                startActivity(toHotline);
//            }
//        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMinZoomPreference(15.0f);
        mMap.setMaxZoomPreference(20.0f);
        mMap.setOnMapClickListener(this);
//        mMap.setOnMarkerClickListener(this);
        mMap.setOnCircleClickListener(this);
//        final LatLng putatan = new LatLng(14.397420, 121.033051);

        final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("conilocationdata");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                for(DataSnapshot ds : dataSnapshot.getChildren()) {


                    Bundle bundle = getIntent().getExtras();
                    if(bundle != null) {
                        double lat1 = bundle.getDouble("lat");
                        double lon2 = bundle.getDouble("lon");

                        LatLng location = new LatLng(lat1, lon2);
                        marker = mMap.addMarker(new MarkerOptions()
                                .position(location));

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));

                        circle = mMap.addCircle(new CircleOptions()
                                .center(location)
                                .radius(100f)
                                .strokeColor(Color.rgb(216, 191, 216))
                                .clickable(true)
                                .fillColor(Color.TRANSPARENT));


                        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));

                        mRef.getKey();


                    } else {
                        System.out.println("Bundle null");
                    }



                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mRef.orderByKey().limitToLast(1).addValueEventListener(postListener);

    }

    public boolean onOptionsItemSelected (MenuItem item){

        int id = item.getItemId();

        switch (id) {

            case R.id.menu_logout:
                Intent toMain = new Intent(MapActivity.this, MainActivity.class);
                startActivity(toMain);
                Toast.makeText(MapActivity.this, "Disconnected.", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    //    SMS PERMISSIONS

    private void showRequestPermissionsInfoAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.permission_alert_dialog_title);
        builder.setMessage(R.string.permission_dialog_message);
        builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                requestReadAndSendSmsPermission();
            }
        });
        builder.show();
    }

    private boolean hasReadSmsPermission() {
        return ContextCompat.checkSelfPermission(MapActivity.this,
                Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MapActivity.this,
                        Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestReadAndSendSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MapActivity.this, Manifest.permission.READ_SMS)) {
            Log.d(TAG, "shouldShowRequestPermissionRationale(), no permission requested");
            return;
        }
        ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS},
                SMS_PERMISSION_CODE);
    }


    @Override
    public void onLocationChanged(Location location) {



    }

    @Override
    public void onMapClick(LatLng latLng) {

        entered = mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(100f)
                .strokeColor(Color.rgb(255,67,67))
                .clickable(true)
                .fillColor(Color.TRANSPARENT));


        circleBound = FirebaseDatabase.getInstance().getReference("conilocationdata/circles");
        circleBound.push().setValue(latLng);


    }


    @Override
    public void onCircleClick(Circle circle) {
        circle.remove();
    }
}
