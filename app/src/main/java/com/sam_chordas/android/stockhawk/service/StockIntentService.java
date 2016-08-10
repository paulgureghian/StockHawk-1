package com.sam_chordas.android.stockhawk.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.TaskParams;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;

public class StockIntentService extends IntentService {

    public StockIntentService() {
        super("StockIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(StockIntentService.class.getSimpleName(), "Stock Intent Service");
        StockTaskService stockTaskService = new StockTaskService(this);
        Bundle args = new Bundle();

        if (intent.getStringExtra("tag").equals("add")) {
            args.putString(QuoteColumns.SYMBOL, intent.getStringExtra(QuoteColumns.SYMBOL));
        }
        stockTaskService.onRunTask(new TaskParams(intent.getStringExtra("tag"), args));
    }
}