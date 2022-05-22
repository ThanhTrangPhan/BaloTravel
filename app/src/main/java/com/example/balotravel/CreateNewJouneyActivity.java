package com.example.balotravel;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.balotravel.Model.Post;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.example.balotravel.Model.User;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import java.util.List;


public class CreateNewJouneyActivity extends AppCompatActivity implements PlaceDetailBottomSheet.BottomSheetListener{

    protected RecyclerView recyclerView;
    protected RecyclerView.Adapter adapter;
    protected FirebaseAuth mAuth = FirebaseAuth.getInstance();
    protected EditText editText;
    protected ArrayList <com.example.balotravel.Model.Place> placeList = new ArrayList<>();
    protected EditText edtJourneyDescription;

    protected DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://balotravel-9a424-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("posts");
    protected DatabaseReference mUsers = FirebaseDatabase.getInstance("https://balotravel-9a424-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users");
    protected DatabaseReference firebaseDB = FirebaseDatabase.getInstance("https://balotravel-9a424-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
    protected StorageReference mStorage;
    private User currentUser;
    protected Button seeOnMapBtn;
    protected Button saveBtn;
    protected Button changeCoverImgBtn;
    protected ImageView coverImage;
    protected Uri coverImageUri;
    protected String coverImageLink = null;
    protected Button smartSortingBtn;

    protected double currentLatitude = -1;
    protected double currentLongitude = -1;

    private LocationRequest locationRequest;

    protected ArrayList <com.example.balotravel.Model.Place>  tmpList;
    protected float distance[][] = new float [100][100];
    protected int route[] = new int [100];
    protected int routeMin[] = new int [100];
    protected float routeDistance;
    protected float routeDistanceMin;

    protected boolean flag[] = new boolean[100];

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_jouney);
        currentUser = new User();
        currentUser.setUserId(mAuth.getCurrentUser().getUid());
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
        coverImage = (ImageView) findViewById(R.id.coverImage);
        saveBtn = (Button) findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = mDatabase.push().getKey();
                Log.d("post Id",key);

                mDatabase.child(key).setValue(new Post("Chuyến đi của tôi", currentUser.getUserId(), "", edtJourneyDescription.getText().toString() ));
                firebaseDB.child("likes").child(key).setValue(true);
                firebaseDB.child("saves").child(key).setValue(true);

