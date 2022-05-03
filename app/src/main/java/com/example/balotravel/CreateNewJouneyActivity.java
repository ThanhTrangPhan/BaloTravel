package com.example.balotravel;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class CreateNewJouneyActivity extends AppCompatActivity {

    protected EditText editText;

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
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).setInitialQuery(editText.getText().toString()).setCountry("VN").setHint("Hồ Hoàn Kiếm,...").build(CreateNewJouneyActivity.this);
                startActivityForResult(intent, 100);
            }
        });
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyBto2FuliFbIADOWHcH3Sf5LcR85_Mjbns");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK ) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            editText.setText(place.getName());
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerPopupDetailView, new PopupDetailDialogFragment()).commit();
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}