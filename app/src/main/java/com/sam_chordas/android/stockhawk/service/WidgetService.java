package com.sam_chordas.android.stockhawk.Service;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.Data.WidgetDataProvider;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent){
            return new WidgetDataProvider(this, intent);

    }
}