                for (int i=0; i<= placeList.size()-1; i++) {
                    mDatabase.child(key).child("places").child(String.valueOf(i)).child("address").setValue(placeList.get(i).getAddress());
                    mDatabase.child(key).child("places").child(String.valueOf(i)).child("name").setValue(placeList.get(i).getName());
                    mDatabase.child(key).child("places").child(String.valueOf(i)).child("latitude").setValue(placeList.get(i).getLatitude());
                    mDatabase.child(key).child("places").child(String.valueOf(i)).child("longitute").setValue(placeList.get(i).getLongitude());
                    mDatabase.child(key).child("places").child(String.valueOf(i)).child("id").setValue(placeList.get(i).getId());

                    if (coverImageUri != null) {
                        ContentResolver cR2 = getContentResolver();
                        MimeTypeMap mime2 = MimeTypeMap.getSingleton();
                        String imageExtension2 = mime2.getExtensionFromMimeType(cR2.getType(coverImageUri));
                        StorageReference fileReference2 = mStorage.child(key).child("postImage").child(System.currentTimeMillis() + "." + imageExtension2);
                        fileReference2.putFile(coverImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                fileReference2.getDownloadUrl().addOnSuccessListener( new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        final Uri downloadUrl2 = uri;
                                        coverImageLink = downloadUrl2.toString();
                                        mDatabase.child(key).child("postImage").setValue(coverImageLink);
                                    }
                                });
                            }
                        });
                    }



                    for (int j=0; j<= placeList.get(i).getImageList().size()-1; j++) {
                        int numberI = i;
                        int numberJ = j;
                        String image = placeList.get(i).getImageList().get(j);
                        ContentResolver cR = getContentResolver();
                        MimeTypeMap mime = MimeTypeMap.getSingleton();
                        String imageExtension = mime.getExtensionFromMimeType(cR.getType(Uri.parse(image)));

                        StorageReference fileReference = mStorage.child(key).child("places").child(String.valueOf(j)).child("imageList").child(System.currentTimeMillis() + String.valueOf(j) + "." + imageExtension);
                        fileReference.putFile(Uri.parse(image)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                fileReference.getDownloadUrl().addOnSuccessListener( new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        final Uri downloadUrl = uri;
                                        String imageLink = downloadUrl.toString();
                                        Log.d("imaage upload", imageLink);
                                        mDatabase.child(key).child("places").child(String.valueOf(numberI)).child("imageList").child(String.valueOf(numberJ)).setValue(imageLink);
                                    }
                                });
                            }
                        });
                    }
                }
                Toast.makeText(CreateNewJouneyActivity.this, "Tạo chuyến đi thành công", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(CreateNewJouneyActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        seeOnMapBtn = (Button) findViewById(R.id.seeOnMapBtn);
        seeOnMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (placeList.size() > 0) {
                    Intent intent = new Intent(CreateNewJouneyActivity.this, PlaceListOnMapActivity.class);
                    intent.putExtra("BUNDLE", placeList);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Lỗi: Không có địa điểm nào", Toast.LENGTH_SHORT).show();
                }

            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        changeCoverImgBtn = (Button) findViewById(R.id.changeCoverImgBtn);
        changeCoverImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galaryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galaryIntent, 1);
            }
        });

        smartSortingBtn = (Button) findViewById(R.id.smartSort);

        smartSortingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                smartSorting();
            }
        });
    }

    protected void smartSorting() {
        tmpList = new ArrayList<>(placeList);
        getLocation();
        if (currentLatitude == -1 && currentLongitude == -1) {
            Toast.makeText(getApplicationContext(), "Lỗi: Không thể lấy vị trí thiết bị", Toast.LENGTH_SHORT ).show();
        } else {

            Location crntLocation=new Location("crntlocation");
            crntLocation.setLatitude(currentLatitude);
            crntLocation.setLongitude(currentLongitude);
            tmpList.add(0, new com.example.balotravel.Model.Place("current","Current", "", new LatLng(currentLatitude, currentLongitude), "", ""));

            // initial distance array
            for (int i = 0; i<= tmpList.size()-1; i++) {
                flag[i] = true;
                Location locationA = new Location("locationa");
                locationA.setLatitude(tmpList.get(i).getLatitude());
                locationA.setLongitude(tmpList.get(i).getLongitude());

                for (int j=i; j<tmpList.size(); j++) {
                    if (i == j) {
                        distance[i][j] = 0;
                    } else {
                        Location locationB = new Location("locationb");
                        locationB.setLatitude(tmpList.get(j).getLatitude());
                        locationB.setLongitude(tmpList.get(j).getLongitude());

                        distance[i][j] = locationA.distanceTo(locationB);
                        distance[j][i] = distance[i][j];
                        Log.d(" Distance: " + i + " " + j, String.valueOf(distance[i][j]));
                    }
                }
            }

            route[0] = 0;
            routeDistance = 0;
            routeDistanceMin = Float.MAX_VALUE;
            flag[0] = false;
            Try(1);
            String ttt = "Thứ tự: ";
            tmpList.clear();
            for (int i=1; i<=placeList.size(); i++) {
               tmpList.add(placeList.get(routeMin[i]-1));
            }

            placeList = new ArrayList<>(tmpList);
            Toast.makeText(getApplicationContext(), ttt + placeList.get(0).getName() , Toast.LENGTH_SHORT).show();
            adapter = new PlaceListViewAdapter(placeList, this);
            recyclerView.setAdapter(adapter);
        }
    }

    private void Try(int i) {
        for (int j=1; j<=tmpList.size()-1; j++) {
            if (flag[j] && distance[route[i-1]][j] > 0) {
                route[i] = j;

                if (i == tmpList.size() - 1) {
                    routeDistance = 0;
                    Log.d("Route: ", String.valueOf(route[0]) + String.valueOf(route[1]) + String.valueOf(route[2]) + String.valueOf(route[3]) + String.valueOf(route[4]));
                    for (int k = 1; k <= tmpList.size()-1; k++) {
                        routeDistance += distance[route[k-1]][route[k]];
                    };
                    Log.d("Route Distance: ", String.valueOf(routeDistance));
                    if (routeDistance < routeDistanceMin) {

                        routeDistanceMin = routeDistance;
                        Log.d("Route Distance Min: ", String.valueOf(routeDistanceMin));
                        for (int k = 1; k <= tmpList.size() - 1; k++) {
                            routeMin[k] = route[k];
                        }
                    }
                } else {
                    flag[j] = false;
                    Try(i + 1);
                    flag[j] = true;
                }
            }
        }
    }

    private void getLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(CreateNewJouneyActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    // Khởi tạo location
                    Location location = task.getResult();
                    if (location != null) {
                        currentLatitude = location.getLatitude();
                        currentLongitude = location.getLongitude();
                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions(CreateNewJouneyActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

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

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            this.coverImageUri = data.getData();
            coverImage.setImageURI(this.coverImageUri);
        }
    }

    @Override
    public void onButtonClicked(com.google.android.libraries.places.api.model.Place place, ArrayList <String> imageList) {
        placeList.add( new com.example.balotravel.Model.Place(place.getId(), place.getName(), place.getAddress(), place.getLatLng(), imageList));
        adapter = new PlaceListViewAdapter(placeList, this);
        recyclerView.setAdapter(adapter);
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
