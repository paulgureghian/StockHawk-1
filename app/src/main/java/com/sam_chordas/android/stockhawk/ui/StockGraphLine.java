package com.sam_chordas.android.stockhawk.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;
import retrofit2.Response;
import retrofit2.Callback;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.Stock;
import com.sam_chordas.android.stockhawk.service.StockDataEndpoint;

public class StockGraphLine extends AppCompatActivity implements Callback<Stock> {

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
        low = (TextView) findViewById(R.id.low);
        close = (TextView) findViewById(R.id.close);
        volume = (TextView) findViewById(R.id.volume);
        adj_close = (TextView) findViewById(R.id.adj_close);

        Gson gson = new GsonBuilder()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://query.yahooapis.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        StockDataEndpoint stockDataEndpoint = retrofit.create(StockDataEndpoint.class);
        String query = "q=select * from yahoo.finance.historicaldata where symbol='GOOG' and startDate = '2016-01-09' and endDate = '2016-06-09'";
        Call<Stock> call = stockDataEndpoint.getData(query);
        call.enqueue(this);

        }
    @Override
    public void onResponse(Call<Stock> call, Response<Stock> response) {
        int code = response.code();
        if (code == 200) {
            Stock user = response.body();
            Toast.makeText(this, "Connection made", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No connection made" + String.valueOf(code),
                    Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onFailure(Call<Stock> call, Throwable t) {
        Toast.makeText(this, "Nope", Toast.LENGTH_LONG).show();
    }
}



















