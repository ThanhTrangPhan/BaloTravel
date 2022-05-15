package com.example.balotravel;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.balotravel.Model.Post;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CreateNewJouneyActivity extends AppCompatActivity implements PlaceDetailBottomSheet.BottomSheetListener{

    protected RecyclerView recyclerView;
    protected RecyclerView.Adapter adapter;
    protected FirebaseAuth mAuth = FirebaseAuth.getInstance();
    protected EditText editText;
    protected ArrayList <com.example.balotravel.Model.Place> placeList = new ArrayList<com.example.balotravel.Model.Place>();
    protected EditText edtJourneyDescription;
    protected DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://balotravel-9a424-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("posts");
    protected Button seeOnMapBtn;
    protected Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_jouney);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            //Places.initialize(getApplicationContext(), "AIzaSyASo146Eo6JzONrNaJ0gZINEvwibfJ-Xqo");
            Places.initialize(getApplicationContext(), "AIzaSyBhj_LHnpq3EGbr5fGHpj0ORcDx7Rb-B_E");
        }
        recyclerView = (RecyclerView) findViewById(R.id.activePlacesView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        edtJourneyDescription = (EditText) findViewById(R.id.journeyDescription);

        saveBtn = (Button) findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = mDatabase.push().getKey();
                mDatabase.child(key).setValue(new Post("Chuyến đi của tôi", "", "", edtJourneyDescription.getText().toString() ));
                mDatabase.child(key).child("places").setValue(placeList);
                Toast.makeText(CreateNewJouneyActivity.this, "Tạo chuyến đi thành công", Toast.LENGTH_LONG).show();
            }
        });

        seeOnMapBtn = (Button) findViewById(R.id.seeOnMapBtn);
        seeOnMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateNewJouneyActivity.this, PlaceListOnMapActivity.class);
                intent.putExtra("BUNDLE", placeList);
                startActivity(intent);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK ) {
            Place place = Autocomplete.getPlaceFromIntent(data);

            PlaceDetailBottomSheet placeDetailBottomSheet = new PlaceDetailBottomSheet(place);
            placeDetailBottomSheet.show(getSupportFragmentManager(), "placeDetailBottomSheet");

        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onButtonClicked(com.google.android.libraries.places.api.model.Place place, ArrayList <Uri> imageList) {
        placeList.add( new com.example.balotravel.Model.Place(place.getId(), place.getName(), place.getAddress(), place.getLatLng(), imageList));
        adapter = new PlaceListViewAdapter(placeList, this);
        recyclerView.setAdapter(adapter);
        Log.i("Place List:", place.getLatLng().toString());
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP
            | ItemTouchHelper.DOWN ,  ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            Collections.swap(placeList, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            placeList.remove(viewHolder.getAdapterPosition());
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    };
}

