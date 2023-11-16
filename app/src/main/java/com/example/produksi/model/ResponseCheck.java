package com.example.produksi.model;

import com.google.gson.annotations.SerializedName;

public class ResponseCheck {

    @SerializedName("data")
    private Data2 product;

    public Data2 getData2() {
        return product;
    }
}
