package com.example.balotravel.Model;

import com.google.android.gms.maps.model.LatLng;

public class Place {
    private String id;
    private String name;
    private String address;
    private LatLng latLng;
    private String startAt;
    private String finishAt;

    public Place(String id, String name, String address, LatLng latLng) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latLng = latLng;
    }

    public Place(String id, String name, String address, LatLng latLng, String startAt, String finishAt) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latLng = latLng;
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
}
