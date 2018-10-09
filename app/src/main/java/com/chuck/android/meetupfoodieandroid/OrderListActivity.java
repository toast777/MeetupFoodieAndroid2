package com.chuck.android.meetupfoodieandroid;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.Objects;

import static com.chuck.android.meetupfoodieandroid.OrderLoadActivity.PREF_CURRENT_LIST;
import static com.chuck.android.meetupfoodieandroid.adapters.OrderLoadAdapter.EXTRA_ORDERID;

public class OrderListActivity extends AppCompatActivity {
    private TextView tvOrderNumber;
    private String orderNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();

        assert bundle != null;
        String savedOrder = bundle.getString(EXTRA_ORDERID);


        tvOrderNumber = findViewById(R.id.tv_order_list_title);
        tvOrderNumber.setText(savedOrder);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        orderNumber = sharedPreferences.getString(PREF_CURRENT_LIST,"NONE");


        //TODO: Set FAB to open AddNewFoodActivity
        //TODO: Init Firebase DB
        //TODO: Have Firebase check if currentlist is empty or set to none based on userid
        //TODO: If empty do nothing, if not load current list
        //TODO: List Current Food List from firebase
        //
    }

}
