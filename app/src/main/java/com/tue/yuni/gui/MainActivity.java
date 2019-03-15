package com.tue.yuni.gui;

import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.tue.yuni.R;
import com.tue.yuni.gui.canteenDetails.CanteenView;
import com.tue.yuni.models.Product;
import com.tue.yuni.models.Review;
import com.tue.yuni.storage.RemoteStorage;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        // TEST
        RemoteStorage.get(this).getCanteens(
                canteens -> {

                },
                error -> {
                    Log.d("abcd", "dit dus: " + error.getMessage());
                }
        );
        // TEST

        // Products List View Example
        List<String> productCategories = new ArrayList<>();
        List<List<Product>> productsByCategory = new ArrayList<>();
        List<Product> products;
        // Drinks
        products = new ArrayList<>();
        products.add(new Product(0, "Coffee", 3, 55, 0.99f, R.drawable.coffee, null));
        products.add(new Product(1, "Tea", 4, 20, 0.50f, R.drawable.tea, null));
        products.add(new Product(2, "Hot Chocolate", 4.5f, 50, 1.49f, R.drawable.hot_chocolate, new ArrayList<Review>() {{
            add(new Review(0, "Tasty and filling but not enough veggies", 3.5f));
            add(new Review(1, "Rip off, way too expensive", 1.5f));
            add(new Review(2, "Tastes like trash", 0.5f));
            add(new Review(3, "I've never had a hot chocolate taste this good!!", 5.0f));
        }}));
        products.add(new Product(3, "Cappuccino", 5, 20, 0.50f, R.drawable.tea, null));
        products.add(new Product(4, "Coca-Cola", 3.5f, 20, 0.50f, R.drawable.tea, null));
        products.add(new Product(5, "Pepsi", 1.0f, 20, 0.50f, R.drawable.tea, null));
        products.add(new Product(6, "Chinotto", 2.5f, 20, 0.50f, R.drawable.tea, null));
        products.add(new Product(7, "Sprite", 2.5f, 20, 0.50f, R.drawable.tea, null));
        productsByCategory.add(products);
        productCategories.add("Drinks");
        // Sandwich
        products = new ArrayList<>();
        products.add(new Product(3, "Tuna sandwich", 1, 0, 3.99f, R.drawable.coffee, new ArrayList<Review>() {{
            add(new Review(0, "This thing is nasty and costs way too much, STOP SELLING THIS!", 0.0f));
        }}));
        products.add(new Product(4, "Chicken Sandwich", 2.5f, 75, 2.50f, R.drawable.tea, new ArrayList<Review>() {{
            add(new Review(0, "Review 1", 0.0f));
            add(new Review(1, "Review 2", 4.0f));
            add(new Review(2, "Review 3", 2.0f));
        }}));
        productsByCategory.add(products);
        productCategories.add("Sandwich");

        // Create Fragment Transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Setup Fragment Arguments
        Bundle args = new Bundle();
        args.putStringArrayList("productCategories", (ArrayList<String>) productCategories);
        for (int i = 0; i < productCategories.size(); i++)
            args.putParcelableArrayList("productsByCategory" + i, (ArrayList<Product>) productsByCategory.get(i));
        // Create Fragment Itself
        CanteenView storeView = new CanteenView();
        storeView.setArguments(args);
        // Transition to Fragment
        ft.add(R.id.content, storeView);
        ft.commit();
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
}
