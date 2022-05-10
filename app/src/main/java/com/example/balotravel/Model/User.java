package com.example.balotravel.Model;

import java.io.Serializable;

public class User implements Serializable {
    private String username,fullname,bio;
    private int phonenumber;
    private String image_profile;
    private String userId;

    public User() {
    }

    public User(String username, int phonenumber, String fullname, String bio, String image_profile, String userId) {
        this.username = username;
        this.fullname = fullname;
        this.bio = bio;
        this.image_profile = image_profile;
        this.userId = userId;
        this.phonenumber = phonenumber;
    }

    public int getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(int phonenumber) {
        this.phonenumber = phonenumber;
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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
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
