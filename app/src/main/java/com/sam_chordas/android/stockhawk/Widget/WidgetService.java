package com.sam_chordas.android.stockhawk.Widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.Widget.WidgetDataProvider;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent){
            return new WidgetDataProvider(this, intent);

    }
}
