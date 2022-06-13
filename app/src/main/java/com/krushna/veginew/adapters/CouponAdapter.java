package com.krushna.veginew.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.krushna.veginew.R;
import com.krushna.veginew.databinding.ItemCouponlistBinding;
import com.krushna.veginew.models.CouponRoot;
import com.krushna.veginew.retrofit.Const;

import java.util.List;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.CoponViewHolder> {

    private final OnCouponClickListnear couponClickListnear;
    private double subtotal;

    private Context context;
    private List<CouponRoot.DataItem> coupons;

    public CouponAdapter(List<CouponRoot.DataItem> coupons, double subtotal, OnCouponClickListnear couponClickListnear) {

        this.coupons = coupons;
        this.subtotal = subtotal;
        this.couponClickListnear = couponClickListnear;
    }

    @Override
    public void onBindViewHolder(@NonNull CoponViewHolder holder, int position) {

        holder.binding.setCoupon(coupons.get(position));
        holder.binding.tvMinAmount.setTextColor(ContextCompat.getColor(context, R.color.red));
        holder.binding.tvMinAmount.setText(String.valueOf(coupons.get(position).getMinamount()).concat(" " + Const.getCurrency()));
        holder.binding.tvDiscount.setText(coupons.get(position).getDiscount() + "%");
       /* if (coupons.get(position).getType() == 1) {
            holder.binding.tvDiscount.setText(coupons.get(position).getDiscount() + Const.getCurrency());
        } else {
        }*/
        long minAmount = coupons.get(position).getMinamount();
        if (minAmount <= subtotal) {
            holder.binding.tvApply.setVisibility(View.VISIBLE);
        } else {
            holder.binding.tvApply.setVisibility(View.GONE);
        }
        holder.binding.tvApply.setOnClickListener(v -> couponClickListnear.onCouponClick(coupons.get(position)));

    }


    @NonNull
    @Override
    public CoponViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_couponlist, parent, false);
        return new CoponViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return coupons.size();
    }

    public interface OnCouponClickListnear {
        void onCouponClick(CouponRoot.DataItem coupon);
    }


    public class CoponViewHolder extends RecyclerView.ViewHolder {
        ItemCouponlistBinding binding;

        public CoponViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCouponlistBinding.bind(itemView);
        }
    }
}
