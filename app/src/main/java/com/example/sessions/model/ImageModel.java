package com.example.sessions.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageModel {

    @SerializedName("imageId")
    @Expose
    private int imageId;

    @SerializedName("spotId")
    @Expose
    private int spotId;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("image")
    @Expose
    private String image;


    public int getSpotId() {
        return spotId;
    }

    public void setSpotId(int spotId) {
        this.spotId = spotId;
    }


    public String getuserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String  getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
