package com.example.balotravel;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class PlaceDetailBottomSheet extends BottomSheetDialogFragment{
    private Button addToPlaceListBtn;
    private Place place;
    private ImageView imageToUpload;
    private ImageButton uploadBtn;
    private TextView placeNameTxt;
    private TextView placeAddressTxt;
    private LinearLayout uploadedPictureLayout;
    private BottomSheetListener mListener;
    private ArrayList<Uri> imageList = new ArrayList<>();

    public PlaceDetailBottomSheet(com.google.android.libraries.places.api.model.Place place) {
        this.place = place;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.place_detail_bottom_sheet, container, false);
        addToPlaceListBtn = (Button) v.findViewById(R.id.addToPlaceListBtn);
        placeNameTxt = (TextView) v.findViewById(R.id.placeName);
        placeAddressTxt = (TextView) v.findViewById(R.id.placeAddress);
        imageToUpload = (ImageView) v.findViewById(R.id.imageToUploadView);
        uploadBtn = (ImageButton) v.findViewById(R.id.uploadButton);
        placeNameTxt.setText(this.place.getName());
        placeAddressTxt.setText(this.place.getAddress());
        uploadedPictureLayout = (LinearLayout) v.findViewById(R.id.uploadedPictureLayout);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galaryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galaryIntent, 1);
            }
        });
        
        addToPlaceListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onButtonClicked(place, imageList);
                dismiss();
            }
        });
        return v;

    }

    public interface BottomSheetListener {
        void onButtonClicked (com.google.android.libraries.places.api.model.Place place, ArrayList<Uri> imageList);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try{
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " có lỗi");
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            imageList.add(selectedImage);
            ImageView imageToUpload = new ImageView(getActivity());
            imageToUpload.setImageURI(selectedImage);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(260,260);

            // setting the margin in linearlayout
            params.setMargins(0, 0, 10, 0);
            imageToUpload.setLayoutParams(params);

            // adding the image in layout
            uploadedPictureLayout.addView(imageToUpload);
        }
    }
}
