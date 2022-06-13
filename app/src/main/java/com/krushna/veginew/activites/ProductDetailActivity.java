package com.krushna.veginew.activites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.krushna.veginew.R;
import com.krushna.veginew.adapters.DotAdapter;
import com.krushna.veginew.adapters.ImageAdapter;
import com.krushna.veginew.adapters.VarientAdapter;
import com.krushna.veginew.databinding.ActivityProductDetailBinding;
import com.krushna.veginew.models.ProductItem;
import com.krushna.veginew.retrofit.Const;
import com.krushna.veginew.retrofit.RetrofitBuilder;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ProductDetailActivity extends BaseActivity {
    ActivityProductDetailBinding binding;
    CompositeDisposable disposable = new CompositeDisposable();
    private ProductItem data;
    private int lines = 0;

    public static void open(Context context, String pid) {
        context.startActivity(new Intent(context, ProductDetailActivity.class).putExtra(Const.P_ID, pid));
    }

    VarientAdapter varientAdapter = new VarientAdapter();

    private void getProductData(String productId) {
        binding.shimmer.setVisibility(View.VISIBLE);
        binding.lyt1.setVisibility(View.GONE);
        disposable.add(RetrofitBuilder.create().getProductDetails(Const.DEVKEY, productId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe((productRoot, throwable) -> {
                    binding.shimmer.setVisibility(View.GONE);
                    binding.lyt1.setVisibility(View.VISIBLE);
                    //     Log.d("TAG", "getData: err "+throwable.getMessage());
                    if (productRoot != null && productRoot.isStatus()) {
                        data = productRoot.getData();
                        if (data != null)
                            initView();
                    }
                }));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail);

        Intent intent = getIntent();
        String productId = intent.getStringExtra(Const.P_ID);
        if (productId != null && !productId.isEmpty()) {
            getProductData(productId);
        }
        binding.lytGotoCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));

    }

    @Override
    protected void onResume() {
        super.onResume();
        varientAdapter.notifyDataSetChanged();
    }

    private void initView() {
        DotAdapter dotAdapter = new DotAdapter(data.getImages().size());
        binding.rvdots.setAdapter(dotAdapter);
        binding.rvphotos.setAdapter(new ImageAdapter(data.getImages()));
        varientAdapter.setData(data.getPrices(), data);
        binding.rvProductOption.setAdapter(varientAdapter);
        binding.tvProductBenefits.setText(Html.fromHtml(data.getDescription()));
        binding.tvName.setText(data.getName());
        Log.d("TAG", "initView: pname  " + data.getName());

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(binding.rvphotos);

        binding.rvphotos.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager myLayoutManager = (LinearLayoutManager) binding.rvphotos.getLayoutManager();
                int scrollPosition = myLayoutManager.findFirstVisibleItemPosition();
                dotAdapter.changeDot(scrollPosition);
            }
        });


        initWishlistLogic();
        binding.lytAddwishlist.setOnClickListener(v -> {
            sessionManager.toggleWishlist(data);
            initWishlistLogic();
        });

      /*   lines = binding.tvProductBenefits.getText().toString().split(System.getProperty("line.separator")).length;
        // int lines = binding.tvProductBenefits.getLineCount();
        Log.d("TAG", "setData: " + lines);
       */

        ViewTreeObserver vto = binding.tvProductBenefits.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                ViewTreeObserver obs = binding.tvProductBenefits.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                lines = binding.tvProductBenefits.getLineCount();


            }
        });

        if (lines >= 3) {
            binding.tvReadmore.setVisibility(View.VISIBLE);
            binding.tvProductBenefits.setMaxLines(3);
        } else {
            binding.tvReadmore.setVisibility(View.GONE);
        }

        binding.tvReadmore.setOnClickListener(v -> {


            if (binding.tvProductBenefits.getMaxLines() == 3) {
                binding.tvReadmore.setText(getString(R.string.readless));
                binding.tvProductBenefits.setMaxLines(20000);
            } else {
                binding.tvReadmore.setText(getString(R.string.read_more));
                binding.tvProductBenefits.setMaxLines(3);
            }

        });


    }

    private void initWishlistLogic() {
        List<ProductItem> list = sessionManager.getWishlist();
        binding.tvAddremoveToWishlist.setText("Add To Wishlist");
        binding.imgwishlist.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_heartborder));
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == data.getId()) {
                binding.tvAddremoveToWishlist.setText("Remove");
                binding.imgwishlist.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_heart));
            }
        }
     /*   if (list.contains(data)){
binding.imgwishlist.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_heart));
        }else {

        }*/


    }



    public void onClickBack(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}