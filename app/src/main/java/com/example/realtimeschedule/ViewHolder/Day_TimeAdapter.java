package com.example.realtimeschedule.ViewHolder;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.realtimeschedule.GiveDayAndTimeAvailable;
import com.example.realtimeschedule.Interface.ItemClickListener;
import com.example.realtimeschedule.IsAvailable;
import com.example.realtimeschedule.Model.Day_Time;
import com.example.realtimeschedule.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Day_TimeAdapter extends RecyclerView.Adapter<Day_TimeAdapter.Day_TimeViewHolder> {
   private final List<Day_Time> dayList;

   public Day_TimeAdapter(List<Day_Time>daysList){
       this.dayList= daysList;
   }

//    public Day_TimeAdapter(ArrayList<com.example.realtimeschedule.Model.Day_Time> CardLocations,
//                           ItemClickListener listener) {
//        this.CardLocations = CardLocations;
//        this.listener = listener;
//    }

//    public Day_TimeAdapter(ArrayList<Day_Time> cardLocations) {
//    }

    @NonNull
    @Override
    public Day_TimeAdapter.Day_TimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .item_week_day, parent, false);
        return new Day_TimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Day_TimeAdapter.Day_TimeViewHolder holder, int position) {
        String titleCard= dayList.get(position).getTitle();

        holder.setData(titleCard);

    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }

    public static class Day_TimeViewHolder extends RecyclerView.ViewHolder{
        private final TextView titleItem;

        public Day_TimeViewHolder(@NonNull View itemView) {
            super(itemView);

            titleItem= itemView.findViewById(R.id.card_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(itemView.getContext(), GiveDayAndTimeAvailable.class);
                    itemView.getContext().startActivity(intent);
                }
            });
        }

        public void setData(String titleCard) {
            titleItem.setText(titleCard);
        }
    }
}