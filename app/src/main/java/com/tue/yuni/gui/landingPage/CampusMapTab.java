package com.tue.yuni.gui.landingPage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tue.yuni.R;
import com.tue.yuni.gui.MainActivity;
import com.tue.yuni.gui.canteenDetails.CanteenView;
import com.tue.yuni.models.Product;
import com.tue.yuni.models.Review;

import java.util.ArrayList;
import java.util.List;

public class CampusMapTab extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_landing_page_canteen_map, null);  // Place Holder
        ((Button)view.findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Products List View Example
                List<String> productCategories = new ArrayList<>();
                List<List<Product>> productsByCategory = new ArrayList<>();
                List<Product> products;
                // Drinks
                products = new ArrayList<>();
                products.add(new Product(0, "Coffee", 3, 55, 0.99f, R.drawable.coffee, "test", null));
                products.add(new Product(1, "Tea", 4, 20, 0.50f, R.drawable.tea, "test", null));
                products.add(new Product(2, "Hot Chocolate", 4.5f, 50, 1.49f, R.drawable.hot_chocolate, "test", new ArrayList<Review>(){{
                    add(new Review(0, "Tasty and filling but not enough veggies", 3.5f));
                    add(new Review(1, "Rip off, way too expensive", 1.5f));
                    add(new Review(2, "Tastes like trash", 0.5f));
                    add(new Review(3, "I've never had a hot chocolate taste this good!!", 5.0f));
                }}));
                products.add(new Product(3, "Cappuccino", 5, 20, 0.50f, R.drawable.tea,"test", null));
                products.add(new Product(4, "Coca-Cola", 3.5f, 20, 0.50f, R.drawable.tea,"test", null));
                products.add(new Product(5, "Pepsi", 1.0f, 20, 0.50f, R.drawable.tea,"test", null));
                products.add(new Product(6, "Chinotto", 2.5f, 20, 0.50f, R.drawable.tea,"test", null));
                products.add(new Product(7, "Sprite", 2.5f, 20, 0.50f, R.drawable.tea,"test", null));
                productsByCategory.add(products);
                productCategories.add("Drinks");
                // Sandwich
                products = new ArrayList<>();
                products.add(new Product(8, "Tuna sandwich", 1, 0, 3.99f, R.drawable.coffee,"test", new ArrayList<Review>(){{
                    add(new Review(0, "This thing is nasty and costs way too much, STOP SELLING THIS!", 0.0f));
                }}));
                products.add(new Product(9, "Chicken Sandwich", 2.5f, 75, 2.50f, R.drawable.tea,"test", new ArrayList<Review>(){{
                    add(new Review(0, "Review 1", 0.0f));
                    add(new Review(1, "Review 2", 4.0f));
                    add(new Review(2, "Review 3", 2.0f));
                }}));
                productsByCategory.add(products);
                productCategories.add("Sandwich");

                // Create Fragment Transaction
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                // Setup Fragment Arguments
                Bundle args = new Bundle();
                args.putStringArrayList("productCategories", (ArrayList<String>)productCategories);
                for (int i = 0; i < productCategories.size(); i++)
                    args.putParcelableArrayList("productsByCategory" + i, (ArrayList<Product>) productsByCategory.get(i));
                // Create Fragment Itself
                CanteenView storeView = new CanteenView();
                storeView.setArguments(args);
                // Transition to Fragment
                ft.addToBackStack("LandingPage");
                ft.replace(R.id.content, storeView);
                ft.commit();
            }
        });
        return view;
    }
}
