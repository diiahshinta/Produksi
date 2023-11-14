package com.example.produksi;

import com.example.produksi.model.ResponseCheck;
import com.example.produksi.model.ResponseFG;
import com.example.produksi.model.ResponseList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("api/sertem/list")
    Call<ArrayList<ResponseList>> getListMutasi();

    @GET("dataprint")
    Call<ResponseCheck> getStatus(@Header("Authorization") String token,
                                  @Query("barcode") String barcode,
                                  @Query("level") String level);

    @GET("api/sertem/add/{id}/{barcode}")
    Call<ResponseFG> saveFG(@Path("id") String id,
                            @Path(value = "barcode", encoded = true) String barcode);

}
