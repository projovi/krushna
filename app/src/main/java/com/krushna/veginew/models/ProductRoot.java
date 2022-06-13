package com.krushna.veginew.models;

import com.google.gson.annotations.SerializedName;

public class ProductRoot {

    @SerializedName("data")
    private ProductItem data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public ProductItem getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }





}