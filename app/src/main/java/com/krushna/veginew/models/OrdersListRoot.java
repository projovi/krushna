package com.krushna.veginew.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrdersListRoot {

    @SerializedName("data")
    private List<OrderDetail> data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public List<OrderDetail> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }



}