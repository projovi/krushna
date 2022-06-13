package com.krushna.veginew.models;

import com.google.gson.annotations.SerializedName;

public class OrderDetailRoot {

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;
    @SerializedName("data")
    private OrderDetail orderDetail;

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }
}