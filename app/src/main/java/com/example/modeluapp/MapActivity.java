package com.example.modeluapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.modeluapp.databinding.ActivityMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.maps.GeoApiContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import DirectionHelpers.FetchURL;
//import DirectionHelpers.TaskLoadedCallback;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    DatabaseReference locationRef;
    MarkerOptions myLocation, destination;
    String jID, location;
    Location curLocation;
    Button directionBnt, weather;
    List<Address> list;
    List<MarkerOptions> markerOptions = new ArrayList<>();
    private Polyline polyline;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private static final String TAG = "LOCATION";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    private boolean mLocationPermissionGranted = false;
    private static int LOCATION_PERMISSION_REQ_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15;
    private GeoApiContext geoApiContext = null;
    private Handler mHandler = new Handler();
    private Runnable mRunnable;
    private static final int LOCATION_UPDATE_INTERVAL = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        location = getIntent().getExtras().get("location").toString();
        //getLocationPermission();
        directionBnt = findViewById(R.id.directionBnt);
        weather = findViewById(R.id.weather);
        //geoLocate(location);
        getMyLocation();


        myLocation = new MarkerOptions().position(new LatLng(curLocation.getLatitude(), curLocation.getLongitude())).title("MyLocation");
        Address address = list.get(0);
        destination = new MarkerOptions().position(new LatLng(address.getLatitude(), address.getLongitude())).title("Destination");

        //String url = getUrl(myLocation.getPosition(), destination.getPosition(), "driving");

//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.mapFrag);
//        mapFragment.getMapAsync(this);

       // MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFrag);


        directionBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new FetchURL(MapActivity.this).execute(getUrl(myLocation.getPosition(), destination.getPosition(), "driving"));
            }
        });

        markerOptions.add(myLocation);
        markerOptions.add(destination);
    }

    private void getMyLocation() {
        Log.d(TAG, "getDeviceLocation: ");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if(mLocationPermissionGranted){
                Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: FOUND LOCATION");
                            curLocation = (Location) task.getResult();

//                            moveCamera(new LatLng(curLocation.getLatitude(), curLocation.getLongitude()),
//                                    DEFAULT_ZOOM, "My Location");
                        }else{
                            Log.d(TAG, "onComplete: current location == null");
                            Toast.makeText(getApplicationContext(), "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }
        }catch(SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException" + e.getMessage());
        }
        geoLocate(location);
    }



//    private String getLocation() {
//    }

    private void geoLocate(String location) {
        Geocoder geocoder = new Geocoder(MapActivity.this);
        list = new ArrayList();
        try {
            list = geocoder.getFromLocationName(location, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(list.size()>0){
            Address address = list.get(0);
            Log.d(TAG, "address found: " + address.toString());

//            LatLng jobLocation = new LatLng(address.getLatitude(), address.getLongitude());
//            mMap.addMarker(new MarkerOptions().position(jobLocation).title("Job Location"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(jobLocation));

//            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
//            mMap.moveCamera(new com.google.type.LatLng()address.getLatitude(), address.getLongitude(), DEFAULT_ZOOM);
//            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));
//

//            mMap.addMarker(new MarkerOptions().position(jobLocation).title("Job Location"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(jobLocation));
        }
    }

    private String getUrl(LatLng origin, LatLng destination, String modeT) {
        String str_origin = "origin" + origin.latitude + "," + origin.longitude;

        String str_des = "destination" + destination.latitude + "," + destination.longitude;

        String mode = "mode" + modeT;

        String params = str_origin + "&" + str_des + "&" + mode;

        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + params + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

//    @Override
//    public void onTaskDone(Object... values) {
//        if(polyline != null)
//            polyline.remove();
//
//        polyline = mMap.addPolyline((PolylineOptions) values[0]);
//    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(myLocation);
        mMap.addMarker(destination);
        showMarkers();
    }

    private void showMarkers() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for(MarkerOptions m : markerOptions){
            builder.include(m.getPosition());
        }
        LatLngBounds bounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.30);

        CameraUpdate cupdate = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        mMap.animateCamera(cupdate);
    }
}