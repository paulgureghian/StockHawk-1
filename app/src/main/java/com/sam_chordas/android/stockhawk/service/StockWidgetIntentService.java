package com.sam_chordas.android.stockhawk.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.Context;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;

public class StockWidgetIntentService extends IntentService {

    public StockWidgetIntentService() {
        super("StockWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    //    RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
        //        R.layout.stock_widget);

      //  Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
      //  intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds);

      //  PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
      //  remoteViews.setOnClickPendingIntent(R.id.widget_list, pendingIntent);


       // for (int appWidgetId : appWidgetIds) {
        //    updateAppWidget(context, appWidgetManager, appWidgetId);


        }
    }


