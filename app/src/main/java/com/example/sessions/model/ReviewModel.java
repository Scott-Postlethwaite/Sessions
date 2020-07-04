package com.example.sessions.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewModel {
/*
    @SerializedName("friends")
    @Expose
    private List<Spot> spots = null;

    public List<Spot> getSpots() {
        return spots;
    }

    public void setFriends(List<Spot> spots) {
        this.spots = spots;
    }

*/

    //  public class Spot {

    @SerializedName("spotID")
    @Expose
    private int spotId;
    @SerializedName("userId")
    @Expose
    private String userId;

    @SerializedName("street")
    @Expose
    private float street;
    @SerializedName("park")
    @Expose
    private float park;
    @SerializedName("overall")
    @Expose
    private float overall;


    public int getSpotId() {
        return spotId;
    }

    public void setSpotId(int spotId) {
        this.spotId = spotId;
    }

    public String getUserId() {
        return userId;
    }

    public void setSpotUserId(String userId) {
        this.userId = userId;
    }

    public float getStreet() {
        return street;
    }

    public void setStreet(float street) {
        this.street = street;
    }

    public float getPark() {
        return park;
    }

    public void setPark(float park) {
        this.park = park;
    }

    public float getOverall() {
        return overall;
    }

    public void setOverall(float overall) {
        this.overall = overall;
    }

}
