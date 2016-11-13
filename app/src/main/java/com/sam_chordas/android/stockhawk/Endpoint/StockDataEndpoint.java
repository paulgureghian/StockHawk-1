package com.sam_chordas.android.stockhawk.Endpoint;

import com.sam_chordas.android.stockhawk.POJO.Stock;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StockDataEndpoint {

    @GET("v1/public/yql?&format=json&diagnostics=true&env=store://datatables.org/alltableswithkeys&callback=")
    Call<List<Stock>> getData(
            @Query("q") String symbol);
}

