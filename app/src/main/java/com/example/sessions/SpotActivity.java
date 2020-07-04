package com.example.sessions;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.sessions.model.ImageModel;
import com.example.sessions.model.ReviewModel;
import com.example.sessions.model.SpotModel;
import com.example.sessions.rest.ApiClient;
import com.example.sessions.rest.services.UserInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class SpotActivity extends AppCompatActivity {

    SpotModel spot = new SpotModel();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.spotName)
    TextView spotName;
    @BindView(R.id.requirements)
    TextView requirements;
    @BindView(R.id.features)
    TextView features;
    @BindView(R.id.report)
    Button report;
    @BindView(R.id.go)
    Button go;
    @BindView(R.id.review)
    Button review;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.textView6)
    TextView textView6;
    @BindView(R.id.parkRating)
    RatingBar parkRating;
    @BindView(R.id.textView12)
    TextView textView12;
    @BindView(R.id.textView11)
    TextView textView11;
    @BindView(R.id.textView10)
    TextView textView10;
    @BindView(R.id.streetRating)
    RatingBar streetRating;
    @BindView(R.id.overallRating)
    RatingBar overallRating;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    List<ImageModel> imageModels;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot);
        ButterKnife.bind(this);

        String sessionId = getIntent().getStringExtra("SPOT_ID");
        final int spotId = Integer.parseInt(sessionId);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back_white);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        SpotModel inSpot;
        showSpot(spotId);
        getPics(spotId);

        Log.d(TAG, "onResponse: Ryan Lambert Likes Toessssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss    i.e response.body is null");

        context = this;



        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + String.valueOf(spot.getSpotLat()) + "," + String.valueOf(spot.getSpotLong()));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(SpotActivity.this, ReviewActivity.class);
                intent.putExtra("SPOT_ID", String.valueOf(spotId));
                startActivity(intent);
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
                    spotName.setText(spot.getName());
                    requirements.setText(spot.getRequirements());
                    features.setText(spot.getFeatures());
                    showReviews(markerId);
                    getPics(markerId);

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

    private void getPics(final int markerId) {


        imageModels = new ArrayList<>();
        UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", String.valueOf(markerId));
        Call<List<ImageModel>> call = userInterface.getAllPics(params);
        call.enqueue(new Callback<List<ImageModel>>() {
            @Override
            public void onResponse(Call<List<ImageModel>> call, Response<List<ImageModel>> response) {
                if (response.body() != null) {
                    imageModels.addAll(response.body());
                    ViewPagerAdapter adapter = new ViewPagerAdapter(context,imageModels);
                    viewpager.setAdapter(adapter);
                    Log.d(TAG, "onResponse: it passesssssssssssssssssssssssssssssssssssssssssss");

                } else {
                    Log.d(TAG, "onResponse: Ryan Lambert Likes Toessssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");

                }
            }

            @Override
            public void onFailure(Call<List<ImageModel>> call, Throwable t) {
                Log.d(TAG, "onResponse: Ummmmmmmmmmmmmmmmmmm what the cunt");

            }

        });

    }

    private void showReviews(final int markerId) {
        final List<ReviewModel> reviews = new ArrayList<>();
        UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", String.valueOf(markerId));
        Call<List<ReviewModel>> call = userInterface.getAllReviews(params);
        call.enqueue(new Callback<List<ReviewModel>>() {
            @Override
            public void onResponse(Call<List<ReviewModel>> call, Response<List<ReviewModel>> response) {
                if (response.body() != null) {
                    Log.d(TAG, "onResponse: waaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaattttttttttttttttttttttttttttttttttttttttttttttttttttttttttt");
                    reviews.addAll(response.body());

                    float streetTotal = 0;
                    float parkTotal = 0;
                    float overallTotal = 0;
                    for (int i = 0; i < reviews.size(); i++) {
                        streetTotal += reviews.get(i).getStreet();
                        parkTotal += reviews.get(i).getPark();
                        overallTotal += reviews.get(i).getOverall();
                    }

                    streetRating.setRating(streetTotal / reviews.size());
                    parkRating.setRating(parkTotal / reviews.size());
                    overallRating.setRating(overallTotal / reviews.size());


                } else {
                    Log.d(TAG, "onResponse: Ryan Lambert Likes Toessssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");

                }
            }

            @Override
            public void onFailure(Call<List<ReviewModel>> call, Throwable t) {
                Log.d(TAG, "onResponse: Ummmmmmmmmmmmmmmmmmm what the cunt");
            }
        });
    }

/*
    public class ImageAdapter extends PagerAdapter {
        private Context context;
        private int[] GalImages = new int[] {
                R.drawable.cap8, R.drawable.cap2, R.drawable.cap3, R.drawable.cap1,R.drawable.cap5,
                R.drawable.cap6, R.drawable.cap7, R.drawable.cap9,R.drawable.cap4,
                R.drawable.cap10

        };
        ImageAdapter(Context context)
        {
            this.context=context;
        }

        @Override
        public int getCount() {
            return GalImages.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);
            int padding = context.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setImageResource(GalImages[position]);
            container.addView(imageView, 0);
            return imageView;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ImageView) object);
        }
    }

*/

}