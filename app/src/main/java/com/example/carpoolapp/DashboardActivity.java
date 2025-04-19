package com.example.carpoolapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {

    private Button postRideBtn, findRideBtn, profileBtn, logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitydashboard);

        postRideBtn = findViewById(R.id.postRideBtn);
        findRideBtn = findViewById(R.id.findRideBtn);
        profileBtn = findViewById(R.id.profileBtn);
        logoutBtn = findViewById(R.id.logoutBtn);

        postRideBtn.setOnClickListener(v -> {
            // TODO: Create and navigate to PostRideActivity
            startActivity(new Intent(this, PostRideActivity.class));
        });

        findRideBtn.setOnClickListener(v -> {
            // TODO: Create and navigate to FindRideActivity
            startActivity(new Intent(this, FindRideActivity.class));
        });

        profileBtn.setOnClickListener(v -> {
            // TODO: Create and navigate to ProfileActivity
            startActivity(new Intent(this, ProfileActivity.class));
        });

        logoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

}
