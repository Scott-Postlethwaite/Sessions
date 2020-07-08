package com.example.sessions;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.sessions.model.SpotModel;
import com.example.sessions.rest.ApiClient;
import com.example.sessions.rest.services.UserInterface;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ReportActivity extends AppCompatActivity {

    ProgressDialog progressDialog;


    String sessionId;
    @BindView(R.id.postBtnTxt)
    TextView postBtnTxt;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.image)
    ImageView image;
    SpotModel spot = new SpotModel();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);
        sessionId = getIntent().getStringExtra("SPOT_ID");
        showSpot(Integer.parseInt(sessionId));
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


        postBtnTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPost();
            }
        });

    }



    private void uploadPost() {


        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String reason = String.valueOf(name.getText());

        progressDialog.show();

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        builder.addFormDataPart("reporter", userId);
        builder.addFormDataPart("reportedUser", spot.getSpotUserId());
        builder.addFormDataPart("spotId", sessionId);
        builder.addFormDataPart("reason", reason);




        MultipartBody multipartBody = builder.build();

        UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);
        Call<Integer> call = userInterface.uploadReport(multipartBody);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                progressDialog.dismiss();
                if (response.body() != null && response.body() == 1) {
                    Toast.makeText(ReportActivity.this, "Post is Successfull", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ReportActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ReportActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

                Toast.makeText(ReportActivity.this, "Something went wrong but like really wrong !", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });


    }


    private void showSpot(final int markerId) {
        UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", String.valueOf(markerId));
        Call<SpotModel> call = userInterface.getSpot(params);
        call.enqueue(new Callback<SpotModel>() {
            @Override
            public void onResponse(Call<SpotModel> call, Response<SpotModel> response) {
                if (response.body() != null) {
                    spot = response.body();
                } else {
                    Log.d(TAG, "onResponse: Ryan Lambert Likes Toessssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss    i.e response.body is null");

                }
            }

            @Override
            public void onFailure(Call<SpotModel> call, Throwable t) {
                Log.d(TAG, "onResponse: Ooooooooooooooooooooooooooooooooooooooooooooooooooon failure response");
            }
        });
    }

}



