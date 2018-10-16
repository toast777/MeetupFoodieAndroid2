package com.chuck.android.meetupfoodieandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chuck.android.meetupfoodieandroid.models.CustomFoodItem;
import com.chuck.android.meetupfoodieandroid.models.FirebaseFoodTopping;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.chuck.android.meetupfoodieandroid.AddToppingsActivity.CONST_TOPPINGS;
import static com.chuck.android.meetupfoodieandroid.OrderLoadActivity.PREF_CURRENT_LIST;
import static com.chuck.android.meetupfoodieandroid.StartActivity.CONSTANT_NONE;
import static com.chuck.android.meetupfoodieandroid.adapters.FirebaseToppingsAdapter.EXTRA_PARCEL_FOOD_TOPPING;
import static com.chuck.android.meetupfoodieandroid.adapters.FoodListAdapter.EXTRA_PARCEL_CUSTOM_FOOD_ITEM;

public class ListToppingsActivity extends AppCompatActivity {
    CustomFoodItem customizedFoodItem;
    FirebaseFoodTopping topping;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    public static final String PREF_CURRENT_FOOD_ITEM = "Current Food Item";
    private TextView toppingsListTitle;
    private TextView toppingsListTest;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_toppings);

        FloatingActionButton fab = findViewById(R.id.fab_add_toppings);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddToppingsActivity.class);
                view.getContext().startActivity(intent);
            }
        });


        Bundle bundle = getIntent().getExtras();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();

        if (bundle != null){
            customizedFoodItem = bundle.getParcelable(EXTRA_PARCEL_CUSTOM_FOOD_ITEM);
            topping = bundle.getParcelable(EXTRA_PARCEL_FOOD_TOPPING);
           if (customizedFoodItem != null)
            {
                editor.putString(PREF_CURRENT_FOOD_ITEM,customizedFoodItem.getId());
            }
            editor.apply();
        }

        String customizedFoodId = sharedPreferences.getString(PREF_CURRENT_FOOD_ITEM,CONSTANT_NONE);
        String orderID = sharedPreferences.getString(PREF_CURRENT_LIST,CONSTANT_NONE);
        //load user id

        //check if you have a food id and an order id
        if (customizedFoodId.equals(CONSTANT_NONE) || orderID.equals(CONSTANT_NONE)){
            //display error
        }
        else {


            //Load the toppings list from firebase
            //Load DB with references above
            toppingsListTitle = findViewById(R.id.tv_toppings_list_title);
            toppingsListTest = findViewById(R.id.textView3);

            toppingsListTitle.setText(getString(R.string.food_id_label, customizedFoodId));
            if (topping != null){
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = auth.getCurrentUser();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                assert currentUser != null;
                DatabaseReference toppingRef = database.getReference("users")
                        .child(currentUser.getUid()).child("Orders").child(orderID)
                        .child("foodItems").child(customizedFoodId).child(CONST_TOPPINGS);

                String toppingKey = toppingRef.push().getKey();
                assert toppingKey != null;
                toppingRef.child(toppingKey).setValue(topping);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(PREF_CURRENT_FOOD_ITEM, toppingKey);
                editor.apply();
                toppingsListTest.setText("Topping Name: " + topping.getToppingName());
            }


        }
    }
}
