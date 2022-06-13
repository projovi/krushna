package com.krushna.veginew.activites;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.krushna.veginew.R;
import com.krushna.veginew.bottomSheets.CityAeraBottomsheet;
import com.krushna.veginew.databinding.ActivityAddAddressBinding;
import com.krushna.veginew.models.AeraRoot;
import com.krushna.veginew.models.CityRoot;
import com.krushna.veginew.models.DeliveryAddress;
import com.krushna.veginew.retrofit.ApiCalling;
import com.krushna.veginew.retrofit.Const;
import com.krushna.veginew.retrofit.RetrofitBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AddAddressActivity extends BaseActivity {
    public static final int MAP_ACTIVITYCODE = 200;
    ActivityAddAddressBinding binding;
    ApiCalling apiCalling;
    List<CityRoot.DataItem> cites = new ArrayList<>();
    List<AeraRoot.DataItem> areas = new ArrayList<>();
    CityAeraBottomsheet cityAeraBottomsheet;
    private CityRoot.DataItem selectedCity = null;
    private AeraRoot.DataItem selectedArea = null;
    private boolean isUpdet = false;
    private int deleveryAddressId;

    private String latitude = "";
    private String longitude = "";

    private boolean defaultFound;
    private int addressType = 0;
    private DeliveryAddress address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_address);
        apiCalling = new ApiCalling(this);
        cityAeraBottomsheet = new CityAeraBottomsheet(this);


        Intent intent = getIntent();
        if (intent != null) {
            defaultFound = intent.getBooleanExtra("defaultFound", true);
        }
        if (intent != null && intent.getStringExtra("address") != null && !intent.getStringExtra("address").equals("")) {
            address = new Gson().fromJson(intent.getStringExtra("address"), DeliveryAddress.class);
            if (address != null) {
                isUpdet = true;
                binding.btnAddAddAddress.setText(R.string.updae);

                setAddress(address);
            }

        }
        initListner();
        getCities();
        if (!defaultFound) {
            binding.chkAddressDefult.setChecked(true);
        }

    }

    private void setAddress(DeliveryAddress address) {
        deleveryAddressId = address.getId();
        binding.etFName.setText(address.getFirstname());

        binding.etMobile.setText(address.getNumber());
        binding.etMobile2.setText(String.valueOf((address.getAltNumber()) != null ? address.getAltNumber() : ""));
        binding.etAddressLine.setText(address.getAddress());

        binding.etLandmark.setText(address.getLandmark());
        binding.etCity.setText(address.getCity().getName());
        binding.etAera.setText(address.getArea().getName());

        binding.etPin.setText(String.valueOf(address.getPincode()));

        binding.chkAddressDefult.setChecked(address.getIsDefault() == 1);

        selectAddressType(address.getAddressType());

        if (address.getLongitude() != null && address.getLatitude() != null) {
            latitude = address.getLatitude();
            longitude = address.getLongitude();
            binding.tvLocation.setText("Location Fetched");
            binding.tvLocation.setTextColor(ContextCompat.getColor(this, R.color.green));
            binding.tvLocation.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.light_green)));
            //   mylatlang = new LatLng(Double.parseDouble(address.getLatitude()), Double.parseDouble(address.getLongitude()));
        }


        binding.btnAddAddAddress.setOnClickListener(v -> uploadData());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAP_ACTIVITYCODE) {
            if (resultCode == RESULT_OK) {
                String lat = data.getStringExtra("lat");
                String lang = data.getStringExtra("long");
                if (lat != null && !lat.isEmpty()) {
                    Log.d("TAG", "onActivityResult: " + lat);
                    latitude = lat;
                    longitude = lang;

                    binding.tvLocation.setText(getString(R.string.locationfatched));
                    binding.tvLocation.setTextColor(ContextCompat.getColor(this, R.color.green));
                    binding.tvLocation.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.light_green)));

                } else {

                    Toast.makeText(this, "Location Not Found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Location Not Found", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void getCities() {

        apiCalling.getCities(new ApiCalling.OnCitiyGetListner() {
            @Override
            public void onFetched(List<CityRoot.DataItem> data) {
                cites.addAll(data);
                if (selectedCity == null && address != null) {
                    for (int i = 0; i < cites.size(); i++) {
                        if (cites.get(i).getId() == address.getCity().getId()) {
                            selectedCity = cites.get(i);
                            getAeraByCity();
                        }
                    }

                }
                binding.etCity.setOnClickListener(v -> {
                    cityAeraBottomsheet = new CityAeraBottomsheet(AddAddressActivity.this);
                    cityAeraBottomsheet.showCities(cites, (CityRoot.DataItem city1) -> {

                        selectedCity = city1;


                        binding.etCity.setText(selectedCity.getName());
                        getAeraByCity();
                    });
                });
            }

            @Override
            public void onFail() {

            }
        });
    }

    private void initListner() {
        binding.lnrHome.setOnClickListener(v -> selectAddressType(1));
        binding.lnrWork.setOnClickListener(v -> selectAddressType(2));
        binding.lnrOther.setOnClickListener(v -> selectAddressType(3));
        binding.btnAddAddAddress.setOnClickListener(v -> {
            uploadData();
        });

        binding.etAera.setOnClickListener(v -> {
            if (selectedCity == null) {
                Toast.makeText(this, "Select City First", Toast.LENGTH_SHORT).show();
                return;
            }
            cityAeraBottomsheet = new CityAeraBottomsheet(AddAddressActivity.this);
            cityAeraBottomsheet.showAreas(areas, area -> {
                selectedArea = area;
                binding.etAera.setText(selectedArea.getName());
            });
        });


        binding.tvLocation.setOnClickListener(v -> {
            startActivityForResult(new Intent(this, MapActivity.class), MAP_ACTIVITYCODE);
        });
        redioListnear();
    }

    private void selectAddressType(int i) {
        addressType = i;
        if (i == 1) {
            binding.lnrHome.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.orange)));
            binding.lnrWork.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.light_gray)));
            binding.lnrOther.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.light_gray)));
            binding.ivHome.setColorFilter(ContextCompat.getColor(this, R.color.white));
            binding.ivWork.setColorFilter(ContextCompat.getColor(this, R.color.black));
            binding.tvHome.setTextColor(ContextCompat.getColor(this, R.color.white));
            binding.tvWork.setTextColor(ContextCompat.getColor(this, R.color.black));
            binding.tvOther.setTextColor(ContextCompat.getColor(this, R.color.black));
        }
        if (i == 2) {
            binding.lnrWork.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.orange)));
            binding.lnrHome.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.light_gray)));
            binding.lnrOther.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.light_gray)));
            binding.ivWork.setColorFilter(ContextCompat.getColor(this, R.color.white));
            binding.ivHome.setColorFilter(ContextCompat.getColor(this, R.color.black));
            binding.tvHome.setTextColor(ContextCompat.getColor(this, R.color.black));
            binding.tvWork.setTextColor(ContextCompat.getColor(this, R.color.white));
            binding.tvOther.setTextColor(ContextCompat.getColor(this, R.color.black));
        }
        if (i == 3) {
            binding.lnrHome.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.light_gray)));
            binding.lnrWork.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.light_gray)));
            binding.lnrOther.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.orange)));
            binding.ivWork.setColorFilter(ContextCompat.getColor(this, R.color.black));
            binding.ivHome.setColorFilter(ContextCompat.getColor(this, R.color.black));
            binding.tvHome.setTextColor(ContextCompat.getColor(this, R.color.black));
            binding.tvWork.setTextColor(ContextCompat.getColor(this, R.color.black));
            binding.tvOther.setTextColor(ContextCompat.getColor(this, R.color.white));
        }
    }


    private void getAeraByCity() {
        apiCalling.getAeraByCity(selectedCity.getId(), new ApiCalling.OnAreaGetListner() {
            @Override
            public void onFetched(List<AeraRoot.DataItem> data) {
                areas.addAll(data);
                if (selectedArea == null && address != null) {
                    for (int i = 0; i < areas.size(); i++) {
                        if (areas.get(i).getId() == address.getArea().getId()) {
                            selectedArea = areas.get(i);
                        }
                    }

                }
            }

            @Override
            public void onFail() {

            }
        });
    }

    private void redioListnear() {

    }

    private void uploadData() {
        String fName = binding.etFName.getText().toString();

        String mobile = binding.etMobile.getText().toString();
        String mobile2 = binding.etMobile2.getText().toString();
        String addressLine = binding.etAddressLine.getText().toString();

        String landmark = binding.etLandmark.getText().toString();
        String city = binding.etCity.getText().toString();
        String aera = binding.etAera.getText().toString();

        String pincode = binding.etPin.getText().toString();

        if (fName.equals("")) {
            showToast(getString(R.string.firstname));

            return;
        }

        if (mobile.equals("")) {
            showToast(getString(R.string.selectmbile));

            return;
        } else if (mobile.length() < 10) {
            showToast(getString(R.string.entervalidmobilenumber));

            return;
        }
        if (!mobile2.isEmpty() && mobile2.length() < 10) {
            showToast(getString(R.string.entervalidmobilenumber));
            return;
        }


        if (addressLine.equals("")) {
            showToast(getString(R.string.selectaptament));

            return;
        }

        if (landmark.equals("")) {
            showToast(getString(R.string.enterlandark));

            return;
        }
        if (city.equals(getString(R.string.city))) {
            showToast(getString(R.string.selectcity));

            return;
        }
        if (aera.equals(getString(R.string.aera))) {
            showToast(getString(R.string.selectarea));

            return;
        }

        if (pincode.equals("")) {
            showToast(getString(R.string.enterpincode));

            return;
        }

        int isDefult = checkDefault();

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", String.valueOf(sessionManager.getUser().getId()));
        map.put("fullname", fName);
        //  map.put("lastname", lName);
        map.put("number", mobile);
        if (!mobile2.equals("")) {
            map.put("alt_number", mobile2);
        }
        // map.put("home_number", houseno);
        //  map.put("street", street);
        map.put("address", addressLine);

        map.put("landmark", landmark);
        map.put("city_id", String.valueOf(selectedCity.getId()));
        map.put("area_id", String.valueOf(selectedArea.getId()));
        map.put("pincode", pincode);
        map.put("address_type", String.valueOf(addressType));
        map.put("is_default", String.valueOf(isDefult));

        map.put("latitude", String.valueOf(latitude));
        map.put("longitude", String.valueOf(longitude));

        if (isUpdet) {
            map.put("id", String.valueOf(deleveryAddressId));
            disposable.add(RetrofitBuilder.create()
                    .updateAddress(Const.DEVKEY, sessionManager.getUser().getToken(), map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable1 -> {
                        binding.pd.setVisibility(View.VISIBLE);
                    })

                    .doOnDispose(() -> {

                    })
                    .subscribe((addressRoot, throwable) -> {
                        binding.pd.setVisibility(View.GONE);
                        if (addressRoot != null) {
                            if (addressRoot.isStatus()) {
                                Toast.makeText(this, "Address Updated Successfully", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            } else {
                                Toast.makeText(AddAddressActivity.this, addressRoot.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            try {
                                Log.d("TAG", "uploadData: er " + throwable.getMessage());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    })
            );

        } else {
            disposable.add(RetrofitBuilder.create()
                    .addAddress(Const.DEVKEY, sessionManager.getUser().getToken(), map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable1 -> {
                        binding.pd.setVisibility(View.VISIBLE);
                    })

                    .doOnDispose(() -> {

                    })
                    .subscribe((addressRoot, throwable) -> {
                        binding.pd.setVisibility(View.GONE);
                        if (addressRoot != null) {
                            if (addressRoot.isStatus()) {
                                Toast.makeText(this, "Address Added Successfully", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            } else {
                                Toast.makeText(AddAddressActivity.this, addressRoot.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            try {
                                Log.d("TAG", "uploadData: er " + throwable.getMessage());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    })
            );

        }


    }

    private void showToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    public void onClickBack(View view) {
        onBackPressed();
    }


    private int checkDefault() {
        if (binding.chkAddressDefult.isChecked()) {
            return 1;
        } else {
            return 0;
        }

    }
}