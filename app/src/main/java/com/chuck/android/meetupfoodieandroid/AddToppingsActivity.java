package com.chuck.android.meetupfoodieandroid;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chuck.android.meetupfoodieandroid.adapters.FirebaseFoodAdapter;
import com.chuck.android.meetupfoodieandroid.models.CustomFoodItem;
import com.chuck.android.meetupfoodieandroid.models.FirebaseFoodItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.chuck.android.meetupfoodieandroid.adapters.FoodListAdapter.EXTRA_PARCEL_CUSTOM_FOOD_ITEM;

public class AddToppingsActivity extends AppCompatActivity {
    TextView topTitle;
    CustomFoodItem customizedFoodItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_toppings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        topTitle = findViewById(R.id.tv_addTop_title);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            customizedFoodItem = bundle.getParcelable(EXTRA_PARCEL_CUSTOM_FOOD_ITEM);
        }

        topTitle.setText(customizedFoodItem.getId());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //TODO: Load any toppings if they exist
        //TODO: Allow price customization total at first, maybe allow topping cust later
        //TODO: Set FAB to open ListToppingsActivity


    }

}
