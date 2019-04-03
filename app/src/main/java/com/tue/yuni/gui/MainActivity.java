package com.tue.yuni.gui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.tue.yuni.R;
import com.tue.yuni.gui.landingPage.LandingView;
import com.tue.yuni.models.Location;
import com.tue.yuni.services.location.LocationService;
import com.tue.yuni.storage.FavouriteStorage;
import com.tue.yuni.storage.PasswordStorage;
import com.tue.yuni.storage.RemoteStorage;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                vibrator.vibrate(150);
            }
        });
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

        LocationService.initialise(
                this,
                (loc) -> {
                    Log.d("abcd", LocationService.getWalkingTime(
                            new Location(5.484207, 51.446439),
                            loc
                    ) + "");
                }
        );

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
}
