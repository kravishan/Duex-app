package com.example.duex;
// This use for display use

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    private final List<MyItems> items;
    private final Context context;


    public MyAdapter(List<MyItems> items, Context context) {
        this.items = items;
        this.context = context;

    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, null));
    }

    public void scheduleNotification(String dueDate, String name) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
        Date dueDateObj;
        try {
            dueDateObj = dateFormat.parse(dueDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }


        Date today = new Date();

        Intent intent = new Intent(context, HomeworkNotificationReceiver.class);
        intent.putExtra("name", name);

        Calendar dueDateCal = Calendar.getInstance();
        dueDateCal.setTime(dueDateObj);
        Calendar todayCal = Calendar.getInstance();
        todayCal.setTime(today);

        int dueDay = dueDateCal.get(Calendar.DAY_OF_MONTH);
        int dueMonth = dueDateCal.get(Calendar.MONTH);
        int dueYear = dueDateCal.get(Calendar.YEAR);

        int todayDay = todayCal.get(Calendar.DAY_OF_MONTH);
        int todayMonth = todayCal.get(Calendar.MONTH);
        int todayYear = todayCal.get(Calendar.YEAR);

        Log.d("MyActivity", "dueDateObj: " + dueDay + "/" + dueMonth + "/" + dueYear);
        Log.d("MyActivity", "today: " + todayDay + "/" + todayMonth + "/" + todayYear);

        PendingIntent pendingIntent;
        if (dueDay == todayDay && dueMonth == todayMonth && dueYear == todayYear) {

            intent.putExtra("message", "Homework \"" + name +  "\"  is due today");
            pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        } else if (dueDay == todayDay && dueMonth == todayMonth && dueYear == todayYear) {

//            intent.putExtra("message", "Homework \"" + name +  "\"  is due today");
//            pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {

            intent.putExtra("message", "Your homework \"" + name + "\" is overdue!");
            pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long alarmTime = dueDateObj.getTime();
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MyItems myItems = items.get(position);
        holder.title.setText(myItems.getName());
        holder.description.setText(myItems.getAddHomeworkDescription());
        holder.dueTime.setText(myItems.getDueTime());
        holder.dueDate.setText(myItems.getdueDate());
        holder.latitude.setText(myItems.getLatitude());
        holder.longitude.setText(myItems.getLongitude());

        // Schedule notification for the homework
        String dueDateString = myItems.getdueDate();
        scheduleNotification(dueDateString, myItems.getName());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Confirmation");
                builder.setMessage("Are you sure you want to delete this item?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference updateRef = db.child("Homework");

                        updateRef.child(uid).orderByChild("name").equalTo(myItems.getName())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot nodeSnapshot: dataSnapshot.getChildren()) {
                                            nodeSnapshot.getRef().removeValue();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        throw databaseError.toException();
                                    }
                                });
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Code to dismiss the alert dialog
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        holder.edite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final DialogPlus dialogPlus=DialogPlus.newDialog(holder.title.getContext())
                       .setContentHolder(new ViewHolder(R.layout.dialogcontent))
                       .setExpanded(true,1500)
                       .create();

               View myView=dialogPlus.getHolderView();
                EditText title=myView.findViewById(R.id.hTitle);
                EditText description=myView.findViewById(R.id.hDescription);
                Button submit=myView.findViewById(R.id.usubmit);

                title.setText(myItems.getName());
                description.setText(myItems.getAddHomeworkDescription());

                dialogPlus.show();


                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String,Object> map=new HashMap<>();
                        map.put("name",title.getText().toString());
                        map.put("addHomeworkDescription",description.getText().toString());

                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference updateRef = db.child("Homework");

                        updateRef.child(uid).orderByChild("name").equalTo(myItems.getName())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot nodeSnapshot: dataSnapshot.getChildren()) {
                                    nodeSnapshot.getRef().child("name").setValue(map.get("name"));
                                    dialogPlus.dismiss();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                throw databaseError.toException();
                            }
                        });
                    }
                });
            }
        });




        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");

        try {
            Date dueDate = format.parse(dueDateString);
            SimpleDateFormat newFormat = new SimpleDateFormat("MMM dd");
            String date = newFormat.format(dueDate);
            holder.dueDate.setText(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView title, description, dueDate, dueTime, latitude, longitude;
        ImageView edite,delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            dueDate = itemView.findViewById(R.id.dueDate);
            dueTime = itemView.findViewById(R.id.time);
            latitude = itemView.findViewById(R.id.latitude);
            longitude = itemView.findViewById(R.id.longitude);

            edite=(ImageView)itemView.findViewById(R.id.options);
            delete=(ImageView)itemView.findViewById(R.id.close);
        }
        public TextView getDueTime() {
            return dueTime;
        }
    }
}
