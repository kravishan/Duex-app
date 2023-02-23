package com.example.duex;
// This for sending data to firebase
import java.util.Date;

public class Homework {
    String name;
    String addHomeworkDescription;
    String dueDate;
    String dueTime;
    String Event;
    Double latitude;
    Double longitude;

    public Homework() {}


    public Homework(String name, String addHomeworkDescription, Date dueDate, String dueTime, String event, double latitude, double longitude) {
        this.name = name;
        this.addHomeworkDescription = addHomeworkDescription;
        this.dueDate = String.valueOf(dueDate);
        dueTime = dueTime;
        this.dueTime = dueTime;
        Event = event;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public String getName() {return name;}

    public String getAddHomeworkDescription() {
        return addHomeworkDescription;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getTime() {
        return dueTime;
    }

    public String getEvent() {
        return Event;
    }

    public Double getLatitude(){return latitude;}

    public Double getLongitude(){return longitude;}



}
