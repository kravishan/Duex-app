package com.example.duex;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Add_dataActivity extends AppCompatActivity {

    EditText mAddHomeworkTitle;
    EditText mAddHomeworkDescription;
    DatePicker mDueDate;
    String mTime;
    EditText mEvent;
    Button mBtnAddHomework;

    DatabaseReference homeworkDbRef;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private double latitude;
    private double longitude;

    private TextView latitudeTextView;
    private TextView longitudeTextView;
    private ImageButton getLocationButton;

    protected static final int RESULT_SPEECH = 1;
    private ImageButton speakBtn;
    private EditText sText;

    TimePicker selectedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        mAddHomeworkTitle = findViewById(R.id.addHomeworkTitle);
        mAddHomeworkDescription = findViewById(R.id.addHomeworkDescription);
        mDueDate = findViewById(R.id.dueDate);
        selectedTime = findViewById(R.id.timePicker);
        mEvent = findViewById(R.id.Event);
        mBtnAddHomework = findViewById(R.id.btnAddHomework);

        speakBtn = findViewById(R.id.speechButton);
        sText = findViewById(R.id.addHomeworkDescription);
        speakBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                    sText.setText("");
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Your device doesn't support Speech to Text", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });


        homeworkDbRef = FirebaseDatabase.getInstance().getReference().child("Homework");

        selectedTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                String time = String.format("%02d:%02d", hourOfDay, minute);
                mTime = time;
                // You can store the selected time in a variable or do something else with it here.
            }
        });


        mBtnAddHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertHomeworkData();
            }
        });


        latitudeTextView = (TextView) findViewById(R.id.latitude_textview);
        longitudeTextView = (TextView) findViewById(R.id.longitude_textview);
        getLocationButton = (ImageButton) findViewById(R.id.get_location_button);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                latitudeTextView.setText(String.valueOf(latitude));
                longitudeTextView.setText(String.valueOf(longitude));

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            } else {
                Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case RESULT_SPEECH:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    sText.setText(text.get(0));
                }
                break;
        }
    }

    private void configureButton() {
        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(Add_dataActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Add_dataActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            }
        });
    }


    private void insertHomeworkData() {
        String name = mAddHomeworkTitle.getText().toString();
        String description = mAddHomeworkDescription.getText().toString();

        String time = mTime;

        String event = mEvent.getText().toString();

        Calendar calendar = Calendar.getInstance();
        calendar.set(mDueDate.getYear(), mDueDate.getMonth(), mDueDate.getDayOfMonth());
        Date dueDate = calendar.getTime();

        latitudeTextView.setText(String.valueOf(latitude));
        longitudeTextView.setText(String.valueOf(longitude));




        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Homework homework = new Homework(name, description, dueDate, time, event, latitude, longitude);

        homeworkDbRef.child(uid).push().setValue(homework);
        Toast.makeText(Add_dataActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(Add_dataActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}
