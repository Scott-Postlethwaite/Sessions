package com.example.sessions;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.example.sessions.rest.ApiClient;
import com.example.sessions.rest.services.UserInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends AppCompatActivity {
    String imageUploadUrl = "";
    boolean isImageSelected = false;
    ProgressDialog progressDialog;
    File compressedImageFile = null;
    @BindView(R.id.postBtnTxt)
    TextView postBtnTxt;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.streetRating)
    RatingBar streetRating;
    @BindView(R.id.parkRating)
    RatingBar parkRating;
    @BindView(R.id.overallRating)
    RatingBar overallRating;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.textView7)
    TextView textView7;
    @BindView(R.id.textView9)
    TextView textView9;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.relativeLayout)
    ConstraintLayout relativeLayout;
    @BindView(R.id.add_image)
    Button addImage;

    String sessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ButterKnife.bind(this);

        sessionId = getIntent().getStringExtra("SPOT_ID");

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back_white);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Uploading...");


        addImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ImagePicker.create(ReviewActivity.this)
                        .folderMode(true)
                        .single()
                        .start();
            }
        });
        postBtnTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPost();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image selectedImage = ImagePicker.getFirstImageOrNull(data);
            try {
                compressedImageFile = new Compressor(this)
                        .setQuality(75)
                        .compressToFile(new File(selectedImage.getPath()));

                isImageSelected = true;
                Picasso.get().load(new File(selectedImage.getPath())).placeholder(R.drawable.default_image_placeholder).into(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void uploadPost() {
        float street= streetRating.getRating();
        float park = parkRating.getRating();
        float overall= overallRating.getRating();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            progressDialog.show();

            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);

            builder.addFormDataPart("spotId",sessionId);
            builder.addFormDataPart("street", String.valueOf(street));
            builder.addFormDataPart("park", String.valueOf(park));
            builder.addFormDataPart("overall", String.valueOf(overall));
            builder.addFormDataPart("userId", userId);

            if (isImageSelected) {
                builder.addFormDataPart("isImageSelected", "1");
                builder.addFormDataPart("file", compressedImageFile.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), compressedImageFile));
            } else {
                builder.addFormDataPart("isImageSelected", "0");
            }

            MultipartBody multipartBody = builder.build();

            UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);
            Call<Integer> call = userInterface.uploadReview(multipartBody);
            call.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    progressDialog.dismiss();
                    if (response.body() != null && response.body() == 1) {
                        Toast.makeText(ReviewActivity.this, "Post is Successfull", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ReviewActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ReviewActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();

                    }


                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {

                    Toast.makeText(ReviewActivity.this, "Something went wrong but like really wrong !", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });



        }


    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromTouch) {
        final int numStars = ratingBar.getNumStars();

        }

    }



