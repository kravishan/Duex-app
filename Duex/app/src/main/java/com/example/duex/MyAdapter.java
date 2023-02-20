package com.example.duex;
// This use for display
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.BreakIterator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
//        holder.dueDate.setText(myItems.getdueDate());

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
    //MyViewHolder class to holad view reference for ecery item
    static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView title, description, dueDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            dueDate = itemView.findViewById(R.id.dueDate);

        }
    }
}
