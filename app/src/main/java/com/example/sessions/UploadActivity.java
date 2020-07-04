package com.example.sessions;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.example.sessions.rest.ApiClient;
import com.example.sessions.rest.services.UserInterface;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class UploadActivity extends AppCompatActivity {
    @BindView(R.id.postBtnTxt)
    TextView postBtnTxt;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.keywords)
    EditText keywords;
    @BindView(R.id.requirements)
    EditText requirements;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.add_image)
    Button addImage;

    String imageUploadUrl="";
    boolean isImageSelected = false;
    ProgressDialog progressDialog;
    File compressedImageFile = null;
    boolean mLocationPermissionGranted=false;
    int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=1;
   private Location lastKnownLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ButterKnife.bind(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getLocationPermission();
        getDeviceLocation();

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
                ImagePicker.create(UploadActivity.this)
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
        if(ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image selectedImage = ImagePicker.getFirstImageOrNull(data);
            try{
                compressedImageFile = new Compressor(this)
                        .setQuality(75)
                        .compressToFile(new File(selectedImage.getPath()));

                isImageSelected = true;
                Picasso.get().load(new File(selectedImage.getPath())).placeholder(R.drawable.default_image_placeholder).into(image);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the handset's location.
                            lastKnownLocation = task.getResult();

                            Log.e("Ryan","KEEEEEEEEEEEEEEEEEEEEEEEEETTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");


                        } else {
                            Log.e("Ryan","LambertLikesToesssssssssssssssssssssssssssssssssss");

                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void uploadPost() {
        String spName = name.getText().toString();
        String spRequitements = requirements.getText().toString();
        String spFeatures = keywords.getText().toString();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (spName.trim().length() > 0 || isImageSelected) {
            progressDialog.show();

            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);

            builder.addFormDataPart("name", spName);
            builder.addFormDataPart("spotLat", String.valueOf(lastKnownLocation.getLatitude()));
            builder.addFormDataPart("spotLong", String.valueOf(lastKnownLocation.getLongitude()));
            builder.addFormDataPart("requirements",spRequitements);
            builder.addFormDataPart("features",spFeatures);
            builder.addFormDataPart("spotUserId", userId);

            if (isImageSelected) {
                builder.addFormDataPart("isImageSelected", "1");
                builder.addFormDataPart("file", compressedImageFile.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), compressedImageFile));
            } else {
                builder.addFormDataPart("isImageSelected", "0");
            }

            MultipartBody multipartBody = builder.build();

            UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);
            Call<Integer> call = userInterface.uploadStatus(multipartBody);
            call.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    progressDialog.dismiss();
                    if(response.body()!=null && response.body()==1){
                        Toast.makeText(UploadActivity.this, "Post is Successfull", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UploadActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(UploadActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();

                    }


                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Toast.makeText(UploadActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });

        } else {
            Toast.makeText(UploadActivity.this, "Please write your post first", Toast.LENGTH_SHORT).show();
        }


    }


}
