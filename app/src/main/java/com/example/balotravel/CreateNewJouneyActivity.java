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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
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
    protected StorageReference mStorage;

    protected Button seeOnMapBtn;
    protected Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_jouney);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mStorage = FirebaseStorage.getInstance().getReference("posts");
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


                for (int i=0; i<= placeList.size()-1; i++) {
                    mDatabase.child(key).child("places").child(String.valueOf(i)).child("address").setValue(placeList.get(i).getAddress());
                    mDatabase.child(key).child("places").child(String.valueOf(i)).child("name").setValue(placeList.get(i).getName());
                    mDatabase.child(key).child("places").child(String.valueOf(i)).child("latitude").setValue(placeList.get(i).getLatitude());
                    mDatabase.child(key).child("places").child(String.valueOf(i)).child("longitute").setValue(placeList.get(i).getLongitude());
                    mDatabase.child(key).child("places").child(String.valueOf(i)).child("id").setValue(placeList.get(i).getId());

                    for (int j=0; j<= placeList.get(i).getImageList().size()-1; j++) {
                        int numberI = i;
                        int numberJ = j;
                        Uri image = placeList.get(i).getImageList().get(j);
                        ContentResolver cR = getContentResolver();
                        MimeTypeMap mime = MimeTypeMap.getSingleton();
                        String imageExtension = mime.getExtensionFromMimeType(cR.getType(image));

                        StorageReference fileReference = mStorage.child(key).child("places").child(String.valueOf(j)).child("imageList").child(System.currentTimeMillis() + String.valueOf(j) + "." + imageExtension);
                        fileReference.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                fileReference.getDownloadUrl().addOnSuccessListener( new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        final Uri downloadUrl = uri;
                                        String imageLink = downloadUrl.toString();
                                        mDatabase.child(key).child("places").child(String.valueOf(numberI)).child("imageList").child(String.valueOf(numberJ)).setValue(imageLink);
                                    }
                                });
                            }
                        });
                    }
                }
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

