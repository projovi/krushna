package com.krushna.veginew.activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.krushna.veginew.R;
import com.krushna.veginew.adapters.MyOrderAdapter;
import com.krushna.veginew.databinding.ActivityMyOrdersBinding;
import com.krushna.veginew.models.OrderDetail;
import com.krushna.veginew.popups.ConfirmationPopup;
import com.krushna.veginew.retrofit.Const;
import com.krushna.veginew.retrofit.RetrofitBuilder;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MyOrdersActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    ActivityMyOrdersBinding binding;
    CompositeDisposable disposable = new CompositeDisposable();
    MyOrderAdapter myOrderAdapter = new MyOrderAdapter();
    private int start = 0;
    private boolean isLoding = false;
    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_orders);

        binding.rvOrders.setAdapter(myOrderAdapter);
        binding.shimmer.setVisibility(View.VISIBLE);
        getData();
        initListnears();
        binding.swipe.setOnRefreshListener(this);
    }

    private void initListnears() {
        binding.rvOrders.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!binding.rvOrders.canScrollVertically(1)) {
                    LinearLayoutManager manager = (LinearLayoutManager) binding.rvOrders.getLayoutManager();
                    Log.d("TAG", "onScrollStateChanged: ");

                    int visibleItemcount = manager.getChildCount();
                    int totalitem = manager.getItemCount();
                    int firstvisibleitempos = manager.findFirstCompletelyVisibleItemPosition();

                    Log.d("TAG", "onScrollStateChanged:187   " + visibleItemcount);
                    Log.d("TAG", "onScrollStateChanged:188 " + totalitem);

                    if (!isLoding && (visibleItemcount + firstvisibleitempos >= totalitem) && firstvisibleitempos >= 0) {
                        isLoding = true;
                        start = start + Const.LIMIT;
                        Log.d("TAG", "onScrollStateChanged: search " + start);
                        //   binding.pd2.setVisibility(View.VISIBLE);
                        getData();
                    }
                }
            }
        });

    }

    private void getData() {

        disposable.add(RetrofitBuilder.create().getMyOrders(Const.DEVKEY, getToken(), start, Const.LIMIT).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> {

                })
                .doOnDispose(() -> {

                })
                .subscribe((myordersRoot, throwable) -> {
                    binding.shimmer.setVisibility(View.GONE);
                    binding.swipe.setRefreshing(false);
                    //     Log.d("TAG", "getData: err "+throwable.getMessage());
                    if (myordersRoot != null && myordersRoot.isStatus() && !myordersRoot.getData().isEmpty()) {
                        myOrderAdapter.addData(myordersRoot.getData());

                    } else if (start == 0) {
                        binding.lyt404.setVisibility(View.VISIBLE);
                    }
                }));

        myOrderAdapter.setOnOrderClickListnear(new MyOrderAdapter.OnOrderClickListnear() {
            @Override
            public void onClickOpen(OrderDetail order) {
                startActivity(new Intent(MyOrdersActivity.this, OrderDetailActivity.class).putExtra("orderId", order.getOrderId()));

            }

            @Override
            public void onClickCancel(OrderDetail order) {
                openCancelSheet(order);
            }
        });
    }

    private void openCancelSheet(OrderDetail order1) {
        new ConfirmationPopup(this, "Cancel this order ?", "Do you really want to delete this order? \nHope you know that this action can't be undone.", new ConfirmationPopup.OnPopupClickListner() {
            @Override
            public void onPositive() {
                cancelOrder(order1);
            }

            @Override
            public void onNegative() {

            }
        }, "Yes", "No");

    }

    private void cancelOrder(OrderDetail order1) {
        disposable.add(RetrofitBuilder.create().cancelOrder(Const.DEVKEY, getToken(), order1.getOrderId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> {
                    binding.pBar.setVisibility(View.VISIBLE);
                })
                .doOnDispose(() -> {

                })
                .subscribe((restResponse, throwable) -> {
                    binding.pBar.setVisibility(View.GONE);

                    //     Log.d("TAG", "getData: err "+throwable.getMessage());
                    if (restResponse != null && restResponse.isStatus()) {
                        Toast.makeText(this, "Order Cancel  Successfully", Toast.LENGTH_SHORT).show();

                        onRefresh();
                        //  addressAdapter.delete(datum);

                    } else {
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }

                }));

    }

    public void onClickBack(View view) {
        onBackPressed();
    }

    @Override
    public void onRefresh() {
        myOrderAdapter.clear();
        start = 0;
        binding.shimmer.setVisibility(View.VISIBLE);
        getData();
    }
}