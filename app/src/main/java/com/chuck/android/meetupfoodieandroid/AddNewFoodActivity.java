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

import com.chuck.android.meetupfoodieandroid.adapters.FirebaseFoodAdapter;
import com.chuck.android.meetupfoodieandroid.models.FirebaseFoodItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddNewFoodActivity extends AppCompatActivity {
    private static final String TAG = "AddNewFoodActivity";
    List<FirebaseFoodItem> mFoodItems = new ArrayList<>();
    private RecyclerView rvFoodList;
    private FirebaseFoodAdapter adapter;
    LinearLayoutManager foodLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_food);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rvFoodList = findViewById(R.id.rv_firebase_food);
        initRecyclerView();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Kansas City").child("Papa Johns");

        // Read from the database - in wrong activity :P
        myRef.child("food items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FirebaseFoodItem food = snapshot.getValue(FirebaseFoodItem.class);
                    mFoodItems.add(food);
                    Log.i(TAG, "food loaded");
                }
                adapter.setFoodList(mFoodItems);

            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });
    }
    private void initRecyclerView() {
        foodLayoutManager = new LinearLayoutManager(this);
        rvFoodList.setLayoutManager(foodLayoutManager);
        adapter = new FirebaseFoodAdapter();
        rvFoodList.setAdapter(adapter);
    }


}
