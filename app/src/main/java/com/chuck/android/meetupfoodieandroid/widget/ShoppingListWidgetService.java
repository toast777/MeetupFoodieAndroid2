package com.chuck.android.meetupfoodieandroid.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class ShoppingListWidgetService extends RemoteViewsService {
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ShoppingListWidgetAdaptor(this.getApplicationContext());
    }
}
