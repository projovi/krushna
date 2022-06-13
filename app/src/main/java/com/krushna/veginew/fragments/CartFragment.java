package com.krushna.veginew.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.room.Room;

import com.krushna.veginew.R;
import com.krushna.veginew.activites.BaseActivity;
import com.krushna.veginew.activites.DeliveryOptionsActivity;
import com.krushna.veginew.activites.MainActivity;
import com.krushna.veginew.adapters.CartAdapter;
import com.krushna.veginew.dao.AppDatabase;
import com.krushna.veginew.dao.CartOffline;
import com.krushna.veginew.databinding.FragmentCartBinding;
import com.krushna.veginew.interfaces.LoginListnraer;
import com.krushna.veginew.models.UserRoot;
import com.krushna.veginew.popups.ConfirmationPopup;
import com.krushna.veginew.retrofit.Const;
import com.krushna.veginew.retrofit.RetrofitBuilder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class CartFragment extends BaseFragment {


    FragmentCartBinding binding;
    CartAdapter cartAdapter = new CartAdapter();
    private AppDatabase db;
    private double totalPrice = 0;
    CompositeDisposable disposable = new CompositeDisposable();
    List<String> product_ids = new ArrayList<>();

    public CartFragment() {
        // Required empty public constructor
    }

    private List<CartOffline> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false);
        db = Room.databaseBuilder(requireActivity(),
                AppDatabase.class, Const.DB_NAME).allowMainThreadQueries().build();
        cartAdapter.initAdapter(getActivity());
        getCartData();
        initView();
        return binding.getRoot();
    }

    private void getCartData() {
        list.clear();
        list = db.cartDao().getall();
        cartAdapter.addData(list);
        cartAdapter.setOnCartClickListnear(new CartAdapter.OnCartClickListnear() {
            @Override
            public void onNotify() {
                calculateTotal();
            }

            @Override
            public void onDelete(CartOffline product, int pos) {
                new ConfirmationPopup(getActivity(), getString(R.string.deleteproduct), getString(R.string.deleteconfirlmsg), new ConfirmationPopup.OnPopupClickListner() {
                    @Override
                    public void onPositive() {
                        deleteProduct(product, pos);
                    }

                    @Override
                    public void onNegative() {

                    }
                }, "OK", "Cancel");


            }
        });
        calculateTotal();
        if (list.isEmpty()) {
            binding.lyt404.setVisibility(View.VISIBLE);
        }
    }

    private void deleteProduct(CartOffline product, int pos) {


        AppDatabase db = Room.databaseBuilder(getContext(),
                AppDatabase.class, Const.DB_NAME).allowMainThreadQueries().build();
        db.cartDao().deleteObjbyPid(product.getPriceUnitId());
        Log.d("TAG", "deleteProduct: " + product.getName());
        cartAdapter.removeItem(pos);
        list.remove(product);
        calculateTotal();
        if (list.isEmpty()) {
            binding.lyt404.setVisibility(View.VISIBLE);
        }
    }

    private void calculateTotal() {
        Log.d("TAG", "calculateTotal: ");
        totalPrice = 0;
        for (CartOffline product : db.cartDao().getall()) {

            product_ids.add(String.valueOf(product.getPid()));

            double p = Double.parseDouble(product.getPrice());
            long quantity = product.getQuantity();
            double price = p * quantity;
            Log.d("TAG", "calculateTotal: forrr " + price + "  qq " + quantity);
            totalPrice = (totalPrice + price);
        }


        binding.tvTotalAmount.setText(Const.getCurrency() + new DecimalFormat("###.##").format(totalPrice));

    }

    private void initView() {
        binding.rvCart.setAdapter(cartAdapter);
        binding.tvCheckout.setOnClickListener(v -> {
            if (list.isEmpty()) {
                Toast.makeText(context, "Your Cart Is Empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {


                checkProductAvaiblity();

            } else {
                if (getActivity() != null) {

                    ((BaseActivity) getActivity()).openLoginSheet(new LoginListnraer() {
                        @Override
                        public void onLoginSuccess(UserRoot.User u) {

                        }

                        @Override
                        public void onFailure() {

                        }

                        @Override
                        public void onDismiss() {
                            if (getActivity() == null) return;
                            ((MainActivity) getActivity()).binding.menuhome.performClick();    // todo
                        }
                    });
                }

            }
        });
    }

    private void checkProductAvaiblity() {
        disposable.add(RetrofitBuilder.create().checkProduct(Const.DEVKEY, getToken(), product_ids).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> {
                    binding.pd.setVisibility(View.VISIBLE);
                })
                .doAfterTerminate(() -> binding.pd.setVisibility(View.GONE))
                .subscribe((restResponse, throwable) -> {
                    binding.pd.setVisibility(View.GONE);
                    //     Log.d("TAG", "getData: err "+throwable.getMessage());
                    if (restResponse != null && restResponse.isStatus()) {
                        startActivity(new Intent(getActivity(), DeliveryOptionsActivity.class));

                    } else {
                        if (restResponse != null) {
                            Toast.makeText(getActivity(), restResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }));

    }
}