package com.chuck.android.meetupfoodieandroid.adapters;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chuck.android.meetupfoodieandroid.R;
import com.chuck.android.meetupfoodieandroid.models.Order;

import java.util.List;

public class OrderLoadAdapter extends RecyclerView.Adapter<OrderLoadAdapter.OrderLoadViewHolder>{

    private List<Order> orderList;

    public class OrderLoadViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ConstraintLayout orderLayout;
        TextView orderID;
        TextView orderDate;
        TextView orderTotal;


        OrderLoadViewHolder(View v) {
            super(v);
            //Bind Viewholder Text objects
            orderLayout = v.findViewById(R.id.rv_order_list);
            orderID = v.findViewById(R.id.rvtv_order_id);
            orderDate = v.findViewById(R.id.rvtv_order_date);
            orderTotal = v.findViewById(R.id.rvtv_order_total);
            v.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            //do something
        }
    }


    @NonNull
    @Override
    public OrderLoadAdapter.OrderLoadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.order_list_item,parent,false);
        return new OrderLoadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderLoadAdapter.OrderLoadViewHolder holder, int position) {
        if (orderList != null)
        {
            holder.orderID.setText(orderList.get(position).getId());
            holder.orderDate.setText(orderList.get(position).getDate());
            holder.orderTotal.setText(Double.toString(orderList.get(position).getTotal()));
            //What do we populate textview with
        }
    }

    public void setOrderList(List<Order> currentFoodList) {
        this.orderList = currentFoodList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (orderList != null)
            return orderList.size();
        else
            return 0;
    }
}
