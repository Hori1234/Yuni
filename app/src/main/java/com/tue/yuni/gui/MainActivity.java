package com.tue.yuni.gui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.tue.yuni.R;
import com.tue.yuni.gui.util.MotionDetector;
import com.tue.yuni.gui.util.OnShakeListener;
import com.tue.yuni.gui.landingPage.LandingView;
import com.tue.yuni.models.Location;
import com.tue.yuni.models.canteen.Canteen;
import com.tue.yuni.services.location.LocationService;
import com.tue.yuni.storage.FavouriteStorage;
import com.tue.yuni.storage.PasswordStorage;
import com.tue.yuni.storage.RemoteStorage;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnShakeListener {
    MotionDetector motionDetector;
    List<Canteen> canteens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Instantiate Layout
        setContentView(R.layout.activity_main);
        // Instantiate Motion Detector
        motionDetector = new MotionDetector(this);
        motionDetector.setShakeDetection(true);
        motionDetector.shakeListener = this;
        // Instantiate PasswordStorage
        PasswordStorage.initialize(getApplicationContext());
        // Instantiate Favorite Storage
        FavouriteStorage.initialize(getApplicationContext());
        // Instantiate Remote Storage
        RemoteStorage.initialise(getApplicationContext());
        // Initial Fragment Transaction
        if (savedInstanceState == null) {
            // Create Fragment Transaction
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.content, new LandingView());
            ft.commit();
        }

        LocationService.initialize(this,  (locationService, success) -> {
            // Location service is initialized
            if (!success) {
                // Exit app since permission was denied
                System.exit(0);
            }
        });
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Used for saving UI Status on Screen Rotation
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onShake(final int count) {
        // Shake 3 times
        if (count == 3) {
            // Get Location to use in ShakeDetector
            LocationService.get().requestLocation(location -> {
                // Check if list of canteens already exists
                if (canteens == null) {
                    // Get all canteens locations for the ShakeDetector;
                    RemoteStorage.get().getCanteens(canteens1 -> {
                        canteens = canteens1;
                        // Call function again
                        onShake(count);
                    }, e -> {
                        // ToDo Error
                    });
                } else {
                    // Run through all canteens to find out in which canteen we are
                    for (int i = 0; i < canteens.size(); i++) {
                        // Get distance to canteen in Km
                        float d = LocationService.distanceInKm(location, canteens.get(i).getLocation());
                        Log.d("ShakeDetector", "Distance to canteen " + canteens.get(i).getName() + ": " + d + "km");
                        // Check if canteen is within 50m
                        if (d < 0.05) {
                            RemoteStorage.get().createBusynessEntry(canteens.get(i).getId(), () -> {
                                // Not Necessary
                            }, e -> {
                                // Not Necessary
                            });
                            // Notification
                            Toast.makeText(getApplicationContext(), "Notifying canteen of busyness", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    // Notification
                    Toast.makeText(getApplicationContext(), "You are not in a canteen", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
  
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LocationService.get().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
