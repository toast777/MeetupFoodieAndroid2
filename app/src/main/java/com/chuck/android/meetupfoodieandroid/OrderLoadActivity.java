package com.chuck.android.meetupfoodieandroid;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chuck.android.meetupfoodieandroid.adapters.FirebaseFoodAdapter;
import com.chuck.android.meetupfoodieandroid.adapters.OrderLoadAdapter;
import com.chuck.android.meetupfoodieandroid.models.CustomFoodItem;
import com.chuck.android.meetupfoodieandroid.models.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.chuck.android.meetupfoodieandroid.OrderSelectRestActivity.PREF_CURRENT_LOCATION;
import static com.chuck.android.meetupfoodieandroid.OrderSelectRestActivity.PREF_REST;
import static com.chuck.android.meetupfoodieandroid.StartActivity.CONSTANT_NONE;
import static com.chuck.android.meetupfoodieandroid.StartActivity.PREF_REGION;

public class OrderLoadActivity extends AppCompatActivity {
    public static final String PREF_CURRENT_LIST = "Current List Key";
    private static final String TAG = "Order Load Activity" ;

    List<Order> orders = new ArrayList<>();
    //Bind UI elements
    @BindView(R.id.rv_order_list) RecyclerView rvOrderList;
    @BindView(R.id.tv_load_order_title) TextView loadInstructions;
    @BindView(R.id.iv_app_logo) ImageView logo;
    @BindView(R.id.toolbar) Toolbar toolbar;

    //Bind Strings
    @BindString(R.string.db_id) String dbID;
    @BindString(R.string.db_location) String dbLocation;
    @BindString(R.string.db_region) String dbRegion;
    @BindString(R.string.db_rest) String dbRest;
    @BindString(R.string.db_food_items) String dbFoodItems;
    @BindString(R.string.db_total) String dbTotal;
    @BindString(R.string.db_date) String dbDate;
    @BindString(R.string.db_users) String dbUsers;
    @BindString(R.string.db_orders) String dbOrders;

    @BindString(R.string.logo_url) String logoURL;

    private OrderLoadAdapter adapter;
    LinearLayoutManager orderLayoutManager;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_load);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        //Disable up arrow since top of hierarchy, startactivity first only on initial load
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        initRecyclerView();
        new DownloadImageTask(logo).execute(logoURL);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference(dbUsers);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        // Read from the database
        assert currentUser != null;
        userRef.child(currentUser.getUid()).child(dbOrders).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<CustomFoodItem> customFoodItems = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String id = snapshot.child(dbID).getValue(String.class);
                    String location = snapshot.child(dbLocation).getValue(String.class);
                    String region = snapshot.child(dbRegion).getValue(String.class);
                    String restaurant = snapshot.child(dbRest).getValue(String.class);
                    if (dataSnapshot.child(dbFoodItems).exists())
                    {
                        for (DataSnapshot snapshot2 : dataSnapshot.child(dbFoodItems).getChildren())
                        {
                            customFoodItems.add(snapshot2.child(dbFoodItems).getValue(CustomFoodItem.class));
                        }
                    }
                    Double total = snapshot.child(dbTotal).getValue(Double.class);
                    String date = snapshot.child(dbDate).getValue(String.class);

                    orders.add(new Order(id,region,date,total,location,restaurant, customFoodItems));
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


        Date date = new Date();
        String strDate = dateFormat.format(date);

        Order newOrder = new Order(listKey,sharedPreferences.getString(PREF_REGION,CONSTANT_NONE),strDate);

        assert listKey != null;
        userRef.child(currentUser.getUid()).child(dbOrders).child(listKey).setValue(newOrder);


        Intent intent = new Intent(getApplicationContext(), OrderSelectRestActivity.class);
        startActivity(intent);
    }
    public void loadLastOrder(View view) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if ((sharedPreferences.contains(PREF_REST) & sharedPreferences.contains(PREF_CURRENT_LOCATION)))
        {
            String restName = sharedPreferences.getString(PREF_REST,CONSTANT_NONE);
            String location = sharedPreferences.getString(PREF_CURRENT_LOCATION,CONSTANT_NONE);
            if (restName.equals(CONSTANT_NONE)) {
                Intent intent = new Intent(getApplicationContext(), OrderSelectRestActivity.class);
                startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            }
            else if (location.equals(CONSTANT_NONE)) {
                Intent intent = new Intent(getApplicationContext(), OrderSelectLocationActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            }
            else {
                Intent intent = new Intent(getApplicationContext(), OrderListActivity.class);
                startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            }
        }
        else
            Toast.makeText(this, R.string.error_load_last, Toast.LENGTH_SHORT).show();
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
    //From Medium Article how to load image async - https://medium.com/@crossphd/android-image-loading-from-a-string-url-6c8290b82c5e
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap bmp = null;
            try {
                InputStream in = new java.net.URL(urlDisplay).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bmp;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
