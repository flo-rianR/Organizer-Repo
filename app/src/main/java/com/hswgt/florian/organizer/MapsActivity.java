package com.hswgt.florian.organizer;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import database.MySQLiteHelper;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int PERMISSION_REQUEST_LOCATION = 1;
    private GoogleMap mMap;
    Marker marker;
    private MySQLiteHelper eDB;
    float zoomLevel = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        eDB = new MySQLiteHelper(this);
        askForPermission();
        Log.d("DEBUG", "In oncreate");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("DEBUG", "In requestpermissionresult");

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            Log.d("DEBUG", "granted");
            Toast.makeText(this, "Berechtigung LOCATION erlaubt!", Toast.LENGTH_SHORT).show();
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } else {
            Log.d("DEBUG", "not granted");
            finish();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("DEBUG", "map ready");
        mMap = googleMap;
        Location location = getBestLocation();
        if(location != null)
        {

            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(currentLocation).title("You were here!"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, zoomLevel));
        }


        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

           @Override
           public void onMapLongClick (LatLng latLng){
               Geocoder geocoder = new Geocoder(MapsActivity.this);
               List<Address> list;
               try {
                   list = geocoder.getFromLocation(latLng.latitude,
                           latLng.longitude, 1);
               } catch (IOException e) {
                   return;
               }
               Address address = list.get(0);
               if (marker != null) {
                   marker.remove();
               }
               showDialog(latLng, address);
           }
       });
    }

    private void showDialog(final LatLng latLng, final Address address)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        String message = "";
        if(address.getPostalCode() != null) message = address.getPostalCode() + " ";
        if(address.getLocality() != null) message += address.getLocality() + " \r\n";
        if(address.getAddressLine(0) != null) message += address.getAddressLine(0);
        message += "\r\n" + latLng.latitude + ", " + latLng.longitude;

        dialog.setTitle("Diesen Standort hinzufügen?");
        dialog.setMessage(message);
        dialog.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bundle extras = getIntent().getExtras();
                String location = "";
                if(address.getPostalCode() != null) location += address.getPostalCode(); location += " ";
                if(address.getLocality() != null) location += address.getLocality(); location += " ";
                if(address.getAddressLine(0) != null) location += address.getAddressLine(0);
                eDB.addLocationToEntry(extras.getLong("list"), location, latLng.latitude, latLng.longitude);
                finish();

            }
        });

        dialog.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }



    private Location getBestLocation()
    {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location bestResult = null;
        float bestAccuracy = Float.MAX_VALUE;
        long bestAge = Long.MIN_VALUE;
        List<String> matchingProviders = lm.getAllProviders();
        Location location = null;

        for (String provider : matchingProviders) {

            try {
                location = lm.getLastKnownLocation(provider);
            }
            catch (SecurityException se)
            {
                se.printStackTrace();
            }

            if (location != null) {

                float accuracy = location.getAccuracy();
                long time = location.getTime();

                if (accuracy < bestAccuracy) {

                    bestResult = location;
                    bestAccuracy = accuracy;
                    bestAge = time;

                }
            }
        }
            return bestResult;
    }

    private void askForPermission()
    {
        Log.d("DEBUG", "In askforpermission");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            Log.d("DEBUG", "hat sie noch nicht");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_LOCATION);
        }else{
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }
}
