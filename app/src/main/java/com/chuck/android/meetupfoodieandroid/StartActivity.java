package com.chuck.android.meetupfoodieandroid;


import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.chuck.android.meetupfoodieandroid.utils.FirebaseLogin;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends AppCompatActivity {
    public static final String PREF_REGION = "Region Preference";
    private static final List<String> regionArray = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_start);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SharedPreferences.Editor editor = sharedPreferences.edit();


        //Log in Anonymous - more auth choices in future

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser == null) {
            FirebaseLogin fLogin = new FirebaseLogin();
        }



        if ( !(sharedPreferences.contains(PREF_REGION)) ) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference();
            final String TAG = "Start Activity" ;
            final RadioGroup radioRegions = findViewById(R.id.rg_region);
            myRef.child("Regions").addValueEventListener(new ValueEventListener() {
                int counter = 0;
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String key = snapshot.getKey();
                        regionArray.add(key);
                        RadioButton regionButton = new RadioButton(getApplicationContext());
                        regionButton.setText(key);
                        regionButton.setId(counter);
                        radioRegions.addView(regionButton);
                        counter++;
                    }
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "loadPost:onCancelled", error.toException());
                }
            });
            //set listener to radio button group
            radioRegions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int regionID) {
                    int selectedRadioButtonId = radioRegions.getCheckedRadioButtonId();
                    RadioButton radioBtn = findViewById(selectedRadioButtonId);
                    editor.putString(PREF_REGION, radioBtn.getText().toString());
                    editor.apply();
                    Toast.makeText(StartActivity.this, radioBtn.getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        //Goto to next Activity if Firebase Logged in and SharedPref set
        if (sharedPreferences.contains(PREF_REGION) && (currentUser != null))
        {
            Intent intent = new Intent(getApplicationContext(), OrderLoadActivity.class);
            startActivity(intent);
        }





    }
}
