package com.chuck.android.meetupfoodieandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chuck.android.meetupfoodieandroid.adapters.FirebaseFoodAdapter;
import com.chuck.android.meetupfoodieandroid.adapters.OrderLoadAdapter;
import com.chuck.android.meetupfoodieandroid.models.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.chuck.android.meetupfoodieandroid.OrderSelectRestActivity.PREF_CURRENT_LOCATION;
import static com.chuck.android.meetupfoodieandroid.OrderSelectRestActivity.PREF_REST;
import static com.chuck.android.meetupfoodieandroid.StartActivity.CONSTANT_NONE;
import static com.chuck.android.meetupfoodieandroid.StartActivity.PREF_REGION;

public class OrderLoadActivity extends AppCompatActivity {
    public static final String PREF_CURRENT_LIST = "Current List Key";
    List<Order> orders = new ArrayList<>();
    private RecyclerView rvOrderList;
    private OrderLoadAdapter adapter;
    LinearLayoutManager orderLayoutManager;
    private static final String TAG = "Order Load Activity" ;
    private TextView loadInstructions;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_load);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvOrderList = findViewById(R.id.rv_order_list);
        loadInstructions = findViewById(R.id.tv_load_order_title);
        initRecyclerView();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("users");

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Read from the database
        assert currentUser != null;
        userRef.child(currentUser.getUid()).child("Orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Order order = snapshot.getValue(Order.class);
                    orders.add(order);
                    Log.i(TAG, "order loaded");
                }
                if (orders.size() != 0){
                    rvOrderList.setVisibility(View.VISIBLE);
                    loadInstructions.setVisibility(View.VISIBLE);
                    adapter.setOrderList(orders);
                }


            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });
    }

    public void startNewOrder(View view) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("users");

        assert currentUser != null;
        String listKey = userRef.child(currentUser.getUid()).child("Orders").push().getKey();

        editor.putString(PREF_CURRENT_LIST, listKey);
        editor.putString(PREF_CURRENT_LOCATION,CONSTANT_NONE);
        editor.putString(PREF_REST,CONSTANT_NONE);
        editor.apply();


        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        Date date = new Date();
        String strDate = dateFormat.format(date);

        Order newOrder = new Order(listKey,sharedPreferences.getString(PREF_REGION,CONSTANT_NONE),strDate,0,CONSTANT_NONE,CONSTANT_NONE);

        assert listKey != null;
        userRef.child(currentUser.getUid()).child("Orders").child(listKey).setValue(newOrder);

        //TODO: Firebase set current order to NONE
        //TODO: Create Firebase user area - customize security so user can modify area
        // TODO: 10/8/2018 goto selectrest
        Intent intent = new Intent(getApplicationContext(), OrderSelectRestActivity.class);
        startActivity(intent);
    }
    public void loadLastOrder(View view) {
        // TODO: 10/8/2018 goto orderlist
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if ((sharedPreferences.contains(PREF_REST) & sharedPreferences.contains(PREF_CURRENT_LOCATION)))
        {
            String restName = sharedPreferences.getString(PREF_REST,CONSTANT_NONE);
            String location = sharedPreferences.getString(PREF_CURRENT_LOCATION,CONSTANT_NONE);
            if (restName.equals(CONSTANT_NONE))
            {
                Intent intent = new Intent(getApplicationContext(), OrderSelectRestActivity.class);
                startActivity(intent);
            }
            else if (location.equals(CONSTANT_NONE))
            {
                Intent intent = new Intent(getApplicationContext(), OrderSelectLocationActivity.class);
                startActivity(intent);
            }
            else
            {
                Intent intent = new Intent(getApplicationContext(), OrderListActivity.class);
                startActivity(intent);
            }
        }
        else
        {
            Toast.makeText(this, R.string.error_load_last, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_region, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.change_region:
                Intent intent = new Intent(getApplicationContext(), SettingsRegionAcitvity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void initRecyclerView() {
        orderLayoutManager = new LinearLayoutManager(this);
        rvOrderList.setLayoutManager(orderLayoutManager);
        adapter = new OrderLoadAdapter();
        rvOrderList.setAdapter(adapter);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvOrderList.addItemDecoration(itemDecoration);
    }

}
