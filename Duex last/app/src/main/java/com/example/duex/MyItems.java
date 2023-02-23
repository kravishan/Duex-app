package com.example.duex;

import android.os.Parcel;
import android.os.Parcelable;

// This use for display data
public class MyItems implements Parcelable {

    private final String name, addHomeworkDescription, dueDate, dueTime, latitude, longitude;


    public MyItems(String name, String addHomeworkDescription, String dueDate, String dueTime, Double latitude, Double longitude) {
        this.name = name;
        this.addHomeworkDescription = addHomeworkDescription;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.latitude = String.valueOf(latitude);
        this.longitude = String.valueOf(longitude);
//        this.event = event;
    }

    protected MyItems(Parcel in) {
        name = in.readString();
        addHomeworkDescription = in.readString();
        dueDate = in.readString();
        dueTime = in.readString();
        latitude = in.readString();
        longitude = in.readString();

    }

    public static final Creator<MyItems> CREATOR = new Creator<MyItems>() {
        @Override
        public MyItems createFromParcel(Parcel in) {
            return new MyItems(in);
        }

        @Override
        public MyItems[] newArray(int size) {
            return new MyItems[size];
        }
    };

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

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(addHomeworkDescription);
        dest.writeString(dueDate);
        dest.writeString(dueTime);
    }


}
