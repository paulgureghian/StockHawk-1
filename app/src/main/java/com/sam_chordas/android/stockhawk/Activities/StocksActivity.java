package com.sam_chordas.android.stockhawk.Activities;

import android.app.LoaderManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.DataBase.QuoteColumns;
import com.sam_chordas.android.stockhawk.DataBase.QuoteProvider;
import com.sam_chordas.android.stockhawk.EventBus.IOException;
import com.sam_chordas.android.stockhawk.EventBus.MessageEvent;
import com.sam_chordas.android.stockhawk.Adapters.QuoteCursorAdapter;
import com.sam_chordas.android.stockhawk.Utils.RecyclerViewItemClickListener;
import com.sam_chordas.android.stockhawk.EventBus.RefreshUpdaterMessage;
import com.sam_chordas.android.stockhawk.EventBus.StockAdded;
import com.sam_chordas.android.stockhawk.EventBus.StockRemover;
import com.sam_chordas.android.stockhawk.Utils.Utils;
import com.sam_chordas.android.stockhawk.Services.StockIntentService;
import com.sam_chordas.android.stockhawk.Services.StockTaskService;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.melnykov.fab.FloatingActionButton;
import com.sam_chordas.android.stockhawk.TouchHelper.SimpleItemTouchHelperCallback;
import com.sam_chordas.android.stockhawk.Widget.StockWidget;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class StocksActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    boolean isConnected;
    private Cursor mCursor;
    private Context mContext;
    private Intent mServiceIntent;
    private QuoteCursorAdapter mCursorAdapter;
    private static final int CURSOR_LOADER_ID = 0;
    public SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        mContext = this;
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        setContentView(R.layout.activity_my_stocks);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                mServiceIntent = new Intent(StocksActivity.this, StockIntentService.class);

                mServiceIntent.putExtra("tag", "update");
                if (isConnected) {
                    startService(mServiceIntent);
                } else {
                    Toast.makeText(mContext, getString(R.string.network_toast), Toast.LENGTH_SHORT).show();
                }
            }
        });
        mServiceIntent = new Intent(this, StockIntentService.class);

        if (savedInstanceState == null) {

            mServiceIntent.putExtra("tag", "init");
            if (isConnected) {
                startService(mServiceIntent);
            } else {
                Toast.makeText(mContext, getString(R.string.network_toast), Toast.LENGTH_SHORT).show();
            }
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);

        mCursorAdapter = new QuoteCursorAdapter(this, null);
        recyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(this,
                new RecyclerViewItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        Intent intent = new Intent(mContext, StockGraphLine.class);
                        mCursor = mCursorAdapter.getCursor();
                        mCursor.moveToPosition(position);
                        String symbol = mCursor.getString(mCursor.getColumnIndex(QuoteColumns.SYMBOL));
                        intent.putExtra(QuoteColumns.SYMBOL, symbol);
                        mContext.startActivity(intent);
                    }
                }));
        recyclerView.setAdapter(mCursorAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToRecyclerView(recyclerView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isConnected) {
                    new MaterialDialog.Builder(mContext).title(R.string.symbol_search)
                            .content(R.string.content_test)
                            .inputType(InputType.TYPE_CLASS_TEXT)
                            .input(R.string.input_hint, R.string.input_prefill, new MaterialDialog.InputCallback() {
                                @Override
                                public void onInput(MaterialDialog dialog, CharSequence input) {

                                    Cursor cursor = getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                                            new String[]{QuoteColumns.SYMBOL}, QuoteColumns.SYMBOL + "= ?",
                                            new String[]{input.toString()}, null);
                                    if (cursor.getCount() != 0) {
                                        Toast toast =
                                                Toast.makeText(StocksActivity.this, R.string.saved,
                                                        Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
                                        toast.show();
                                    } else {

                                        mServiceIntent.putExtra("tag", "add");
                                        mServiceIntent.putExtra(QuoteColumns.SYMBOL, input.toString());
                                        startService(mServiceIntent);
                                        cursor.close();
                                    }
                                }
                            })
                            .show();
                } else {
                    Toast.makeText(mContext, getString(R.string.network_toast), Toast.LENGTH_SHORT).show();

                }
            }
        });
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mCursorAdapter);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        CharSequence mTitle = getTitle();
        if (isConnected) {
            long period = 3600L;
            long flex = 10L;
            String periodicTag = "periodic";

            PeriodicTask periodicTask = new PeriodicTask.Builder()
                    .setService(StockTaskService.class)
                    .setPeriod(period)
                    .setFlex(flex)
                    .setTag(periodicTag)
                    .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
                    .setRequiresCharging(false)
                    .build();

            GcmNetworkManager.getInstance(this).schedule(periodicTask);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void IOException(IOException event) {
        Toast.makeText(getApplicationContext(), mContext.getResources().getString(R.string.no_connection_made), Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStockAdded(StockAdded event) {
        Toast.makeText(getApplicationContext(), mContext.getResources().getString(R.string.stock_added), Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStockRemoved(StockRemover event) {
        Toast.makeText(getApplicationContext(), mContext.getResources().getString(R.string.stock_removed), Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshComplete(RefreshUpdaterMessage event) {
        mSwipeRefreshLayout.setRefreshing(false);

        Toast.makeText(getApplicationContext(), mContext.getResources().getString(R.string.refresh_complete), Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInvalidStockSymbol(MessageEvent event) {
        Toast.makeText(getApplicationContext(), mContext.getResources().getString(R.string.invalid_stock_symbol), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_stocks, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_change_units) {

            Utils.showPercent = !Utils.showPercent;
            this.getContentResolver().notifyChange(QuoteProvider.Quotes.CONTENT_URI, null);
        }
        if (id == R.id.menu_refresh) {

            mServiceIntent = new Intent(StocksActivity.this, StockIntentService.class);

            mServiceIntent.putExtra("tag", "update");
            if (isConnected) {
                startService(mServiceIntent);
                mSwipeRefreshLayout.setRefreshing(true);

            } else {
                Toast.makeText(mContext, getString(R.string.network_toast), Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(this, QuoteProvider.Quotes.CONTENT_URI,
                new String[]{QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE,
                        QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP},
                QuoteColumns.ISCURRENT + " = ?",
                new String[]{"1"},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
        mCursor = data;

        ComponentName name = new ComponentName(this, StockWidget.class);
        int[] ids = AppWidgetManager.getInstance(this).getAppWidgetIds(name);

        Intent intent = new Intent(this, StockWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);

        DatabaseUtils.dumpCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
























