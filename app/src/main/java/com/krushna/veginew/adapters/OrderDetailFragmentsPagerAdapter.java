package com.krushna.veginew.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.krushna.veginew.fragments.OrderItemsFragment;
import com.krushna.veginew.fragments.OrderSummaryFragment;
import com.krushna.veginew.models.OrderDetail;


public class OrderDetailFragmentsPagerAdapter extends FragmentPagerAdapter {


    private String status;
    private OrderDetail order;

    public OrderDetailFragmentsPagerAdapter(OrderDetail order, @NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);


        this.order = order;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        if (position == 0) {

            return new OrderSummaryFragment(order);
        } else {

            return new OrderItemsFragment(order);
        }
    }


    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Summary";
        } else {
            return "Items";
        }
    }
}
