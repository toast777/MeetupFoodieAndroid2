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

import java.util.List;

public class OrderLoadAdapter extends RecyclerView.Adapter<OrderLoadAdapter.OrderLoadViewHolder>{

    private List<Order> orderList;
    public static final String EXTRA_ORDERID = "Order ID";
    private int counter;

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
            //Check if position is legend
            if (position != 0)
            {
                Intent intent = new Intent(view.getContext(), OrderListActivity.class);
                intent.putExtra(EXTRA_ORDERID,orderList.get(position-1).getId());
                view.getContext().startActivity(intent);
            }

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
        //Add Legend
        if (counter == 0 )
        {
            holder.orderID.setText("Order ID\n");
            holder.orderDate.setText("Order Date\n");
            holder.orderTotal.setText("Order Total\n");
            counter++;
        }
        else if (orderList != null)
        {
            holder.orderID.append((orderList.get(position-1).getId()).substring(0, 6));
            holder.orderDate.append(orderList.get(position-1).getDate().toString());
            holder.orderTotal.append(Double.toString(orderList.get(position-1).getTotal()));

        }
    }

    public void setOrderList(List<Order> currentFoodList) {
        this.orderList = currentFoodList;
        this.counter = 0;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (orderList != null)
            return orderList.size()+1;
        else
            return 0;
    }
}
