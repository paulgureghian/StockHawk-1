package com.sam_chordas.android.stockhawk.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Response;
import retrofit2.Converter.Factory;
import retrofit2.Callback;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.Stock;
import com.sam_chordas.android.stockhawk.service.StockDataEndpoint;
import com.sam_chordas.android.stockhawk.service.YahooUser;

import java.lang.reflect.Type;

public class StockGraphLine extends AppCompatActivity implements Callback<YahooUser> {

    public static final String EXTRA_STOCK = "stock";
    Stock mStock;
    TextView symbol;
    TextView date;
    TextView open;
    TextView high;
    TextView low;
    TextView close;
    TextView volume;
    TextView adj_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_graph_line);
        LineChart chart = (LineChart) findViewById(R.id.chart);
        LineData data = new LineData();
        chart.setData(data);
        chart.setDescription("Stock's value over time");
        chart.invalidate();

        symbol = (TextView) findViewById(R.id.symbol);
        date = (TextView) findViewById(R.id.date);
        open = (TextView) findViewById(R.id.open);
        high = (TextView) findViewById(R.id.high);
        low = (TextView)findViewById(R.id.low);
        close = (TextView)findViewById(R.id.close);
        volume = (TextView) findViewById(R.id.volume);
        adj_close = (TextView) findViewById(R.id.adj_close);

       //hi


    }

    public void onClick(View view) {
        Gson gson = new GsonBuilder()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://query.yahooapis.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        StockDataEndpoint stockDataEndpoint = retrofit.create(StockDataEndpoint.class);
        Class<StockDataEndpoint> call = StockDataEndpoint.class;
        // call.enqueue(this);
    }

    @Override
    public void onResponse(Call<YahooUser> call, Response<YahooUser> response) {
        int code = response.code();
        if (code == 200) {
            YahooUser user = response.body();
            Toast.makeText(this, "Connection made", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No connection made" + String.valueOf(code),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(Call<YahooUser> call, Throwable t) {
        Toast.makeText(this, "Nope", Toast.LENGTH_LONG).show();
    }
}

















