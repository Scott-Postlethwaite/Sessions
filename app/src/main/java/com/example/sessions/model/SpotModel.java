package com.example.sessions.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpotModel {
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
        @SerializedName("spotUserId")
        @Expose
        private String spotUserId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("spotLat")
        @Expose
        private double spotLat;
        @SerializedName("spotLong")
        @Expose
        private double spotLong;
        @SerializedName("requirements")
        @Expose
        private String requirements;
        @SerializedName("features")
        @Expose
        private String features;
        @SerializedName("user")
        @Expose
        private String user;
        @SerializedName("coord")
        @Expose
        private LatLng coord;


        public int getSpotId() {
            return spotId;
        }

        public void setSpotId(int spotId) {
            this.spotId = spotId;
        }

        public String getSpotUserId() {
            return spotUserId;
        }


        public void setSpotUserId(String spotUserId) {
            this.spotUserId = spotUserId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRequirements() {
            return requirements;
        }

        public void setRequirements(String requirements) {
            this.requirements = requirements;
        }

        public String getFeatures() {
            return features;
        }

        public void setFeatures(String features) {
            this.features = features;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }


        public double getSpotLat() {
            return spotLat;
        }

        public void setSpotLat(int spotLat) {
            this.spotLat = spotLat;
        }

        public double getSpotLong() {
            return spotLong;
        }

        public void setSpotLong(int spotLong) {
            this.spotLong = spotLong;
        }

        public LatLng getCoord() {
            coord = new LatLng(spotLat, spotLong);
            return coord;
        }

        public void setCoord(LatLng coord) {
            this.coord = coord;
        }
    }

//}
