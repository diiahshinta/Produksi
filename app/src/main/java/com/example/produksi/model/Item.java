package com.example.produksi.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Item implements Serializable {
    @Expose
    String id_sjp,nomor_sjp;

    public String getId_sjp() {
        return id_sjp;
    }

    public void setId_sjp(String id_sjp) {
        this.id_sjp = id_sjp;
    }

    public String getNomor_sjp() {
        return nomor_sjp;
    }

    public void setNomor_sjp(String nomor_sjp) {
        this.nomor_sjp = nomor_sjp;
    }
}
