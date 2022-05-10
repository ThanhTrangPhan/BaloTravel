package com.example.balotravel;

import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;

import com.example.balotravel.Model.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.balotravel.databinding.ActivityPlaceListOnMapBinding;

import java.util.ArrayList;

public class PlaceListOnMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityPlaceListOnMapBinding binding;
    protected ArrayList<Place> placeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        placeList = (ArrayList<Place>) intent.getSerializableExtra("BUNDLE");
        binding = ActivityPlaceListOnMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        int index = 0;
        for (Place place: this.placeList) {
            LatLng sydney = new LatLng(place.getLatitude(), place.getLongitude());
//            mMap.addMarker(new MarkerOptions().position(sydney).title(place.getName()));
            Bitmap.Config conf = Bitmap.Config.ARGB_8888;
            Bitmap bmp = Bitmap.createBitmap(120, 120, conf);
            Canvas canvas1 = new Canvas(bmp);

// paint defines the text color, stroke width and size
            Paint color = new Paint();
            color.setTextSize(50);
            color.setColor(Color.RED);


// modify canvas
            canvas1.drawBitmap(BitmapFactory.decodeResource(getResources(),
                    R.drawable.map_marker), null, new Rect(0,0,120,120), null);
            canvas1.drawText(String.valueOf(++index), 45, 60, color);
            //canvas1.drawText(place.getName(), 80, 120, color);

// add marker to Map
            mMap.addMarker(new MarkerOptions()
                    .position(sydney)
                    .icon(BitmapDescriptorFactory.fromBitmap(bmp))
                    .title(place.getName())
                    // Specifies the anchor to be at a particular point in the marker image.
                    .anchor(0.5f, 1));


            builder.include(sydney);
        }
        LatLngBounds bounds = builder.build();
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 300));

    }
}