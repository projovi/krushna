package com.krushna.veginew.activites;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.krushna.veginew.R;
import com.krushna.veginew.adapters.RatingAdapter;
import com.krushna.veginew.databinding.ActivityRatingAndReivewBinding;
import com.krushna.veginew.retrofit.Const;
import com.krushna.veginew.retrofit.RetrofitBuilder;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class RatingAndReivewActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    ActivityRatingAndReivewBinding binding;
    CompositeDisposable disposable = new CompositeDisposable();
    RatingAdapter ratingAdapter = new RatingAdapter();
    private int start = 0;
    private boolean isLoding = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rating_and_reivew);
        binding.swipe.setRefreshing(false);
        binding.rvrating.setAdapter(ratingAdapter);

        binding.shimmer.setVisibility(View.VISIBLE);
        getRatings();
        initListnear();
        binding.swipe.setOnRefreshListener(this);
    }

    private void initListnear() {
        binding.rvrating.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!binding.rvrating.canScrollVertically(1)) {
                    LinearLayoutManager manager = (LinearLayoutManager) binding.rvrating.getLayoutManager();
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
                        getRatings();
                    }
                }
            }
        });

    }

    private void getRatings() {
        disposable.add(RetrofitBuilder.create().getAllRating(Const.DEVKEY, getToken(), start, Const.LIMIT).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> {

                })
                .doOnDispose(() -> {

                }).doOnSuccess(ratingReviewListRoot -> {

                })
                .subscribe((ratingRoot, throwable) -> {
                    binding.shimmer.setVisibility(View.GONE);
                    binding.swipe.setRefreshing(false);
                    //     Log.d("TAG", "getData: err "+throwable.getMessage());
                    if (ratingRoot != null && ratingRoot.isStatus() && !ratingRoot.getData().isEmpty()) {
                        ratingAdapter.addData(ratingRoot.getData());
                    } else if (start == 0) {
                        binding.lyt404.setVisibility(View.VISIBLE);
                    }
                }));

    }

    public void onClickBack(View view) {
        onBackPressed();
    }

    @Override
    public void onRefresh() {
        start = 0;
        ratingAdapter.clear();
        binding.shimmer.setVisibility(View.VISIBLE);
        getRatings();
    }
}