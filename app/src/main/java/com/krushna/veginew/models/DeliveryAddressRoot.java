package com.krushna.veginew.models;

import com.google.gson.annotations.SerializedName;

public class DeliveryAddressRoot {

    @SerializedName("data")
    private DeliveryAddress data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public DeliveryAddress getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

}