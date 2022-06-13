package com.krushna.veginew.models;

import com.google.gson.annotations.SerializedName;

public class SettingRoot {

    @SerializedName("meassage")
    private String meassage;

    @SerializedName("data")
    private Data data;

    @SerializedName("status")
    private boolean status;

    public String getMeassage() {
        return meassage;
    }

    public Data getData() {
        return data;
    }

    public boolean isStatus() {
        return status;
    }

    public static class Data {

        @SerializedName("number")
        private String number;

        @SerializedName("updated_at")
        private String updatedAt;

        @SerializedName("terms")
        private String terms;

        @SerializedName("shippingcharge")
        private String shippingcharge;

        @SerializedName("privacy_policy")
        private String privacyPolicy;

        @SerializedName("about")
        private String about;

        @SerializedName("created_at")
        private Object createdAt;

        @SerializedName("id")
        private int id;

        @SerializedName("quantity")
        private int maxQuantity;

        public int getMaxQuantity() {
            return maxQuantity;
        }

        public void setMaxQuantity(int maxQuantity) {
            this.maxQuantity = maxQuantity;
        }

        @SerializedName("email")
        private String email;

        @SerializedName("currencies")
        private String currencies;

        public String getNumber() {
            return number;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public String getTerms() {
            return terms;
        }

        public String getShippingcharge() {
            return shippingcharge;
        }

        public String getPrivacyPolicy() {
            return privacyPolicy;
        }

        public String getAbout() {
            return about;
        }

        public Object getCreatedAt() {
            return createdAt;
        }

        public int getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        public String getCurrencies() {
            return currencies;
        }
    }
}