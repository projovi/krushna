package com.krushna.veginew.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.krushna.veginew.R;
import com.krushna.veginew.databinding.ItemMyordersBinding;
import com.krushna.veginew.models.OrderDetail;
import com.krushna.veginew.retrofit.Const;

import java.text.DecimalFormat;
import java.util.List;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.OderItemViewHolder> {


    private List<OrderDetail.OrderproductsItem> orderproducts;

    public OrderItemsAdapter(List<OrderDetail.OrderproductsItem> orderproducts) {

        this.orderproducts = orderproducts;
    }

    @NonNull
    @Override
    public OderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myorders, parent, false);
        return new OderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OderItemViewHolder holder, int position) {
        DecimalFormat df = new DecimalFormat("###.##");
        OrderDetail.OrderproductsItem item = orderproducts.get(position);
        holder.binding.tvProductName.setText(item.getProductName());
        float price = Float.parseFloat(item.getPrice()) * item.getQuantity();
        holder.binding.tvProductPrice.setText(Const.getCurrency() + df.format(price));
        holder.binding.tvProductWeight.setText(item.getPriceUnit().concat(" * " + item.getQuantity()));
        Glide.with(holder.binding.getRoot().getContext())

                .load(Const.IMAGE_URL + item.getImage())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.binding.imageOrderProduct);
    }

    @Override
    public int getItemCount() {
        return orderproducts.size();
    }

    public static class OderItemViewHolder extends RecyclerView.ViewHolder {
        ItemMyordersBinding binding;

        public OderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemMyordersBinding.bind(itemView);
        }
    }
}
