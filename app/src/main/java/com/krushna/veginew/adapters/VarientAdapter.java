package com.krushna.veginew.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.krushna.veginew.R;
import com.krushna.veginew.dao.AppDatabase;
import com.krushna.veginew.dao.CartUtils;
import com.krushna.veginew.databinding.ItemProductPriceBinding;
import com.krushna.veginew.models.PricesItem;
import com.krushna.veginew.models.ProductItem;
import com.krushna.veginew.retrofit.Const;

import java.text.DecimalFormat;
import java.util.List;

public class VarientAdapter extends RecyclerView.Adapter<VarientAdapter.VarientViewHolder> {
    private List<PricesItem> prices;
    private ProductItem product;
    private Context context;
    private AppDatabase db;
    private CartUtils cartUtils;

    public VarientAdapter() {


    }

    @NonNull
    @Override
    public VarientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        db = Room.databaseBuilder(context,
                AppDatabase.class, Const.DB_NAME).allowMainThreadQueries().build();

        cartUtils = new CartUtils(context);
        return new VarientViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_price, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VarientViewHolder holder, int position) {
        holder.setData(position);
    }

    public void setData(List<PricesItem> prices, ProductItem product) {
        this.prices = prices;
        this.product = product;
    }

    @Override
    public int getItemCount() {
        return prices.size();
    }

    public class VarientViewHolder extends RecyclerView.ViewHolder {
        ItemProductPriceBinding binding;

        public VarientViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemProductPriceBinding.bind(itemView);
        }

        public void setData(int pos) {
            DecimalFormat df = new DecimalFormat("###.##");

            PricesItem data = prices.get(pos);
            binding.tvProductPrice.setText(Const.getCurrency() + data.getSalePrice());
            binding.tvProductweight.setText(df.format(Float.parseFloat(data.getUnit())) + " " + data.getUnits().getTitle());


            checkCartData(product, pos);
            setCartData(product, pos);
        }

        private void setCartData(ProductItem product, int pos) {
            binding.tvAdd.setOnClickListener(v -> {
                cartUtils.add(product, product.getPrices().get(pos));

                long quantity = cartUtils.getCartdata(String.valueOf(product.getPrices().get(pos).getId()));
                binding.tvCount.setText(String.valueOf(quantity));
                checkCartData(product, pos);
            });
            binding.btnPlus.setOnClickListener(v -> {
                cartUtils.add(product, product.getPrices().get(pos));
                long quantity = cartUtils.getCartdata(String.valueOf(product.getPrices().get(pos).getId()));
                binding.tvCount.setText(String.valueOf(quantity));
                checkCartData(product, pos);
            });
            binding.btnMinus.setOnClickListener(v -> {
                long quantity = cartUtils.getCartdata(String.valueOf(product.getPrices().get(pos).getId()));
                if (quantity >= 1) {
                    quantity--;
                    cartUtils.less(quantity, product.getPrices().get(pos));
                }
                binding.tvCount.setText(String.valueOf(quantity));
                checkCartData(product, pos);
            });
        }

        private void checkCartData(ProductItem product, int pos) {
            Log.d("TAG", "checkCartData: " + cartUtils.getCartdata(String.valueOf(product.getPrices().get(pos).getId())));
            if (cartUtils.getCartdata(String.valueOf(product.getPrices().get(pos).getId())) == 0) {
                binding.tvAdd.setVisibility(View.VISIBLE);
                binding.lytCount.setVisibility(View.GONE);
            } else {
                long quantity = cartUtils.getCartdata(String.valueOf(product.getPrices().get(pos).getId()));
                binding.tvCount.setText(String.valueOf(quantity));
                binding.tvAdd.setVisibility(View.GONE);
                binding.lytCount.setVisibility(View.VISIBLE);
            }

        }

    }
}
