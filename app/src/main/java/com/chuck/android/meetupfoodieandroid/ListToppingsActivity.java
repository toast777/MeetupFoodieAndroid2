package com.chuck.android.meetupfoodieandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chuck.android.meetupfoodieandroid.adapters.FirebaseToppingsAdapter;
import com.chuck.android.meetupfoodieandroid.models.CustomFoodItem;
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

import static com.chuck.android.meetupfoodieandroid.AddToppingsActivity.CONST_TOPPINGS;
import static com.chuck.android.meetupfoodieandroid.OrderLoadActivity.PREF_CURRENT_LIST;
import static com.chuck.android.meetupfoodieandroid.StartActivity.CONSTANT_NONE;
import static com.chuck.android.meetupfoodieandroid.adapters.FirebaseToppingsAdapter.EXTRA_PARCEL_FOOD_TOPPING;
import static com.chuck.android.meetupfoodieandroid.adapters.FoodListAdapter.EXTRA_PARCEL_CUSTOM_FOOD_ITEM;

public class ListToppingsActivity extends AppCompatActivity {
    CustomFoodItem customizedFoodItem;
    CustomFoodItem customFood;
    FirebaseFoodTopping topping;
    List<FirebaseFoodTopping> mFoodToppings = new ArrayList<>();
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    public static final String PREF_CURRENT_FOOD_ITEM = "Current Food Item";
    public static final String PREF_ALLOWED_TOPPINGS = "Free Toppings";
    public static final String PREF_USED_TOPPINGS = "Used Toppings";

    private RecyclerView rvFoodToppings;
    private FirebaseToppingsAdapter toppingsAdapter;
    LinearLayoutManager toppingsLayoutManager;
    private static final String TAG = "List Toppings Activity";


    private TextView toppingsListTitle;
    private TextView toppingsFreeTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_toppings);

        FloatingActionButton fab = findViewById(R.id.fab_add_toppings);
        rvFoodToppings = findViewById(R.id.rv_list_toppings);
        initRecyclerView();
        toppingsListTitle = findViewById(R.id.tv_toppings_list_title);
        toppingsFreeTitle = findViewById(R.id.tv_included_toppings);

        //Load and save Shared Prefs
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            customizedFoodItem = bundle.getParcelable(EXTRA_PARCEL_CUSTOM_FOOD_ITEM);
            topping = bundle.getParcelable(EXTRA_PARCEL_FOOD_TOPPING);
            if (customizedFoodItem != null) {
                editor.putString(PREF_CURRENT_FOOD_ITEM,customizedFoodItem.getId());
                editor.putInt(PREF_ALLOWED_TOPPINGS,customizedFoodItem.getFoodItem().getNumAddOns());
                editor.apply();
            }
            editor.apply();
        }
        String customizedFoodId = sharedPreferences.getString(PREF_CURRENT_FOOD_ITEM,CONSTANT_NONE);
        String orderID = sharedPreferences.getString(PREF_CURRENT_LIST,CONSTANT_NONE);
        final int numAddons = sharedPreferences.getInt(PREF_ALLOWED_TOPPINGS,0);

        toppingsFreeTitle.setText("Included Toppings: " + Integer.toString(numAddons) );


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddToppingsActivity.class);
                view.getContext().startActivity(intent);
            }
        });
        //check if you have a food id and an order id
        if (customizedFoodId.equals(CONSTANT_NONE) || orderID.equals(CONSTANT_NONE)){
            //display error
        }
        else {
            //Load the toppings list from firebase
            //Load DB with references above
            //Init firebase and get current user
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = auth.getCurrentUser();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            assert currentUser != null;

            DatabaseReference toppingRef = database.getReference("users")
                    .child(currentUser.getUid()).child("Orders").child(orderID)
                    .child("foodItems").child(customizedFoodId).child(CONST_TOPPINGS);



            toppingsListTitle.setText(getString(R.string.food_id_label, customizedFoodId));
            if (topping != null){
                String toppingKey = toppingRef.push().getKey();
                assert toppingKey != null;
                toppingRef.child(toppingKey).setValue(topping);
            }

            toppingRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        FirebaseFoodTopping topping = snapshot.getValue(FirebaseFoodTopping.class);
                        mFoodToppings.add(topping);
                        Log.i(TAG, "food loaded");
                    }
                    toppingsAdapter.setToppingList(mFoodToppings,false,numAddons);

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "loadPost:onCancelled", error.toException());
                }
            });


        }
    }
    private void initRecyclerView() {
        toppingsLayoutManager = new LinearLayoutManager(this);
        rvFoodToppings.setLayoutManager(toppingsLayoutManager);
        toppingsAdapter = new FirebaseToppingsAdapter();
        rvFoodToppings.setAdapter(toppingsAdapter);
    }
}
