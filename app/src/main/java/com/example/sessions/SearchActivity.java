package com.example.sessions;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.search_recy)
    RecyclerView searchRecy;
    SearchAdapter searchAdapter;
    List<SpotModel> spots = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this, MainActivity.class));
            }
        });
        searchAdapter = new SearchAdapter(SearchActivity.this, spots);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this);
        searchRecy.setLayoutManager(layoutManager);
        searchRecy.setAdapter(searchAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_view,menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search1).getActionView();
        searchView.setIconified(false);
        searchView.setQueryHint("Search Spots ");
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchFromDb(query, true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query.length() > 1) {
                    searchFromDb(query, false);
                } else {
                    spots.clear();
                    searchAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });

        return true;
    }

    private void searchFromDb(String query, boolean b) {
        UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);
        Map<String, String> params = new HashMap<String, String>();
        params.put("keyword", query);

        Call<List<SpotModel>> call = userInterface.search(params);
        call.enqueue(new Callback<List<SpotModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<SpotModel>> call, @NonNull Response<List<SpotModel>> response) {
                spots.clear();
                spots.addAll(response.body());
                searchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<List<SpotModel>> call, @NonNull Throwable t) {

            }
        });
    }
}





