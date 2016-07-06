package com.sam_chordas.android.stockhawk.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import retrofit2.Response;
import retrofit2.Converter.Factory;
import retrofit2.Callback;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sam_chordas.android.stockhawk.R;

import java.lang.reflect.Type;

public class StockGraphLine extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_graph_line);
        LineChart chart = (LineChart) findViewById(R.id.chart);
        LineData data = new LineData();
        chart.setData(data);
        chart.setDescription("Stock's value over time");
        chart.invalidate();
    }

    public void StockDataEndpoint(View view) {


        Gson gson = new GsonBuilder()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://query.yahooapis.com")

                .build();
      //  YahooAPI yahooAPI = retrofit.create(YahooAPI.class);

    }
}

















