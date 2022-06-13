package com.krushna.veginew.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.krushna.veginew.R;
import com.krushna.veginew.databinding.ItemRatingBinding;
import com.krushna.veginew.models.RatingReviewListRoot;
import com.krushna.veginew.retrofit.Const;

import java.util.ArrayList;
import java.util.List;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.RatingViewHolder> {


    private List<RatingReviewListRoot.DataItem> data = new ArrayList<>();
    private Context context;

    @NonNull
    @Override
    public RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rating, parent, false);
        return new RatingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingViewHolder holder, int position) {
        holder.setRatingData(position);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addData(List<RatingReviewListRoot.DataItem> data) {
        this.data.addAll(data);
        notifyItemRangeInserted(this.data.size(), data.size());
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }


    public class RatingViewHolder extends RecyclerView.ViewHolder {
        ItemRatingBinding binding;

        public RatingViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemRatingBinding.bind(itemView);
        }

        public void setRatingData(int position) {
            RatingReviewListRoot.DataItem dataitem = data.get(position);

            Glide.with(context).load(Const.IMAGE_URL + dataitem.getUser().getImage()).placeholder(R.drawable.user_place_holder).circleCrop().into(binding.imgUser);
            binding.tvname.setText(dataitem.getUser().getFirstname());
            binding.msg.setText(dataitem.getReview());
            float rating = Float.parseFloat(dataitem.getRating());

            binding.ratingbar.setRating(rating);
        }
    }
}
