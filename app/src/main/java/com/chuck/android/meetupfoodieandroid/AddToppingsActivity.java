package com.chuck.android.meetupfoodieandroid;

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
import com.chuck.android.meetupfoodieandroid.adapters.FirebaseToppingsAdapter;
import com.chuck.android.meetupfoodieandroid.models.CustomFoodItem;
import com.chuck.android.meetupfoodieandroid.models.FirebaseFoodItem;
import com.chuck.android.meetupfoodieandroid.models.FirebaseFoodTopping;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.chuck.android.meetupfoodieandroid.OrderSelectRestActivity.PREF_REST;
import static com.chuck.android.meetupfoodieandroid.StartActivity.CONSTANT_NONE;
import static com.chuck.android.meetupfoodieandroid.StartActivity.PREF_REGION;
import static com.chuck.android.meetupfoodieandroid.adapters.FoodListAdapter.EXTRA_PARCEL_CUSTOM_FOOD_ITEM;

public class AddToppingsActivity extends AppCompatActivity {
    TextView topTitle;
    CustomFoodItem customizedFoodItem;
    private static final String TAG = "AddNewToppingActivity";
    public static final String CONST_TOPPINGS = "toppings";
    List<FirebaseFoodTopping> mFoodToppings = new ArrayList<>();
    private RecyclerView rvFoodToppings;
    private FirebaseToppingsAdapter toppingsAdapter;
    LinearLayoutManager toppingsLayoutManager;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_toppings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        topTitle = findViewById(R.id.tv_addTop_title);
        Bundle bundle = getIntent().getExtras();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String region = sharedPreferences.getString(PREF_REGION,CONSTANT_NONE);
        String restaurant = sharedPreferences.getString(PREF_REST,CONSTANT_NONE);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        rvFoodToppings = findViewById(R.id.rv_firebase_topping);
        initRecyclerView();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference toppingsRef = database.getReference(region).child(restaurant).child(CONST_TOPPINGS);

        toppingsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FirebaseFoodTopping topping = snapshot.getValue(FirebaseFoodTopping.class);
                    mFoodToppings.add(topping);
                    Log.i(TAG, "food loaded");
                }
                toppingsAdapter.setToppingList(mFoodToppings);

            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });


    }
    private void initRecyclerView() {
        toppingsLayoutManager = new LinearLayoutManager(this);
        rvFoodToppings.setLayoutManager(toppingsLayoutManager);
        toppingsAdapter = new FirebaseToppingsAdapter();
        rvFoodToppings.setAdapter(toppingsAdapter);
    }

}
