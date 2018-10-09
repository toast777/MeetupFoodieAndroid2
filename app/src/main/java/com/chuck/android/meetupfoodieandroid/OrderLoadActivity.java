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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import static com.chuck.android.meetupfoodieandroid.StartActivity.PREF_REGION;

public class OrderLoadActivity extends AppCompatActivity {
    public static final String PREF_CURRENT_LIST = "Current List Key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_load);
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void startNewOrder(View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("users");

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        assert currentUser != null;
        String listKey = userRef.child(currentUser.getUid()).push().getKey();

        editor.putString(PREF_CURRENT_LIST, listKey);
        editor.apply();

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String strDate = dateFormat.format(date);

        assert listKey != null;
        userRef.child(currentUser.getUid()).child(listKey).child("Region").setValue(sharedPreferences.getString(PREF_REGION,"NONE"));
        userRef.child(currentUser.getUid()).child(listKey).child("Date Start").setValue(strDate,"NONE");



        //Push Date as well and store current order number to Shared Preference



        //TODO: Firebase set current order to NONE
        //TODO: Create Firebase user area - customize security so user can modify area
        // TODO: 10/8/2018 goto selectrest
        Intent intent = new Intent(getApplicationContext(), OrderSelectRestActivity.class);
        startActivity(intent);
    }
    public void loadLastOrder(View view) {
        // TODO: 10/8/2018 goto orderlist
        Intent intent = new Intent(getApplicationContext(), OrderStartActivity.class);
        startActivity(intent);
    }
    public void loadOrder(View view) {
        // TODO: 10/7/2018 Create a Recyclerview to list current orders, click it goes to next activity
        // TODO: 10/8/2018 goto orderlist
        Intent intent = new Intent(getApplicationContext(), OrderStartActivity.class);
        startActivity(intent);
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

}
