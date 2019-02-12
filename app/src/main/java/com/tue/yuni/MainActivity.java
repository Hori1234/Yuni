package com.tue.yuni;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Load Layout into Content
        LinearLayout content = findViewById(R.id.content);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_products, null);
        content.addView(view);

        // Products List View Example
        List<Product> products = new ArrayList<>();
        products.add(new Product(0, "Coffee", 3, 55, 0.99f, R.drawable.coffee, null));
        products.add(new Product(1, "Tea", 4, 20, 0.50f, R.drawable.tea, null));
        products.add(new Product(2, "Hot Chocolate", 4.5f, 80, 1.49f, R.drawable.hot_chocolate, new ArrayList<ProductReview>(){{
            add(new ProductReview(0, "Tasty and filling but not enough veggies", 3.5f));
            add(new ProductReview(1, "Rip off, way too expensive", 1.5f));
            add(new ProductReview(1, "Tastes like trash", 0.5f));
            add(new ProductReview(1, "I've never had a hot chocolate taste this good!!", 5.0f));
        }}));
        products.add(new Product(3, "Tuna sandwich", 1, 0, 3.99f, R.drawable.coffee, null));
        products.add(new Product(4, "Chicken Sandwich", 2.5f, 75, 2.50f, R.drawable.tea, null));
        products.add(new Product(5, "Pulled pork sandwich", 3.5f, 80, 4.09f, R.drawable.hot_chocolate, null));
        ProductsListView productsListView = new ProductsListView(this, (ListView)findViewById(R.id.productsList), products);


        //Toast.makeText(this, "Welcome", Toast.LENGTH_LONG).show();
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
