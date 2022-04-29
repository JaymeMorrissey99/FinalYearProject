//package Fragments;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.Fragment;
//
//import android.Manifest;
//import android.content.pm.PackageManager;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import com.example.modeluapp.R;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.database.DatabaseReference;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class MapsFragment extends Fragment {
//
//    private GoogleMap mMap;
//    //private ActivityMapsBinding binding;
//    DatabaseReference locationRef;
//    String jID, locationString;
//    private FusedLocationProviderClient fusedLocationProviderClient;
//
//
//    private static final String TAG = "LOCATION";
//    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
//    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
//
//    private boolean mLocationPermissionGranted = false;
//    private static int LOCATION_PERMISSION_REQ_CODE = 1234;
//    private static final float DEFAULT_ZOOM = 15;
//
//    private OnMapReadyCallback callback = new OnMapReadyCallback() {
//
//        @Override
//        public void onMapReady(GoogleMap googleMap) {
//            mMap = googleMap;
//            mMap.getUiSettings().setZoomControlsEnabled(true);
//            if (mLocationPermissionGranted) {
//                getDeviceLocation();
//                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
//                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
//                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//                    return;
//                }
//                mMap.setMyLocationEnabled(true);
//                //geoLocate(location);
//            }
//        }
//    };
//
//    private void getDeviceLocation() {
//        Log.d(TAG, "getDeviceLocation: ");
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
//        try {
//            if(mLocationPermissionGranted){
//                Task location = fusedLocationProviderClient.getLastLocation();
//                location.addOnCompleteListener(new OnCompleteListener() {
//                    @Override
//                    public void onComplete(@NonNull Task task) {
//                        if(task.isSuccessful()){
//                            Log.d(TAG, "onComplete: FOUND LOCATION");
//                            Location curLocation = (Location) task.getResult();
//
//                            moveCamera(new LatLng(curLocation.getLatitude(), curLocation.getLongitude()),
//                                    DEFAULT_ZOOM, "My Location");
//                        }else{
//                            Log.d(TAG, "onComplete: current location == null");
//                            Toast.makeText(getContext(), "unable to get current location", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                });
//            }
//        }catch(SecurityException e){
//            Log.e(TAG, "getDeviceLocation: SecurityException" + e.getMessage());
//        }
//        geoLocate(locationString);
//    }
//
//    private void geoLocate(String location) {
//        Geocoder geocoder = new Geocoder(getContext());
//        List<Address> list = new ArrayList();
//        try {
//            list = geocoder.getFromLocationName(location, 1);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if(list.size()>0){
//            Address address = list.get(0);
//            Log.d(TAG, "address found: " + address.toString());
//
////            LatLng jobLocation = new LatLng(address.getLatitude(), address.getLongitude());
////            mMap.addMarker(new MarkerOptions().position(jobLocation).title("Job Location"));
////            mMap.moveCamera(CameraUpdateFactory.newLatLng(jobLocation));
//
////            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
////            mMap.moveCamera(new com.google.type.LatLng()address.getLatitude(), address.getLongitude(), DEFAULT_ZOOM);
//            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));
//
////            mMap.addMarker(new MarkerOptions().position(jobLocation).title("Job Location"));
////            mMap.moveCamera(CameraUpdateFactory.newLatLng(jobLocation));
//        }
//    }
//
//    private void moveCamera(LatLng latLng, float defaultZoom, String my_location) {
//        Log.d(TAG, "moveCamera: MOVING CAMERA TO: LAT:" + latLng.latitude + ", long: " + latLng.longitude);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, defaultZoom));
//        if(!my_location.equals("My Location")) {
//            MarkerOptions options = new MarkerOptions().position(latLng).title(my_location);
//            mMap.addMarker(options);
//        }
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        View view= inflater.inflate(R.layout.fragment_maps, container, false);
//
//        locationString = getArguments().getString("location");
////        location = getIntent().getExtras().get("location").toString();
//        getLocationPermission();
//
//        return view;
//    }
//
//    private void getLocationPermission() {
//        String permission[] = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
//        if(ContextCompat.checkSelfPermission(getContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//            if(ContextCompat.checkSelfPermission(getContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//                mLocationPermissionGranted = true;
//                initMap();
//            }else{
//                ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQ_CODE);
//            }
//        }else{
//            ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQ_CODE);
//        }
//    }
//
//    private void initMap() {
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        SupportMapFragment mapFragment =
//                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//        if (mapFragment != null) {
//            mapFragment.getMapAsync(callback);
//        }
//    }
//
//
//
//
//
//}