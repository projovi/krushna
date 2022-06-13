package com.krushna.veginew.adapters;

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
import com.krushna.veginew.databinding.ItemProductsBinding;
import com.krushna.veginew.models.ProductItem;
import com.krushna.veginew.retrofit.Const;

import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context context;
    private List<ProductItem> products;

    private CartUtils cartUtils;
    private AppDatabase db;

    public ProductAdapter() {


    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_products, parent, false));
    }

    public void initAdapter(Context context) {
        this.context = context;
        db = Room.databaseBuilder(context,
                AppDatabase.class, Const.DB_NAME).allowMainThreadQueries().build();

        cartUtils = new CartUtils(context);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void addData(List<ProductItem> products) {

        this.products = products;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ItemProductsBinding binding;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemProductsBinding.bind(itemView);
        }

        public void setData(int position) {
            DecimalFormat df = new DecimalFormat("###.##");
            ProductItem product = products.get(position);
            Glide.with(context).load(Const.IMAGE_URL + product.getImages().get(0).getImage())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder).into(binding.imgProduct);

            binding.tvName.setText(product.getName());
            String price = Const.getCurrency() + String.valueOf(product.getPrices().get(0).getSalePrice());
            binding.tvRate.setText(price + " / " + product.getPrices().get(0).getUnit() + " " + product.getPrices().get(0).getUnits().getTitle());

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
