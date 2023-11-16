package com.example.produksi.model;

import com.google.gson.annotations.SerializedName;

public class ResponseFG {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("barcode")
    private String barcode;

    public String getBarcode() {
        return barcode;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
