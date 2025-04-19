package com.example.carpoolapp;

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
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rideList);
        rideListView.setAdapter(adapter);

        searchButton.setOnClickListener(v -> searchRides());
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
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error loading rides", Toast.LENGTH_SHORT).show());
    }
}
