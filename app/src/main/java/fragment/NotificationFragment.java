package fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sessions.NotificationAdapter;
import com.example.sessions.R;
import com.example.sessions.model.ReportModel;
import com.example.sessions.rest.ApiClient;
import com.example.sessions.rest.services.UserInterface;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends Fragment {


    List<ReportModel> reports=new ArrayList<>();
    NotificationAdapter notificationAdapter;
    Context context;
    @BindView(R.id.search_recy)
    RecyclerView searchRecy;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this, view);
        searchFromDb(FirebaseAuth.getInstance().getCurrentUser().getUid(), false);

        final FragmentActivity c = getActivity();
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());

        searchRecy.setLayoutManager(layoutManager);


        notificationAdapter = new NotificationAdapter(c, reports);
        searchRecy.setAdapter(notificationAdapter);
        searchRecy.setItemAnimator(new DefaultItemAnimator());


        return view;
    }


    private void searchFromDb(String query, boolean b) {
        UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", query);

        Call<List<ReportModel>> call = userInterface.myReports(params);
        call.enqueue(new Callback<List<ReportModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<ReportModel>> call, @NonNull Response<List<ReportModel>> response) {
                reports.clear();
                reports.addAll(response.body());
                notificationAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(@NonNull Call<List<ReportModel>> call, @NonNull Throwable t) {

            }
        });
    }

}