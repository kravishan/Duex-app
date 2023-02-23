package com.example.duex;
//Update

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button button, profileButton, mapButton;
    FirebaseUser user;
    TextView addTaskButton, helpCenterBtn;


    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private final List<MyItems> myItemsList = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final RecyclerView recyclerView = findViewById(R.id.homeworkRecycler);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserUid = currentUser.getUid();

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                myItemsList.clear();

                for(DataSnapshot user : snapshot.child("Homework").getChildren()){
                    String uid = user.getKey();
                    if (uid.equals(currentUserUid)) {
                        for(DataSnapshot Homework : user.getChildren()){
                            if(Homework.hasChild("name") && Homework.hasChild("addHomeworkDescription") && Homework.hasChild("dueDate") && Homework.hasChild("time")){
                                final String getName = Homework.child("name").getValue(String.class);
                                final String getDescription = Homework.child("addHomeworkDescription").getValue(String.class);
                                final String getdueDate = Homework.child("dueDate").getValue(String.class);
                                final String getdueTime = Homework.child("time").getValue(String.class);
                                final Double latitude = Homework.child("latitude").getValue(Double.class);
                                final Double longitude = Homework.child("longitude").getValue(Double.class);

                                MyItems myItems = new MyItems(getName, getDescription, getdueDate, getdueTime, latitude, longitude);
                                myItemsList.add(myItems);
                            }
                        }
                    }
                }
                recyclerView.setAdapter(new MyAdapter(myItemsList, MainActivity.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        addTaskButton = findViewById(R.id.addTask);
        profileButton = findViewById(R.id.accountSettings);
        helpCenterBtn = findViewById(R.id.helpCenter);
        mapButton = findViewById(R.id.map);
        user = auth.getCurrentUser();
        if (user == null){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else {
        }

        helpCenterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), mainHelp.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Add_dataActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), mapActivity.class);

                // Create a list to hold the data for all the homework items
                List<List<String>> allData = new ArrayList<>();

                // Loop through all the homework items and add their data to the list
                for (MyItems myItem : myItemsList) {
                    List<String> data = new ArrayList<>();
                    data.add(myItem.getName());
                    data.add(myItem.getLatitude().toString());
                    data.add(myItem.getLongitude().toString());
                    allData.add(data);
                }

                // Add the list of all homework items data as an extra to the intent
                intent.putExtra("allData", (Serializable) allData);

                startActivity(intent);
            }
        });





    }
}