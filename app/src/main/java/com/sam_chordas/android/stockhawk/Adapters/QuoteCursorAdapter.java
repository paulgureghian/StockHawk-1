package com.sam_chordas.android.stockhawk.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sam_chordas.android.stockhawk.EventBus.StockRemover;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.DataBase.QuoteColumns;
import com.sam_chordas.android.stockhawk.DataBase.QuoteProvider;
import com.sam_chordas.android.stockhawk.TouchHelper.ItemTouchHelperAdapter;
import com.sam_chordas.android.stockhawk.TouchHelper.ItemTouchHelperViewHolder;
import com.sam_chordas.android.stockhawk.Utils.Utils;

import org.greenrobot.eventbus.EventBus;

public class QuoteCursorAdapter extends CursorRecyclerViewAdapter<QuoteCursorAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {

    private static Context mContext;
    private static Typeface robotoLight;
    private boolean isPercent;

    public QuoteCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        robotoLight = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Light.ttf");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_quote, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final Cursor cursor) {
        viewHolder.symbol.setText(cursor.getString(cursor.getColumnIndex(QuoteColumns.SYMBOL)));
        viewHolder.bidPrice.setText(cursor.getString(cursor.getColumnIndex(QuoteColumns.BIDPRICE)));
        viewHolder.bidPrice.setContentDescription(mContext.getResources().getString(R.string.bid_price) + cursor.getString(cursor.getColumnIndex(QuoteColumns.BIDPRICE)));
        int sdk = Build.VERSION.SDK_INT;
        if (cursor.getInt(cursor.getColumnIndex(QuoteColumns.ISUP)) == 1) {
            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                viewHolder.change.setBackground(
                        mContext.getDrawable(R.drawable.percent_change_pill_green));
            } else {
                viewHolder.change.setBackground(
                        mContext.getDrawable(R.drawable.percent_change_pill_green));
            }
        } else {
            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                viewHolder.change.setBackground(
                        mContext.getDrawable(R.drawable.percent_change_pill_red));
            } else {
                viewHolder.change.setBackground(
                        mContext.getDrawable(R.drawable.percent_change_pill_red));
            }
        }
        if (Utils.showPercent) {
            viewHolder.change.setText(cursor.getString(cursor.getColumnIndex(QuoteColumns.PERCENT_CHANGE)));
            viewHolder.change.setContentDescription(mContext.getResources().getString(R.string.change) + cursor.getString(cursor.getColumnIndex(QuoteColumns.PERCENT_CHANGE)));
        } else {
            viewHolder.change.setText(cursor.getString(cursor.getColumnIndex(QuoteColumns.CHANGE)));
            viewHolder.change.setContentDescription(mContext.getResources().getString(R.string.change) + cursor.getString(cursor.getColumnIndex(QuoteColumns.CHANGE)));
        }
    }

    @Override
    public void onItemDismiss(int position) {
        Cursor c = getCursor();
        c.moveToPosition(position);
        String symbol = c.getString(c.getColumnIndex(QuoteColumns.SYMBOL));
        mContext.getContentResolver().delete(QuoteProvider.Quotes.withSymbol(symbol), null, null);
        notifyItemRemoved(position);
        EventBus.getDefault().post(new StockRemover());
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements ItemTouchHelperViewHolder, View.OnClickListener {
        public final TextView symbol;
        public final TextView bidPrice;
        public final TextView change;

        public ViewHolder(View itemView) {
            super(itemView);
            symbol = (TextView) itemView.findViewById(R.id.stock_symbol);
            symbol.setTypeface(robotoLight);
            bidPrice = (TextView) itemView.findViewById(R.id.bid_price);
            change = (TextView) itemView.findViewById(R.id.change);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackground(mContext.getDrawable(R.drawable.md_btn_shape));
        }

        @Override
        public void onClick(View v) {
        }
    }
}
