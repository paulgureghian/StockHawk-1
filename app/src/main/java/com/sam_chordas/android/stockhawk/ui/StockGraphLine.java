package com.sam_chordas.android.stockhawk.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Response;
import retrofit2.Callback;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.Stock;
import com.sam_chordas.android.stockhawk.service.StockDataEndpoint;
import com.sam_chordas.android.stockhawk.service.StocksDeserializer;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class StockGraphLine extends AppCompatActivity implements Callback<List<Stock>> {

    List<Stock> items;
    private LineChart lineChart;
    Type listType = new TypeToken<List<Stock>>() {}.getType();
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
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().registerTypeAdapter(
                        listType, new StocksDeserializer()).create()))
                .build();
        StockDataEndpoint stockDataEndpoint = retrofit.create(StockDataEndpoint.class);
        String query = "select * from yahoo.finance.historicaldata where symbol='GOOG' and startDate = '2016-06-09' and endDate = '2016-06-19'";
        Call<List<Stock>> call = stockDataEndpoint.getData(query);
        call.enqueue(this);
    }
    @Override
    public void onResponse(Call<List<Stock>> call, Response<List<Stock>> response) {

        items = response.body();
        displayChart();

        Log.d("res", response.raw().toString());

        int code = response.code();
        List<Stock> items = response.body();

        for (Stock item : items) {
            Log.i("item", item.getHigh());
        }
        if (code == 200) {

            Toast.makeText(this, "Connection made", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No connection made" + String.valueOf(code),
                    Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onFailure(Call<List<Stock>> call, Throwable t) {
        Toast.makeText(this, "Nope", Toast.LENGTH_LONG).show();
    }
    private void displayChart() {

        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<Entry> yVals = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            xVals.add(i, items.get(i).getDate());
            yVals.add(new Entry(Float.valueOf(items.get(i).getClose()), i));
        }
        LineDataSet dataSet = new LineDataSet(yVals, "close");
        LineData lineData = new LineData(xVals, dataSet);
        lineChart.setData(lineData);
    }
}


















