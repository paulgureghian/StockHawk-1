package com.sam_chordas.android.stockhawk.Data;

public class Stock {
    String Symbol;
    String Date;
    String Open;
    String High;
    String Low;
    String Close;
    String Volume;
    String Adj_Close;
    String Bid_Price;
    String Change;
    String Percent_Change;

    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String symbol) {
        this.Symbol = symbol;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        this.Date = date;
    }

    public String getOpen() {
        return Open;
    }

    public void setOpen(String open) {
        this.Open = open;
    }

    public String getHigh() {
        return High;
    }

    private void setHigh(String high) {
        this.High = high;
    }

    public String getLow() {
        return Low;
    }

    public void setLow(String low) {
        this.Low = low;
    }

    public String getClose() {
        return Close;
    }

    public void setClose(String close) {
        this.Close = close;
    }

    public String getVolume() {
        return Volume;
    }

    public void setVolume(String volume) {
        this.Volume = volume;
    }

    public String getAdj_Close() {
        return Adj_Close;
    }

    public void setAdj_Close(String adj_close) {
        this.Adj_Close = adj_close;
    }

    public String getBid_Price() {
        return Bid_Price;
    }

    public void setBid_Price(String bid_price) {
        this.Bid_Price = bid_price;
    }

    public String getChange() {
        return Change;
    }

    public void setChange(String change) {
        this.Change = change;
    }

    public String getPercent_Change() {
        return Percent_Change;
    }

    public void setPercent_Change(String percent_change) {
        this.Percent_Change = percent_change;
    }
}




