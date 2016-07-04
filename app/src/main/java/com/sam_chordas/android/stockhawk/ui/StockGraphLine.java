package com.sam_chordas.android.stockhawk.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.sam_chordas.android.stockhawk.R;

public class StockGraphLine extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_graph_line);
        LineChart chart = (LineChart) findViewById(R.id.chart);
        LineData  data = new LineData();
        chart.setData(data);
        chart.setDescription("Stock's value over time");
        chart.invalidate();



    }
}
