package com.example.realtimeschedule;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.realtimeschedule.Adapter.SlotsViewHolder;
import com.example.realtimeschedule.Model.Bookings;
import com.example.realtimeschedule.Model.Day_Time;
import com.example.realtimeschedule.Model.SlotsModel;
import com.example.realtimeschedule.ViewHolder.BookingsViewHolder;
//import com.example.realtimeschedule.ViewHolder.Day_TimeViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.TimeZone;
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

import de.hdodenhof.circleimageview.CircleImageView;


public class BookingFormActivity extends AppCompatActivity  {
    private String Sname, email, senderPassword, Semail, date, time, sDesignation, userId;
    private Button submitButton;
    private ImageView yourImage;
    private EditText yourName, yourEmail, DateTime, Time, yourDesignation;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String UserBookingRandomKey, downloadImageUrl;
    private StorageReference userImagesRef;
    private DatabaseReference userRef, spinnerData;
    private ProgressDialog loadingBar;
    private AlarmManager alarmManager;

    Spinner spinner;
    List<String> designations;
    private RecyclerView recyclerView;
    DatabaseReference slotsRef;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerOptions<SlotsModel> options;
    //FirebaseRecyclerAdapter<SlotsModel, SlotsViewHolder> SlotsAdapter;
    DrawerLayout drawerLayout;
    TextView nav_name;

    ArrayList<String> slotTimes = new ArrayList<>();
    ArrayList<String> docTimings = new ArrayList<>();
    ArrayList<String> docReserved = new ArrayList<>();

    HashMap<String, ArrayList<String>> itemList = new HashMap<>();
    BookAppointmentAdapter adapter;

    RecyclerView slotCards;
    CircleImageView img;
    TextView name, exp, degree, place;
    Button btn;
    String dr_name;
    EditText remarks;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private ValueEventListener mListener;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_form_activity);

//        slotCards = findViewById(R.id.slotsRecycler);
//
//
//        mDatabase = FirebaseDatabase.getInstance();
//        mRef = mDatabase.getReference("Slots");
//
//
//        mListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                slotTimes.clear();
//                docTimings.clear();
//                docReserved.clear();
//                for (DataSnapshot slot: snapshot.getChildren()) {
//                    String stand = slot.getValue(String.class);
//
//                    slotTimes.add(slot.getKey());
//                    if (stand.equals("Available"))
//                        docTimings.add(slot.getKey());
//                    else if (stand.equals("Reserved"))
//                        docReserved.add(slot.getKey());
//                }
//                Collections.sort(slotTimes, new Comparator<String>() {
//                    @SuppressLint("SimpleDateFormat")
//                    @Override
//                    public int compare(String o1, String o2) {
//                        try {
//                            return new SimpleDateFormat("hh:mm a").parse(o1).compareTo(new SimpleDateFormat("hh:mm a").parse(o2));
//                        } catch (ParseException e) {
//                            return 0;
//                        }
//                    }
//                });
//                slotCards(slotTimes);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        };
//        mRef.child("Today").addValueEventListener(mListener);
//
//    }
//    private void slotCards(ArrayList<String> slotTimes) {
//        slotCards.setHasFixedSize(true);
//        slotCards.setLayoutManager(new LinearLayoutManager(this));
//
//        ArrayList<String> sectionList = new ArrayList<>();
//        sectionList.add("Today's Slots");
//        itemList.put(sectionList.get(0), slotTimes);
//        adapter = new BookAppointmentAdapter(this, sectionList, itemList, docTimings, docReserved, dr_name);
//        GridLayoutManager manager = new GridLayoutManager(this, 3);
//
//        slotCards.setLayoutManager(manager);
//        adapter.setLayoutManager(manager);
//        slotCards.setAdapter(adapter);
//
//
//
//        alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);



        DateTime = (EditText) findViewById(R.id.editTextDate);
//        Time = (EditText) findViewById(R.id.editTextTime);
        yourDesignation=(EditText) findViewById(R.id.myDesignation);

        spinner= (Spinner) findViewById(R.id.spinner);
        designations= new ArrayList<>();

        spinnerData= FirebaseDatabase.getInstance().getReference();

        spinnerData.child("Designations").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot:snapshot.getChildren()) {
                    String spinnerName = childSnapshot.child("designation").getValue(String.class);
                    designations.add(spinnerName);
                }
                ArrayAdapter<String> adapter= new ArrayAdapter<>(BookingFormActivity.this,
                        android.R.layout.simple_spinner_item,designations);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item= spinner.getSelectedItem().toString();
                yourDesignation.setText(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        DateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimeDialog(DateTime);

            }
        });

