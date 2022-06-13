package com.krushna.veginew.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchProductRoot {

    @SerializedName("data")
    private List<ProductItem> data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public List<ProductItem> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }


}