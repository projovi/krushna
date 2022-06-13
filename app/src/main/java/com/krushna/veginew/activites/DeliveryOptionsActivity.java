package com.krushna.veginew.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.krushna.veginew.R;
import com.krushna.veginew.adapters.DeliveryAddressOptionsAdapter;
import com.krushna.veginew.databinding.ActivityDeliveryOptionsBinding;
import com.krushna.veginew.models.DeliveryAddress;
import com.krushna.veginew.retrofit.Const;
import com.krushna.veginew.retrofit.RetrofitBuilder;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DeliveryOptionsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    ActivityDeliveryOptionsBinding binding;

    private String token;
    private DeliveryAddressOptionsAdapter deliveryAddressOptionsAdapter = new DeliveryAddressOptionsAdapter();
    ;
    private String addressId;
    private boolean isEmpty = false;
    private String lat = "";
    private String lang = "";
    CompositeDisposable disposable = new CompositeDisposable();
    private DeliveryAddress addressObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_delivery_options);

        initView();

binding.swipe.setOnRefreshListener(this);
        initListnear();
    }


    private void initListnear() {
        deliveryAddressOptionsAdapter.setOnAddressSelectListnear(address -> {
            addressId = String.valueOf(address.getId());
            //   lat = address.getLatitude();
            //   lang = address.getLongitude();
            addressObj = address;
        });


        binding.tvPay.setOnClickListener(v -> {
            if (isEmpty) {
                startActivity(new Intent(this, AddAddressActivity.class));
            } else {
                Intent intent = new Intent(this, PaymentSummaryActivity.class);
                intent.putExtra("addressId", addressId);
                intent.putExtra("address", new Gson().toJson(addressObj));
                //   intent.putExtra("lat", lat);
                // intent.putExtra("lang", lang);
                startActivity(intent);
            }

        });


    }

    private void initView() {
        isEmpty = false;
        disposable.add(RetrofitBuilder.create().getAllDeliveryAddress(Const.DEVKEY, getToken()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> {
                    binding.shimmer.setVisibility(View.VISIBLE);
                })
                .doOnDispose(() -> {
                    binding.shimmer.setVisibility(View.VISIBLE);
                })
                .subscribe((deliveryAddressRoot, throwable) -> {
                    binding.shimmer.setVisibility(View.GONE);
                    binding.swipe.setRefreshing(false);
                    //     Log.d("TAG", "getData: err "+throwable.getMessage());
                    if (deliveryAddressRoot != null && deliveryAddressRoot.isStatus() && !deliveryAddressRoot.getData().isEmpty()) {
                        deliveryAddressOptionsAdapter.addData(deliveryAddressRoot.getData());
                        binding.rvAddress.setAdapter(deliveryAddressOptionsAdapter);
                        addressObj = deliveryAddressRoot.getData().get(0);
                    } else {
                        isEmpty = true;
                        binding.lyt404.setVisibility(View.VISIBLE);

                    }
                }));

    }

    @Override
    public void onRefresh() {
        initView();
    }


    public void onClickBack(View view) {
        onBackPressed();
    }


}