package com.example.sessions;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.sessions.model.ImageModel;
import com.example.sessions.rest.ApiClient;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.ContentValues.TAG;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private List<ImageModel> imageUrls;

    public ViewPagerAdapter(Context context, List<ImageModel> imageUrls){
        this.context=context;
        this.imageUrls =imageUrls;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
return view ==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        Picasso.get()
                .load(ApiClient.BASE_URL_1+imageUrls.get(position).getImage())
                .fit()
                .centerCrop()
                .into(imageView);
        container.addView(imageView);

        Log.d(TAG, imageUrls.get(position).getImage());


        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
container.removeView((View)object);    }

}
