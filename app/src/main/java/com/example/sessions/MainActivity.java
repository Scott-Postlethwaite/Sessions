package com.example.sessions;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fragment.MapFragment;
import fragment.NotificationFragment;
import fragment.PrivateFragment;
import fragment.tbcFragment;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.search2)
    ImageView search2;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.framelayout)
    FrameLayout framelayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigation;

    boolean mLocationPermissionGranted = false;
    int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private Location lastKnownLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;

    MapFragment mapFragment;
    tbcFragment tbcfragment;
    NotificationFragment notificationFragment;
    PrivateFragment privateFragment;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getLocationPermission();
        bottomNavigation.inflateMenu(R.menu.bottom_navigation_main);
        bottomNavigation.setItemBackgroundResource(R.color.colorPrimary);
        bottomNavigation.setItemTextColor(ContextCompat.getColorStateList(bottomNavigation.getContext(), R.color.nav_item_colors));
        bottomNavigation.setItemIconTintList(ContextCompat.getColorStateList(bottomNavigation.getContext(), R.color.nav_item_colors));

        mapFragment = new MapFragment();
        tbcfragment = new tbcFragment();
        notificationFragment = new NotificationFragment();
        privateFragment = new PrivateFragment();

        setFragment(tbcfragment);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.newsfeed_fragment:
                        setFragment(tbcfragment);
                        break;

                    case R.id.profile_friends:
                        setFragment(mapFragment);
                        break;


                    case R.id.profile_fragment:
                        setFragment(privateFragment);
                        break;

                    case R.id.profile_notification:
                        setFragment(notificationFragment);

                }


                return true;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UploadActivity.class));
            }
        });

    }

    public void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.commit();
    }


    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    @OnClick(R.id.search2)
    public void onViewClicked() {
        startActivity(new Intent(MainActivity.this, SearchActivity.class));

    }
}
