package com.example.produksi.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class data_scan implements Serializable {
    @Expose
    private String datascan;

    public data_scan(String datascan) {
        this.datascan = datascan;
    }

    public String getDatascan() {
        return datascan;
    }

    public void setDatascan(String datascan) {
        this.datascan = datascan;
    }
}
