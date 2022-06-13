package com.krushna.veginew.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.krushna.veginew.R;
import com.krushna.veginew.adapters.CategoryGridAdapter;
import com.krushna.veginew.databinding.FragmentCategoryBinding;
import com.krushna.veginew.models.CategoryRoot;
import com.krushna.veginew.retrofit.Const;
import com.krushna.veginew.retrofit.RetrofitBuilder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class CategoryFragment extends Fragment {

    FragmentCategoryBinding binding;
    CategoryGridAdapter categoryAdapter = new CategoryGridAdapter();
    private CompositeDisposable disposable = new CompositeDisposable();
    private List<CategoryRoot.DataItem> data = new ArrayList<>();

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false);

        getData();
        initListnear();
        return binding.getRoot();
    }

    private void initListnear() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.btnSearch.setOnClickListener(v -> {
            searchData(binding.etSearch.getText().toString());
        });
    }

    private void searchData(String toString) {
        binding.lyt404.setVisibility(View.GONE);
        if (toString.equals("")) {
            initView();
            Log.d("TAG", "searchData: a  " + toString);
            return;
        }
        List<CategoryRoot.DataItem> tempList = new ArrayList<>();
        Log.d("TAG", "searchData: sizw " + data.size());
        for (CategoryRoot.DataItem category : data) {
            Log.d("TAG", "searchData: " + toString);
            if (category.getTitle().toLowerCase().contains(toString.toLowerCase())) {
                Log.d("TAG", "searchData: matched");
                tempList.add(category);
            }
        }
        Log.d("TAG", "searchData: temp size " + tempList.size());
        //  categoryAdapter.clear();
        categoryAdapter.addData(tempList);
        Log.d("TAG", "searchData: sizw2 " + data.size());
        if (tempList.isEmpty()) {
            binding.lyt404.setVisibility(View.VISIBLE);
        }
    }

    public void getData() {


        disposable.add(RetrofitBuilder.create().getCategories(Const.DEVKEY).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> {
                    binding.shimmer.setVisibility(View.VISIBLE);
                    binding.rvcategoryr.setVisibility(View.GONE);
                })
                .doOnDispose(() -> {

                })
                .subscribe((categoryRoot, throwable) -> {
                    binding.shimmer.setVisibility(View.GONE);
                    binding.rvcategoryr.setVisibility(View.VISIBLE);
                    //     Log.d("TAG", "getData: err "+throwable.getMessage());
                    if (categoryRoot != null && categoryRoot.isStatus()) {
                        data = categoryRoot.getData();
                        if (data != null)
                            initView();
                    }
                }));

    }

    private void initView() {
        //   categoryAdapter.clear();
        categoryAdapter.addData(data);
        binding.rvcategoryr.setAdapter(categoryAdapter);
    }
}