package com.krushna.veginew.models;

import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;

public class ApplyCouponRoot {

    @SerializedName("subtotal")
    private float subtotal;

    @SerializedName("message")
    private String message;

    @SerializedName("discount_percent")
    private int discountPercent;

    @SerializedName("coupan_discount")
    private float coupanDiscount;

    @SerializedName("status")
    private boolean status;

    public float getSubtotal() {
        return Float.parseFloat(new DecimalFormat("###.##").format(subtotal));
    }

    public String getMessage() {
        return message;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public float getCoupanDiscount() {
        return coupanDiscount;
    }

    public boolean isStatus() {
        return status;
    }
}