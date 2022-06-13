package com.krushna.veginew.activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.krushna.veginew.R;
import com.krushna.veginew.adapters.OrderDetailFragmentsPagerAdapter;
import com.krushna.veginew.databinding.ActivityOrderDetailBinding;
import com.krushna.veginew.databinding.BottomsheetHaveAnIssueBinding;
import com.krushna.veginew.models.OrderDetail;
import com.krushna.veginew.popups.LoderPopup;
import com.krushna.veginew.retrofit.Const;
import com.krushna.veginew.retrofit.RetrofitBuilder;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class OrderDetailActivity extends BaseActivity {
    ActivityOrderDetailBinding binding;
    CompositeDisposable disposable = new CompositeDisposable();
    BottomsheetHaveAnIssueBinding bottomsheetHaveAnIssueBinding;
    private BottomSheetDialog bottomSheetDialog;
    private String orderId;
    private OrderDetail orderDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail);

        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        if (orderId != null && !orderId.isEmpty()) {
            getOrderDetails();

        }

    }

    private void getOrderDetails() {
        disposable.add(RetrofitBuilder.create().getOrderDetail(Const.DEVKEY, getToken(), orderId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> {
                    binding.pBar.setVisibility(View.VISIBLE);
                })
                .doOnDispose(() -> {

                })
                .subscribe((orderDetailRoot, throwable) -> {
                    binding.pBar.setVisibility(View.GONE);
                    //     Log.d("TAG", "getData: err "+throwable.getMessage());
                    if (orderDetailRoot != null && orderDetailRoot.isStatus() && orderDetailRoot.getOrderDetail() != null) {
                        orderDetail = orderDetailRoot.getOrderDetail();
                        OrderDetailFragmentsPagerAdapter pagerAdapter = new OrderDetailFragmentsPagerAdapter(orderDetail, getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
                        binding.viewPager.setAdapter(pagerAdapter);
                        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tablayout));
                        initView();
                        binding.tvOrderId.setText(orderId);
                        binding.lytBottom.setVisibility(orderDetail.getStatus() == 4 ? View.VISIBLE : View.GONE);
                        if (orderDetailRoot.getOrderDetail().getHasComplaint() == 1) {
                            binding.lytBottom.setVisibility(View.GONE);
                        }
                        binding.tvNewIssue.setOnClickListener(v -> openBottomSheet());
                    } else {
                        binding.lyt404.setVisibility(View.VISIBLE);
                    }
                    if (throwable != null) {
                        Log.i("TAG", "getOrderDetails: err " + throwable.toString());
                    }
                }));

    }

    private void openBottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;
            FrameLayout bottomSheet = (FrameLayout) d.findViewById(R.id.design_bottom_sheet);
            BottomSheetBehavior.from(bottomSheet)
                    .setState(BottomSheetBehavior.STATE_EXPANDED);
        });
        bottomsheetHaveAnIssueBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.bottomsheet_have_an_issue, null, false);
        bottomSheetDialog.setContentView(bottomsheetHaveAnIssueBinding.getRoot());
        bottomSheetDialog.show();
        bottomsheetHaveAnIssueBinding.tvOrderId.setText(orderId);
        bottomsheetHaveAnIssueBinding.btnClose.setOnClickListener(v -> bottomSheetDialog.dismiss());
        bottomsheetHaveAnIssueBinding.lytBottom.setOnClickListener(view -> {
            if (bottomsheetHaveAnIssueBinding.etDes.getText().toString().length() >= 100) {
                setndComplaint();
            } else {
                Toast.makeText(this, "Please describe your issue briefly", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setndComplaint() {

        String title = bottomsheetHaveAnIssueBinding.etTitle.getText().toString();
        String des = bottomsheetHaveAnIssueBinding.etDes.getText().toString();
        String mobile = bottomsheetHaveAnIssueBinding.etMobile.getText().toString();
        if (title.equals("")) {
            Toast.makeText(this, getString(R.string.writetitlefirst), Toast.LENGTH_SHORT).show();
            return;
        }
        if (des.equals("")) {
            Toast.makeText(this, getString(R.string.writedescriptionfirst), Toast.LENGTH_SHORT).show();
            return;
        }
        if (mobile.equals("")) {
            Toast.makeText(this, getString(R.string.entermobilenumber), Toast.LENGTH_SHORT).show();
            return;
        }
        if (mobile.length() > 11) {
            Toast.makeText(this, getString(R.string.entervalidmobilenumber), Toast.LENGTH_SHORT).show();
            return;
        }
        if (mobile.length() < 10) {
            Toast.makeText(this, getString(R.string.entervalidmobilenumber), Toast.LENGTH_SHORT).show();
            return;
        }
        binding.pBar.setVisibility(View.VISIBLE);
        LoderPopup loderPopup = new LoderPopup(this);
        disposable.add(RetrofitBuilder.create().raiseComplaint(Const.DEVKEY, getToken(), orderId, mobile, title, des)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> {
                    binding.pBar.setVisibility(View.VISIBLE);
                    loderPopup.show();
                })
                .doOnDispose(() -> {

                })
                .subscribe((restResponse, throwable) -> {
                    binding.pBar.setVisibility(View.GONE);
                    loderPopup.cencel();
                    //     Log.d("TAG", "getData: err "+throwable.getMessage());
                    if (restResponse != null && restResponse.isStatus()) {
                        binding.lytBottom.setVisibility(View.GONE);
                        Toast.makeText(this, "Complain Raised Successfully", Toast.LENGTH_SHORT).show();
                        //  addressAdapter.delete(datum);

                    } else {
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                    bottomSheetDialog.dismiss();
                }));

    }

    private void initView() {
//        binding.tablayout.addTab(binding.tablayout.newTab().setText(R.string.summary));
//        binding.tablayout.addTab(binding.tablayout.newTab().setText(R.string.items));
        binding.tablayout.setupWithViewPager(binding.viewPager);

    }

    public void onClickBack(View view) {
        onBackPressed();
    }
}