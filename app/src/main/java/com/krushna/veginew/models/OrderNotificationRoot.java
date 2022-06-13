package com.krushna.veginew.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderNotificationRoot {

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

    public static class Order {

        @SerializedName("date")
        private String date;

        @SerializedName("reason")
        private Object reason;

        @SerializedName("address_id")
        private int addressId;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("payment_type")
        private int paymentType;

        @SerializedName("shipping_charge")
        private String shippingCharge;

        @SerializedName("dbname")
        private String dbname;

        @SerializedName("updated_at")
        private String updatedAt;

        @SerializedName("user_id")
        private int userId;

        @SerializedName("total_amount")
        private String totalAmount;

        @SerializedName("subtotal")
        private String subtotal;

        @SerializedName("dbnumber")
        private String dbnumber;

        @SerializedName("start_delivery")
        private int startDelivery;

        @SerializedName("id")
        private int id;

        @SerializedName("deliveryBoy_id")
        private int deliveryBoyId;

        @SerializedName("order_id")
        private String orderId;

        @SerializedName("coupon_discount")
        private String couponDiscount;

        @SerializedName("status")
        private int status;

        public String getDate() {
            return date;
        }

        public Object getReason() {
            return reason;
        }

        public int getAddressId() {
            return addressId;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public int getPaymentType() {
            return paymentType;
        }

        public String getShippingCharge() {
            return shippingCharge;
        }

        public String getDbname() {
            return dbname;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public int getUserId() {
            return userId;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public String getSubtotal() {
            return subtotal;
        }

        public String getDbnumber() {
            return dbnumber;
        }

        public int getStartDelivery() {
            return startDelivery;
        }

        public int getId() {
            return id;
        }

        public int getDeliveryBoyId() {
            return deliveryBoyId;
        }

        public String getOrderId() {
            return orderId;
        }

        public String getCouponDiscount() {
            return couponDiscount;
        }

        public int getStatus() {
            return status;
        }
    }

    public static class DataItem {

        @SerializedName("updated_at")
        private String updatedAt;

        @SerializedName("user_id")
        private int userId;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("id")
        private int id;

        @SerializedName("order_id")
        private int orderId;

        @SerializedName("status")
        private int status;

        @SerializedName("order")
        private Order order;

        public String getUpdatedAt() {
            return updatedAt;
        }

        public int getUserId() {
            return userId;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public int getId() {
            return id;
        }

        public int getOrderId() {
            return orderId;
        }

        public int getStatus() {
            return status;
        }

        public Order getOrder() {
            return order;
        }
    }
}