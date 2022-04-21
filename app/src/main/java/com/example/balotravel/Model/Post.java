package com.example.balotravel.Model;

public class Post {
    private int postId;
    private String postPublishher;
    private String postImage;
    private String description;

    public Post(int postId, String postPublishher, String postImage, String description) {
        this.postId = postId;
        this.postPublishher = postPublishher;
        this.postImage = postImage;
        this.description = description;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getPostPublishher() {
        return postPublishher;
    }

    public void setPostPublishher(String postPublishher) {
        this.postPublishher = postPublishher;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) { this.description = description; }
}
