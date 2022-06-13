package com.krushna.veginew.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.krushna.veginew.R;
import com.krushna.veginew.SessionManager;
import com.krushna.veginew.VegiUtils;
import com.krushna.veginew.databinding.ItemOrdersListBinding;
import com.krushna.veginew.models.OrderDetail;
import com.krushna.veginew.retrofit.Const;

import java.util.ArrayList;
import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyOrderViewHolder> {


    public Context context;
    OnOrderClickListnear onOrderClickListnear;
    SessionManager sessionManager;
    private List<OrderDetail> data = new ArrayList<>();

    public OnOrderClickListnear getOnOrderClickListnear() {
        return onOrderClickListnear;
    }

    public void setOnOrderClickListnear(OnOrderClickListnear onOrderClickListnear) {
        this.onOrderClickListnear = onOrderClickListnear;
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderViewHolder holder, int position) {

        holder.setOrderData(position);
    }

    @NonNull
    @Override
    public MyOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        sessionManager = new SessionManager(context);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orders_list, parent, false);
        return new MyOrderViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public void addData(List<OrderDetail> data) {

        this.data.addAll(data);
        notifyItemRangeInserted(this.data.size(), data.size());

    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }


    public interface OnOrderClickListnear {
        void onClickOpen(OrderDetail order);

        void onClickCancel(OrderDetail order);
    }

    public class MyOrderViewHolder extends RecyclerView.ViewHolder {
        ItemOrdersListBinding binding;

        public MyOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemOrdersListBinding.bind(itemView);

        }

        public void setOrderData(int position) {
            OrderDetail order = data.get(position);
            binding.tvOrderId.setText(order.getOrderId());
            binding.tvItemCount.setText("Total Items: ".concat(String.valueOf(order.getOrderproducts().size())));
            binding.tvItemPrice.setText("".concat(Const.getCurrency()).concat(order.getTotalAmount()));
            binding.tvDate.setText(order.getDate());


            String status = VegiUtils.getOrderStatus(order.getStatus());
            binding.tvStatus.setBackgroundTintList(ColorStateList.valueOf(VegiUtils.getOrderStatusColor(itemView.getContext(), order.getStatus())));
            binding.tvStatus.setText(status);
            binding.getRoot().setOnClickListener(v -> {
                onOrderClickListnear.onClickOpen(order);
            });
           /* if (order.getStatus()==5){
                binding.btnCancel.setVisibility(View.VISIBLE);
            }else {
                binding.btnCancel.setVisibility(View.GONE);
            }*/
            if (order.getStatus() == 4) {
                binding.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.white));
                binding.btnCancel.setVisibility(View.GONE);
            } else if (order.getStatus() == 5) {
                binding.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.white));
                binding.btnCancel.setVisibility(View.GONE);
            } else {
                binding.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.white));
                binding.btnCancel.setVisibility(View.VISIBLE);
            }
            binding.btnCancel.setOnClickListener(v -> {
                if (order.getPaymentType() == 1) {
                    onOrderClickListnear.onClickCancel(order);
                } else {
                    Toast.makeText(binding.getRoot().getContext(), binding.getRoot().getContext().getResources().getString(R.string.only_on), Toast.LENGTH_SHORT).show();
                }

            });


        }
    }
}
