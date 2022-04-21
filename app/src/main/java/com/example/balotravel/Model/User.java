package com.example.balotravel.Model;

public class User {
    private String username;
    private String image_profile;
    private String userId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public User(String username, String image_profile, String userId) {
        this.username = username;
        this.image_profile = image_profile;
        this.userId = userId;
    }
}
