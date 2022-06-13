package com.krushna.veginew.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.krushna.veginew.R;
import com.krushna.veginew.activites.MainActivity;
import com.krushna.veginew.adapters.CategoryAdapter;
import com.krushna.veginew.adapters.DotAdapter;
import com.krushna.veginew.adapters.ProductMainAdapter;
import com.krushna.veginew.adapters.SliderAdapterExample;
import com.krushna.veginew.databinding.FragmentHomeBinding;
import com.krushna.veginew.models.HomePageRoot;
import com.krushna.veginew.retrofit.Const;
import com.krushna.veginew.retrofit.RetrofitBuilder;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class HomeFragment extends Fragment {


    FragmentHomeBinding binding;
    CompositeDisposable disposable = new CompositeDisposable();
    private SliderAdapterExample sliderAdapterExample;

    public HomeFragment() {
        // Required empty public constructor
    }

    private HomePageRoot.Data data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        getData();


        return binding.getRoot();
    }

    ProductMainAdapter productMainAdapter = new ProductMainAdapter();

    private void getData() {
        disposable.add(RetrofitBuilder.create().getHomePageData(Const.DEVKEY).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> {

                    binding.scrollview.setVisibility(View.GONE);
                    binding.shimmer.setVisibility(View.VISIBLE);
                })
                .doOnDispose(() -> {

                })
                .subscribe((homePageRoot, throwable) -> {
                    binding.scrollview.setVisibility(View.VISIBLE);
                    binding.shimmer.setVisibility(View.GONE);
                    //     Log.d("TAG", "getData: err "+throwable.getMessage());
                    if (homePageRoot != null && homePageRoot.isStatus()) {
                        data = homePageRoot.getData();
                        if (data != null)
                            initView();
                    }
                }));
    }

    @Override
    public void onResume() {
        super.onResume();
        productMainAdapter.notifyDataSetChanged();
    }

    private void initView() {

        if (data.getBanner() != null) {
            sliderAdapterExample = new SliderAdapterExample(data.getBanner());
            binding.imageSlider.setAdapter(sliderAdapterExample);
            setupLogicAutoSlider();
        }
        if (data.getCategory() != null) {
            binding.rvcategoryr.setAdapter(new CategoryAdapter(data.getCategory()));
        }
        if (data.getCategoryWithProduct() != null) {
            productMainAdapter.addData(data.getCategoryWithProduct());
            binding.rvMain.setAdapter(productMainAdapter);
        }

        binding.scrollview.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (v.canScrollVertically(1)) {
                Log.d("TAG", "onScrollChange:  yesss 1111111");
            } else {
                Log.d("TAG", "onScrollChange: nooo  11111");
            }

            if (scrollY > oldScrollY) {
                Log.d("TAG", "Scroll DOWN");

                ((MainActivity) getActivity()).showBottomMenu(true);
            }
            if (scrollY < oldScrollY) {
                Log.d("TAG", "Scroll UP");
                ((MainActivity) getActivity()).showBottomMenu(false);
            }
        });
    }

    private void setupLogicAutoSlider() {

        DotAdapter dotAdapter = new DotAdapter(data.getBanner().size());
        binding.rvdots.setAdapter(dotAdapter);
        binding.imageSlider.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager myLayoutManager = (LinearLayoutManager) binding.imageSlider.getLayoutManager();
                int scrollPosition = myLayoutManager.findFirstVisibleItemPosition();
                dotAdapter.changeDot(scrollPosition);
            }
        });


        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int pos = 0;
            boolean flag = true;

            @Override
            public void run() {

                if (pos == sliderAdapterExample.getItemCount() - 1) {
                    flag = false;
                } else if (pos == 0) {
                    flag = true;
                }
                if (flag) {
                    pos++;
                } else {
                    pos--;
                }

                binding.imageSlider.smoothScrollToPosition(pos);
                handler.postDelayed(this, 2000);

            }
        };

        handler.postDelayed(runnable, 2000);



       /* final int duration = 10;
        final int pixelsToMove = 30;
         final Handler mHandler = new Handler(Looper.getMainLooper());
         final Runnable SCROLLING_RUNNABLE = new Runnable() {

            @Override
            public void run() {

                if (((LinearLayoutManager)binding.imageSlider.getLayoutManager()).findLastCompletelyVisibleItemPosition()<sliderAdapterExample.getItemCount()-1){
                    binding.imageSlider.smoothScrollToPosition(((LinearLayoutManager)binding.imageSlider.getLayoutManager()).findFirstVisibleItemPosition()+1);
                  //  sliderAdapterExample.moveObj();
                }else if (((LinearLayoutManager)binding.imageSlider.getLayoutManager()).findLastCompletelyVisibleItemPosition()==sliderAdapterExample.getItemCount()-1){

                   // binding.imageSlider.smoothScrollToPosition(0);
                }
                mHandler.postDelayed(this, 2000);
            }
        };
        binding.imageSlider.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItem = ((LinearLayoutManager)binding.imageSlider.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                if(lastItem == ((LinearLayoutManager)binding.imageSlider.getLayoutManager()).getItemCount()-1){
                    mHandler.removeCallbacks(SCROLLING_RUNNABLE);
                    Handler postHandler = new Handler();
                    postHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(null);
                            sliderAdapterExample = new SliderAdapterExample(data.getBanner());
                            recyclerView.setAdapter(sliderAdapterExample);
                            mHandler.postDelayed(SCROLLING_RUNNABLE, 2000);
                        }
                    }, 2000);
                }
            }
        });
        mHandler.postDelayed(SCROLLING_RUNNABLE, 2000);*/
    }

}