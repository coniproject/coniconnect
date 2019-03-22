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
        GoogleMap.OnMarkerClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMapClickListener,
        ResultCallback<Status>
      {

    //Map Access
    private GoogleMap mMap;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mChild;
    Marker marker;
    private static final String TAG = "UserProfile";
    private final Handler handler = new Handler();
    private static final float DEFAULT_ZOOM = 15f;
    SmsReceiver smsReceiver = new SmsReceiver();
    private HashMap<String, Double> coordinates = new HashMap<String, Double>();

    private GoogleApiClient client;
    private GeofencingClient geofencingClient;
    private LocationRequest locationRequest;
    private Marker currentLocationmMarker;
    private Location lastlocation;

          public static final int REQUEST_LOCATION_CODE=99;
          int PROXIMITY_RADIUS=10000;
    LatLng latLngStart;
    private static final String NOTIFICATION_MSG = "NOTIFICATION MSG";
    // Create a Intent send by the notification
    public static Intent makeNotificationIntent(Context context, String msg) {
              Intent intent = new Intent( context, MapActivity.class );
              intent.putExtra( NOTIFICATION_MSG, msg );
              return intent;
    }
          private static final String NOTIFICATION_MSG = "NOTIFICATION MSG";

          private PendingIntent geoFencePendingIntent;
          public final int GEOFENCE_REQ_CODE = 0;

          Circle circle;
          DatabaseReference circleBound;
    private FusedLocationProviderClient fusedLocationClient;

          //SMS
    private static final int SMS_PERMISSION_CODE = 0;

    //Floating Action Button

    boolean isOpen = false;

    FloatingActionButton fab_menu, fab_nearby,
            fab_home, fab_hotline, fab_zones,
            fab_reg, fab_logout, fab_update;

    Animation FabOpen, FabClose, FabRotateCW, FabRotateAntiCW;

double latitude,longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        geofencingClient = LocationServices.getGeofencingClient(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        createGoogleApi();

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
        fab_nearby = findViewById(R.id.fab_nearbyplaces);
        fab_home = findViewById(R.id.fab_maphome);
        fab_hotline = findViewById(R.id.fab_hotline);
        fab_zones = findViewById(R.id.fab_safezone);
        fab_reg = findViewById(R.id.fab_addmember);

        fab_update = findViewById(R.id.fab_update);

        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        FabRotateCW = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        FabRotateAntiCW = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlock);

        //Floating Action Button

        fab_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isOpen) {
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
                    fab_update.startAnimation(FabOpen);
                    fab_menu.startAnimation(FabRotateCW);

                    fab_nearby.setClickable(true);
                    fab_home.setClickable(true);
                    fab_hotline.setClickable(true);
                    fab_zones.setClickable(true);
                    fab_reg.setClickable(true);
                    fab_update.setClickable(true);
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
//                Intent toHome = new Intent(MapActivity.this, MapActivity.class);
//                startActivity(toHome);

                //Reload Maps Instance
                finish();
                startActivity(getIntent());

            }
        });

        fab_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toNearby = new Intent(MapActivity.this, NearbyPlaces.class);
                startActivity(toNearby);
            }
        });

        fab_hotline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toHotline = new Intent(MapActivity.this, Hotline.class);
                startActivity(toHotline);
            }
        });

    }


          @Override
          public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
              switch(requestCode)
              {
                  case REQUEST_LOCATION_CODE:
                      if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                      {
                          if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) !=  PackageManager.PERMISSION_GRANTED)
                          {
                              if(client == null)
                              {
                                  bulidGoogleApiClient();
                              }
                              mMap.setMyLocationEnabled(true);
                          }
                      }
                      else
                      {
                          Toast.makeText(this,"Permission Denied" , Toast.LENGTH_LONG).show();
                      }
              }
          }

          protected synchronized void bulidGoogleApiClient() {
              client = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
              client.connect();

          }

          public void onClick(View v)
          {
              Object dataTransfer[] = new Object[2];
              GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();

              switch(v.getId())
              {
                  case R.id.B_search:
                      EditText tf_location =  findViewById(R.id.TF_location);
                      String location = tf_location.getText().toString();
                      List<Address> addressList;


                      if(!location.equals(""))
                      {
                          Geocoder geocoder = new Geocoder(this);

                          try {
                              addressList = geocoder.getFromLocationName(location, 5);

                              if(addressList != null)
                              {
                                  for(int i = 0;i<addressList.size();i++)
                                  {
                                      LatLng latLng = new LatLng(addressList.get(i).getLatitude() , addressList.get(i).getLongitude());
                                      MarkerOptions markerOptions = new MarkerOptions();
                                      markerOptions.position(latLng);
                                      markerOptions.title(location);
                                      mMap.addMarker(markerOptions);
                                      mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                      mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                                  }
                              }
                          } catch (IOException e) {
                              e.printStackTrace();
                          }
                      }
                      break;
//                  case R.id.B_hopistals:
//                      mMap.clear();
//                      String hospital = "hospital";
//                      String url = getUrl(latitude, longitude, hospital);
//                      dataTransfer[0] = mMap;
//                      dataTransfer[1] = url;
//
//                      getNearbyPlacesData.execute(dataTransfer);
//                      Toast.makeText(MapActivity.this, "Showing Nearby Hospitals", Toast.LENGTH_SHORT).show();
//                      break;
//
//
//                  case R.id.B_schools:
//                      mMap.clear();
//                      String school = "school";
//                      url = getUrl(latitude, longitude, school);
//                      dataTransfer[0] = mMap;
//                      dataTransfer[1] = url;
//
//                      getNearbyPlacesData.execute(dataTransfer);
//                      Toast.makeText(MapActivity.this, "Showing Nearby Schools", Toast.LENGTH_SHORT).show();
//                      break;
//                  case R.id.B_restaurants:
//                      mMap.clear();
//                      String resturant = "restuarant";
//                      url = getUrl(latitude, longitude, resturant);
//                      dataTransfer[0] = mMap;
//                      dataTransfer[1] = url;
//
//                      getNearbyPlacesData.execute(dataTransfer);
//                      Toast.makeText(MapActivity.this, "Showing Nearby Restaurants", Toast.LENGTH_SHORT).show();
//                      break;
//                  case R.id.B_to:
              }
          }


          private String getUrl(double latitude , double longitude , String nearbyPlace)
          {

              StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
              googlePlaceUrl.append("location="+latitude+","+longitude);
              googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
              googlePlaceUrl.append("&type="+nearbyPlace);
              googlePlaceUrl.append("&sensor=true");
              googlePlaceUrl.append("&key="+"AIzaSyDKNJyIaDzu7oRy84xqlVbOZ99-jCumD3g");

              Log.d("MapsActivity", "url = "+googlePlaceUrl.toString());

              return googlePlaceUrl.toString();
          }



          @Override
          public void onLocationChanged(Location location) {
              latitude = location.getLatitude();
              longitude = location.getLongitude();
              lastlocation = location;
              if(currentLocationmMarker != null)
              {
                  currentLocationmMarker.remove();

              }
              Log.d("lat = ",""+latitude);
              LatLng latLng = new LatLng(location.getLatitude() , location.getLongitude());
              MarkerOptions markerOptions = new MarkerOptions();
              markerOptions.position(latLng);
              markerOptions.title("Current Location");
              markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
              currentLocationmMarker = mMap.addMarker(markerOptions);

//              if(client != null)
//              {
//                  fusedLocationClient.getLastLocation().addOnSuccessListener(this, locations -> {
//                      if (locations != null) {
//                          latitude = locations.getLatitude();
//                          longitude = locations.getLongitude();
//                          txtLocation.setText(String.format(Locale.US, "%s -- %s", latitude, longitude)
//                          );
//                      }
//                  });
//              } else {
//                  Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
//              }
////                  LocationServices.FusedLocationApi.removeLocationUpdates(client,this);

          }


          @Override
          public void onConnected(@Nullable Bundle bundle) {
              locationRequest = new LocationRequest();
              locationRequest.setInterval(100);
              locationRequest.setFastestInterval(1000);
              locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


//              if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED)
//              {
//                  LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
//              }
          }

          @Override
          public void onConnectionSuspended(int i) {
              Log.d(TAG, "Google Connection Suspended");
          }

          @Override
          public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
              Log.e(TAG, "Connection Failed:" + connectionResult.getErrorMessage());
          }


          public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.menu_acctset:
                Intent toAcct = new Intent(MapActivity.this,AccountSettings.class);
                startActivity(toAcct);
                break;

            case R.id.menu_famlist:
