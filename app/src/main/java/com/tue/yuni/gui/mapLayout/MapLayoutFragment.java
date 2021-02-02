package com.tue.yuni.gui.mapLayout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.tue.yuni.R;
import com.tue.yuni.gui.canteenDetails.CanteenView;
import com.tue.yuni.models.canteen.Canteen;
import com.tue.yuni.services.location.LocationService;
import com.tue.yuni.services.network.NetworkService;
import com.tue.yuni.storage.FavouriteStorage;
import com.tue.yuni.storage.RemoteStorage;

import java.util.List;


public class MapLayoutFragment extends Fragment implements OnMapReadyCallback, RemoteStorage.CanteensDataHandler, RemoteStorage.ErrorHandler, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {
    private GoogleMap mGoogleMaps;
    private MapView mapView;
    private View view;
    private List<Canteen> canteens;
    private Marker[] markers;
    private Marker shownMarker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_map_layout, container, false);
        shownMarker = null;
        return view;
    }
    @Override
    public void onViewCreated(View view, @NonNull Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);

        mapView = (MapView)view.findViewById(R.id.mapView);
        if (mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());
        mGoogleMaps = googleMap;
        // Required so that clicking an open marker opens the CanteenView
        mGoogleMaps.setOnMarkerClickListener(this);
        // Require to have multi-line snippet boxes
        mGoogleMaps.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                LinearLayout info = new LinearLayout(getContext());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getContext());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(getContext());
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
        // Required to fix broken isInfoWindowsShown function
        mGoogleMaps.setOnMapClickListener(this);

        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getContext(), R.raw.mapstyle));
        } catch (Error e) {
        }

        //x -y map
            //origin corner 51.446212, 5.483913
            //top right 51.449665, 5.495399
        //get latlong for corners for specified place
        LatLng one = new LatLng(51.446212, 5.483913);
        LatLng two = new LatLng(51.449665, 5.495399);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        //add them to builder
        builder.include(one);
        builder.include(two);

        LatLngBounds bounds = builder.build();

        //get width and height to current display screen
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        // 20% padding
        double pad = 0.10f;
        int padding = (int) (width * pad);

        //set latlong bounds
        mGoogleMaps.setLatLngBoundsForCameraTarget(bounds);

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //move camera to fill the bound to screen
        mGoogleMaps.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));
        //set zoom to level to current so that you won't be able to zoom out viz. move outside bounds
        mGoogleMaps.setMinZoomPreference(mGoogleMaps.getCameraPosition().zoom);

        // Query Server for canteens, if network is available
        if (NetworkService.networkAvailabilityHandler(getActivity().getApplicationContext())){
            RemoteStorage.get().getCanteens(this, this);}
    }

    @Override
    public void onReceive(List<Canteen> canteens) {
        this.canteens = canteens;
        if (markers != null) {
            for (int i = 0; i < markers.length; i++) {
                markers[i].remove();
            }
        }
        markers = new Marker[canteens.size()];
        //
        Bitmap bitmap = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.marker_icon), 100, 100, true);
        // Create Custom marker Icon
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(38);
        // Create All Markers and add them to the map
        for (int i = 0; i < canteens.size(); i++){
            // Create Marker at Canteen Position
            Marker marker = mGoogleMaps.addMarker(new MarkerOptions().position(
                    new LatLng(canteens.get(i).getLocation().getLatitude(), canteens.get(i).getLocation().getLongitude())
            ));
            // Custom Marker
            Rect bounds = new Rect();
            paint.getTextBounds(canteens.get(i).getName(), 0, canteens.get(i).getName().length(), bounds);
            Bitmap canvasBitmap = Bitmap.createBitmap(
                    bounds.width() > bitmap.getWidth() ? bounds.width() + 2 : bitmap.getWidth() + 2,
                    bitmap.getHeight() + bounds.height() + 4,
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(canvasBitmap);
            canvas.drawBitmap(bitmap, (int)((canvas.getWidth() - bitmap.getWidth()) / 2.0), 0, paint);
            canvas.drawText(canteens.get(i).getName(), (int)((bounds.width() - canvasBitmap.getWidth()) / 2.0) , bitmap.getHeight() + bounds.height() + 4, paint);
            // Canteen Status
            int status = Canteen.getCanteenCurrentOpenStatus(canteens.get(i));
            if (status == 0)
                marker.setTitle(getContext().getString(R.string.closed));
            else if (status == 1)
                marker.setTitle(getContext().getString(R.string.closing));
            else if (status == 2)
                marker.setTitle(getContext().getString(R.string.open));
            // Favourites Count
            int count = 0;
            for (int m = 0; m < canteens.get(i).getMenuItems().size(); m++) {
                if (FavouriteStorage.get().checkFavorite(canteens.get(i).getMenuItems().get(m).getId()))
                    count++;
            }
            if (count == 1)
                marker.setSnippet(count + " " + getContext().getString(R.string.favAvailable));
            else
                marker.setSnippet(count + " " + getContext().getString(R.string.favAvailable));
            // End
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(canvasBitmap));
            markers[i] = marker;
            markers[i].setTag(i);
        }
        // Request Location
        LocationService.get().requestLocation((location -> {
            for (int i = 0; i < markers.length; i++) {
                // Add distance to canteen in minutes to the marker
                markers[i].setSnippet(String.format("%s\n%d ", markers[i].getSnippet(), (int)LocationService.getWalkingTime(canteens.get(i).getLocation(), location)) +
                        getContext().getString(R.string.minutes));
            }
        }));
    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // Check if InfoBox is Shown
        if (marker.equals(shownMarker)) {
            // Create CanteenView Fragment
            CanteenView canteenView = new CanteenView();
            Bundle args = new Bundle();
            args.putParcelable("Canteen", canteens.get((int)marker.getTag()));
            canteenView.setArguments(args);
            // Transition to Fragment
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.addToBackStack("LandingPage");
            ft.replace(R.id.content, canteenView);
            ft.commit();
        } else {
            shownMarker = marker;
            marker.showInfoWindow();
        }
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        shownMarker = null;
    }
}
