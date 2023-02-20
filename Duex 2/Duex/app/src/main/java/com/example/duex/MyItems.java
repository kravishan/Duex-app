package com.example.duex;

import com.google.firebase.auth.FirebaseAuth;

// This use for display
public class MyItems {

    private final String name, addHomeworkDescription, dueDate, dueTime;


    public MyItems(String name, String addHomeworkDescription, String dueDate, String dueTime) {
        this.name = name;
        this.addHomeworkDescription = addHomeworkDescription;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
//        this.event = event;
    }

    public String getName() {
        return name;
    }

    public String getAddHomeworkDescription() {
        return addHomeworkDescription;
    }

    public String getdueDate() {
        return dueDate;
    }

    public String getDueTime() {return dueTime;}
//
//    public String getEvent() {
//        return event;
//    }
}
