package com.sam_chordas.android.stockhawk.data;

import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.data.QuoteDatabase;

import java.util.ArrayList;
import java.util.List;

public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {


    QuoteProvider quoteProvider = new QuoteProvider();
    List<Stock> collection = new ArrayList<>();
    Context context;
    Intent intent;
    private Cursor mCursor;

    private void initData() {
        collection.clear();

        mCursor = context.getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI, null, null, null, null);

        DatabaseUtils.dumpCursor(mCursor);

        int index = mCursor.getColumnIndex(QuoteColumns.SYMBOL);

        if (mCursor != null) {
            while (mCursor.moveToNext()) {

                Stock stock = new Stock();
                stock.setSymbol  (mCursor.getString (mCursor.getColumnIndex(QuoteColumns.SYMBOL)));
                stock.setBid_Price(mCursor.getString(mCursor.getColumnIndex(QuoteColumns.BIDPRICE)));
                stock.setChange(mCursor.getString(mCursor.getColumnIndex(QuoteColumns.CHANGE)));

                collection.add(stock);
            }
        } else {
        }
        mCursor.close();
    }
    public WidgetDataProvider(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
    }
    @Override
    public void onCreate() {
        initData();
    }
    @Override
    public void onDataSetChanged() {
        initData();
    }
    @Override
    public void onDestroy() {
    }
    @Override
    public int getCount() {
        return collection.size();
    }
    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteView = new RemoteViews(context.getPackageName(),
                R.layout.widget_layout);
        remoteView.setTextViewText(R.id.symbol, collection.get(position).getSymbol());
        remoteView.setTextViewText(R.id.bid_price, collection.get(position).getBid_Price());
        remoteView.setTextViewText(R.id.change, collection.get(position).getChange());
        remoteView.setTextColor(R.layout.widget_layout, Color.BLACK);
        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


}
