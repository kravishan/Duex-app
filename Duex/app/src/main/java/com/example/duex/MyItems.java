package com.example.duex;
// This use for display
public class MyItems {

    private final String name, addHomeworkDescription, dueDate;

    public MyItems(String name, String addHomeworkDescription, String dueDate) {
        this.name = name;
        this.addHomeworkDescription = addHomeworkDescription;
        this.dueDate = dueDate;
//        this.time = time;
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
//
//    public String getTime() {
//        return time;
//    }
//
//    public String getEvent() {
//        return event;
//    }
}
