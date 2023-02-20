package com.example.duex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class Add_dataActivity extends AppCompatActivity {

    EditText mAddHomeworkTitle;
    EditText mAddHomeworkDescription;
    DatePicker mDueDate;
    EditText mTime;
    EditText mEvent;
    Button mBtnAddHomework;

    DatabaseReference homeworkDbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        mAddHomeworkTitle = findViewById(R.id.addHomeworkTitle);
        mAddHomeworkDescription = findViewById(R.id.addHomeworkDescription);
        mDueDate = findViewById(R.id.dueDate);
        mTime = findViewById(R.id.Time);
        mEvent = findViewById(R.id.Event);
        mBtnAddHomework = findViewById(R.id.btnAddHomework);

        homeworkDbRef = FirebaseDatabase.getInstance().getReference().child("Homework");

        mBtnAddHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertHomeworkData();
            }
        });
    }

    private void insertHomeworkData() {
        String name = mAddHomeworkTitle.getText().toString();
        String description = mAddHomeworkDescription.getText().toString();
        String time = mTime.getText().toString();
        String event = mEvent.getText().toString();
        Calendar calendar = Calendar.getInstance();
        calendar.set(mDueDate.getYear(), mDueDate.getMonth(), mDueDate.getDayOfMonth());
        Date dueDate = calendar.getTime();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Homework homework = new Homework(name, description, dueDate, time, event);

        homeworkDbRef.child(uid).push().setValue(homework);
        Toast.makeText(Add_dataActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(Add_dataActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}
