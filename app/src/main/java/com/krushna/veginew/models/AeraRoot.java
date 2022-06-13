package com.krushna.veginew.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AeraRoot {

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
        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setCityId(int cityId) {
            this.cityId = cityId;
        }

        @SerializedName("updated_at")
        private String updatedAt;

        @SerializedName("name")
        private String name;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("id")
        private int id;

        @SerializedName("city_id")
        private int cityId;

        public String getUpdatedAt() {
            return updatedAt;
        }

        public String getName() {
            return name;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public int getId() {
            return id;
        }

        public int getCityId() {
            return cityId;
        }
    }
}