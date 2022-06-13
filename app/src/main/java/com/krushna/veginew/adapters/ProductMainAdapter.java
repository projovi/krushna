package com.krushna.veginew.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.krushna.veginew.R;
import com.krushna.veginew.activites.SearchActivity;
import com.krushna.veginew.databinding.ItemMainBinding;
import com.krushna.veginew.models.HomePageRoot;
import com.krushna.veginew.retrofit.Const;

import java.util.List;

public class ProductMainAdapter extends RecyclerView.Adapter<ProductMainAdapter.ProductMainViewHolder> {
    private List<HomePageRoot.CategoryWithProductItem> categoryWithProduct;
    private Context context;

    public ProductMainAdapter() {


    }

    @NonNull
    @Override
    public ProductMainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ProductMainViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductMainViewHolder holder, int position) {

        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return categoryWithProduct.size();
    }

    public void addData(List<HomePageRoot.CategoryWithProductItem> categoryWithProduct) {

        this.categoryWithProduct = categoryWithProduct;
    }

    public class ProductMainViewHolder extends RecyclerView.ViewHolder {
        ItemMainBinding binding;
        ProductAdapter productMainAdapter = new ProductAdapter();

        public ProductMainViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemMainBinding.bind(itemView);
            productMainAdapter.initAdapter(context);
        }

        public void setData(int position) {
            HomePageRoot.CategoryWithProductItem dataItem = categoryWithProduct.get(position);
            binding.tvCatName.setText(dataItem.getTitle());
            productMainAdapter.addData(dataItem.getProducts());
            binding.rvProducts.setAdapter(productMainAdapter);

            binding.tvSeeAll.setOnClickListener(v -> context.startActivity(new Intent(context, SearchActivity.class).putExtra(Const.STR_CNAME, dataItem.getTitle()).putExtra(Const.STR_CID, String.valueOf(dataItem.getId()))));
        }
    }
}
