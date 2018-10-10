 package com.chuck.android.meetupfoodieandroid;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import static com.chuck.android.meetupfoodieandroid.OrderSelectRestActivity.PREF_CURRENT_LOCATION;
import static com.chuck.android.meetupfoodieandroid.OrderSelectRestActivity.PREF_REST;
import static com.chuck.android.meetupfoodieandroid.StartActivity.CONSTANT_NONE;
import static com.chuck.android.meetupfoodieandroid.StartActivity.PREF_REGION;

 public class OrderSelectLocationActivity extends AppCompatActivity {
     private RecyclerView rvLocationList;
     private RestaurantAdapter locationAdapter;
     LinearLayoutManager locationLayoutManager;
     private TextView locationInstuctions;
     private FirebaseDatabase database;
     private DatabaseReference locationRef;
     private List<String> locationList = new ArrayList<>();
     private SharedPreferences sharedPreferences;
     private static final String TAG = "Restaurant Location Activity" ;
     private String region;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_select_location);

         //Init Restaurant Location RV
         rvLocationList = findViewById(R.id.rv_rest_location);
         locationInstuctions = findViewById(R.id.tv_location_title);
         initLocationRecyclerView();

         final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
         region = sharedPreferences.getString(PREF_REGION,CONSTANT_NONE);
         database = FirebaseDatabase.getInstance();


         final String currentRest = sharedPreferences.getString(PREF_REST,CONSTANT_NONE);

         if (!currentRest.equals(CONSTANT_NONE))
         {
             locationRef = database.getReference(region).child(currentRest).child("locations");
             locationRef.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                         String locationName = snapshot.getValue(String.class);
                         locationList.add(locationName);
                         Log.i(TAG, "order loaded");
                     }
                     locationAdapter.setRestaurantList(locationList,PREF_CURRENT_LOCATION);
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
             Toast.makeText(getApplicationContext(), "No Restaurant Set", Toast.LENGTH_SHORT).show();
         }
    }
     private void initLocationRecyclerView() {
         locationLayoutManager = new LinearLayoutManager(this);
         locationLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
         rvLocationList.setLayoutManager(locationLayoutManager);
         locationAdapter = new RestaurantAdapter(R.id.rv_rest_location);
         rvLocationList.setAdapter(locationAdapter);
         RecyclerView.ItemDecoration itemDecoration = new
                 DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
         rvLocationList.addItemDecoration(itemDecoration);
     }
}
