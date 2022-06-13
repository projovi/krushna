package com.krushna.veginew.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CouponRoot {

    @SerializedName("data")
    private List<DataItem> data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public List<DataItem> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public static class DataItem {

        @SerializedName("coupon_code")
        private String couponCode;

        @SerializedName("updated_at")
        private String updatedAt;

        @SerializedName("description")
        private String description;

        @SerializedName("discount")
        private int discount;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("id")
        private int id;

        @SerializedName("type")
        private int type;

        @SerializedName("minamount")
        private int minamount;

        public String getCouponCode() {
            return couponCode;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public String getDescription() {
            return description;
        }

        public int getDiscount() {
            return discount;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public int getId() {
            return id;
        }

        public int getType() {
            return type;
        }

        public int getMinamount() {
            return minamount;
        }
    }
}