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
import com.chuck.android.meetupfoodieandroid.models.FirebaseFoodItem;

import java.util.List;

public class FirebaseFoodAdapter extends RecyclerView.Adapter<FirebaseFoodAdapter.FoodItemViewHolder>{

    private List<FirebaseFoodItem> foodList;

    public class FoodItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ConstraintLayout foodLayout;
        TextView foodName;
        TextView foodPrice;
        FoodItemViewHolder(View v) {
            super(v);
            //Define Viewholder Text objects
            foodLayout = v.findViewById(R.id.rv_firebase_food);
            foodName = v.findViewById(R.id.rvtv_food_name);
            foodPrice = v.findViewById(R.id.rvtv_food_price);
            v.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Intent myIntent = new Intent(view.getContext(), OrderListActivity.class);
            Intent intent = myIntent.putExtra("EXTRA_FOOD_NAME", foodList.get(position).getName());
            myIntent.putExtra("EXTRA_FOOD_IMGSRC", foodList.get(position).getImgSrc());
            myIntent.putExtra("EXTRA_FOOD_NUMBERADDONS", foodList.get(position).getNumAddOns());
            myIntent.putExtra("EXTRA_FOOD_PRICE", foodList.get(position).getPrice());
            myIntent.putExtra("EXTRA_FOOD_ALLOWADDONS",foodList.get(position).isAllowAddOns());
            view.getContext().startActivity(myIntent);
        }
    }
    @NonNull
    @Override
    public FirebaseFoodAdapter.FoodItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.food_item_list,parent,false);
        return new FoodItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FirebaseFoodAdapter.FoodItemViewHolder holder, int position) {
        String name = foodList.get(position).getName();
        if (foodList != null)
        {
            holder.foodName.setText( (foodList.get(position).getName()) );
            holder.foodPrice.setText(Double.toString(foodList.get(position).getPrice()));
            //What do we populate textview with
        }
    }
    public void setFoodList(List<FirebaseFoodItem> currentFoodList) {
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
