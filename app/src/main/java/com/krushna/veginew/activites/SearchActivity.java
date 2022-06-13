package com.krushna.veginew.activites;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.krushna.veginew.R;
import com.krushna.veginew.adapters.SearchAdapter;
import com.krushna.veginew.databinding.ActivitySearchBinding;
import com.krushna.veginew.databinding.BottomsheetSortingBinding;
import com.krushna.veginew.models.ProductItem;
import com.krushna.veginew.retrofit.Const;
import com.krushna.veginew.retrofit.RetrofitBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    ActivitySearchBinding binding;
    CompositeDisposable disposable = new CompositeDisposable();
    SearchAdapter searchAdapter = new SearchAdapter();
    private int start = 0;
    private String keyword = "";
    private List<ProductItem> products = new ArrayList<>();
    private String categoryId;

    private BottomsheetSortingBinding sortingBinding;
    private int sortType = 1;
    private boolean isLoading = false;
    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        sortingBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.bottomsheet_sorting, null, false);
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(sortingBinding.getRoot());

        Intent intent = getIntent();
        categoryId = intent.getStringExtra(Const.STR_CID);
        String categoryName = intent.getStringExtra(Const.STR_CNAME);
        if (categoryName != null && !categoryName.isEmpty()) {
            binding.tvTitle.setText(categoryName);
        }
        searchAdapter.initAdapter(this);
        initView();
        initListnear();
        binding.swipe.setOnRefreshListener(this);
    }

    private void initListnear() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                initView();
            }
        });
        binding.btnSearch.setOnClickListener(v -> {
            initView();
        });


        binding.rvSearch.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!binding.rvSearch.canScrollVertically(1)) {
                    LinearLayoutManager manager = (LinearLayoutManager) binding.rvSearch.getLayoutManager();
                    Log.d("TAG", "onScrollStateChanged: ");

                    int visibleItemcount = manager.getChildCount();
                    int totalitem = manager.getItemCount();
                    int firstvisibleitempos = manager.findFirstCompletelyVisibleItemPosition();

                    Log.d("TAG", "onScrollStateChanged:187   " + visibleItemcount);
                    Log.d("TAG", "onScrollStateChanged:188 " + totalitem);

                    if (!isLoading && (visibleItemcount + firstvisibleitempos >= totalitem) && firstvisibleitempos >= 0) {
                        isLoading = true;
                        start = start + Const.LIMIT;
                        Log.d("TAG", "onScrollStateChanged: search " + start);
                        //   binding.pd2.setVisibility(View.VISIBLE);
                        getSearchData();
                    }
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        searchAdapter.notifyDataSetChanged();
    }

    private void initView() {
        start = 0;
        disposable.clear();
        searchAdapter.clear();
        keyword = binding.etSearch.getText().toString();
        binding.shimmer.setVisibility(View.VISIBLE);
        getSearchData();
        binding.rvSearch.setAdapter(searchAdapter);
    }

    @Override
    public void onRefresh() {
        initView();
    }

    private void getSearchData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("search_keyword", keyword);
        if (categoryId != null && !categoryId.isEmpty()) {

            map.put("category_id", categoryId);
        }
        if (sortType != 0) {
            map.put("sort_by", String.valueOf(sortType));
        }
        binding.lyt404.setVisibility(View.GONE);
        disposable.add(RetrofitBuilder.create().searchProduct(Const.DEVKEY, map, start, Const.LIMIT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((data, throwable) -> {
                    binding.shimmer.setVisibility(View.GONE);
                    binding.swipe.setRefreshing(false);
                    //     Log.d("TAG", "getData: err "+throwable.getMessage());
                    if (data != null && data.isStatus() && !data.getData().isEmpty()) {
                        isLoading = false;
                        products = data.getData();
                        if (!products.isEmpty())
                            searchAdapter.addData(products);
                    } else if (start == 0) {
                        binding.lyt404.setVisibility(View.VISIBLE);
                    }

                }));

    }

    public void onClickSort(View view) {

        bottomSheetDialog.show();
        if (sortType == 1) {
            sortingBinding.rb1.setChecked(true);
        } else if (sortType == 2) {
            sortingBinding.rb2.setChecked(true);
        } else {
            sortingBinding.rb3.setChecked(true);
        }

        sortingBinding.rb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortingBinding.rb1.setChecked(false);
                sortingBinding.rb2.setChecked(false);
                sortingBinding.rb3.setChecked(false);
                sortingBinding.rb1.setChecked(true);
                sortType = 1;
            }
        });
        sortingBinding.rb2.setOnClickListener(v -> {
            sortingBinding.rb1.setChecked(false);
            sortingBinding.rb2.setChecked(false);
            sortingBinding.rb3.setChecked(false);
            sortingBinding.rb2.setChecked(true);
            sortType = 2;
        });
        sortingBinding.rb3.setOnClickListener(v -> {
            sortingBinding.rb1.setChecked(false);
            sortingBinding.rb2.setChecked(false);
            sortingBinding.rb3.setChecked(false);
            sortingBinding.rb3.setChecked(true);
            sortType = 3;
        });


        sortingBinding.imgClose.setOnClickListener(view1 -> bottomSheetDialog.dismiss());
        sortingBinding.tvApply.setOnClickListener(v -> {

            initView();
            bottomSheetDialog.dismiss();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onClickBack(View view) {
        onBackPressed();
    }
}