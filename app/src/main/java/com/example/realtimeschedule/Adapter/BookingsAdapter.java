package com.example.realtimeschedule.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realtimeschedule.Model.Booking;
import com.example.realtimeschedule.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Booking> bookings;
    protected DatabaseReference bookingsRef;
    public BookingsAdapter(Context context, ArrayList<Booking> bookings) {
        this.context = context;
        this.bookings = bookings;
        bookingsRef = FirebaseDatabase.getInstance().getReference("Bookings");
    }

    @NonNull
    @Override
    public BookingsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.booking_items_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingsAdapter.ViewHolder holder, int position) {
        Booking booking = bookings.get(position);
        Log.d("Polled Booking", booking.toString()+" with user "+booking.getUser().toString());
        holder.bind(booking);
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        TextView clientName, clientEmail, appointmentDate, designation;
        Button btnServed, btnCancel;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initialize layout
            imageView = itemView.findViewById(R.id.officer_image);
            clientName = itemView.findViewById(R.id.clientName);
            clientEmail= itemView.findViewById(R.id.clientEmail);
            appointmentDate= itemView.findViewById(R.id.BookingDate);
            designation= itemView.findViewById(R.id.displayDesignation);
            btnServed= itemView.findViewById(R.id.btnServed);
            btnCancel= itemView.findViewById(R.id.btnCancel);
        }

        public void bind(Booking booking){
            clientName.setText(booking.getUser().getUsername());
            clientEmail.setText(booking.getUser().getEmail());
            appointmentDate.setText(booking.getDate());
            designation.setText(booking.getUser().getUserType());
            Picasso.get().load(booking.getUser().getImage()).into(imageView);

            btnServed.setOnClickListener(view -> {
                int index =  bookings.indexOf(booking);
                bookings.remove(booking);
                // update recyclerview
                notifyItemRemoved(index);
                // update database
                updateServed(booking.getId());
            });
            btnCancel.setOnClickListener(view -> {
                int index = bookings.indexOf(booking);
                bookings.remove(booking);
                // notify recyclerview
                notifyItemRemoved(index);
                // still update as served
                updateServed(booking.getId());
            });
        }

        @Override
        public void onClick(View view) {
            
        }

        private void updateServed(String bookingId){
            bookingsRef.child(bookingId).child("served").setValue(true);
            //
        }
    }
}
