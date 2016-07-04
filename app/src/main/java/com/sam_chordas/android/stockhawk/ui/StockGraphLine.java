package com.sam_chordas.android.stockhawk.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.sam_chordas.android.stockhawk.R;

public class StockGraphLine extends Activity {

    LineChart chart = (LineChart) findViewById(R.id.chart);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_graph_line);
    }
}
