package com.krushna.veginew.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.krushna.veginew.R;
import com.krushna.veginew.databinding.ItemNotificationBinding;
import com.krushna.veginew.models.NotificationRoot;
import com.krushna.veginew.retrofit.Const;

import java.util.ArrayList;
import java.util.List;

public class NotificatioaAdapter extends RecyclerView.Adapter<NotificatioaAdapter.NotificationViewHolder> {

    private Context context;
    List<NotificationRoot.DataItem> data = new ArrayList<>();


    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.setNotification(position);
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addData(List<NotificationRoot.DataItem> data) {

        this.data.addAll(data);
        notifyItemRangeInserted(this.data.size(), data.size());
    }


    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        ItemNotificationBinding binding;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemNotificationBinding.bind(itemView);
        }

        public void setNotification(int position) {
            NotificationRoot.DataItem notification = data.get(position);
            binding.tvtitle.setText(notification.getTitle());
            binding.tvNotification.setText(notification.getMessage());
            if (notification.getImage() != null) {
                Glide.with(context).load(Const.IMAGE_URL + notification.getImage()).into(binding.image);
            } else {
                binding.image.setVisibility(View.GONE);
            }
            binding.tvdate.setText(notification.getCreatedAt());
        }
    }
}
