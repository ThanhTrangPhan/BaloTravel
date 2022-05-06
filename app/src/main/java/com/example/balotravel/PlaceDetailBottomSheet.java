package com.example.balotravel;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class PlaceDetailBottomSheet extends BottomSheetDialogFragment {
    private Button addToPlaceListBtn;
    private Place place;

    private TextView placeNameTxt;
    private TextView placeAddressTxt;

    private BottomSheetListener mListener;

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

        placeNameTxt.setText(this.place.getName());
        placeAddressTxt.setText(this.place.getAddress());

        addToPlaceListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onButtonClicked(place);
                dismiss();
            }
        });
        return v;
    }

    public interface BottomSheetListener {
        void onButtonClicked (com.google.android.libraries.places.api.model.Place place);
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
}
