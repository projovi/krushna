package com.krushna.veginew.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.krushna.veginew.fragments.ComplainFragment;


public class TickitPagerAdapter extends FragmentPagerAdapter {


    public TickitPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new ComplainFragment(0);
        } else {
            return new ComplainFragment(1);
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
            return "Open";
        } else {
            return "Resolved";
        }
    }
}
