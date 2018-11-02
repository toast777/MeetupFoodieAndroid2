package com.chuck.android.meetupfoodieandroid;


import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.chuck.android.meetupfoodieandroid.utils.FirebaseLogin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartActivity extends AppCompatActivity {
    public static final String PREF_REGION = "Region Preference";
    private static final List<String> regionArray = new ArrayList<>();
    public static final String CONSTANT_NONE = "NONE";
    private FirebaseAuth auth;
    @BindView(R.id.rg_region) RadioGroup radioRegions;
    private String TAG = "Start Activity" ;
    private SharedPreferences sharedPreferences;
    static {
        //Will only get instance once, was crashing app if loaded twice
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Init Firebase
        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_start);
        ButterKnife.bind(this); // bind butterknife

        auth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = auth.getCurrentUser();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //Goto Order Load Screen if Firebase Logged in and SharedPref set
        if (sharedPreferences.contains(PREF_REGION) && (currentUser != null))
        {
            Intent intent = new Intent(getApplicationContext(), OrderLoadActivity.class);
            startActivity(intent);
        }

        //Log in Anonymous - more auth choices in future
        if (currentUser == null) {
            auth.signInAnonymously()
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            final String TAG = "FIREBASE LOGIN" ;

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInAnonymously:success");
                                FirebaseUser user = auth.getCurrentUser();
                                loadRegions();

                            } else {
                                // If sign in fails, display a message to the user.
                                FirebaseException e =(FirebaseException) task.getException();
                                Log.w(TAG, "signInAnonymously:failure",e);
                                Toast.makeText(StartActivity.this, "User Authentication Failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            // ...
                        }
                    });
        }
        //Check if user got logged in but didn't select preference
        if ( !(sharedPreferences.contains(PREF_REGION)) ) {
            loadRegions();
        }

    }
    public void loadRegions() {
        //Init SharedPrefs
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        //If region pref is not set

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference();

            //Load Regions from Firebase
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
            //set listener to regions radio button group
            radioRegions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int regionID) {
                    int selectedRadioButtonId = radioRegions.getCheckedRadioButtonId();
                    RadioButton radioBtn = findViewById(selectedRadioButtonId);
                    //Save the Pref
                    editor.putString(PREF_REGION, radioBtn.getText().toString());
                    editor.apply();
                    //Goto Order Load Screen
                    Intent intent = new Intent(getApplicationContext(), OrderLoadActivity.class);
                    startActivity(intent);
                }
            });
        }
}
