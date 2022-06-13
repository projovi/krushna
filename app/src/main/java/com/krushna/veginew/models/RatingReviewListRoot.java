package com.krushna.veginew.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RatingReviewListRoot {

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

    public static class User {

        @SerializedName("image")
        private Object image;

        @SerializedName("firstname")
        private String firstname;

        @SerializedName("updated_at")
        private String updatedAt;

        @SerializedName("login_type")
        private String loginType;

        @SerializedName("identity")
        private String identity;

        @SerializedName("device_token")
        private String deviceToken;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("device_type")
        private int deviceType;

        @SerializedName("id")
        private int id;

        @SerializedName("lastname")
        private String lastname;

        @SerializedName("token")
        private String token;

        public Object getImage() {
            return image;
        }

        public String getFirstname() {
            return firstname;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public String getLoginType() {
            return loginType;
        }

        public String getIdentity() {
            return identity;
        }

        public String getDeviceToken() {
            return deviceToken;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public int getDeviceType() {
            return deviceType;
        }

        public int getId() {
            return id;
        }

        public String getLastname() {
            return lastname;
        }

        public String getToken() {
            return token;
        }
    }

    public static class DataItem {

        @SerializedName("updated_at")
        private String updatedAt;

        @SerializedName("user_id")
        private int userId;

        @SerializedName("review")
        private String review;

        @SerializedName("rating")
        private String rating;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("id")
        private int id;

        @SerializedName("order_id")
        private String orderId;

        @SerializedName("user")
        private User user;

        public String getUpdatedAt() {
            return updatedAt;
        }

        public int getUserId() {
            return userId;
        }

        public String getReview() {
            return review;
        }

        public String getRating() {
            return rating;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public int getId() {
            return id;
        }

        public String getOrderId() {
            return orderId;
        }

        public User getUser() {
            return user;
        }
    }
}