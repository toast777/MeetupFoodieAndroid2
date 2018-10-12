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
import com.chuck.android.meetupfoodieandroid.OrderSelectLocationActivity;
import com.chuck.android.meetupfoodieandroid.OrderSelectRestActivity;
import com.chuck.android.meetupfoodieandroid.R;
import com.chuck.android.meetupfoodieandroid.models.Order;

import java.util.List;

import static com.chuck.android.meetupfoodieandroid.StartActivity.CONSTANT_NONE;

public class OrderLoadAdapter extends RecyclerView.Adapter<OrderLoadAdapter.OrderLoadViewHolder>{

    private List<Order> orderList;
    public static final String EXTRA_ORDERID = "Order ID";
    private int counter;

    //Add header from stackoverflow - https://stackoverflow.com/questions/30840352/recyclerview-display-textview-at-top
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_CELL = 1;

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;
        else
            return TYPE_CELL;
    }

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
                String location = orderList.get(position-1).getLocation();
                String restName = orderList.get(position-1).getRestaurant();
               if (restName.equals(CONSTANT_NONE))
               {
                   Intent restIntent = new Intent(view.getContext(), OrderSelectRestActivity.class);
                   view.getContext().startActivity(restIntent);
               }
               else if (location.equals(CONSTANT_NONE))
               {
                   Intent restIntent = new Intent(view.getContext(), OrderSelectLocationActivity.class);
                   view.getContext().startActivity(restIntent);
               }
               else {
                   Intent intent = new Intent(view.getContext(), OrderListActivity.class);
                   intent.putExtra(EXTRA_ORDERID,orderList.get(position-1).getId());
                   view.getContext().startActivity(intent);
               }

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
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                holder.orderID.setText("Order ID\n");
                holder.orderDate.setText("Order Date\n");
                holder.orderTotal.setText("Order Total\n");
                break;
            case TYPE_CELL:
                holder.orderID.setText((orderList.get(position-1).getId()).substring(0, 6));
                holder.orderDate.setText(orderList.get(position-1).getDate().toString());
                holder.orderTotal.setText(Double.toString(orderList.get(position-1).getTotal()));
                break;
        }
    }

    public void setOrderList(List<Order> currentFoodList) {
        this.orderList = currentFoodList;
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
