package com.example.duex;

import java.util.Date;

public class Homework {
    String name;
    String addHomeworkDescription;
    String dueDate;
    String Time;
    String Event;

    public Homework(String name, String addHomeworkDescription, Date dueDate, String time, String event) {
        this.name = name;
        this.addHomeworkDescription = addHomeworkDescription;
        this.dueDate = String.valueOf(dueDate);
        Time = time;
        Event = event;
    }

    public String getName() {return name;}

    public String getAddHomeworkDescription() {
        return addHomeworkDescription;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getTime() {
        return Time;
    }

    public String getEvent() {
        return Event;
    }
}
