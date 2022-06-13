package com.krushna.veginew.activites;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.stripe.Stripe.apiKey;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.room.Room;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.krushna.veginew.R;
import com.krushna.veginew.adapters.CouponAdapter;
import com.krushna.veginew.adapters.OrderItemsTextAdapter;
import com.krushna.veginew.dao.AppDatabase;
import com.krushna.veginew.dao.CartOffline;
import com.krushna.veginew.databinding.ActivityPaymentSummaryBinding;
import com.krushna.veginew.databinding.BottomsheetProductweightBinding;
import com.krushna.veginew.models.CouponRoot;
import com.krushna.veginew.models.DeliveryAddress;
import com.krushna.veginew.popups.PaymentAleartPopup;
import com.krushna.veginew.retrofit.Const;
import com.krushna.veginew.retrofit.RetrofitBuilder;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.model.StripeIntent.Status;
import com.stripe.android.view.CardInputWidget;
import com.stripe.param.PaymentIntentCreateParams;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class PaymentSummaryActivity extends BaseActivity {
    private static final String TAG = "paymentactivity";
    ActivityPaymentSummaryBinding binding;
    boolean isShow = false;
    String paymentName = "Cash on Delivery";
    String paymentId = "Cash on Delivery";
    BottomSheetDialog bottomSheetDialog;
    List<String> pids = new ArrayList<>();
    long discount = 0;
    String deliveryAddress = "";
    String userAddress = "";
    List<String> unitids = new ArrayList<>();
    List<String> quantities = new ArrayList<>();
    List<String> prices = new ArrayList<>();
    List<String> productnames = new ArrayList<>();
    List<String> images = new ArrayList<>();
    List<String> priceunits = new ArrayList<>();
    List<String> product_ids = new ArrayList<>();
    List<String> priceunitnames = new ArrayList<>();
    List<String> totalprices = new ArrayList<>();
    List<CartOffline> list = new ArrayList<>();
    CompositeDisposable disposable = new CompositeDisposable();
    private String token;
    private int paymentType = 1;
    private double totalamount = 0;
    private String paymentIntentClientSecret;
    private Stripe stripe;
    private String lang = "";
    private String lat = "";
    private String addressString = "";
    private double shippingCharge = 0;
    private String couponCode = "";
    private boolean couponSelected = false;
    private double subTotal;
    private DeliveryAddress address;
    private int couponDiscount = 0;
    private AppDatabase db;
    private int selectedCouponId = 0;
    private List<CouponRoot.DataItem> coupons = new ArrayList<>();

    public static void expand(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    private void radioButtonListnear() {
        binding.radioCash.setChecked(true);
        binding.radioCash.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.radioOnline.setChecked(false);
                binding.radioCash.setChecked(true);
                paymentType = 1;
                toggleCardUI();
            }
        });
        binding.radioOnline.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.radioCash.setChecked(false);
                binding.radioOnline.setChecked(true);
                paymentType = 2;
                toggleCardUI();
            }
        });
    }

    private void toggleCardUI() {
        if (paymentType == 1) {
            binding.lytCard.setVisibility(View.GONE);
            binding.btnPlaceOrder.setText(R.string.placeorder);
        } else {
            binding.lytCard.setVisibility(View.VISIBLE);
            binding.btnPlaceOrder.setText(R.string.checkout);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_summary);


        apiKey = Const.STRIPE_SECRET_KEY;

        stripe = new Stripe(
                getApplicationContext(),
                Objects.requireNonNull(Const.STRIPE_PUBLISHABLE_KEY)
        );


        Intent intent = getIntent();
        //  lat = intent.getStringExtra("lat");
        //  lang = intent.getStringExtra("lang");

        addressString = intent.getStringExtra("address");
        Log.d(TAG, "onCreate: address " + addressString);
        if (addressString != null && !addressString.equals("")) {
            DeliveryAddress tempAddress = new Gson().fromJson(addressString, DeliveryAddress.class);
            if (tempAddress != null) {
                address = tempAddress;
                setAddress();

            }
        }
        shippingCharge = Double.parseDouble(sessionManager.getSetting().getShippingcharge());
        initView();
        getCouponList();
        radioButtonListnear();
        initListnear();

    }

    private void setAddress() {
        binding.tvName.setText(address.getFirstname());
        userAddress = address.getAddress().concat(" " +
                address.getArea().getName() + " " + address.getCity().getName() + " " + address.getPincode());
        binding.tvAddress.setText(userAddress);

    }

    private void getCouponList() {
        disposable.add(RetrofitBuilder.create().getCoupons(Const.DEVKEY, getToken()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> {

                })
                .doOnDispose(() -> {

                })
                .subscribe((couponRoot, throwable) -> {

                    //     Log.d("TAG", "getData: err "+throwable.getMessage());
                    if (couponRoot != null && couponRoot.isStatus() && !couponRoot.getData().isEmpty()) {
                        coupons = couponRoot.getData();
                        binding.btnCoupon.setOnClickListener(v -> openCouponBottomSheet());
                    } else {

                    }
                }));

    }

    private void applyCoupon() {

        if (selectedCouponId == 0) {
            Toast.makeText(this, "Coupon Is Not Valid", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.pBar.setVisibility(View.VISIBLE);
        binding.etCoupon.setTextColor(ContextCompat.getColor(PaymentSummaryActivity.this, android.R.color.holo_green_light));
        binding.etCoupon.setText(couponCode);
        disposable.add(RetrofitBuilder.create().applyCoupon(Const.DEVKEY, getToken(), String.valueOf(selectedCouponId), String.valueOf(subTotal))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> {
                    binding.pBar.setVisibility(View.VISIBLE);
                })
                .doOnDispose(() -> {

                })
                .subscribe((couponRoot, throwable) -> {
                    binding.pBar.setVisibility(View.GONE);

                    //     Log.d("TAG", "getData: err "+throwable.getMessage());
                    if (couponRoot != null && couponRoot.isStatus()) {
                        subTotal = couponRoot.getSubtotal();
                        binding.tvDiscountPrice.setText(String.valueOf(couponRoot.getCoupanDiscount()));
                        setTotalPrice();
                        Log.d(TAG, "applyCoupon:  dissa " + couponRoot.getCoupanDiscount());


                    } else {

                    }
                    bottomSheetDialog.dismiss();
                }));


    }

    private void openCouponBottomSheet() {

        bottomSheetDialog = new BottomSheetDialog(this);
        BottomsheetProductweightBinding couopnSheet = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.bottomsheet_productweight, null, false);
        bottomSheetDialog.setContentView(couopnSheet.getRoot());
        couopnSheet.tvCoupon.setVisibility(View.VISIBLE);
        CouponAdapter couponAdapter = new CouponAdapter(coupons, subTotal, new CouponAdapter.OnCouponClickListnear() {
            @Override
            public void onCouponClick(CouponRoot.DataItem coupon) {
                Log.i(TAG, "onCouponClick: id  " + coupon.getId());
                couponSelected = true;
                selectedCouponId = coupon.getId();
                couponCode = coupon.getCouponCode();
                binding.etCoupon.setText(couponCode);
                bottomSheetDialog.dismiss();
                applyCoupon();

            }
        });
        couopnSheet.listProductWeight.setAdapter(couponAdapter);
        bottomSheetDialog.show();
        couopnSheet.tvCancel.setOnClickListener(v -> bottomSheetDialog.dismiss());

    }

    private void initView() {

        db = Room.databaseBuilder(this,
                AppDatabase.class, Const.DB_NAME).allowMainThreadQueries().build();
        list.clear();
        list = db.cartDao().getall();

        OrderItemsTextAdapter orderItemsTextAdapter = new OrderItemsTextAdapter(list);
        binding.rvOrdersItems.setAdapter(orderItemsTextAdapter);
        binding.tvOrderItems.setText("Order Items (".concat(String.valueOf(list.size())).concat(")"));


        for (int i = 0; i <= list.size() - 1; i++) {
            CartOffline product = list.get(i);
            Log.d(TAG, "uploadCart:forpids  " + product.getPid() + " qq " + product.getQuantity());
            product_ids.add(String.valueOf(product.getPid()));
            quantities.add(String.valueOf(product.getQuantity()));
            prices.add(String.valueOf(product.getPrice()));
            productnames.add(String.valueOf(product.getName()));
            images.add(String.valueOf(product.getImageUrl()));
            priceunits.add(String.valueOf(product.getPriceUnit()));
            priceunitnames.add(String.valueOf(product.getPriceUnitName()));

            double p = Double.parseDouble(product.getPrice());
            long quantity = product.getQuantity();
            double price = p * quantity;
            totalprices.add(String.valueOf(price));


        }
        setPrice();


        binding.lytCard.setVisibility(View.GONE);
        paymentType = 1;

        binding.cardInputWidget.setPostalCodeEnabled(false);
        binding.cardInputWidget.setPostalCodeRequired(false);

        binding.pBar.setVisibility(View.GONE);
    }


    private void initListnear() {

        binding.tvOrderItems.setOnClickListener(v -> {
            if (isShow) {
                binding.imgArrow.setRotation(0);
                binding.rvOrdersItems.setVisibility(View.GONE);
                isShow = false;
            } else {
                binding.imgArrow.setRotation(180);
                binding.rvOrdersItems.setVisibility(View.VISIBLE);
                isShow = true;
            }
        });
        binding.imgArrow.setOnClickListener(v -> {
            if (isShow) {
                binding.imgArrow.setRotation(0);

                isShow = false;
                binding.rvOrdersItems.setVisibility(View.GONE);
            } else {
                binding.imgArrow.setRotation(180);
                binding.rvOrdersItems.setVisibility(View.VISIBLE);
                isShow = true;
                expand(binding.rvOrdersItems);
            }
        });


        binding.btnPlaceOrder.setOnClickListener(v -> {
            disposable.add(RetrofitBuilder.create().checkProduct(Const.DEVKEY, getToken(), product_ids).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable1 -> {
                        binding.pBar.setVisibility(View.VISIBLE);
                    })
                    .doAfterTerminate(() -> binding.pBar.setVisibility(View.GONE))
                    .subscribe((restResponse, throwable) -> {
                        binding.pBar.setVisibility(View.GONE);
                        //     Log.d("TAG", "getData: err "+throwable.getMessage());
                        if (restResponse != null && restResponse.isStatus()) {
                            if (paymentType == 1) {
                                paymentName = Const.COD;
                                paymentId = Const.COD;
                                placeOrder();
                            } else {
                                PaymentMethodCreateParams params = binding.cardInputWidget.getPaymentMethodCreateParams();
                                if (params != null) {
                                    confirmPayment();
                                } else {
                                    Toast.makeText(this, getString(R.string.entercarddetails), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } else {
                            if (restResponse != null) {
                                Toast.makeText(PaymentSummaryActivity.this, restResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }));

        });
    }

    private void setPrice() {
        totalamount = 0;
        subTotal = 0;
        binding.tvShippnigCharge.setText(Const.getCurrency() + String.valueOf(shippingCharge));
        setSubTotal();
        setTotalPrice();
    }


    public void onClickBack(View view) {
        super.onBackPressed();
    }

    private void confirmPayment() {
//
//        binding.pBar.setVisibility(View.VISIBLE);
        binding.rltPayment.setVisibility(View.VISIBLE);
        new MyTask().execute();
        Log.d(TAG, "confirmPayment: ");


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));

    }

    private void displayAlert(@NonNull String title,
                              @Nullable String message, String amount, boolean b) {
        binding.pBar.setVisibility(View.GONE);
        new PaymentAleartPopup(this, title, message, amount, new PaymentAleartPopup.OnPopupClickListner() {
            @Override
            public void onPositive() {
                binding.rltPayment.setVisibility(View.GONE);
//                if (b) {
//                    placeOrder();
//                } else {
//                    Toast.makeText(PaymentSummaryActivity.this, R.string.somethingwentwrong, Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onNegative() {

            }
        }, "OK", "");


    }

    private void setTotalPrice() {
        totalamount = 0;
        totalamount = subTotal + shippingCharge;
        binding.tvSubtotalPrice.setText(Const.getCurrency() + String.valueOf(df.format(subTotal)));
        binding.tvtotalPrice.setText(Const.getCurrency() + String.valueOf(df.format(totalamount)));
        binding.tvTotalPrice.setText(Const.getCurrency() + String.valueOf(df.format(totalamount)));
    }

    private void setSubTotal() {
        subTotal = 0;
        for (int i = 0; i <= list.size() - 1; i++) {
            CartOffline product = list.get(i);
            //  long price = Long.parseLong(product.getPrice()) * product.getQuantity();
            double p = Double.parseDouble(product.getPrice());
            long quantity = product.getQuantity();
            double price = p * quantity;

            Log.d(TAG, "getTotalAmount: " + product.getPrice() + " * " + product.getQuantity());
            subTotal = subTotal + price;
        }
        binding.tvSubtotalPrice.setText(Const.getCurrency() + String.valueOf(df.format(subTotal)));

    }


    private void placeOrder() {
        if (list.isEmpty()) {
            Toast.makeText(this, getString(R.string.noorderfound), Toast.LENGTH_SHORT).show();
            return;
        }
        if (paymentType == 1)
            binding.pBar.setVisibility(View.VISIBLE);
        if (addressString.equals("")) {
            Toast.makeText(this, getString(R.string.somethingwentwrong), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        Log.d(TAG, "placeOrder: " + lat);
        Log.d(TAG, "placeOrder: " + lang);

        disposable.add(RetrofitBuilder.create().placeOrder(Const.DEVKEY, getToken(), quantities,
                String.valueOf(shippingCharge), String.valueOf(totalamount), String.valueOf(paymentType), paymentId, paymentName, String.valueOf(address.getId()),
                String.valueOf(couponDiscount), lat, lang,
                String.valueOf(subTotal), prices, productnames, images, priceunits, product_ids, priceunitnames, totalprices).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> {
                    if (paymentType == 1)
                        binding.pBar.setVisibility(View.VISIBLE);
                })
                .doAfterTerminate(() -> binding.pBar.setVisibility(View.GONE))
                .subscribe((restResponse, throwable) -> {

                    //     Log.d("TAG", "getData: err "+throwable.getMessage());
                    if (restResponse != null && restResponse.isStatus()) {
                        Toast.makeText(this, "Order Placed", Toast.LENGTH_SHORT).show();

                        List<CartOffline> offlineList = db.cartDao().getall();

                        for (CartOffline cart : offlineList) {
                            db.cartDao().deleteObjbyPid(cart.getPriceUnitId());
                        }
                        Intent intent = new Intent(PaymentSummaryActivity.this, MainActivity.class);
                        startActivity(intent);
                        finishAffinity();

                    } else {
                        if (restResponse != null) {
                            Toast.makeText(this, restResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }));

    }

    private class PaymentResultCallback
            implements ApiResultCallback<PaymentIntentResult> {
        @NonNull
        private final WeakReference<PaymentSummaryActivity> activityRef;

        PaymentResultCallback(@NonNull PaymentSummaryActivity activity) {
            activityRef = new WeakReference<>(activity);
            Log.d(TAG, "PaymentResultCallback: ");
        }

        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final PaymentSummaryActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            PaymentIntent paymentIntent = result.getIntent();
            Status status = paymentIntent.getStatus();
            if (status == Status.Succeeded) {

                // Payment completed successfully
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Log.d(TAG, "onSuccess: payment== " + gson.toString());
                long amount = paymentIntent.getAmount() / 100;
                String message = "Status: " + paymentIntent.getStatus().toString();
                binding.lytPayment.tvStatus.setText(R.string.completing_your_order);
                binding.lytPayment.tvStatus.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(PaymentSummaryActivity.this, R.color.green)));
                paymentName = Const.STRIPE;
                paymentId = paymentIntent.getId();
                placeOrder();
//                activity.displayAlert(
//                        "Payment completed",
//                        message, amount + Const.getCurrency(), true
//                );
            } else if (status == Status.RequiresPaymentMethod) {
                // Payment failed – allow retrying using a different payment method
                activity.displayAlert(
                        "Payment failed",
                        Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage(), "",
                        false);
            }
        }

        @Override
        public void onError(@NonNull Exception e) {
            final PaymentSummaryActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            // Payment request failed – allow retrying using the same payment method
            Log.d(TAG, "onSuccess: error== " + e.toString());

            activity.displayAlert("Error", e.getMessage(), "", false);
        }
    }

    private class MyTask extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... strings) {

            com.stripe.Stripe.apiKey = Const.STRIPE_SECRET_KEY;

            PaymentIntentCreateParams params1 =
                    PaymentIntentCreateParams.builder()
                            .setAmount((long) totalamount * 100)
                            .setDescription("User id :" + " email: ")
                            .setReceiptEmail(sessionManager.getUser().getIdentity())
                            //   .putExtraParam("email",sessionManager.getUser().getData().getEmail())
                            .setShipping(
                                    PaymentIntentCreateParams.Shipping.builder()
                                            .setName(sessionManager.getUser().getFirstname())
                                            .setPhone(address.getNumber())
                                            .setAddress(
                                                    PaymentIntentCreateParams.Shipping.Address.builder()
                                                            .setLine1(address.getAddress().concat(" ").concat(address.getArea().getName().concat(address.getCity().getName())))
                                                            .setPostalCode("91761")
                                                            .setLine2("")
                                                            .setCity("")
                                                            .setState("")
                                                            .setCountry("US")
                                                            .build())
                                            .build())
                            .setCurrency(Const.STRIPE_CURRENCY)
                            .addPaymentMethodType("card")
                            .build();

            com.stripe.model.PaymentIntent paymentIntent = null;

            try {
                paymentIntent = com.stripe.model.PaymentIntent.create(params1);
            } catch (com.stripe.exception.StripeException e) {
                e.printStackTrace();
                Log.d(TAG, "startCheckout: errr 64 " + e);
            }


            paymentIntentClientSecret = paymentIntent != null ? paymentIntent.getClientSecret() : null;

            Log.d(TAG, "doInBackground:0 " + paymentIntentClientSecret);

            Log.d(TAG, "doInBackground:1 " + paymentIntentClientSecret);
            return paymentIntentClientSecret;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (paymentType == 2) {
                CardInputWidget cardInputWidget = findViewById(R.id.cardInputWidget);
                cardInputWidget.setPostalCodeRequired(false);
                cardInputWidget.setPostalCodeEnabled(false);
                PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();

                if (params != null && paymentIntentClientSecret != null) {
                    Log.d(TAG, "confirmPayment: " + params.toString());
                    ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                            .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
                    stripe.confirmPayment(PaymentSummaryActivity.this, confirmParams);
                    Log.d(TAG, "onResponse: cps == " + confirmParams.getClientSecret());
                }
            }

        }
    }


}