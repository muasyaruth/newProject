package com.example.realtimeschedule;


import static com.google.firebase.storage.FirebaseStorage.getInstance;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.realtimeschedule.Model.Bookings;
import com.example.realtimeschedule.ViewHolder.BookingsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

//How To Sort My Recyclerview According To Date And Time With Examples
public class BookingsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);



        recyclerView = findViewById(R.id.service_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        //Query db = FirebaseDatabase.getInstance().getReference().child("Email").child(mCurrentUser.getUid()).child("Appointment").orderByChild("date");


    }

    private void DateTimePair() {
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference bookingsref = FirebaseDatabase.getInstance().getReference().child("Bookings");

        FirebaseRecyclerOptions<Bookings> options = new FirebaseRecyclerOptions.Builder<Bookings>()
                .setQuery(bookingsref, Bookings.class)
                .build();

        FirebaseRecyclerAdapter<Bookings, BookingsViewHolder> adapter = new FirebaseRecyclerAdapter
                <Bookings, BookingsViewHolder>(options) {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected void onBindViewHolder(@NonNull BookingsViewHolder bookingsViewHolder,
                                            int i, @NonNull Bookings bookings) {

                Bookings item= getItem(i);

                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                String id = firebaseAuth.getUid();

                bookingsViewHolder.clientName.setText(bookings.getSname());
                bookingsViewHolder.clientEmail.setText(bookings.getSemail());
                bookingsViewHolder.designation.setText(bookings.getDesignation());
                bookingsViewHolder.appointmentDate.setText(bookings.getDate());
//                bookingsViewHolder.appointmentTime.setText(bookings.getTime());
                bookingsViewHolder.designation.setText(bookings.getDesignation());
                Picasso.get().load(bookings.getImage()).into(bookingsViewHolder.imageView);


                System.out.println("==========================="+bookings.getDate());//ebu run
//You need to parse date in dd-MM-yyyy pattern first and then format it to the pattern of your choice.
//                List<String> str = Collections.singletonList(bookings.getDate());
//                List<String> str = new ArrayList<>();
//                str.add(bookings.getDate());
//
//                for(int j =0;j<str.size();j++){
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
////                    List<LocalDateTime> dateTime = Collections.singletonList(LocalDateTime.parse(str.get(j), formatter));
//
//                    List<LocalDateTime> dateTime = new ArrayList<>();
//                    dateTime.add(LocalDateTime.parse(str.get(j),formatter));
//                    dateTime.forEach(n->{
//                        long millis  = n.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
//                        long timeNow = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
//                        if (timeNow<millis){
//                            System.out.println("schedule reminder");
//
//                            long sendNotification = millis- LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
//
//                            /*schedule reminder*/
//                            Timer timer = new Timer();
//                            TimerTask timerTask = new TimerTask() {
//                                @Override
//                                public void run() {
//                                    System.out.println("Scheduling reminder------------------------:)");
//                                    sendmail();
//                                }
//                            };
//
//                            try {
//                                timer.schedule(timerTask, sendNotification);
//                            }catch(IllegalArgumentException ex){
//                                ex.printStackTrace();
//                            }
////                            sendmail();
//                        }else {
//                            System.out.println("time past");
//                        }
//                        System.out.println("======================date list===="+n.toString());//connect simu tuone
//
//
//                    });
//
//                }
                
                 bookingsViewHolder.reschedule.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });

                bookingsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent= new Intent(BookingsActivity.this, BookingDetails.class);
                        intent.putExtra("Name", item. getSname());
                        intent.putExtra("Image", item. getImage());
                        intent.putExtra("Email", item. getSemail());
                        intent.putExtra("Date", item. getDate());
                        intent.putExtra("BookingId", id);
                        startActivity(intent);

                    }
                });

            }

            @NonNull
            @Override
            public BookingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.booking_items_layout, parent, false);
                BookingsViewHolder holder = new BookingsViewHolder(view);
                return holder;


            }

        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }

    private void sendmail() {
        String email="ruthmuasya2000@gmail.com";
        String senderPassword="grcwtaoqrvhgwyox";
        System.out.println("email is ============"+email+ "and  password======"+senderPassword);
        String messageToSend = "Hello Sir, Please check the appointments that have been send to you 15 minutes before start";
        Properties properties = new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.ssl.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","465");

        Session session= Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                //return super.getPasswordAuthentication();
                return  new PasswordAuthentication(email, senderPassword);
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse
                    ("fstfITVC@gmail.com"));//37869793VC
            message.setSubject("Booking appointments");
            message.setText(messageToSend);
            Transport.send(message);
        } catch (AddressException e) {
            e.printStackTrace();
        }
        catch (MessagingException e) {
            e.printStackTrace();

        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }
}