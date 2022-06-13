package com.krushna.veginew.retrofit;

import com.krushna.veginew.BuildConfig;

public class Const {


    public static final String BASE_URL = "http://----------/api/";
    public static final String IMAGE_URL = "http://----------/public/storage/";
    public static final String DEVKEY = "------------";
    public static final String STRIPE_PUBLISHABLE_KEY = "------------";
    public static final String STRIPE_SECRET_KEY = "------------";

    public static final String DB_NAME = BuildConfig.APPLICATION_ID;
    public static final String PREF_NAME = BuildConfig.APPLICATION_ID;



    public static final String IS_LOGIN = "islogin";
    public static final String USER = "user";
    public static final String NOTIFICATION_TOKEN = "notificationtoken";
    public static final String USER_IMAGE = "userimgurl";
    public static final String P_ID = "productID";
    public static final int LIMIT = 10;
    public static final String WISHLIST = "wishlist";
    public static final String EXPET_HOLD_PENDING_ORDER_COUNT = "expetholdpending";
    public static final String SETTING = "setting";
    public static final int PRODUCT_RESULT = 512;
    public static final String PRODUCT_DATA = "product_data";
    public static final String STR_CID = "cid";
    public static final String STR_CNAME = "cname";
    public static final String STRIPE_CURRENCY = "INR";
    public static final String COD = "COD";
    public static final String STRIPE = "STRIPE";
    public static final String VEGI = "Vegi";
    public static long MAX_QUANTITY = 10;

    public static long getMaxQuantity() {
        return MAX_QUANTITY;
    }

    public static void setMaxQuantity(long maxQuantity) {
        MAX_QUANTITY = maxQuantity;
    }

    private static String currency = "$";

    public static String getCurrency() {
        return currency;
    }

    public static void setCurrency(String currency) {
        Const.currency = currency;
    }
}
