package com.example.realtimeschedule;

import android.app.Activity;
import android.graphics.Color;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class BookAppointmentAdapter extends SectionedRecyclerViewAdapter<BookAppointmentAdapter.ViewHolder>{

    Activity activity;
    ArrayList<String> sectionList;
    HashMap<String, ArrayList<String>> itemList;
    ArrayList<String> docTimings = new ArrayList<>();
    ArrayList<String> docReserved = new ArrayList<>();

    int selectedSection = -1;
    int selectedItem = -1;

    private DatabaseReference ref;
    String dr_name;


    public BookAppointmentAdapter(Activity activity, ArrayList<String> sectionList, HashMap<String, ArrayList<String>> itemList,
                                  ArrayList<String> docTimings, ArrayList<String> docReserved, String dr_name) {
        this.activity = activity;
        this.sectionList = sectionList;
        this.itemList = itemList;
        this.docTimings = docTimings;
        this.docReserved = docReserved;
        this.dr_name = dr_name;

        ref = FirebaseDatabase.getInstance().getReference("Slots").child("Today");
        notifyDataSetChanged();
    }
    @Override
    public int getSectionCount() {
        return sectionList.size();
    }

    @Override
    public int getItemCount(int section) {
        return itemList.get(sectionList.get(section)).size();
    }

    @Override
    public void onBindHeaderViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.textView.setText(sectionList.get(i));

    }

    @Override
    public int getItemViewType(int section, int relativePosition, int absolutePosition) {
        if(section == 1)
            return 0;
        return super.getItemViewType(section, relativePosition, absolutePosition);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i, int i1, int i2) {
        String sItem = itemList.get(sectionList.get(i)).get(i1);

        viewHolder.textView.setText(sItem);
        viewHolder.textView.setOnClickListener(v -> {
            if (docTimings.contains(sItem)) {
                Toast.makeText(activity, sItem, Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(activity, "Please select an Available slot", Toast.LENGTH_SHORT).show();
            }
            selectedSection = i;
            selectedItem = i1;
            notifyDataSetChanged();
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slot_item, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView= itemView.findViewById(R.id.timeSlotItem);
        }
    }

}
