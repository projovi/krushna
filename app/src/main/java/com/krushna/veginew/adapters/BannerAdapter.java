package com.krushna.veginew.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.krushna.veginew.R;
import com.krushna.veginew.databinding.ItemBannerBinding;
import com.krushna.veginew.models.HomePageRoot;
import com.krushna.veginew.retrofit.Const;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {
    private List<HomePageRoot.BannerItem> banner;
    private Context context;

    public BannerAdapter(List<HomePageRoot.BannerItem> banner) {

        this.banner = banner;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new BannerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        Glide.with(context).load(Const.IMAGE_URL + banner.get(position).getImage()).centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder).into(holder.bannerBinding.img);
    }

    @Override
    public int getItemCount() {
        return banner.size();
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {
        ItemBannerBinding bannerBinding;
        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerBinding = ItemBannerBinding.bind(itemView);
        }
    }
}
