package com.krushna.veginew.fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonSyntaxException;
import com.krushna.veginew.R;
import com.krushna.veginew.VegiUtils;
import com.krushna.veginew.databinding.BottomSheetratingBinding;
import com.krushna.veginew.databinding.FragmentOrderSummaryBinding;
import com.krushna.veginew.models.OrderDetail;
import com.krushna.veginew.popups.LoderPopup;
import com.krushna.veginew.retrofit.Const;
import com.krushna.veginew.retrofit.RetrofitBuilder;

import java.text.DecimalFormat;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class OrderSummaryFragment extends BaseFragment {

    FragmentOrderSummaryBinding binding;
    CompositeDisposable disposable = new CompositeDisposable();
    private BottomSheetDialog bottomSheetDialog;
    private OrderDetail orderDetail;

    public OrderSummaryFragment(OrderDetail orderDetail) {


        this.orderDetail = orderDetail;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_summary, container, false);


        if (orderDetail != null) {
            setOrderDetails();
            initListnear();
        }
        return binding.getRoot();
    }

    private void setOrderDetails() {

        if (orderDetail != null) {
            if (orderDetail.getRating() != null && orderDetail.getRating().getRating() != null && !orderDetail.getRating().getRating().isEmpty()) {
                binding.ratingbar.setRating(Float.parseFloat(orderDetail.getRating().getRating()));
                //  binding.ratingbar.setActivated(Float.parseFloat(orderDetail.getRating().getRating()) == 0);
                binding.ratingbar.setActivated(Float.parseFloat(orderDetail.getRating().getRating()) == 0);
                // binding.ratingbar.setEnabled(false);

            } else {

            }


            binding.tvdate.setText(orderDetail.getDate());
            binding.tvOrderId.setText(orderDetail.getOrderId());
            binding.tvPaymentType.setText(String.valueOf(orderDetail.getPaymentType()));
            binding.tvOrderItems.setText(String.valueOf(orderDetail.getOrderproducts().size()));
            DecimalFormat decimalFormat = new DecimalFormat("###.##");
            binding.tvSheepingCharges.setText(Const.getCurrency() + decimalFormat.format(Float.parseFloat(orderDetail.getShippingCharge())));
            binding.tvCoupenAmount.setText(Const.getCurrency() + decimalFormat.format(Float.parseFloat(orderDetail.getCouponDiscount())));
            binding.tvSubAmount.setText(Const.getCurrency() + decimalFormat.format(Float.parseFloat(orderDetail.getSubtotal())));
            binding.tvTotalAmount.setText(Const.getCurrency() + decimalFormat.format(Float.parseFloat(orderDetail.getTotalAmount())));
            String status = VegiUtils.getOrderStatus(orderDetail.getStatus());
            binding.tvStatus.setText(status);
            binding.tvStatus.setBackgroundTintList(ColorStateList.valueOf(VegiUtils.getOrderStatusColor(getActivity(), orderDetail.getStatus())));


            if (orderDetail.getStatus() == 4) {
                if (orderDetail.getRating() != null) {
                    binding.lytrating.setVisibility(View.VISIBLE);
                    binding.tvSendFeedBack.setVisibility(View.GONE);
                } else {
                    binding.lytrating.setVisibility(View.GONE);
                    binding.tvSendFeedBack.setVisibility(View.VISIBLE);
                }
            } else {
                binding.lytrating.setVisibility(View.GONE);
                binding.tvSendFeedBack.setVisibility(View.GONE);
            }

        }

        if (orderDetail != null && orderDetail.getOrderaddress() != null) {
            OrderDetail.Orderaddress address = orderDetail.getOrderaddress();


            try {

                binding.tvUserName.setText(address.getFirstname());
                binding.tvAddress.setText(address.getAddress() + " " +
                        address.getArea() + " " + address.getCity() + " " + address.getPincode());
                binding.tvMobile.setText(address.getNumber());


            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }


            binding.tvEmail.setText(sessionManager.getUser().getIdentity());


        }
    }

    private void initListnear() {
        binding.tvSendFeedBack.setOnClickListener((v) -> {
            openBottomSheet(0);
        });
    }

    private void openBottomSheet(float rating) {
        bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;
            FrameLayout bottomSheet = (FrameLayout) d.findViewById(R.id.design_bottom_sheet);
            BottomSheetBehavior.from(bottomSheet)
                    .setState(BottomSheetBehavior.STATE_EXPANDED);
        });
        BottomSheetratingBinding sheetratingBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.bottom_sheetrating, null, false);
        bottomSheetDialog.setContentView(sheetratingBinding.getRoot());
        bottomSheetDialog.show();
        sheetratingBinding.ratingbar.setRating(rating);
        sheetratingBinding.imgClose.setOnClickListener(v -> bottomSheetDialog.dismiss());
        sheetratingBinding.tvSubmit.setOnClickListener(v -> {
            String s = sheetratingBinding.etDes.getText().toString();
            int i = (int) sheetratingBinding.ratingbar.getRating();
            if (!s.equals("")) {
                submitRating(i, sheetratingBinding.etDes.getText().toString());
            } else {
                sheetratingBinding.etDes.setError("Required");
            }
        });
    }

    private void submitRating(float rating, String s) {
        LoderPopup loderPopup = new LoderPopup(getActivity());
        disposable.add(RetrofitBuilder.create().addRating(Const.DEVKEY, getToken(), orderDetail.getOrderId(), String.valueOf(rating), s)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> {
                    binding.pd.setVisibility(View.VISIBLE);
                    loderPopup.show();
                })
                .doOnDispose(() -> {

                })
                .subscribe((restResponse, throwable) -> {
                    binding.pd.setVisibility(View.GONE);
                    loderPopup.cencel();

                    //     Log.d("TAG", "getData: err "+throwable.getMessage());
                    if (restResponse != null && restResponse.isStatus()) {
                        Toast.makeText(getActivity(), "Review Added  Successfully", Toast.LENGTH_SHORT).show();
                        binding.tvSendFeedBack.setVisibility(View.GONE);
                        binding.ratingbar.setRating(rating);
                        binding.lytrating.setVisibility(View.VISIBLE);


                    } else {
                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                    bottomSheetDialog.dismiss();
                }));

    }


}