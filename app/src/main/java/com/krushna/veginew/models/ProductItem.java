package com.krushna.veginew.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductItem {

    @SerializedName("images")
    private List<ImagesItem> images;

    @SerializedName("category_id")
    private int categoryId;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("id")
    private int id;

    @SerializedName("stock")
    private int stock;

    @SerializedName("category")
    private Category category;

    @SerializedName("prices")
    private List<PricesItem> prices;

    public List<ImagesItem> getImages() {
        return images;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getId() {
        return id;
    }

    public int getStock() {
        return stock;
    }

    public Category getCategory() {
        return category;
    }

    public List<PricesItem> getPrices() {
        return prices;
    }

    public static class ImagesItem {

        @SerializedName("image")
        private String image;

        @SerializedName("updated_at")
        private String updatedAt;

        @SerializedName("product_id")
        private int productId;

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

        public int getProductId() {
            return productId;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public int getId() {
            return id;
        }
    }

    public static class Category {

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

