package com.example.balotravel.Model;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

public class Place implements Serializable {
    private String id;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
 //   private LatLng latLng;
    private String startAt;
    private String finishAt;
    private ArrayList <Uri> imageList = new ArrayList<>();

    public Place(String id, String name, String address, LatLng latLng, ArrayList <Uri> imageList) {
        this.id = id;
        this.name = name;
        this.address = address;
  //      this.latLng = latLng;
        this.latitude = latLng.latitude;
        this.longitude = latLng.longitude;
        this.imageList = imageList;
    }

    public Place(String id, String name, String address, LatLng latLng, String startAt, String finishAt) {
        this.id = id;
        this.name = name;
        this.address = address;
//        this.latLng = latLng;
        this.startAt = startAt;
        this.finishAt = finishAt;
    }

    public Place() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStartAt() {
        return startAt;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public String getFinishAt() {
        return finishAt;
    }

    public void setFinishAt(String finishAt) {
        this.finishAt = finishAt;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }


    public ArrayList<Uri> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<Uri> imageList) {
        this.imageList = imageList;
    }

    public void addNewImage(Uri newImg) {

    }
//    public LatLng getLatLng() {

//        return latLng;
//    }
//
//    public void setLatLng(LatLng latLng) {
//        this.latLng = latLng;
//    }
}
