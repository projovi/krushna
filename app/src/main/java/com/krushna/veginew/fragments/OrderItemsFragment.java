package com.krushna.veginew.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.krushna.veginew.R;
import com.krushna.veginew.adapters.OrderItemsAdapter;
import com.krushna.veginew.databinding.FragmentOrderItemsBinding;
import com.krushna.veginew.models.OrderDetail;
import com.krushna.veginew.retrofit.Const;

import java.text.DecimalFormat;


public class OrderItemsFragment extends Fragment {
    FragmentOrderItemsBinding binding;
    private OrderDetail orderDetail;

    public OrderItemsFragment(OrderDetail orderDetail) {

        this.orderDetail = orderDetail;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_items, container, false);

        if (orderDetail != null && orderDetail.getOrderproducts() != null) {

            setData();
        }
        return binding.getRoot();
    }

    private void setData() {
        binding.tvItemCount.setText(String.valueOf(orderDetail.getOrderproducts().size()).concat(" items"));
        binding.tvtotalPrice.setText(Const.getCurrency().concat(" " + orderDetail.getSubtotal()));

        OrderItemsAdapter orderItemsAdapter = new OrderItemsAdapter(orderDetail.getOrderproducts());
        binding.rvOrderItems.setAdapter(orderItemsAdapter);
        binding.tvShippingCharge.setText(Const.getCurrency().concat(" " + new DecimalFormat("###.##").format(Float.parseFloat(orderDetail.getShippingCharge()))));
    }


}