package com.chuck.android.meetupfoodieandroid;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chuck.android.meetupfoodieandroid.adapters.RestaurantAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.chuck.android.meetupfoodieandroid.StartActivity.CONSTANT_NONE;
import static com.chuck.android.meetupfoodieandroid.StartActivity.PREF_REGION;

public class OrderSelectRestActivity extends AppCompatActivity{
    private RecyclerView rvRestList;
    private RestaurantAdapter adapter;
    LinearLayoutManager restLayoutManager;
    public static final String PREF_REST = "restaurant";
    public static final String PREF_CURRENT_LOCATION = "location";
    private TextView restInstructions;
    private FirebaseDatabase database;
    private DatabaseReference restRef;
    private List<String> restaurantList = new ArrayList<>();
    private static final String TAG = "Restaurant List Activity" ;
    private SharedPreferences sharedPreferences;
    private String region;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_select_rest);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //Init Restaurant Name RV
        rvRestList = findViewById(R.id.rv_rest);
        restInstructions = findViewById(R.id.tv_rest_title);
        initRestRecyclerView();

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        region = sharedPreferences.getString(PREF_REGION,CONSTANT_NONE);
        database = FirebaseDatabase.getInstance();
        if (!region.equals("NONE"))
        {
            restRef = database.getReference(region);
            restRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String restName = snapshot.getKey();
                        restaurantList.add(restName);
                        Log.i(TAG, "order loaded");
                    }
                    adapter.setRestaurantList(restaurantList,PREF_REST);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "loadPost:onCancelled", error.toException());
                }
            });
        }
        else
        {
            Toast.makeText(this, R.string.error_no_region, Toast.LENGTH_SHORT).show();
        }

    }
    private void initRestRecyclerView() {
        restLayoutManager = new LinearLayoutManager(this);
        restLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRestList.setLayoutManager(restLayoutManager);
        adapter = new RestaurantAdapter(R.id.rv_rest);
        rvRestList.setAdapter(adapter);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvRestList.addItemDecoration(itemDecoration);
    }


}
