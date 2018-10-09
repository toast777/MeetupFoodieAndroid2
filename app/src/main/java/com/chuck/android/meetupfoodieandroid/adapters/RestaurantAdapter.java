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
import com.chuck.android.meetupfoodieandroid.models.Order;
import com.chuck.android.meetupfoodieandroid.models.Restaurant;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

    private List<Restaurant> restaurantList;
    public static final String PREF_REST = "Selected Restaurant";

    public class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ConstraintLayout restLayout;
        TextView restName;



        RestaurantViewHolder(View v) {
            super(v);
            //Bind Viewholder Text objects
            restLayout = v.findViewById(R.id.rv_rest);
            restName = v.findViewById(R.id.rvtv_rest_name);
            v.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            //Set Shared Preference Rest Name

        }
    }


    @NonNull
    @Override
    public RestaurantAdapter.RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.rest_list_item,parent,false);
        return new RestaurantAdapter.RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantAdapter.RestaurantViewHolder holder, int position) {
        if (restaurantList != null)
        {
            holder.restName.setText(restaurantList.get(position).getName());
        }
    }

    public void setRestaurantList(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (restaurantList != null)
            return restaurantList.size();
        else
            return 0;
    }
}
