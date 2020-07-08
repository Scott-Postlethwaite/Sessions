package com.example.sessions.rest.services;

import com.example.sessions.LoginActivity;
import com.example.sessions.model.ImageModel;
import com.example.sessions.model.ReportModel;
import com.example.sessions.model.ReviewModel;
import com.example.sessions.model.SpotModel;
import com.example.sessions.model.User;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface UserInterface {

    @POST("login")
    Call<Integer> singin(@Body LoginActivity.UserInfo userInfo);

    @GET("loadownprofile")
    Call<User> loadownProfile(@QueryMap Map<String, String> params);

    @POST("addspot")
    Call<Integer> uploadStatus(@Body MultipartBody requestBody);

    @POST("uploadImage")
    Call<Integer> uploadImage(@Body MultipartBody requestBody);

    @POST("postreview")
    Call<Integer> uploadReview(@Body MultipartBody requestBody);

    @POST("postreport")
    Call<Integer> uploadReport(@Body MultipartBody requestBody);

    @GET("allspots")
    Call<List<SpotModel>> getAllSpots();

    @GET("allpics")
    Call<List<ImageModel>> getAllPics(@QueryMap Map<String,String> params);

    @GET("allreviews")
    Call<List<ReviewModel>> getAllReviews(@QueryMap Map<String,String> params);

    @GET("getspot")
    Call<SpotModel> getSpot(@QueryMap Map<String,String> params);

    @GET("search")
    Call<List<SpotModel>> search(@QueryMap Map<String, String> params);

    @GET("myspots")
    Call<List<SpotModel>> mySpots(@QueryMap Map<String, String> params);

    @GET("myreports")
    Call<List<ReportModel>> myReports(@QueryMap Map<String, String> params);

}