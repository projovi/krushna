package com.krushna.veginew.models;

import android.text.format.DateUtils;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ComplainRoot {

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
        private String image;

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

        public String getImage() {
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

        @SerializedName("complaints_id")
        private String complaintsId;

        @SerializedName("updated_at")
        private String updatedAt;

        @SerializedName("user_id")
        private int userId;

        @SerializedName("mobile_no")
        private long mobileNo;

        @SerializedName("description")
        private String description;

        @SerializedName("answer")
        private String answer;

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("id")
        private int id;

        @SerializedName("title")
        private String title;

        @SerializedName("order_id")
        private String orderId;

        @SerializedName("user")
        private User user;

        @SerializedName("status")
        private int status;

        public String getComplaintsId() {
            return complaintsId;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public int getUserId() {
            return userId;
        }

        public long getMobileNo() {
            return mobileNo;
        }

        public String getDescription() {
            return description;
        }

        public String getCreatedAt() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            try {
                long time = sdf.parse(createdAt).getTime();
                long now = System.currentTimeMillis();
                CharSequence ago =
                        DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
                return ago.toString();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return createdAt;
        }


        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getOrderId() {
            return orderId;
        }

        public User getUser() {
            return user;
        }

        public int getStatus() {
            return status;
        }
    }
}