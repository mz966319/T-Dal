package com.example.t_dal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {

    private ConstraintLayout editProfile,editTAProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editProfile = (ConstraintLayout) findViewById(R.id.edit_profile_button_settings);
        editTAProfile = (ConstraintLayout) findViewById(R.id.edit_ta_profile_button_settings);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        editTAProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}