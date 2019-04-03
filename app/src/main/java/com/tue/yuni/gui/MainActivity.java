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
import com.tue.yuni.services.location.LocationService;
import com.tue.yuni.storage.FavouriteStorage;
import com.tue.yuni.storage.PasswordStorage;
import com.tue.yuni.storage.RemoteStorage;

public class MainActivity extends AppCompatActivity implements OnShakeListener {
    MotionDetector motionDetector;

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
            if (success) {
                locationService.requestLocation(location -> {
                    Log.d("abcd", LocationService.getWalkingTime(
                            new Location(5.484207, 51.446439),
                            location
                    ) + "");
                });
            } else {
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
    public void onShake(int count) {
        // Shake 5 times
        if (count == 3) {
            // ToDo
            Toast.makeText(getApplicationContext(), "Shaked 3 times", Toast.LENGTH_SHORT).show();
        }
    }
  
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LocationService.get().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
