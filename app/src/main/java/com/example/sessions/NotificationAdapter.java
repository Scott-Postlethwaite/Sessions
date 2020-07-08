package com.example.sessions;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sessions.model.ReportModel;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    Context context;
    List<ReportModel> reports;

    public NotificationAdapter(Context context, List<ReportModel> reports) {
        this.context = context;
        this.reports = reports;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_list, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final ReportModel report = reports.get(position);
        holder.userName.setText(report.getReason());
        if(!report.getReason().isEmpty()) {
            Picasso.get().load(report.getReason()).placeholder(R.drawable.icon_notification)
                    .networkPolicy(NetworkPolicy.OFFLINE).into(holder.userImage, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onError(Exception e) {
                    Picasso.get().load(report.getReason()).placeholder(R.drawable.icon_notification)
                            .into(holder.userImage);
                }
            });
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(v.getWindowToken(),0);
                String spotid = String.valueOf(report.getSpotId());
                context.startActivity(new Intent(context, SpotActivity.class).putExtra("SPOT_ID",spotid));
            }
        });

    }

    @Override
    public int getItemCount() {
        if (reports!=null)
        {
        return reports.size();}
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.user_image)
        ImageView userImage;
        @BindView(R.id.user_name)
        TextView userName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}