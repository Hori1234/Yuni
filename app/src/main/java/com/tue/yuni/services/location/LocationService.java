package com.tue.yuni.services.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.tue.yuni.models.Location;

import java.text.DecimalFormat;

import static java.lang.Math.asin;
import static java.lang.Math.sqrt;

public class LocationService {
    private static LocationService locationService = null;

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    private static final int LATITUDE_CONSTANT = 111200; //=1 degrees, because 0.005959 degrees = 663.63 m
    private static final int LONGITUDE_CONSTANT = 70000; //= 1 degrees, because 0.15668 degrees = 1100 m
    private static final int WALKING_VELOCITY = 5000; //m per hour
    private static final int WALK_TIME_IN_MINUTES = 60;
    //60: walking time in minutes; 1:time in hours; 3600: time in seconds
    private static FusedLocationProviderClient mFusedLocationClient;
    private static DecimalFormat deci2 = new DecimalFormat(".##");
    private Activity activity;
    private LocationInitializedListener parent;

    public static double getWalkingTime(Location start, Location end) {
        double deltaLat = Math.abs(start.getLatitude() - end.getLatitude());
        double deltaLon = Math.abs(start.getLongitude() - end.getLongitude());

        return (deltaLat * LATITUDE_CONSTANT + deltaLon * LONGITUDE_CONSTANT) / WALKING_VELOCITY * WALK_TIME_IN_MINUTES;
    }

    public static void initialize(Activity activity, LocationInitializedListener onDone) {
        if (locationService == null) {
            synchronized (LocationService.class) {
                locationService = new LocationService(activity, onDone);
            }
        }
    }

    public static LocationService get(){
        return locationService;
    }

    private LocationService(Activity activity, LocationInitializedListener onDone) {
        this.parent = onDone;
        this.activity = activity;
        if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Request Permission if not granteed
                Log.d("abcd", "approved 1");
                new AlertDialog.Builder(activity)
                        .setTitle("Required Location Permission")
                        .setMessage("You have to give this permission before you can use the app")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(activity,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                            }
                        })
                        .setNegativeButton(
                                "Cancel",
                                (dialogInterface, i) -> {
                                    dialogInterface.dismiss();
                                    System.exit(0);
                                })
                        .setCancelable(false)
                        .create()
                        .show();
        } else {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
            onDone.onInitialized(this, true);
        }
    }

    public boolean requestLocation(final LocationReceivedListener listener) {
        if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Request a location update
            mFusedLocationClient.requestLocationUpdates(new LocationRequest(), new LocationCallback(){
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    if (locationResult.getLocations().size() > 0) {
                        android.location.Location loc = locationResult.getLocations().get(locationResult.getLocations().size() - 1);
                        Log.d("LocationService", "Accuracy: " + loc.getAccuracy());
                        // Logic to handle location object
                        double latitude = loc.getLatitude();
                        double longitude = loc.getLongitude();

                        // Notify listener
                        listener.onLocationReceived(new Location(longitude, latitude));
                    }
                }

                @Override
                public void onLocationAvailability(LocationAvailability locationAvailability) {
                    super.onLocationAvailability(locationAvailability);
                }
            }, null);
            return true;
        } else {
            return false;
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
                    parent.onInitialized(this, true);
                } else {
                    parent.onInitialized(this, false);
                }
                return;
            }
        }

    }

    public static float distanceInKm(Location start, Location dest) {
        double p = 0.017453292519943295;     //Pi/180
        double a = 0.5 -
                Math.cos((start.getLatitude() - dest.getLatitude()) * p)/2 + Math.cos(start.getLatitude() * p) *
                Math.cos(dest.getLatitude() * p) * (1 - Math.cos((dest.getLongitude() - start.getLongitude()) * p)) / 2;
        return (float)(12742 * asin(sqrt(a))); //2*R*asin...
    }
}
