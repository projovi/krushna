package com.krushna.veginew.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.krushna.veginew.R;
import com.krushna.veginew.databinding.ItemProductImageBinding;
import com.krushna.veginew.models.ProductItem;
import com.krushna.veginew.retrofit.Const;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private List<ProductItem.ImagesItem> images;
    private Context context;

    public ImageAdapter(List<ProductItem.ImagesItem> images) {

        this.images = images;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_image, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Glide.with(context).load(Const.IMAGE_URL + images.get(position).getImage()).placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder).into(holder.binding.img);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ItemProductImageBinding binding;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemProductImageBinding.bind(itemView);
        }
    }
}
