package com.example.produksi.model;

import com.google.gson.annotations.SerializedName;

public class ResponseList {

    @SerializedName("id_barang_masuk")
    private String idBarangMasuk;

    @SerializedName("id_location")
    private String idLocation;

    @SerializedName("tanggal_barang_masuk")
    private String tanggal;

    @SerializedName("kode_sertem_produksi")
    private String kodeSertem;

    @SerializedName("shift_barang_masuk")
    private String shift;

    @SerializedName("keterangan_barang_masuk")
    private String keterangan;

    @SerializedName("metode")
    private String metode;

    @SerializedName("verifikasi")
    private String verifikasi;

    public String getIdBarangMasuk() {
        return idBarangMasuk;
    }

    public String getIdLocation() {
        return idLocation;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public String getKodeSertem() {
        return kodeSertem;
    }

    public String getMetode() {
        return metode;
    }

    public String getShift() {
        return shift;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getVerifikasi() {
        return verifikasi;
    }
}
