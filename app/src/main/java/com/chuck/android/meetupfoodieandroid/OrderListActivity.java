package com.chuck.android.meetupfoodieandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.chuck.android.meetupfoodieandroid.adapters.FoodListAdapter;
import com.chuck.android.meetupfoodieandroid.models.CustomFoodItem;
import com.chuck.android.meetupfoodieandroid.models.FirebaseFoodItem;
import com.chuck.android.meetupfoodieandroid.models.FirebaseFoodTopping;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.chuck.android.meetupfoodieandroid.ListToppingsActivity.PREF_CURRENT_FOOD_ITEM;
import static com.chuck.android.meetupfoodieandroid.OrderLoadActivity.PREF_CURRENT_LIST;
import static com.chuck.android.meetupfoodieandroid.StartActivity.CONSTANT_NONE;
import static com.chuck.android.meetupfoodieandroid.adapters.FirebaseFoodAdapter.EXTRA_PARCEL_FOOD_ITEM;
import static com.chuck.android.meetupfoodieandroid.adapters.OrderLoadAdapter.EXTRA_ORDERID;

public class OrderListActivity extends AppCompatActivity {
    private TextView tvOrderNumber;
    private String orderNumber;
    private FirebaseFoodItem addedFood;

    private static final String TAG = "OrderFoodActivity";
    List<CustomFoodItem> foodlistItems = new ArrayList<>();
    private RecyclerView rvOrderFoodList;
    private FoodListAdapter adapter;
    LinearLayoutManager foodListLayoutManager;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Change ICON
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddNewFoodActivity.class);
                view.getContext().startActivity(intent);
            }
        });
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            orderNumber = bundle.getString(EXTRA_ORDERID);
            addedFood = bundle.getParcelable(EXTRA_PARCEL_FOOD_ITEM);
        }
        if (orderNumber == null)
        {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            orderNumber = sharedPreferences.getString(PREF_CURRENT_LIST,"NONE");
        }

        tvOrderNumber = findViewById(R.id.tv_order_list_title);
        tvOrderNumber.setText(orderNumber);

        rvOrderFoodList = findViewById(R.id.rv_order_food);
        initRecyclerView();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        assert currentUser != null;
        DatabaseReference ordersRef = database.getReference("users").child(currentUser.getUid()).child("Orders").child(orderNumber).child("foodItems");

        if (addedFood != null)
        {
            //Firebase Stuff
            //foodlistItems.add(new CustomFoodItem(addedFood,0,new ArrayList<FirebaseFoodTopping>()));
            String listKey = ordersRef.push().getKey();
            assert listKey != null;

            CustomFoodItem customAddedFood = new CustomFoodItem(addedFood,0.00,new ArrayList<FirebaseFoodTopping>(),listKey);
            ordersRef.child(listKey).setValue(customAddedFood);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(PREF_CURRENT_FOOD_ITEM, listKey);
            editor.apply();
        }

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<FirebaseFoodTopping> toppings = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Double customPrice = snapshot.child("customPrice").getValue(Double.class);
                    FirebaseFoodItem foodItem = snapshot.child("fooditem").getValue(FirebaseFoodItem.class);
                    if (dataSnapshot.child("toppings").exists())
                    {
                        for (DataSnapshot snapshot2 : dataSnapshot.child("toppings").getChildren())
                        {
                            toppings.add(snapshot2.getValue(FirebaseFoodTopping.class));
                        }
                    }
                    String id = snapshot.child("id").getValue(String.class);
                    CustomFoodItem food = new CustomFoodItem(foodItem,customPrice,toppings,id);
                    // TODO: 10/16/2018 crashes need to map class 
                    foodlistItems.add(food);
                    Log.i(TAG, "food loaded");
                }
                adapter.setFoodList(foodlistItems);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });
        //Add RView for firebase food
    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        orderNumber = sharedPreferences.getString(PREF_CURRENT_LIST,CONSTANT_NONE);
//        tvOrderNumber = findViewById(R.id.tv_order_list_title);
//        String orderID = getString(R.string.order_id_label) + orderNumber;
//        tvOrderNumber.setText(orderID);
//    }
    private void initRecyclerView() {
        foodListLayoutManager = new LinearLayoutManager(this);
        rvOrderFoodList.setLayoutManager(foodListLayoutManager);
        adapter = new FoodListAdapter();
        rvOrderFoodList.setAdapter(adapter);
    }
}
