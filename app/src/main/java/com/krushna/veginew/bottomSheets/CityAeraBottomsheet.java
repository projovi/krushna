package com.krushna.veginew.bottomSheets;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.krushna.veginew.R;
import com.krushna.veginew.adapters.AreaAdapter;
import com.krushna.veginew.adapters.CityAdapter;
import com.krushna.veginew.databinding.BottomSheetAddressBinding;
import com.krushna.veginew.models.AeraRoot;
import com.krushna.veginew.models.CityRoot;

import java.util.ArrayList;
import java.util.List;

public class CityAeraBottomsheet {
    BottomSheetDialog bottomSheetDialog;
    BottomSheetAddressBinding addressBinding;
    private Context context;

    public CityAeraBottomsheet(Context context) {
        this.context = context;
        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;
            FrameLayout bottomSheet = (FrameLayout) d.findViewById(R.id.design_bottom_sheet);
            BottomSheetBehavior.from(bottomSheet)
                    .setState(BottomSheetBehavior.STATE_EXPANDED);
        });
        addressBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.bottom_sheet_address, null, false);
        bottomSheetDialog.setContentView(addressBinding.getRoot());
        addressBinding.tvCancel.setOnClickListener(view -> bottomSheetDialog.dismiss());
    }

    public void showCities(List<CityRoot.DataItem> cites, OnCityClickLisner onCityClickLisner) {
        Log.d("TAG", "showCities: ");
        CityAdapter cityAdapter = new CityAdapter(cites, city1 -> {
            onCityClickLisner.onCityClick(city1);
            bottomSheetDialog.dismiss();
        });
        addressBinding.rvNames.setAdapter(cityAdapter);
        addressBinding.tvCity.setText("Select City");

        bottomSheetDialog.show();

        addressBinding.etSearch.setText("");
        addressBinding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //ll
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<CityRoot.DataItem> newCities = new ArrayList<>();
                for (CityRoot.DataItem city1 : cites) {
                    if (city1.getName() != null && (city1.getName().contains(s.toString().toUpperCase()) || city1.getName().contains(s.toString().toLowerCase()))) {

                        newCities.add(city1);
                    }
                    //something here
                }
                if (!newCities.isEmpty()) {

                    CityAdapter cityAdapter = new CityAdapter(newCities, city1 -> {

                        onCityClickLisner.onCityClick(city1);
                        bottomSheetDialog.dismiss();
                    });
                    addressBinding.rvNames.setAdapter(cityAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //ll
            }
        });
    }

    public void showAreas(List<AeraRoot.DataItem> areas, OnAreaClickLisner onAreaClickLisner) {
        Log.d("TAG", "showAreas: ");
        AreaAdapter cityAdapter = new AreaAdapter(areas, aera -> {
            onAreaClickLisner.onCityClick(aera);
            bottomSheetDialog.dismiss();
        });
        addressBinding.rvNames.setAdapter(cityAdapter);
        addressBinding.tvCity.setText("Select Area");

        bottomSheetDialog.show();

        addressBinding.etSearch.setText("");
        addressBinding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //ll
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<AeraRoot.DataItem> newAreas = new ArrayList<>();
                for (AeraRoot.DataItem area : areas) {
                    if (area.getName() != null && (area.getName().contains(s.toString().toUpperCase()) || area.getName().contains(s.toString().toLowerCase()))) {

                        newAreas.add(area);
                    }
                    //something here
                }
                if (!newAreas.isEmpty()) {

                    AreaAdapter cityAdapter = new AreaAdapter(areas, aera -> {
                        onAreaClickLisner.onCityClick(aera);
                        bottomSheetDialog.dismiss();
                    });
                    addressBinding.rvNames.setAdapter(cityAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //ll
            }
        });
    }

    public interface OnCityClickLisner {
        void onCityClick(CityRoot.DataItem city1);
    }


    public interface OnAreaClickLisner {
        void onCityClick(AeraRoot.DataItem city1);
    }

}
