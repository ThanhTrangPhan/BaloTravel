package com.example.balotravel.Model;

import java.util.ArrayList;
import java.util.Map;

public class Post {
    private String postId;
    private String postPublisher;
    private String postImage;
    private String nameCollection;
    private String description;
    private ArrayList <Place> placeList;

    public Post(String nameCollection, String postPublisher, String postImage, String description, ArrayList <Place> placeList) {
        this.nameCollection = nameCollection;
        this.postPublisher = postPublisher;
        this.postImage = postImage;
        this.description = description;
        this.placeList = placeList;
    }

    public Post(String nameCollection, String postPublisher, String postImage, String description) {
        this.nameCollection = nameCollection;
        this.postPublisher = postPublisher;
        this.postImage = postImage;
        this.description = description;

    }

    public Post() {
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
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

    public String getNameCollection() {
        return nameCollection;
    }

    public void setNameCollection(String nameCollection) {
        this.nameCollection = nameCollection;
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
