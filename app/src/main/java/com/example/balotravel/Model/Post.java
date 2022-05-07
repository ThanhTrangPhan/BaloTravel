package com.example.balotravel.Model;

import java.util.ArrayList;
import java.util.Map;

public class Post {
    private int postId;
    private String postPublisher;
    private String postImage;
    private String name;
    private String description;
    private ArrayList <Place> placeList;

    public Post(String name, String postPublisher, String postImage, String description, ArrayList <Place> placeList) {
        this.name = name;
        this.postPublisher = postPublisher;
        this.postImage = postImage;
        this.description = description;
        this.placeList = placeList;
    }

    public Post(String name, String postPublisher, String postImage, String description) {
        this.name = name;
        this.postPublisher = postPublisher;
        this.postImage = postImage;
        this.description = description;

    }

    public Post() {
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getPostPublisher() {
        return postPublisher;
    }

    public void setPostPublisher(String postPublisher) {
        this.postPublisher = postPublisher;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Place> getPlaceList() {
        return placeList;
    }

    public void setPlaceList(ArrayList<Place> placeList) {
        this.placeList = placeList;
    }
}
