package com.example.modeluapp;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.modeluapp.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.model.DirectionsResult;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    DatabaseReference locationRef;
    MarkerOptions myLocation, destination;
    String jID, location;
    Location curLocation;
    Button directionBnt;
    List<Address> list;
    List<MarkerOptions> markerOptions = new ArrayList<>();
    private Polyline polyline;
    private FusedLocationProviderClient fusedLocationProviderClient;
    //LatLng myLocation;

    private Polyline currentPolyline;
    private MarkerOptions place1, place2;



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
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        Toolbar toolbar = (Toolbar)findViewById(R.id.tb);

        location = getIntent().getExtras().get("location").toString();
        getLocationPermission();
      //  Toast.makeText(this, ""+location, Toast.LENGTH_SHORT).show();


        directionBnt = findViewById(R.id.directionBnt);

//        myLocation = new MarkerOptions().position(new LatLng(curLocation.getLatitude(), curLocation.getLongitude())).title("MyLocation");
//        Address address = list.get(0);
//        destination = new MarkerOptions().position(new LatLng(address.getLatitude(), address.getLongitude())).title("Destination");

        //String url = getUrl(myLocation.getPosition(), destination.getPosition(), "driving");

//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);


        directionBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                directions();
                //new FetchURL(MapsActivity.this).execute(getUrl(myLocation.getPosition(), destination.getPosition(), "driving"));
            }
        });

//        markerOptions.add(myLocation);
//        markerOptions.add(destination);

    }

    private void directions() {
//        myLocation = new MarkerOptions().position(new LatLng(curLocation.getLatitude(), curLocation.getLongitude())).title("MyLocation");
//        Address address = list.get(0);
//        destination = new MarkerOptions().position(new LatLng(address.getLatitude(), address.getLongitude())).title("Destination");
//
//        new FetchURL(MapsActivity.this).execute(getUrl(myLocation.getPosition(), destination.getPosition(), "driving"));
//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .build();
//        Request request = new Request.Builder()
//                .url("https://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&key=YOUR_API_KEY")
//                .method("GET", null)
//                .build();
//        Response response = client.newCall(request).execute();

    }

    private String getUrl(LatLng origin, LatLng destination, String modeT) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        String str_des = "destination=" + destination.latitude + "," + destination.longitude;

        String mode = "mode" + modeT;

        String params = str_origin + "&" + str_des + "&" + mode;

        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + params + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (mLocationPermissionGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            mMap.setMyLocationEnabled(true);
            //geoLocate(location);
        }

    }

    private void getDeviceLocation() {
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

                            moveCamera(new LatLng(curLocation.getLatitude(), curLocation.getLongitude()),
                                    DEFAULT_ZOOM, "My Location");
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

        //geoLocate(location);
        LatLng jobLocation = new LatLng(53.34087988233079, -6.260624488091135);
        mMap.addMarker(new MarkerOptions().position(jobLocation).title("Job Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(jobLocation));
    }

    private void geoLocate(String location) {
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        list = new ArrayList();
        try {
            list = geocoder.getFromLocationName(location, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(list.size()>0){
            Address address = list.get(0);
            Log.d(TAG, "address found: " + address.toString());


//            LatLng jobLocation = new LatLng(53.34087988233079, -6.260624488091135);
//            mMap.addMarker(new MarkerOptions().position(jobLocation).title("Job Location"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(jobLocation));

//            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
//            mMap.moveCamera(new com.google.type.LatLng()address.getLatitude(), address.getLongitude(), DEFAULT_ZOOM);
            moveCamera(new LatLng(53.34087988233079, -6.260624488091135), DEFAULT_ZOOM, address.getAddressLine(0));

            //moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));
            Log.d(TAG, "geoLocate: "+address.getLatitude() +address.getLongitude());
            //mMap.addMarker(new MarkerOptions().position(address.getLatitude(), address.getLongitude()).title("Job"));
            //mMap.addMarker(new MarkerOptions().position(53.340899099375314, -6.260560115078857).title("Job Location"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(jobLocation));
        }
    }

    private void moveCamera(LatLng latLng, float defaultZoom, String title) {
        Log.d(TAG, "moveCamera: MOVING CAMERA TO: LAT:" + latLng.latitude + ", long: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, defaultZoom));
        if(!title.equals("My Location")) {
//            MarkerOptions options = new MarkerOptions().position(latLng).title(title);
//            mMap.addMarker(options);
            MarkerOptions options = new MarkerOptions().position(latLng).title(title);
            mMap.addMarker(options);
//            calculateDirections(options);
        }
    }

    private void getLocationPermission() {
        String permission[] = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQ_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQ_CODE);
        }
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//        if(geoApiContext == null){
//            geoApiContext = new GeoApiContext.Builder().apiKey(getString(R.string.google_maps_key)).build();
//        }
    }
}