//                Intent toList = new Intent(MapActivity.this,FamilyList.class);
//                startActivity(toList);

                Toast.makeText(this, "Under Construction.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_logout:
                Intent toMain = new Intent(MapActivity.this, MainActivity.class);
                startActivity(toMain);
                Toast.makeText(MapActivity.this, "Disconnected.", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMinZoomPreference(15.0f);
        mMap.setMaxZoomPreference(20.0f);
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);
        bulidGoogleApiClient();
        final LatLng putatan = new LatLng(14.397420, 121.033051);


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

                            LatLng location = new LatLng(lat1,lon2);
                            marker = mMap.addMarker(new MarkerOptions()
                                    .position(location)
                                    .title("Here")
                                    .flat(true));

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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


          @Override
          public void onMapClick(LatLng latLng) {
                markerForGeofence(latLng);
          }

          Marker geoFenceMarker;
          private void markerForGeofence(LatLng latLng) {
            MarkerOptions optionMarker = new MarkerOptions()
                    .position(latLng)
                    .title("Geofence Marker");


            if (mMap!=null)
            {
                if (geoFenceMarker!=null)
                {
                    geoFenceMarker.remove();
                }

                geoFenceMarker = mMap.addMarker(optionMarker);
            }

          }

          private void removeGeofenceDraw() {
              Log.d(TAG, "removeGeofenceDraw()");
              if ( geoFenceMarker != null)
                  geoFenceMarker.remove();
              if ( geoFenceLimits != null )
                  geoFenceLimits.remove();
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
              circle = mMap.addCircle(new CircleOptions()
                      .center(latLng)
                      .radius(100f)
                      .strokeColor(Color.rgb(216,191,216))
                      .fillColor(Color.TRANSPARENT));


              circleBound = FirebaseDatabase.getInstance().getReference("conilocationdata/circles");
              circleBound.push().setValue(circle);

          }
      }
