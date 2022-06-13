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

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.krushna.veginew.R;
import com.krushna.veginew.adapters.ComplainAdapter;
import com.krushna.veginew.databinding.BottomsheetHaveAnIssueBinding;
import com.krushna.veginew.databinding.FragmentComplainBinding;
import com.krushna.veginew.retrofit.Const;
import com.krushna.veginew.retrofit.RetrofitBuilder;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class ComplainFragment extends BaseFragment {


    CompositeDisposable disposable = new CompositeDisposable();
    BottomsheetHaveAnIssueBinding bottomsheetHaveAnIssueBinding;
    FragmentComplainBinding binding;
    private BottomSheetDialog bottomSheetDialog;
    private int type;
    private ComplainAdapter complainAdapter;
    private int start = 0;
    private boolean isLoding = false;

    public ComplainFragment(int type) {
        // Required empty public constructor
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_complain, container, false);


        complainAdapter = new ComplainAdapter(type);
        binding.rvComplain.setAdapter(complainAdapter);
        binding.shimmer.setVisibility(View.VISIBLE);
        getMyComplains();

        initListnear();
        return binding.getRoot();

    }

    private void initListnear() {
        binding.rvComplain.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!binding.rvComplain.canScrollVertically(1)) {
                    LinearLayoutManager manager = (LinearLayoutManager) binding.rvComplain.getLayoutManager();
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
                        getMyComplains();
                    }
                }
            }
        });

    }

    private void getMyComplains() {
        binding.lyt404.setVisibility(View.GONE);
        disposable.add(RetrofitBuilder.create().getMyComplains(Const.DEVKEY, getToken(), type, start, Const.LIMIT).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> {

                })
                .doOnDispose(() -> {

                })
                .subscribe((complainRoot, throwable) -> {
                    binding.shimmer.setVisibility(View.GONE);

                    //     Log.d("TAG", "getData: err "+throwable.getMessage());
                    if (complainRoot != null && complainRoot.isStatus() && !complainRoot.getData().isEmpty()) {
                        isLoding = false;
                        complainAdapter.addData(complainRoot.getData());

                    } else if (start == 0) {
                        binding.lyt404.setVisibility(View.VISIBLE);
                    }
                }));

    }

}