package com.example.duex;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class mapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<List<String>> allData;
    private Button selectLocationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "homework_channel";
            String description = "Channel for homework reminders";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("homework_channel", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = getIntent();
        allData = (List<List<String>>) intent.getSerializableExtra("allData");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        selectLocationButton = findViewById(R.id.select_location_button);
        selectLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng latLng = mMap.getCameraPosition().target;
                List<List<String>> remindersWithin1km = getRemindersWithinRadius(latLng, 1000.0);
                displayRemindersOnMap(remindersWithin1km);

                int numReminders = remindersWithin1km.size();
                if (numReminders > 0) {
                    // Create a notification
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(mapActivity.this, "homework_channel")
                            .setSmallIcon(R.drawable.baseline_notifications_active_24)
                            .setContentTitle("Homework reminders available")
                            .setContentText("There are " + numReminders + " homework reminders available in your area.")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mapActivity.this);
                    notificationManager.notify(1, builder.build());
                }
            }
        });



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Loop through all the homework items and add a marker for each item on the map
        for (List<String> data : allData) {
            String name = data.get(0);
            double latitude = Double.parseDouble(data.get(1));
            double longitude = Double.parseDouble(data.get(2));

            LatLng location = new LatLng(latitude, longitude);
        }

        // Set the camera position to the first homework item location
        if (allData.size() > 0) {
            double latitude = Double.parseDouble(allData.get(0).get(1));
            double longitude = Double.parseDouble(allData.get(0).get(2));
            LatLng location = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        }
    }

    private List<List<String>> getRemindersWithinRadius(LatLng center, double radius) {
        List<List<String>> remindersWithinRadius = new ArrayList<>();

        for (List<String> data : allData) {
            double latitude = Double.parseDouble(data.get(1));
            double longitude = Double.parseDouble(data.get(2));

            double distance = calculateDistance(center.latitude, center.longitude, latitude, longitude);
            if (distance <= radius) {
                remindersWithinRadius.add(data);
            }
        }

        return remindersWithinRadius;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        return d * 1000;
    }

    private void displayRemindersOnMap(List<List<String>> reminders) {
        mMap.clear(); // remove all markers

        // Add markers for each reminder within the selected area
        for (List<String> reminder : reminders) {
            String name = reminder.get(0);
            double latitude = Double.parseDouble(reminder.get(1));
            double longitude = Double.parseDouble(reminder.get(2));
            LatLng location = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(location).title(name));
        }

        if (reminders.size() > 0) {
            double latitude = Double.parseDouble(reminders.get(0).get(1));
            double longitude = Double.parseDouble(reminders.get(0).get(2));
            LatLng location = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        }
    }

}
