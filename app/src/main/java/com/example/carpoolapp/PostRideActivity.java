package com.example.carpoolapp;

import static com.example.carpoolapp.R.id.dateInput;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PostRideActivity extends AppCompatActivity {

    private EditText dateInput, timeInput, pickupInput, destinationInput, seatsInput;
    private Button postRideBtn;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_ride);

        // Link UI elements
        dateInput = findViewById(R.id.dateInput);
        timeInput = findViewById(R.id.timeInput);
        pickupInput = findViewById(R.id.pickupInput);
        destinationInput = findViewById(R.id.destinationInput);
        seatsInput = findViewById(R.id.seatsInput);
        postRideBtn = findViewById(R.id.submitRideBtn);

        db = FirebaseFirestore.getInstance();

        postRideBtn.setOnClickListener(v -> {
            String date = dateInput.getText().toString();
            String time = timeInput.getText().toString();
            String pickup = pickupInput.getText().toString();
            String destination = destinationInput.getText().toString();
            String seats = seatsInput.getText().toString();

            if (date.isEmpty() || time.isEmpty() || pickup.isEmpty() || destination.isEmpty() || seats.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, Object> ride = new HashMap<>();
            ride.put("date", date);
            ride.put("time", time);
            ride.put("pickup", pickup);
            ride.put("destination", destination);
            ride.put("seats", Integer.parseInt(seats));

            db.collection("rides")
                    .add(ride)
                    .addOnSuccessListener(documentReference ->
                            Toast.makeText(this, "Ride Posted!", Toast.LENGTH_SHORT).show()
                    )
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Error posting ride", Toast.LENGTH_SHORT).show()
                    );
        });
    }
}
