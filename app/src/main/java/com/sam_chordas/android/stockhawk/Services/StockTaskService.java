package com.sam_chordas.android.stockhawk.Services;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.RemoteException;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.sam_chordas.android.stockhawk.DataBase.QuoteColumns;
import com.sam_chordas.android.stockhawk.DataBase.QuoteProvider;
import com.sam_chordas.android.stockhawk.EventBus.RefreshUpdaterMessage;
import com.sam_chordas.android.stockhawk.EventBus.StockAdded;
import com.sam_chordas.android.stockhawk.Utils.Utils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.greenrobot.eventbus.EventBus;

public class StockTaskService extends GcmTaskService {

    private Context mContext;
    private boolean isUpdate;
    public String getResponse;

    private OkHttpClient client = new OkHttpClient();
    private StringBuilder mStoredSymbols = new StringBuilder();
    private String LOG_TAG = StockTaskService.class.getSimpleName();

    public StockTaskService(Context context) {
        mContext = context;
    }

    String fetchData(String url) throws IOException {
        Log.d("URL", url);
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    @Override
    public int onRunTask(TaskParams params) {
        Cursor cursor;
        if (mContext == null) {
            mContext = this;
        }
        StringBuilder urlStringBuilder = new StringBuilder();
        try {
            urlStringBuilder.append("https://query.yahooapis.com/v1/public/yql?q=");
            urlStringBuilder.append(URLEncoder.encode("select * from yahoo.finance.quotes where symbol "
                    + "in (", "UTF-8"));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (params.getTag().equals("init") || params.getTag().equals("periodic")) {
            isUpdate = true;
            cursor = mContext.getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                    new String[]{"Distinct " + QuoteColumns.SYMBOL}, null,
                    null, null);


            if (cursor != null) {
                if (cursor.getCount() > 0) {

                    DatabaseUtils.dumpCursor(cursor);
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getCount(); i++) {
                        mStoredSymbols.append("\"").append(cursor.getString(cursor.getColumnIndex(QuoteColumns.SYMBOL))).append("\",");
                        cursor.moveToNext();

                        Log.e("get_count", String.valueOf(cursor.getCount()));
                    }


                    mStoredSymbols.replace(mStoredSymbols.length() - 1, mStoredSymbols.length(), ")");
                    try {
                        urlStringBuilder.append(URLEncoder.encode(mStoredSymbols.toString(), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                } else {

                    try {
                        urlStringBuilder.append(
                                URLEncoder.encode("\"YHOO\",\"AAPL\",\"GOOG\",\"MSFT\")", "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                }


            }
        } else if (params.getTag().equals("add")) {
            isUpdate = false;

            String stockInput = params.getExtras().getString(QuoteColumns.SYMBOL);
            try {
                urlStringBuilder.append(URLEncoder.encode("\"" + stockInput + "\")", "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        urlStringBuilder.append("&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables."
                + "org%2Falltableswithkeys&callback=");

        String urlString;

        int result = GcmNetworkManager.RESULT_FAILURE;

        urlString = urlStringBuilder.toString();
        try {
            getResponse = fetchData(urlString);
            result = GcmNetworkManager.RESULT_SUCCESS;
            try {
                ContentValues contentValues = new ContentValues();

                if (isUpdate) {
                    contentValues.put(QuoteColumns.ISCURRENT, 0);
                    mContext.getContentResolver().update(QuoteProvider.Quotes.CONTENT_URI, contentValues,
                            null, null);
                } else {

                    ArrayList<ContentProvider> response = Utils.quoteJsonToContentVals(getResponse);
                    if ((!response.isEmpty()) && (params.getTag().equals("add"))) {

                        EventBus.getDefault().post(new StockAdded());
                    }
                }
                mContext.getContentResolver().applyBatch(QuoteProvider.AUTHORITY,
                        Utils.quoteJsonToContentVals(getResponse));
            } catch (RemoteException | OperationApplicationException e) {
                Log.e(LOG_TAG, "Error applying batch insert", e);
            }
        } catch (IOException e) {
            e.printStackTrace();
            EventBus.getDefault().post(new IOException());
            Log.d(LOG_TAG, "HTTP Error: " + e.getMessage());
        }

        if (params.getTag().equals("update")) {

            EventBus.getDefault().post(new RefreshUpdaterMessage());
        }
        return result;
    }

}
