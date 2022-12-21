package com.example.realtimeschedule.ViewHolder;


import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realtimeschedule.BookingDetails;
import com.example.realtimeschedule.Interface.ItemClickListener;
import com.example.realtimeschedule.R;

public class BookingsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView clientName, currentTime,clientEmail, appointmentDate, appointmentTime,designation;
    public ImageView imageView;
    public String Sname, senderPassword, Semail, date, timeT, sDesignation;
    public ItemClickListener listener;
    public ArrayAdapter adapter;
    public Button btnServed, btnCancel;
    public AlertDialog dialog;
    public EditText timePicker;
//    public Button save,cancel;
    public AlertDialog.Builder dialogBuilder;


    public BookingsViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.officer_image);
        clientName = (TextView) itemView.findViewById(R.id.clientName);
        clientEmail=(TextView) itemView.findViewById(R.id.clientEmail);
        appointmentDate=(TextView) itemView.findViewById(R.id.BookingDate);
//        appointmentTime=(TextView) itemView.findViewById(R.id.BookingTime);
        designation=(TextView) itemView.findViewById(R.id.displayDesignation);
        timePicker= (EditText)itemView.findViewById(R.id.perfectTime);
        btnCancel= (Button) itemView.findViewById(R.id.saveTime);
        btnServed= (Button) itemView.findViewById(R.id.btnServed);
        btnCancel= (Button) itemView.findViewById(R.id.btnCancel);
    }


    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition(), false);
    }


}
