package com.example.duex;
// This use for display
import static com.google.firebase.database.FirebaseDatabase.getInstance;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.FirebaseDatabase;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.BreakIterator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {

        MyItems myItems = items.get(position);
        holder.title.setText(myItems.getName());
        holder.description.setText(myItems.getAddHomeworkDescription());
        holder.dueTime
                .setText(myItems.getDueTime());
//        holder.dueDate.setText(myItems.getdueDate());


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
                                            nodeSnapshot.getRef().child("name").removeValue();
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
                       .setExpanded(true,2100)
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


        String dueDateString = myItems.getdueDate();
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

        private final TextView title, description, dueDate, dueTime;
        ImageView edite,delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            dueDate = itemView.findViewById(R.id.dueDate);
            dueTime = itemView.findViewById(R.id.time);

            edite=(ImageView)itemView.findViewById(R.id.options);
            delete=(ImageView)itemView.findViewById(R.id.close);
        }
        public TextView getDueTime() {
            return dueTime;
        }
    }
}
