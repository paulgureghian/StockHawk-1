package com.sam_chordas.android.stockhawk.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import retrofit2.Response;
import retrofit2.Callback;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.Stock;
import com.sam_chordas.android.stockhawk.service.StockDataEndpoint;
import com.sam_chordas.android.stockhawk.service.StocksDeserializer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StockGraphLine extends AppCompatActivity implements Callback<List<Stock>> {

    Context context;
    String mStockSymbol;
    List<Stock> items;
    private LineChart lineChart;
    Type listType = new TypeToken<List<Stock>>() {}.getType();
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

        Intent intent = getIntent();
        mStockSymbol = intent.getExtras().getString(QuoteColumns.SYMBOL);

        symbol = (TextView) findViewById(R.id.symbol);
        date = (TextView) findViewById(R.id.date);
        open = (TextView) findViewById(R.id.open);
        high = (TextView) findViewById(R.id.high);
        low = (TextView) findViewById(R.id.low);
        close = (TextView) findViewById(R.id.close);
        volume = (TextView) findViewById(R.id.volume);
        adj_close = (TextView) findViewById(R.id.adj_close);
        lineChart = (LineChart) findViewById(R.id.chart);

        Gson gson = new GsonBuilder()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://query.yahooapis.com/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().registerTypeAdapter(
                        listType, new StocksDeserializer()).create()))
                .build();
        StockDataEndpoint stockDataEndpoint = retrofit.create(StockDataEndpoint.class);
        String query = "select * from yahoo.finance.historicaldata where symbol='" + mStockSymbol + "' and startDate = '2016-06-09' and endDate = '2016-06-19'";
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

            // Toast.makeText(this, "Connection made", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, context.getResources().getString(R.string.no_connection_made) + String.valueOf(code),
                    Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onFailure(Call<List<Stock>> call, Throwable t) {
        Toast.makeText(this, context.getResources().getString(R.string.nope) , Toast.LENGTH_LONG).show();
    }
    private void displayChart() {
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setTextSize(11f);

        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<Entry> yVals = new ArrayList<>();

        Collections.reverse(items);

        for (int i = 0; i < items.size(); i++) {
            xVals.add(i, items.get(i).getDate());
            yVals.add(new Entry(Float.valueOf(items.get(i).getClose()), i));
        }
        LineDataSet dataSet = new LineDataSet(yVals, mStockSymbol);
        LineData lineData = new LineData(xVals, dataSet);
        lineChart.setData(lineData);
        lineChart.setDescription(getResources().getString(R.string.stock_value_time));
        lineChart.setDescriptionTextSize(15f);
        lineChart.getLegend().setTextSize(12f);
        lineChart.setPinchZoom(false);
        lineChart.invalidate();
        //lineChart.animateY();
    }
}


















