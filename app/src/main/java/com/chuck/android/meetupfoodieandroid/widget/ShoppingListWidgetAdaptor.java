package com.chuck.android.meetupfoodieandroid.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.chuck.android.meetupfoodieandroid.R;
import com.chuck.android.meetupfoodieandroid.models.CustomFoodItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ShoppingListWidgetAdaptor implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private List<CustomFoodItem> foodItems;
    private SharedPreferences sharedPreferences;

    public ShoppingListWidgetAdaptor(Context context){
        this.context = context;
        this.foodItems = getFoodItems();

    }

    private List<CustomFoodItem> getFoodItems() {
        foodItems = null;
        if ((sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)) != null){
            String listJson = sharedPreferences.getString("json1", "No Data");
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<CustomFoodItem>>(){}.getType();
            foodItems = gson.fromJson(listJson,type);
        }
        return foodItems;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.shopping_list_widget_list_item);
        // TODO: 10/21/2018 Figure out widget layout
        String holder;
        double quantity;
        if ( (holder = foodItems.get(position).getFoodItem().getName()) != null)
            remoteViews.setTextViewText(R.id.widgetIngredient_name, holder);
        if ((quantity = foodItems.get(position).getCustomPrice()) != 0.00)
            remoteViews.setTextViewText(R.id.widgetQuantity, Double.toString(quantity));
        return remoteViews;
    }
    @Override
    public int getCount() {
        return foodItems.size();
    }
    @Override
    public void onDataSetChanged() {
        foodItems = getFoodItems();
    }
    @Override
    public int getViewTypeCount() {
        return 1;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }
    @Override
    public void onCreate() {
    }
    @Override
    public void onDestroy() {
    }
    @Override
    public RemoteViews getLoadingView() {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
}
