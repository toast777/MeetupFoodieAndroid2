package com.chuck.android.meetupfoodieandroid.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chuck.android.meetupfoodieandroid.OrderListActivity;
import com.chuck.android.meetupfoodieandroid.R;
import com.chuck.android.meetupfoodieandroid.models.CustomFoodItem;

import java.util.List;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.FoodItemViewHolder>{

    private List<CustomFoodItem> foodList;
    public static final String EXTRA_PARCEL_CUSTOM_FOOD_ITEM = "custom_foodItem";


    public class FoodItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ConstraintLayout foodLayout;
        TextView foodName;
        TextView foodPrice;
        FoodItemViewHolder(View v) {
            super(v);
            //Define Viewholder Text objects
            foodLayout = v.findViewById(R.id.rv_order_food);
            foodName = v.findViewById(R.id.rvtv_order_food_name);
            foodPrice = v.findViewById(R.id.rvtv_order_food_price);
            v.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Intent myIntent = new Intent(view.getContext(), OrderListActivity.class);
            Intent intent = myIntent.putExtra(EXTRA_PARCEL_CUSTOM_FOOD_ITEM, foodList.get(position));
            view.getContext().startActivity(myIntent);
        }
    }
    @NonNull
    @Override
    public FoodListAdapter.FoodItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.food_order_item_list,parent,false);
        return new FoodItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodListAdapter.FoodItemViewHolder holder, int position) {
        String name = foodList.get(position).getFoodItem().getName();
        if (foodList != null)
        {
            holder.foodName.setText( (foodList.get(position).getFoodItem().getName()) );
            holder.foodPrice.setText(Double.toString(foodList.get(position).getFoodItem().getPrice()));
            //What do we populate textview with
        }
    }
    public void setFoodList(List<CustomFoodItem> currentFoodList) {
        this.foodList = currentFoodList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if (foodList != null)
            return foodList.size();
        else
            return 0;
    }
}
