package com.chuck.android.meetupfoodieandroid.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.chuck.android.meetupfoodieandroid.R;

/**
 * Implementation of App Widget functionality.
 */
public class ShoppingListWidget extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        // There may be multiple widgets active, so update all of them
        int[] realAppWidgetIds = AppWidgetManager.getInstance(context)
                .getAppWidgetIds(new ComponentName(context, ShoppingListWidget.class));
        for (int id : realAppWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.shopping_list_widget);
            Intent serviceIntent = new Intent(context, ShoppingListWidgetService.class);
            remoteViews.setRemoteAdapter(R.id.widgetListView, serviceIntent);
            //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            //String recipeTitle = sharedPreferences.getString("Recipe Title", "NO RECIPE");
            remoteViews.setTextViewText(R.id.widget_title, "Shopping List");
            Intent intent = new Intent(context, ShoppingListWidget.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
            remoteViews.setPendingIntentTemplate(R.id.widgetListView, pendingIntent);
            appWidgetManager.updateAppWidget(id, remoteViews);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

