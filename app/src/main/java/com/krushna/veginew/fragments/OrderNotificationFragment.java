package com.krushna.veginew.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.krushna.veginew.R;
import com.krushna.veginew.adapters.OrderNotificatioaAdapter;
import com.krushna.veginew.databinding.FragmentNotificationBinding;
import com.krushna.veginew.retrofit.Const;
import com.krushna.veginew.retrofit.RetrofitBuilder;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class OrderNotificationFragment extends BaseFragment {


    CompositeDisposable disposable = new CompositeDisposable();
    FragmentNotificationBinding binding;

    OrderNotificatioaAdapter orderNotificatioaAdapter = new OrderNotificatioaAdapter();
    private int start = 0;
    private boolean isLoding = false;

    public OrderNotificationFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false);


        binding.shimmer.setVisibility(View.VISIBLE);
        getOrderNotificationsData();

        binding.rvNotification.setAdapter(orderNotificatioaAdapter);
        initListnear();
        return binding.getRoot();
    }

    private void initListnear() {
        binding.rvNotification.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!binding.rvNotification.canScrollVertically(1)) {
                    LinearLayoutManager manager = (LinearLayoutManager) binding.rvNotification.getLayoutManager();
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
                        getOrderNotificationsData();

                    }
                }
            }
        });

    }


    private void getOrderNotificationsData() {

        disposable.add(RetrofitBuilder.create().getOrderNotification(Const.DEVKEY, getToken(), start, Const.LIMIT).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> {

                })
                .doOnDispose(() -> {

                })
                .subscribe((notificationRoot, throwable) -> {
                    binding.shimmer.setVisibility(View.GONE);

                    //     Log.d("TAG", "getData: err "+throwable.getMessage());
                    if (notificationRoot != null && notificationRoot.isStatus() && !notificationRoot.getData().isEmpty()) {
                        orderNotificatioaAdapter.addData(notificationRoot.getData());
                        isLoding = false;
                    } else if (start == 0) {
                        binding.lyt404.setVisibility(View.VISIBLE);
                    }
                }));

    }

}