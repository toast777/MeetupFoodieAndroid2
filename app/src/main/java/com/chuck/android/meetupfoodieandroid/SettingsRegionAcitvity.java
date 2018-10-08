package com.chuck.android.meetupfoodieandroid;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.chuck.android.meetupfoodieandroid.StartActivity.PREF_REGION;

public class SettingsRegionAcitvity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_region);
        // TODO: 10/8/2018 recreate settings selection in start
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        final String TAG = "Settings Activity" ;
        final RadioGroup radioRegions = findViewById(R.id.rg_settings_region);
        final TextView regionSetting = findViewById(R.id.tv_settings_value);

        if ( sharedPreferences.contains(PREF_REGION) )
        {
            regionSetting.setText(sharedPreferences.getString(PREF_REGION,"NONE"));
        }
            myRef.child("Regions").addValueEventListener(new ValueEventListener() {
            int counter = 0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
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
        //Change to submit and reset current order
        radioRegions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int regionID) {
                int selectedRadioButtonId = radioRegions.getCheckedRadioButtonId();
                RadioButton radioBtn = findViewById(selectedRadioButtonId);
                editor.putString(PREF_REGION, radioBtn.getText().toString());
                editor.apply();
            }
        });
        //after done goto load activity
    }

}
