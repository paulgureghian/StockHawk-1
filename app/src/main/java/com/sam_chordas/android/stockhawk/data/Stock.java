package com.sam_chordas.android.stockhawk.data;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class Stock implements Parcelable {


    private String Symbol;
    private String Date;
    private String Open;
    private String High;
    private String Low;
    private String Close;
    private String Volume;
    private String Adj_Close;

    public Stock() {
    }
    protected Stock(Parcel in) {

        Symbol = in.readString();
        Date = in.readString();
        Open = in.readString();
        High = in.readString();
        Low = in.readString();
        Close = in.readString();
        Volume = in.readString();
        Adj_Close = in.readString();
    }
    public static final Creator<Stock> CREATOR = new Creator<Stock>() {
        @Override
        public Stock createFromParcel(Parcel in) {
            return new Stock(in);
        }
        @Override
        public Stock[] newArray(int size) {
            return new Stock[size];
        }
    };


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
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {


        parcel.writeString(Symbol);
        parcel.writeString(Date);
        parcel.writeString(Open);
        parcel.writeString(High);
        parcel.writeString(Low);
        parcel.writeString(Close);
        parcel.writeString(Volume);
        parcel.writeString(Adj_Close);
    }
    public static class StockResult {
        private List<Stock> results;

        public List<Stock> getResults() {
            return results;
        }
    }
}
