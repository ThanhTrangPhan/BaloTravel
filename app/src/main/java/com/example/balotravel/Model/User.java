package com.example.balotravel.Model;

public class User {
    private String username;
    private String fullname;
    private String image_profile;
    private String userId;

    public User(String username, String fullname, String image_profile, String userId) {
        this.username = username;
        this.fullname = fullname;
        this.image_profile = image_profile;
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getImage_profile() {
        return image_profile;
    }

    public void setImage_profile(String image_profile) {
        this.image_profile = image_profile;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
