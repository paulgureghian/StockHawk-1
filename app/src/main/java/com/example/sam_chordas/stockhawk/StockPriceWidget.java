package com.example.sam_chordas.stockhawk;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.ui.MyStocksActivity;

public class StockPriceWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);

        //  RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.stock_price_widget);
        //  views.setTextViewText(R.id.widget_stock_price, widgetText);
        //  appWidgetManager.updateAppWidget(appWidgetId, views);
        //  updateAppWidget(context, appWidgetManager, appWidgetId);
    }
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        int stockPriceResourceId = R.drawable.example_appwidget_preview;
        String description = "Stock Price";

        for (int appWidgetId : appWidgetIds) {
            int layoutId = R.layout.stock_price_widget;
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.stock_price_widget);
            views.setImageViewResource(R.id.widget_icon, stockPriceResourceId);
            views.setTextViewText(R.id.widget_stock_price, description);

            Intent launchIntent = new Intent(context, MyStocksActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
















