package com.chuck.android.meetupfoodieandroid.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chuck.android.meetupfoodieandroid.ListToppingsActivity;
import com.chuck.android.meetupfoodieandroid.R;
import com.chuck.android.meetupfoodieandroid.models.CustomFoodItem;
import com.chuck.android.meetupfoodieandroid.models.FirebaseFoodTopping;

import java.util.List;

public class FoodSummaryAdapter extends RecyclerView.Adapter<FoodSummaryAdapter.FoodItemViewHolder>{

    private List<CustomFoodItem> foodList;
    private static final int TYPE_TITLE = 0;
    private static final int TYPE_INGREDIENT = 1;


    public class FoodItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView foodName;
        TextView foodPrice;
        TextView foodToppings;
        FoodItemViewHolder(View v) {
            super(v);
            //Define Viewholder Text objects
            foodName = v.findViewById(R.id.rvtv_order_summary_food_name);
            foodPrice = v.findViewById(R.id.rvtv_order_food_summary_price);
            foodToppings =v.findViewById(R.id.rvtv_order_summary_toppings);
            v.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
//            int position = getAdapterPosition();
//            Intent myIntent = new Intent(view.getContext(), ListToppingsActivity.class);
//            Intent intent = myIntent.putExtra(EXTRA_PARCEL_CUSTOM_FOOD_ITEM, foodList.get(position));
//            view.getContext().startActivity(myIntent);
        }
    }
    @NonNull
    @Override
    public FoodSummaryAdapter.FoodItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.food_order_summary_list,parent,false);
        return new FoodItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodSummaryAdapter.FoodItemViewHolder holder, int position) {
        if (foodList != null)
        {
            String name = foodList.get(position).getFoodItem().getName();
            holder.foodName.setText( (foodList.get(position).getFoodItem().getName()) );
            holder.foodPrice.setText(Double.toString(foodList.get(position).getCustomPrice()));
            List<FirebaseFoodTopping> listOfToppings = foodList.get(position).getToppings();
            int toppingsSize = listOfToppings.size();
            String toppingsText = "";
            for(FirebaseFoodTopping topping: listOfToppings)
            {
                toppingsText = toppingsText.concat(topping.getToppingName());
                //If not last item add a comma
                if (!(topping.getToppingName().equals(listOfToppings.get(toppingsSize-1).getToppingName())) )
                    toppingsText = toppingsText.concat(", ");
            }
            holder.foodToppings.setText(toppingsText);

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
