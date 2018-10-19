package com.chuck.android.meetupfoodieandroid;

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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chuck.android.meetupfoodieandroid.adapters.FoodListAdapter;
import com.chuck.android.meetupfoodieandroid.adapters.FoodSummaryAdapter;
import com.chuck.android.meetupfoodieandroid.adapters.OrderLoadAdapter;
import com.chuck.android.meetupfoodieandroid.models.CustomFoodItem;
import com.chuck.android.meetupfoodieandroid.models.FirebaseFoodItem;
import com.chuck.android.meetupfoodieandroid.models.FirebaseFoodTopping;
import com.chuck.android.meetupfoodieandroid.models.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.chuck.android.meetupfoodieandroid.OrderLoadActivity.PREF_CURRENT_LIST;
import static com.chuck.android.meetupfoodieandroid.adapters.FirebaseFoodAdapter.EXTRA_PARCEL_FOOD_ITEM;
import static com.chuck.android.meetupfoodieandroid.adapters.OrderLoadAdapter.EXTRA_ORDERID;

public class OrderSummaryActivity extends AppCompatActivity {

    private RecyclerView rvOrderList;
    private FoodSummaryAdapter adapter;
    LinearLayoutManager orderLayoutManager;
    private TextView loadInstructions;
    private TextView orderSummary;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ImageView logo;
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    private String orderNumber;
    public static final String TAG = "Order Summary Activity";
    private Order summaryOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Check intent if widget was clicked
        //Load order from shared preference
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            orderNumber = bundle.getString(EXTRA_ORDERID);
        }
        if (orderNumber == null)
        {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            orderNumber = sharedPreferences.getString(PREF_CURRENT_LIST,"NONE");
        }
        rvOrderList = findViewById(R.id.rv_order_summary);
        orderSummary = findViewById(R.id.tv_order_summary_total);


        initRecyclerView();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        assert currentUser != null;
        DatabaseReference ordersRef = database.getReference("users").child(currentUser.getUid()).child("Orders").child(orderNumber);
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            //public void onDataChange(DataSnapshot dataSnapshot) {
            public void onDataChange(DataSnapshot snapshot) {
                List<CustomFoodItem> customFoodItems = new ArrayList<>();

                //for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                String id = snapshot.child("id").getValue(String.class);
                String location = snapshot.child("location").getValue(String.class);
                String region = snapshot.child("region").getValue(String.class);
                String restaurant = snapshot.child("restaurant").getValue(String.class);
                if (snapshot.child("foodItems").exists())
                {
                    for (DataSnapshot snapshot2 : snapshot.child("foodItems").getChildren())
                    {
                        List<FirebaseFoodTopping> toppings = new ArrayList<>();
                        Double customPrice = snapshot2.child("customPrice").getValue(Double.class);
                        FirebaseFoodItem foodItem = snapshot2.child("foodItem").getValue(FirebaseFoodItem.class);
                        if (snapshot2.child("toppings").exists())
                        {
                            for (DataSnapshot snapshot3 : snapshot2.child("toppings").getChildren())
                            {
                                toppings.add(snapshot3.getValue(FirebaseFoodTopping.class));
                            }
                        }
                        String foodId = snapshot2.child("id").getValue(String.class);
                        CustomFoodItem food = new CustomFoodItem(foodItem,customPrice,toppings,foodId);
                        customFoodItems.add(food);
                        //toppings.clear();
                    }
                }
                Double total = snapshot.child("total").getValue(Double.class);
                String date = snapshot.child("date").getValue(String.class);
                summaryOrder = new Order(id, region, date, total,location,restaurant,customFoodItems);
                Log.i(TAG, "summary loaded");
                calculateTotals(summaryOrder);
                orderSummary.setText("Total: " + summaryOrder.getTotal().toString());
                adapter.setFoodList(summaryOrder.getFoodItems());
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });

        //Create a new RV and adapter for special order list with summary and toppings
        // TODO: 10/18/2018 Calculate totals and store in order


        // TODO: 10/18/2018 send summary to widget for shopping list
    }
    private void initRecyclerView() {
        orderLayoutManager = new LinearLayoutManager(this);
        rvOrderList.setLayoutManager(orderLayoutManager);
        adapter = new FoodSummaryAdapter();
        rvOrderList.setAdapter(adapter);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvOrderList.addItemDecoration(itemDecoration);
    }
    private void calculateTotals(Order allFood)
    {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        assert currentUser != null;
        DatabaseReference ordersRef = database.getReference("users").child(currentUser.getUid()).child("Orders").child(orderNumber);

        int counterFood = 0;
        int total = 0;
        for(CustomFoodItem foodItem: allFood.getFoodItems())
        {
            double foodPrice = foodItem.getFoodItem().getPrice();
            //summaryOrder.getFoodItems().get(counterFood).setCustomPrice(basePrice);
            //   int counterTop = 0;
            for(FirebaseFoodTopping topping: allFood.getFoodItems().get(counterFood).getToppings())
            {
                foodPrice += topping.getPrice();
                //    counterTop++;
            }
            counterFood++;
            ordersRef.child("foodItems").child(foodItem.getId()).child("customPrice").setValue(foodPrice);
            total += foodPrice;
        }
        ordersRef.child("total").setValue(total);
        Log.i(TAG, "Total: " + Double.toString(total));

    }

}
