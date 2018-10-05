package com.chuck.android.meetupfoodieandroid;


import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.chuck.android.meetupfoodieandroid.utils.FirebaseLogin;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StartActivity extends AppCompatActivity {
    public static final String PREF_REGION = "Region Preference";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_start);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();



        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //Check if region set
        if ( !(sharedPreferences.contains(PREF_REGION)) ) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(PREF_REGION, "Kansas City");
            editor.apply();
        }
        //Log in Anonymous - more auth choices in future
        FirebaseLogin fLogin = new FirebaseLogin();

        //Goto to next Activity if Firebase Logged in and SharedPref set
        if (sharedPreferences.contains(PREF_REGION) && (fLogin.getCurrentUser() != null))
        {
            Intent intent = new Intent(getApplicationContext(), OrderLoadActivity.class);
            startActivity(intent);
        }





    }
}
