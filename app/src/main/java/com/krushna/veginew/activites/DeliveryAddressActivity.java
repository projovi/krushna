package com.krushna.veginew.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.krushna.veginew.R;
import com.krushna.veginew.adapters.AddressAdapter;
import com.krushna.veginew.databinding.ActivityDeliveryAddressBinding;
import com.krushna.veginew.models.DeliveryAddress;
import com.krushna.veginew.popups.ConfirmationPopup;
import com.krushna.veginew.retrofit.Const;
import com.krushna.veginew.retrofit.RetrofitBuilder;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DeliveryAddressActivity extends BaseActivity {
    ActivityDeliveryAddressBinding binding;
    AddressAdapter addressAdapter = new AddressAdapter();


    CompositeDisposable disposable = new CompositeDisposable();
    private boolean defaultAddressFound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_delivery_address);


    }

    @Override
    protected void onResume() {
        super.onResume();
        getAddressList();
        initListnear();
        binding.rvAddress.setAdapter(addressAdapter);

    }

    private void initListnear() {
        addressAdapter.setOnClickDeleveryAddress(datum -> {
            new ConfirmationPopup(this, getString(R.string.deleteaddress), getString(R.string.deleteconfiraddress), new ConfirmationPopup.OnPopupClickListner() {
                @Override
                public void onPositive() {
                    deleteAddress(datum);
                }

                @Override
                public void onNegative() {

                }
            }, "Yes", "No");


        });
    }

    private void deleteAddress(DeliveryAddress datum) {
        disposable.add(RetrofitBuilder.create().deleteAddress(Const.DEVKEY, getToken(), datum.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> {
                    binding.pBar.setVisibility(View.VISIBLE);
                })
                .doOnDispose(() -> {

                })
                .subscribe((restResponse, throwable) -> {
                    binding.pBar.setVisibility(View.GONE);
                    ;
                    //     Log.d("TAG", "getData: err "+throwable.getMessage());
                    if (restResponse != null && restResponse.isStatus()) {
                        Toast.makeText(this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                        //  addressAdapter.delete(datum);
                        getAddressList();
                    } else {
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }));

    }


    private void getAddressList() {
        addressAdapter.clear();
        binding.lyt404.setVisibility(View.GONE);
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
                    if (deliveryAddressRoot != null) {
                        if (deliveryAddressRoot.isStatus()) {
                            if (!deliveryAddressRoot.getData().isEmpty()) {

                                for (int i = 0; i < deliveryAddressRoot.getData().size(); i++) {
                                    if (deliveryAddressRoot.getData().get(i).getIsDefault() == 1) {
                                        defaultAddressFound = true;
                                    }
                                }
                                addressAdapter.addData(deliveryAddressRoot.getData());
                                if (deliveryAddressRoot.getData().size() < 4) {
                                    binding.fabAdd.setVisibility(View.VISIBLE);
                                } else {
                                    binding.fabAdd.setVisibility(View.GONE);
                                }
                            } else {
                                binding.fabAdd.setVisibility(View.VISIBLE);

                                binding.lyt404.setVisibility(View.VISIBLE);
                            }
                        } else {
                            binding.fabAdd.setVisibility(View.VISIBLE);
                            binding.lyt404.setVisibility(View.VISIBLE);
                        }
                    } else {
                        binding.lyt404.setVisibility(View.VISIBLE);
                    }
                }));

    }


    public void onClickBack(View view) {
        onBackPressed();
    }

    public void onClickAdd(View view) {
        startActivity(new Intent(this, AddAddressActivity.class).putExtra("defaultFound", defaultAddressFound));
    }
}