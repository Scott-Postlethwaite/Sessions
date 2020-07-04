package fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.sessions.R;
import com.example.sessions.SpotActivity;
import com.example.sessions.model.SpotModel;
import com.example.sessions.rest.ApiClient;
import com.example.sessions.rest.services.UserInterface;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static android.content.Context.LOCATION_SERVICE;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    Context context;
    ProgressDialog progressDialog;
    List<SpotModel> spotModels;
    View view;
    GoogleMap gmap;
    MapView mapView;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    int REQUEST_LOCATION = 1;
    private boolean mLocationPermissionGranted = false;
    private Location lastKnownLocation;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private final LatLng defaultLocation = new LatLng(55.933033, -3.213820);


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        /*
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if (ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            mLocationPermissionGranted=true;
            Log.e("DB", "PERMISSION GRANTED");
            getDeviceLocation();
        }

*/


        gmap = googleMap;

        LatLng uk = new LatLng(50.736129, -1.988229);

        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(uk, 6));

        Criteria criteria = new Criteria();
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng= new LatLng(latitude,longitude);
         gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,6));


        getSpots();
       // showSpotData(data);

        gmap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int markerId = (int)(marker.getTag());
                String test = String.valueOf(markerId);
                Intent intent = new Intent(getActivity(), SpotActivity.class);
                intent.putExtra("SPOT_ID", test);
                startActivity(intent);
                return false;
            }
        });

    }



    private void getSpots() {
        spotModels = new ArrayList<>();
        UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);
        Call<List<SpotModel>> call = userInterface.getAllSpots();
        call.enqueue(new Callback<List<SpotModel>>() {
            @Override
            public void onResponse(Call<List<SpotModel>> call, Response<List<SpotModel>> response) {
                if (response.body() != null) {
                    Log.d(TAG, "onResponse: waaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaattttttttttttttttttttttttttttttttttttttttttttttttttttttttttt");
                    spotModels.addAll(response.body());
                    showSpotData(spotModels);


                } else {
                    Log.d(TAG, "onResponse: Ryan Lambert Likes Toessssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");

                }
            }
            @Override
            public void onFailure(Call<List<SpotModel>> call, Throwable t) {
                Log.d(TAG, "onResponse: Ummmmmmmmmmmmmmmmmmm what the cunt");
            }
        });
    }

/*
    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener((Executor) this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the handset's location.
                            lastKnownLocation = task.getResult();
                            gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(lastKnownLocation.getLatitude(),
                                            lastKnownLocation.getLongitude()), 6));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            gmap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, 6));
                            gmap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


 */

    private void showSpotData(List<SpotModel> spots) {
        for(int i=0; i<spots.size();i++){
            SpotModel spot = spots.get(i);
            String name = spot.getName();
            LatLng coord = spot.getCoord();
            Marker marker = gmap.addMarker(new MarkerOptions()
                    .position(coord)
                    .title(name));
                    marker.setTag(spot.getSpotId());
        }

    }




}
