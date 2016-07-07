package com.sam_chordas.android.stockhawk.service;


import com.sam_chordas.android.stockhawk.data.Stock;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StockDataEndpoint {

    @GET("v1/public/yql?&format=json&diagnostics=true&env=store://datatables.org/alltableswithkeys&callback=")
    Call<Stock> getData(
            @Query("q=select * from yahoo.finance.historicaldata where symbol='GOOG' and startDate = '2016-01-09' and endDate = '2016-06-09'") String symbol);

}
