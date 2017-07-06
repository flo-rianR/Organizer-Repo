package com.hswgt.florian.organizer;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

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

    private GoogleMap mMap;
    Marker marker;
    private MySQLiteHelper eDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        eDB = new MySQLiteHelper(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        mMap = googleMap;
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("You were here!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));

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
               MarkerOptions options = new MarkerOptions()
                       .title(address.getAddressLine(0))
                       .position(new LatLng(latLng.latitude,
                               latLng.longitude));

               marker = mMap.addMarker(options);
           }
       });
    }

    private void showDialog(final LatLng latLng, final Address address)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        String message = "";
        if(address.getPostalCode() != null) message = address.getPostalCode() + " ";
        if(address.getLocality() != null) message += address.getLocality();
        message += "\r\n" + latLng.latitude + ", " + latLng.longitude;

        dialog.setTitle("Diesen Standort hinzufügen?");
        dialog.setMessage(message);
        dialog.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bundle extras = getIntent().getExtras();
                String location = "";
                if(address.getPostalCode() != null) location += address.getPostalCode(); location += " ";
                if(address.getLocality() != null) location += address.getLocality();
                eDB.addLocationToEntry(extras.getLong("list"), extras.getString("description"), location, latLng.latitude, latLng.longitude);
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
}
