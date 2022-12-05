package com.example.realtimeschedule.ViewHolder;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realtimeschedule.Interface.ItemClickListener;
import com.example.realtimeschedule.R;

public class AppointmentsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView clientFinalName, clientEmail, appointmentFinalDate, appointmentTime,designation;
    public ImageView imageView;
    public String Sname, senderPassword, Semail, date, timeT, sDesignation;
    public ItemClickListener listener;
    public Button details, save, cancel;
    public AlertDialog dialog;
    public EditText timePicker;
    //    public Button save,cancel;
    public AlertDialog.Builder dialogBuilder;

    public AppointmentsViewHolder(@NonNull View itemView) {
        super(itemView);
//        imageView = (ImageView) itemView.findViewById(R.id.officer_image);
//        clientEmail=(TextView) itemView.findViewById(R.id.clientEmail);
//        details=itemView.findViewById(R.id.giveTime);
        clientFinalName = (TextView) itemView.findViewById(R.id.usersName);
        appointmentFinalDate=(TextView) itemView.findViewById(R.id.appointmentDate);
        appointmentTime=(TextView) itemView.findViewById(R.id.appointmentTime);
//        designation=(TextView) itemView.findViewById(R.id.usersDesignation);
//        timePicker= (EditText)itemView.findViewById(R.id.perfectTime);
//        save= (Button) itemView.findViewById(R.id.saveTime);
//        cancel= (Button) itemView.findViewById(R.id.cancelTime);



    }

    @Override
    public void onClick(View view) {

    }
}
