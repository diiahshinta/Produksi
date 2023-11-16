package com.example.produksi.model;

import com.google.gson.annotations.SerializedName;

public class ProductData {
    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }
}
