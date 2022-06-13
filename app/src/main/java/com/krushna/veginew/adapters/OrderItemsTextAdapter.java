package com.krushna.veginew.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.krushna.veginew.R;
import com.krushna.veginew.dao.CartOffline;
import com.krushna.veginew.databinding.ItemOrdersTextBinding;
import com.krushna.veginew.retrofit.Const;

import java.util.List;

public class OrderItemsTextAdapter extends RecyclerView.Adapter<OrderItemsTextAdapter.OrderItemTextViewHOlder> {


    private List<CartOffline> list;

    public OrderItemsTextAdapter(List<CartOffline> list) {

        this.list = list;
    }

    @NonNull
    @Override
    public OrderItemTextViewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orders_text, parent, false);
        return new OrderItemTextViewHOlder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemTextViewHOlder holder, int position) {

        CartOffline item = list.get(position);
        holder.binding.tvProductName.setText(item.getName());
        holder.binding.tvPrice.setText(Const.getCurrency() + item.getPrice());
        holder.binding.tvQuantity.setText(String.valueOf(item.getQuantity()) + " Item");
        holder.binding.tvWeight.setText(item.getPriceUnitName());

        Glide.with(holder.binding.getRoot().getContext())
                .load(Const.IMAGE_URL + item.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.binding.imgproduct);

//ll
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class OrderItemTextViewHOlder extends RecyclerView.ViewHolder {
        ItemOrdersTextBinding binding;

        public OrderItemTextViewHOlder(@NonNull View itemView) {
            super(itemView);
            binding = ItemOrdersTextBinding.bind(itemView);
        }
    }
}
