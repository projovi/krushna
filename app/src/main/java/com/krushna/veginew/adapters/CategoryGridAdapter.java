package com.krushna.veginew.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.krushna.veginew.R;
import com.krushna.veginew.activites.SearchActivity;
import com.krushna.veginew.databinding.ItemCategoriesgridBinding;
import com.krushna.veginew.models.CategoryRoot;
import com.krushna.veginew.retrofit.Const;

import java.util.ArrayList;
import java.util.List;

public class CategoryGridAdapter extends RecyclerView.Adapter<CategoryGridAdapter.CategoryViewHolder> {
    private List<CategoryRoot.DataItem> data = new ArrayList<>();
    private Context context;

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categoriesgrid, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addData(List<CategoryRoot.DataItem> data) {

        this.data = data;
        notifyDataSetChanged();
    }

    public void clear() {
        this.data.clear();
        notifyDataSetChanged();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        ItemCategoriesgridBinding binding;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCategoriesgridBinding.bind(itemView);
        }

        public void setData(int pos) {
            Glide.with(context).load(Const.IMAGE_URL + data.get(pos).getImage()).placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder).into(binding.imgCategory);
            binding.tvName.setText(data.get(pos).getTitle());
            binding.getRoot().setOnClickListener(v -> context.startActivity(new Intent(context, SearchActivity.class).putExtra(Const.STR_CNAME, data.get(pos).getTitle()).putExtra(Const.STR_CID, String.valueOf(data.get(pos).getId()))));
        }
    }
}
