package com.sam_chordas.android.stockhawk.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Stock {

    public List<StockItem> getItems() {
        return items;
    }
    public void setItems(List<StockItem> items) {
        this.items = items;
    }
    private List<StockItem> items;

    public static class StockItem {}

     String Symbol;
     String Date;
     String Open;
     String High;
     String Low;
     String Close;
     String Volume;
     String Adj_Close;

    public String getSymbol() {
        return Symbol;
    }
    public void setSymbol(String symbol) {
        this.Symbol = Symbol;
    }
    public String getDate() {
        return Date;
    }
    public void setDate(String date) {
        this.Date = Date;
    }
    public String getOpen() {
        return Open;
    }
    public void setOpen(String open) {
        this.Open = Open;
    }
    public String getHigh() {
        return High;
    }
    private void setHigh(String high) {
        this.High = High;
    }
    public String getLow() {
        return Low;
    }
    public void setLow(String low) {
        this.Low = Low;
    }
    public String getClose() {
        return Close;
    }
    public void setClose(String close) {
        this.Close = Close;
    }
    public String getVolume() {
        return Volume;
    }
    public void setVolume(String volume) {
        this.Volume = Volume;
    }
    public String getAdj_Close() {
        return Adj_Close;
    }
    public void setAdj_Close(String adj_close) {
        this.Adj_Close = Adj_Close;
    }
}


