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
import com.chuck.android.meetupfoodieandroid.models.FirebaseFoodItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.chuck.android.meetupfoodieandroid.OrderLoadActivity.PREF_CURRENT_LIST;
import static com.chuck.android.meetupfoodieandroid.adapters.OrderLoadAdapter.EXTRA_ORDERID;

public class OrderListActivity extends AppCompatActivity {
    private TextView tvOrderNumber;
    private String orderNumber;
    private static final String TAG = "OrderListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Change ICON
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddNewFoodActivity.class);
                view.getContext().startActivity(intent);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            orderNumber = bundle.getString(EXTRA_ORDERID);
        }
        else
        {
            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            orderNumber = sharedPreferences.getString(PREF_CURRENT_LIST,"NONE");
        }

        tvOrderNumber = findViewById(R.id.tv_order_list_title);
        tvOrderNumber.setText(orderNumber);

    }


}
