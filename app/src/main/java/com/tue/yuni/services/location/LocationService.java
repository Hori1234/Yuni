package com.tue.yuni.services.location;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.tue.yuni.models.Location;

import java.text.DecimalFormat;

public class LocationService {
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    private static final int LATITUDE_CONSTANT = 111200; //=1 degrees, because 0.005959 degrees = 663.63 m
    private static final int LONGITUDE_CONSTANT = 70000; //= 1 degrees, because 0.15668 degrees = 1100 m
    private static final int WALKING_VELOCITY = 5000; //m per hour
    private static final int WALK_TIME_IN_MINUTES = 60;
    //60: walking time in minutes; 1:time in hours; 3600: time in seconds
    private static FusedLocationProviderClient mFusedLocationClient;
    private static DecimalFormat deci2 = new DecimalFormat(".##");

    public static double getWalkingTime(Location start, Location end) {
        double deltaLat = Math.abs(start.getLatitude() - end.getLatitude());
        double deltaLon = Math.abs(start.getLongitude() - end.getLongitude());

        return (deltaLat * LATITUDE_CONSTANT + deltaLon * LONGITUDE_CONSTANT) / WALKING_VELOCITY * WALK_TIME_IN_MINUTES;
    }

    public static void initialise(Activity activity, LocationReceivedListener listener) {
        if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //if permission is not granted

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                new AlertDialog.Builder(activity.getApplicationContext())
                        .setTitle("Required Location Permission")
                        .setMessage("You have to give this permission before you can use the app")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(activity,
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                            }
                        })
                        .setNegativeButton(
                                "Cancel",
                                (dialogInterface, i) -> dialogInterface.dismiss())
                        .create()
                        .show();


            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
            mFusedLocationClient.getLastLocation().addOnSuccessListener(
                    // Activity,
                    location -> {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();

                            // Notify listener
                            listener.onLocationReceived(new Location(longitude, latitude));
                        }
                    });

        }
    }
}
