package com.example.produksi.model;

import com.google.gson.annotations.SerializedName;

public class Data2 {

    @SerializedName("data")
    private ProductData productData;

    public ProductData getProductData() {
        return productData;
    }
}
