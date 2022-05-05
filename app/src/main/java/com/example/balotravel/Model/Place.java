package com.example.balotravel.Model;

public class Place {
    private String id;
    private String name;
    private String address;
    private String startAt;
    private String finishAt;

    public Place(String id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public Place(String id, String name, String address, String startAt, String finishAt) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.startAt = startAt;
        this.finishAt = finishAt;
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
