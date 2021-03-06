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
import com.chuck.android.meetupfoodieandroid.OrderListActivity;
import com.chuck.android.meetupfoodieandroid.R;
import com.chuck.android.meetupfoodieandroid.models.CustomFoodItem;
import com.chuck.android.meetupfoodieandroid.models.FirebaseFoodTopping;

import java.util.List;
import java.util.Locale;

public class FirebaseToppingsAdapter extends RecyclerView.Adapter<FirebaseToppingsAdapter.FoodItemViewHolder>{

    private List<FirebaseFoodTopping> toppingList;
    public static final String EXTRA_PARCEL_FOOD_TOPPING = "foodTopping";
    private boolean allowClick;
    private int numToppingsFree;

    public class FoodItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ConstraintLayout foodLayout;
        TextView foodName;
        TextView foodPrice;
        FoodItemViewHolder(View v) {
            super(v);
            //Define Viewholder Text objects
            foodLayout = v.findViewById(R.id.rv_firebase_topping);
            foodName = v.findViewById(R.id.rvtv_topping_name);
            foodPrice = v.findViewById(R.id.rvtv_topping_price);
            v.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if (allowClick) {
                int position = getAdapterPosition();
                Intent myIntent = new Intent(view.getContext(), ListToppingsActivity.class);
                myIntent.putExtra(EXTRA_PARCEL_FOOD_TOPPING, toppingList.get(position));
                view.getContext().startActivity(myIntent);
            }
        }
    }
    @NonNull
    @Override
    public FirebaseToppingsAdapter.FoodItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.food_topping_list,parent,false);
        return new FoodItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FirebaseToppingsAdapter.FoodItemViewHolder holder, int position) {
        if (toppingList != null)
        {
            holder.foodName.setText( (toppingList.get(position).getToppingName()) );
            if (position < numToppingsFree)
                holder.foodPrice.setText(String.format(Locale.getDefault()
                    , "%1$,.2f", 0.00));
            else
                holder.foodPrice.setText(String.format(Locale.getDefault()
                        , "%1$,.2f",toppingList.get(position).getPrice()));
        }
    }
    public void setToppingList(List<FirebaseFoodTopping> currentFoodList,boolean allowClick,int numToppingsFree) {
        this.allowClick = allowClick;
        this.toppingList = currentFoodList;
        this.numToppingsFree = numToppingsFree;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if (toppingList != null)
            return toppingList.size();
        else
            return 0;
    }
}
