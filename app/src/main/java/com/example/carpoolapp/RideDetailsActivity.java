package com.example.carpoolapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RideDetailsActivity extends AppCompatActivity {

    private TextView rideInfoTextView, rideTimeTextView, availableSeatsTextView, rideDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_details);

        // Initialize TextViews
        rideInfoTextView = findViewById(R.id.rideInfoTextView);
        rideTimeTextView = findViewById(R.id.rideTimeTextView);
        availableSeatsTextView = findViewById(R.id.availableSeatsTextView);


        // Retrieve the ride details from the Intent
        String source = getIntent().getStringExtra("source");
        String destination = getIntent().getStringExtra("destination");
        String time = getIntent().getStringExtra("time");
        String date = getIntent().getStringExtra("date");
        int seatsAvailable = getIntent().getIntExtra("seatsAvailable", 0);

        // Set the text of the TextViews with the ride details
        rideInfoTextView.setText("Source: " + source + "\nDestination: " + destination);
        rideTimeTextView.setText("Time: " + time);
        rideDateTextView.setText("Date: " + date);
        availableSeatsTextView.setText("Seats Available: " + seatsAvailable);
    }
}
