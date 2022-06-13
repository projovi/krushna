package com.krushna.veginew.activites;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.krushna.veginew.R;
import com.krushna.veginew.adapters.WishlistAdapter;
import com.krushna.veginew.databinding.ActivityWishlistBinding;
import com.krushna.veginew.models.ProductItem;
import com.krushna.veginew.popups.ConfirmationPopup;

import java.util.ArrayList;
import java.util.List;

public class WishlistActivity extends BaseActivity {
    ActivityWishlistBinding binding;
    WishlistAdapter wishlistAdapter;
    private List<ProductItem> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wishlist);


        getWishlistData();
    }

    private void getWishlistData() {
        wishlistAdapter = new WishlistAdapter();
        binding.rvWishlist.setAdapter(wishlistAdapter);
        list = sessionManager.getWishlist();

        wishlistAdapter.addData(list);
        wishlistAdapter.setOnWishlistClickListnear((product, position) -> {
            new ConfirmationPopup(this, getString(R.string.deleteproduct), getString(R.string.deleteconfirlmsg), new ConfirmationPopup.OnPopupClickListner() {
                @Override
                public void onPositive() {
                    removeWishlistItem(product, position);
                }

                @Override
                public void onNegative() {

                }
            }, "Yes", "No");


        });
        if (list.isEmpty()) {
            binding.lytEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void removeWishlistItem(ProductItem product, int position) {
        sessionManager.toggleWishlist(product);
        wishlistAdapter.removeItem(position);
        list.remove(position);
        if (list.isEmpty()) {
            binding.lytEmpty.setVisibility(View.VISIBLE);
        }
    }

    public void onClickBack(View view) {
        onBackPressed();
    }
}