//        Time.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                timeDialog(Time);
//
//            }
//        });


        submitButton = (Button) findViewById(R.id.submitRequest);
        yourImage = (ImageView) findViewById(R.id.select_officer_image);
        yourName = (EditText) findViewById(R.id.yourName);
        yourEmail = (EditText) findViewById(R.id.yourEmail);
        loadingBar = new ProgressDialog(this);

        email="ruthmuasya2000@gmail.com";
        senderPassword="grcwtaoqrvhgwyox";
        //

        userImagesRef = FirebaseStorage.getInstance().getReference().child("Service Images");
        userRef = FirebaseDatabase.getInstance().getReference().child("Bookings");

        yourImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("email is ============"+email+ "and  password======"+senderPassword);
                String messageToSend = "Hello Sir, Please check the appointments that have been send to you";
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
                    Toast.makeText(BookingFormActivity.this, "message sent successfully",
                            Toast.LENGTH_SHORT).show();
                } catch (AddressException e) {
                    e.printStackTrace();
                }
                catch (MessagingException e) {
                    e.printStackTrace();

                }
                ValidateData();
            }
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(BookingFormActivity.this, MainActivity.class);

        startActivity(intent);
    }

    private void ValidateData() {

        Sname = yourName.getText().toString();
        Semail = yourEmail.getText().toString();
        sDesignation=yourDesignation.getText() .toString();
        date = DateTime.getText().toString();
//        time = Time.getText().toString();


        if (ImageUri == null) {
            Toast.makeText(this, "User image is mandatory...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Sname)) {
            Toast.makeText(this, "Please write user name...", Toast.LENGTH_SHORT).show();
        } else {
            StoreBookingInformation();
        }
    }

    private void StoreBookingInformation() {


        loadingBar.setTitle("Adding New Booking");
        loadingBar.setMessage("Dear user, please wait as the for your booking request is being sent.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        final StorageReference filePath = userImagesRef.child(ImageUri.getLastPathSegment() + UserBookingRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(BookingFormActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(BookingFormActivity.this, "Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(BookingFormActivity.this, "got the user image Url Successfully...", Toast.LENGTH_SHORT).show();

                            SaveBookingInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void SaveBookingInfoToDatabase() {

        String BOOKINGCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder booking = new StringBuilder();
        Random rnd = new Random();
        while (booking.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * BOOKINGCHARS.length());
            booking.append(BOOKINGCHARS.charAt(index));
        }
        String saltStr = booking.toString();



        HashMap<String, Object> bookingMap = new HashMap<>();
        FirebaseAuth firebaseAuth;
        firebaseAuth= FirebaseAuth.getInstance();
        userId= firebaseAuth.getInstance().getCurrentUser().getUid();
        bookingMap.put("Bid",userId );
        bookingMap.put("date", date);
//        bookingMap.put("time", time);
        bookingMap.put("image", downloadImageUrl);
        bookingMap.put("sname", Sname);
        bookingMap.put("semail", Semail);
        bookingMap.put("designation", sDesignation);

        userRef.child(saltStr).updateChildren(bookingMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(BookingFormActivity.this, MainActivity.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(BookingFormActivity.this, "Booking is successful",
                                    Toast.LENGTH_SHORT).show();

//                            sendReminder(date);
                        } else {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(BookingFormActivity.this, "Error: " + message,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendReminder(String date) {

        String str = date;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter).minusMinutes(2);

        long sendNotification = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() -
                LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("sending reminder");
                sendmail();
            }
        };
        timer.schedule(timerTask, sendNotification);
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


    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();
            yourImage.setImageURI(ImageUri);
        }
    }

    private void dateTimeDialog(EditText date) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfTheMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfTheMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minutes);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm");
                        DateTime.setText(simpleDateFormat.format(calendar.getTime()));

                    }
                };

                new TimePickerDialog(BookingFormActivity.this, timeSetListener, calendar.
                        get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();


            }
        };

        new DatePickerDialog(BookingFormActivity.this, dateSetListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void timeDialog(EditText time) {
        Calendar calendar = Calendar.getInstance();

        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minutes);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                Time.setText(simpleDateFormat.format(calendar.getTime()));

            }
        };

        new TimePickerDialog(BookingFormActivity.this, timeSetListener, calendar.
                get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
    }


}