package com.krushna.veginew.models;

import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;

public class PricesItem {

    @SerializedName("unit")
    private String unit;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("price")
    private String price;

    @SerializedName("product_id")
    private int productId;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("id")
    private int id;

    @SerializedName("units")
    private Units units;

    @SerializedName("sale_price")
    private String salePrice;

    @SerializedName("unit_id")
    private int unitId;

    public String getUnit() {
        return new DecimalFormat("###.##").format(Float.parseFloat(unit));
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getPrice() {
        return new DecimalFormat("###.##").format(Float.parseFloat(salePrice));
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

    public Units getUnits() {
        return units;
    }
    public String getSalePrice() {
        return new DecimalFormat("###.##").format(Float.parseFloat(salePrice));
    }

    public int getUnitId() {
        return unitId;
    }

    public static class Units {

        @SerializedName("updated_at")
        private String updatedAt;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("id")
        private int id;

        @SerializedName("title")
        private String title;

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

