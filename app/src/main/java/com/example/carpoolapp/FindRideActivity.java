package com.example.carpoolapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class FindRideActivity extends AppCompatActivity {

    private EditText pickupInput, destinationInput, seatsInput;
    private Button searchButton;
    private ListView rideListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> rideList;
    private ArrayList<String> rideDates, rideTimes, rideSeats;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_ride_activity);

        pickupInput = findViewById(R.id.pickupInput);
        destinationInput = findViewById(R.id.destinationInput);
        seatsInput = findViewById(R.id.seatsInput);
        searchButton = findViewById(R.id.searchButton);
        rideListView = findViewById(R.id.rideListView);

        db = FirebaseFirestore.getInstance();

        rideList = new ArrayList<>();
        rideDates = new ArrayList<>();
        rideTimes = new ArrayList<>();
        rideSeats = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rideList);
        rideListView.setAdapter(adapter);

        searchButton.setOnClickListener(v -> searchRides());

        // Set an item click listener on the ListView to pass data to RideDetailsActivity
        rideListView.setOnItemClickListener((parent, view, position, id) -> {
            // Get selected ride details
            String rideDate = rideDates.get(position);
            String rideTime = rideTimes.get(position);
            String ridePickup = pickupInput.getText().toString().trim();
            String rideDestination = destinationInput.getText().toString().trim();
            int availableSeats = Integer.parseInt(rideSeats.get(position));

            // Pass the details to RideDetailsActivity
            Intent intent = new Intent(FindRideActivity.this, RideDetailsActivity.class);
            intent.putExtra("source", ridePickup);
            intent.putExtra("destination", rideDestination);
            intent.putExtra("time", rideTime);
            intent.putExtra("date", rideDate);
            intent.putExtra("seatsAvailable", availableSeats);
            startActivity(intent);
        });
    }

    private void searchRides() {
        String pickup = pickupInput.getText().toString().trim();
        String destination = destinationInput.getText().toString().trim();
        String seatsStr = seatsInput.getText().toString().trim();

        if (pickup.isEmpty() || destination.isEmpty() || seatsStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int seats = Integer.parseInt(seatsStr);

        db.collection("rides")
                .whereEqualTo("pickup", pickup)
                .whereEqualTo("destination", destination)
                .whereGreaterThanOrEqualTo("seats", seats)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    rideList.clear();
                    rideDates.clear();
                    rideTimes.clear();
                    rideSeats.clear();

                    if (queryDocumentSnapshots.isEmpty()) {
                        rideList.add("No rides found.");
                    } else {
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            String rideInfo = "Date: " + doc.getString("date") +
                                    "\nTime: " + doc.getString("time") +
                                    "\nPickup: " + doc.getString("pickup") +
                                    "\nDestination: " + doc.getString("destination") +
                                    "\nSeats: " + doc.getLong("seats");
                            rideList.add(rideInfo);

                            // Store additional details to pass to the next activity
                            rideDates.add(doc.getString("date"));
                            rideTimes.add(doc.getString("time"));
                            rideSeats.add(doc.getLong("seats").toString());
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error loading rides", Toast.LENGTH_SHORT).show());
    }
}
