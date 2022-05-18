package com.example.balotravel;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.balotravel.Model.Place;

import java.util.ArrayList;
import java.util.List;

public class PlaceListViewAdapter extends RecyclerView.Adapter<PlaceListViewAdapter.ViewHolder> {

    private ArrayList<Place> placeList;
    private Context context;

    public PlaceListViewAdapter(ArrayList<Place> placeList, Context context) {
        this.placeList = placeList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Place place = placeList.get(position);

        holder.placeName.setText(place.getName());
        holder.placeAddress.setText(place.getAddress());

        for (int i=0; i<place.getImageList().size(); i++) {
            Uri selectedImage = place.getImageList().get(i);
            ImageView imageToUpload = new ImageView(this.context);
            imageToUpload.setImageURI(selectedImage);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(240,240);
            params.setMargins(0, 0, 10, 0);
            imageToUpload.setLayoutParams(params);
            holder.uploadedPictureLayout.addView(imageToUpload);
        }





        // setting the margin in linearlayout


        // adding the image in layout

    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView placeName;
        public TextView placeAddress;
        public LinearLayout uploadedPictureLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placeName = (TextView) itemView.findViewById(R.id.placeName);
            placeAddress = (TextView) itemView.findViewById(R.id.placeAddress);
            uploadedPictureLayout = (LinearLayout) itemView.findViewById(R.id.uploadedPictureLayout);
        }
    }
}