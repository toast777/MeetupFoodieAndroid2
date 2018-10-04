package com.chuck.android.meetupfoodieandroid;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.chuck.android.meetupfoodieandroid.utils.FirebaseLogin;

public class StartActivity extends AppCompatActivity {
    public static final String PREF_REGION = "Region Preference";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //Check if region set
        if ( !(sharedPreferences.contains(PREF_REGION)) ) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(PREF_REGION, "Kansas City");
            editor.apply();
        }
        //Log in Anonymous
        FirebaseLogin fLogin = new FirebaseLogin();

        //Goto to next Activity if Firebase Logged in and SharedPref set
        if (sharedPreferences.contains(PREF_REGION) && (fLogin.getCurrentUser() != null))
        {
            //Goto next activity
        }





    }
}
