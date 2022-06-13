package com.krushna.veginew.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomePageRoot {

    @SerializedName("data")
    private Data data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public Data getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public static class Data {

        @SerializedName("categoryWithProduct")
        private List<CategoryWithProductItem> categoryWithProduct;

        @SerializedName("banner")
        private List<BannerItem> banner;

        @SerializedName("category")
        private List<CategoryItem> category;

        public List<CategoryWithProductItem> getCategoryWithProduct() {
            return categoryWithProduct;
        }

        public List<BannerItem> getBanner() {
            return banner;
        }

        public List<CategoryItem> getCategory() {
            return category;
        }
    }

    public static class CategoryWithProductItem {

        @SerializedName("image")
        private String image;

        @SerializedName("updated_at")
        private String updatedAt;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("id")
        private int id;

        @SerializedName("title")
        private String title;

        @SerializedName("products")
        private List<ProductItem> products;

        public String getImage() {
            return image;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public List<ProductItem> getProducts() {
            return products;
        }
    }

    public static class BannerItem {

        @SerializedName("image")
        private String image;

        @SerializedName("updated_at")
        private String updatedAt;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("id")
        private int id;

        public String getImage() {
            return image;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public int getId() {
            return id;
        }
    }

    public static class CategoryItem {

        @SerializedName("image")
        private String image;

        @SerializedName("updated_at")
        private String updatedAt;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("id")
        private int id;

        @SerializedName("title")
        private String title;

        public String getImage() {
            return image;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }
    }

}