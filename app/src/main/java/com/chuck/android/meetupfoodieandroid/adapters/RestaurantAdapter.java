package com.chuck.android.meetupfoodieandroid.adapters;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chuck.android.meetupfoodieandroid.OrderListActivity;
import com.chuck.android.meetupfoodieandroid.OrderSelectLocationActivity;
import com.chuck.android.meetupfoodieandroid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static com.chuck.android.meetupfoodieandroid.OrderLoadActivity.PREF_CURRENT_LIST;
import static com.chuck.android.meetupfoodieandroid.OrderSelectRestActivity.PREF_CURRENT_LOCATION;
import static com.chuck.android.meetupfoodieandroid.OrderSelectRestActivity.PREF_REST;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

    private List<String> restaurantList;
    private String preferenceName;
    private int layout;

    public RestaurantAdapter(int layout) {
        this.layout = layout;
    }

    public class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ConstraintLayout restLayout;
        TextView restName;

        RestaurantViewHolder(View v) {
            super(v);
            //Bind Viewholder Text objects
            restLayout = v.findViewById(layout);
            restName = v.findViewById(R.id.rvtv_rest_name);
            v.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            //Set Shared Preference Rest Name
            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
            final SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(preferenceName, restaurantList.get(position));
            editor.apply();
            String currentList = sharedPreferences.getString(PREF_CURRENT_LIST,"NONE");
            if (preferenceName.equals(PREF_CURRENT_LOCATION))
            {
                Intent intent = new Intent(view.getContext(), OrderListActivity.class);
                view.getContext().startActivity(intent);
                setFirebasePref(currentList,preferenceName,restaurantList.get(position));
            }
            else if (preferenceName.equals(PREF_REST))
            {
                Intent intent = new Intent(view.getContext(), OrderSelectLocationActivity.class);
                view.getContext().startActivity(intent);
                setFirebasePref(currentList,preferenceName,restaurantList.get(position));
            }
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
            holder.restName.setText(restaurantList.get(position));
        }
    }

    public void setRestaurantList(List<String> restaurantList, String preferenceName) {
        this.restaurantList = restaurantList;
        this.preferenceName = preferenceName;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (restaurantList != null)
            return restaurantList.size();
        else
            return 0;
    }
    private void setFirebasePref(String listID, String preferenceName, String preferenceValue)
    {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("users");
        assert listID != null;
        assert currentUser != null;
        userRef.child(currentUser.getUid()).child("Orders").child(listID).child(preferenceName).setValue(preferenceValue);
    }
}
