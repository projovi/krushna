package com.krushna.veginew.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.krushna.veginew.R;
import com.krushna.veginew.activites.ProductDetailActivity;
import com.krushna.veginew.dao.AppDatabase;
import com.krushna.veginew.dao.CartUtils;
import com.krushna.veginew.databinding.ItemSearchBinding;
import com.krushna.veginew.models.ProductItem;
import com.krushna.veginew.retrofit.Const;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.CartViewHolder> {
    private List<ProductItem> products = new ArrayList<>();
    private Context context;
    private AppDatabase db;
    private CartUtils cartUtils;

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.setData(position);
    }

    public void initAdapter(Activity context) {
        this.context = context;
        db = Room.databaseBuilder(context,
                AppDatabase.class, Const.DB_NAME).allowMainThreadQueries().build();

        cartUtils = new CartUtils(context);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void addData(List<ProductItem> products) {
        this.products.addAll(products);
        notifyItemRangeInserted(this.products.size(), products.size());
    }

    public void clear() {
        products.clear();
        notifyDataSetChanged();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        ItemSearchBinding binding;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemSearchBinding.bind(itemView);
        }

        public void setData(int pos) {
            ProductItem product = products.get(pos);
            binding.tvName.setText(product.getName());
            binding.tvPrice.setText(Const.getCurrency() + product.getPrices().get(0).getSalePrice());
            binding.tvWeight.setText(product.getPrices().get(0).getUnit() + " " + product.getPrices().get(0).getUnits().getTitle());
            Glide.with(context).
                    load(Const.IMAGE_URL + product.getImages().get(0).getImage())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(binding.imgProduct);

            binding.getRoot().setOnClickListener(v -> ProductDetailActivity.open(context, String.valueOf(product.getId())));
            checkCartData(product);
            setCartData(product);

        }

        private void setCartData(ProductItem product) {
            binding.tvAdd.setOnClickListener(v -> {
                cartUtils.add(product, product.getPrices().get(0));

                long quantity = cartUtils.getCartdata(String.valueOf(product.getPrices().get(0).getId()));
                binding.tvCount.setText(String.valueOf(quantity));
                checkCartData(product);
            });
            binding.btnPlus.setOnClickListener(v -> {
                cartUtils.add(product, product.getPrices().get(0));
                long quantity = cartUtils.getCartdata(String.valueOf(product.getPrices().get(0).getId()));
                binding.tvCount.setText(String.valueOf(quantity));
                checkCartData(product);
            });
            binding.btnMinus.setOnClickListener(v -> {
                long quantity = cartUtils.getCartdata(String.valueOf(product.getPrices().get(0).getId()));
                if (quantity >= 1) {
                    quantity--;
                    cartUtils.less(quantity, product.getPrices().get(0));
                }
                binding.tvCount.setText(String.valueOf(quantity));
                checkCartData(product);
            });
        }

        private void checkCartData(ProductItem product) {
            Log.d("TAG", "checkCartData: " + cartUtils.getCartdata(String.valueOf(product.getPrices().get(0).getId())));
            if (cartUtils.getCartdata(String.valueOf(product.getPrices().get(0).getId())) == 0) {
                binding.tvAdd.setVisibility(View.VISIBLE);
                binding.lytCount.setVisibility(View.GONE);
            } else {
                long quantity = cartUtils.getCartdata(String.valueOf(product.getPrices().get(0).getId()));
                binding.tvCount.setText(String.valueOf(quantity));
                binding.tvAdd.setVisibility(View.GONE);
                binding.lytCount.setVisibility(View.VISIBLE);
            }

        }

    }
}
