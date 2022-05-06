package com.example.balotravel;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateNewJouneyActivity extends AppCompatActivity implements PlaceDetailBottomSheet.BottomSheetListener{

    protected RecyclerView recyclerView;
    protected RecyclerView.Adapter adapter;

    protected EditText editText;
    protected ArrayList <com.example.balotravel.Model.Place> placeList = new ArrayList<com.example.balotravel.Model.Place>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_jouney);
        editText = findViewById(R.id.placeInput);
        editText.setFocusable(false);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ID, Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).setCountry("VN").setHint("Hồ Hoàn Kiếm,...").build(CreateNewJouneyActivity.this);
                startActivityForResult(intent, 100);
            }
        });
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyBto2FuliFbIADOWHcH3Sf5LcR85_Mjbns");
        }
        recyclerView = (RecyclerView) findViewById(R.id.activePlacesView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK ) {
            Place place = Autocomplete.getPlaceFromIntent(data);

            Log.i("Place List:", placeList.get(0).getName());

            PlaceDetailBottomSheet placeDetailBottomSheet = new PlaceDetailBottomSheet(place);
            placeDetailBottomSheet.show(getSupportFragmentManager(), "placeDetailBottomSheet");

        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onButtonClicked(com.google.android.libraries.places.api.model.Place place) {

        placeList.add( new com.example.balotravel.Model.Place(place.getId(), place.getName(), place.getAddress()));
        adapter = new PlaceListViewAdapter(placeList, this);
        recyclerView.setAdapter(adapter);
        Log.i("Place List:", "Clicked");
    }
}

