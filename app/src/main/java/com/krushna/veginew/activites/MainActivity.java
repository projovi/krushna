package com.krushna.veginew.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.krushna.veginew.R;
import com.krushna.veginew.databinding.ActivityMainBinding;
import com.krushna.veginew.fragments.CartFragment;
import com.krushna.veginew.fragments.CategoryFragment;
import com.krushna.veginew.fragments.HomeFragment;
import com.krushna.veginew.fragments.ProfileFragment;
import com.krushna.veginew.interfaces.LoginListnraer;
import com.krushna.veginew.models.UserRoot;
import com.krushna.veginew.retrofit.Const;

public class MainActivity extends BaseActivity {
    public ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        setDefaultBottomMenu();
        initMenuListnear();
        initDrawerListnear();
        binding.menuhome.performClick();
        binding.navToolbar.tvContactEMail.setText(sessionManager.getSetting().getEmail());
        binding.navToolbar.tvContactNumber.setText(sessionManager.getSetting().getNumber());
    }

    private void initDrawerListnear() {

        binding.imgMenu.setOnClickListener(v -> binding.drawer.openDrawer(GravityCompat.START));
        binding.navToolbar.lytrating.setOnClickListener(v -> {
            closeDrawer();
            startActivity(new Intent(this, RatingAndReivewActivity.class));
        });
        binding.navToolbar.lytfaq.setOnClickListener(v -> {
            closeDrawer();
            startActivity(new Intent(this, FaqActivity.class));
        });
        binding.navToolbar.lytNotification.setOnClickListener(v -> {
            closeDrawer();
            if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
                startActivity(new Intent(this, NotificationActivity.class));
            } else {
                openLoginSheet(new LoginListnraer() {
                    @Override
                    public void onLoginSuccess(UserRoot.User u) {
                        startActivity(new Intent(MainActivity.this, NotificationActivity.class));
                    }

                    @Override
                    public void onFailure() {
                    }

                    @Override
                    public void onDismiss() {

                    }
                });
            }

        });
        binding.navToolbar.lytAbout.setOnClickListener(v -> {
            closeDrawer();
            WebActivity.open(this, "About Us", sessionManager.getSetting().getAbout());
        });
        binding.navToolbar.lytPrivacy.setOnClickListener(v -> {
            closeDrawer();
            WebActivity.open(this, "Privacy Policy", sessionManager.getSetting().getPrivacyPolicy());
        });
        binding.navToolbar.lytTerms.setOnClickListener(v -> {
            closeDrawer();
            WebActivity.open(this, "Terms & Conditions", sessionManager.getSetting().getTerms());
        });
    }

    private void closeDrawer() {
        binding.drawer.closeDrawer(GravityCompat.START);
    }

    private void initMenuListnear() {
        binding.menuhome.setOnClickListener(v -> {
            setDefaultBottomMenu();
            setBottomMenuColor(true, binding.imghome, binding.tvHome);
            selectFragemnt(new HomeFragment());
        });
        binding.menuCategory.setOnClickListener(v -> {
            setDefaultBottomMenu();
            setBottomMenuColor(true, binding.imgCategory, binding.tvCategory);
            selectFragemnt(new CategoryFragment());
        });
        binding.menuCart.setOnClickListener(v -> {
            setDefaultBottomMenu();
            setBottomMenuColor(true, binding.imgCart, binding.tvCart);
            selectFragemnt(new CartFragment());
        });
        binding.menuProfile.setOnClickListener(v -> {
            setDefaultBottomMenu();
            setBottomMenuColor(true, binding.imgProfile, binding.tvProfile);
            selectFragemnt(new ProfileFragment());
        });


        binding.imgSearch.setOnClickListener(v -> startActivity(new Intent(this, SearchActivity.class)));
    }

    private void selectFragemnt(Fragment homeFragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, homeFragment).commit();
    }

    private void setDefaultBottomMenu() {
        binding.lytbotommenu.setVisibility(View.VISIBLE);
        setBottomMenuColor(false, binding.imghome, binding.tvHome);
        setBottomMenuColor(false, binding.imgCategory, binding.tvCategory);
        setBottomMenuColor(false, binding.imgCart, binding.tvCart);
        setBottomMenuColor(false, binding.imgProfile, binding.tvProfile);
    }

    private void setBottomMenuColor(boolean isSelected, ImageView img, TextView tv) {
        if (isSelected) {
            img.setImageTintList(ContextCompat.getColorStateList(this, R.color.white));
            tv.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else {
            img.setImageTintList(ContextCompat.getColorStateList(this, R.color.gray));
            tv.setTextColor(ContextCompat.getColor(this, R.color.gray));
        }
    }

    public void showBottomMenu(boolean b) {
        if (b) {
            binding.lytbotommenu.setVisibility(View.GONE);
        } else {
            binding.lytbotommenu.setVisibility(View.VISIBLE);
        }
    }
}