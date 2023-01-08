package com.example.realtimeschedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.realtimeschedule.Model.Appointments;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class BookingDetails extends AppCompatActivity {
    ImageView image;
    TextView name, email, date;
    BookingItem bookingItem;
    String newName, newDate;
    EditText editText;
    Button ok;
    String secEmail, senderPassword;


    DatabaseReference appointmentsref;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);


        image= findViewById(R.id.imageViewUser);
        name= findViewById(R.id.textViewPassedName);
        email= findViewById(R.id.textViewPassedEmail);
        date= findViewById(R.id.textViewPassedDate);
        editText= findViewById(R.id.EditTextTime);
        ok= findViewById(R.id.buttonOk);

        secEmail="ruthmuasya2000@gmail.com";
        senderPassword="grcwtaoqrvhgwyox";

        appointmentsref= FirebaseDatabase.getInstance().getReference();

        Intent intent= getIntent();

        String passedImage=intent.getStringExtra("Image");
        String passedName=intent.getStringExtra("Name");
        String passedEmail=intent.getStringExtra("Email");
        String passedDate=intent.getStringExtra("Date");
        //String passeDes=intent.getStringExtra("Designation");

        try {
            Picasso.get().load(passedImage).into(image);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        name.setText(passedName);
        email.setText(passedEmail);
        date.setText(passedDate);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                sendToAppointments();

                System.out.println("email is ============"+email+ "and  password======"+senderPassword);
                String messageToSend = "Hello Sir, Check appointments";
                String messageToSend2 = "Hello "+passedName+", Check appointments";
                Properties properties = new Properties();
                properties.put("mail.smtp.auth","true");
                properties.put("mail.smtp.ssl.enable","true");
                properties.put("mail.smtp.host","smtp.gmail.com");
                properties.put("mail.smtp.port","465");

                Session session= Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        //return super.getPasswordAuthentication();
                        return  new PasswordAuthentication(secEmail, senderPassword);
                    }
                });

                try {
                    MimeMessage message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(secEmail));
                    message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse
                            ("fstfITVC@gmail.com"));//37869793VC
                    message.setSubject("Booking appointments");
                    message.setText(messageToSend);
                    Transport.send(message);
                    Toast.makeText(BookingDetails.this, "message sent successfully", Toast.LENGTH_SHORT).show();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }

                try {
                    MimeMessage message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(secEmail));
                    message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(passedEmail));//37869793VC
                    message.setSubject("Booking appointments");
                    message.setText(messageToSend2);
                    Transport.send(message);
                    Toast.makeText(BookingDetails.this, "message sent successfully", Toast.LENGTH_SHORT).show();
                } catch (AddressException e) {
                    e.printStackTrace();
                }
                catch (MessagingException e) {
                    e.printStackTrace();

                }

                insertData();
            }
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog(editText);
            }

        });




    }

    private void timePickerDialog(EditText time) {
        Calendar calendar = Calendar.getInstance();

        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minutes);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                editText.setText(simpleDateFormat.format(calendar.getTime()));

            }
        };

        new TimePickerDialog(BookingDetails.this, timeSetListener, calendar.
                get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
    }

    private void insertData() {

        Intent intent= new Intent(BookingDetails.this, AppointmentsActivity.class);

        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = userPref.edit();
        editor.putString("NameOfClient",  name.toString());
        editor.putString("DateOfAppointment",  date.toString());
        editor.apply();
        intent.putExtra("NameOfClient",name.toString());
        intent.putExtra("DateOfAppointment",date.toString());
        startActivity(intent);
        //intent.putExtra("Designation",date.toString());
        String Name, des, time, Date, id;
        time= editText.getText().toString();
        id= appointmentsref.push().getKey();

        Appointments appointments= new Appointments(time);
        appointmentsref.child("Appointments").child(id).setValue(appointments).
                addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(BookingDetails.this, "Success", Toast.LENGTH_SHORT).show();

            }
        });

        System.out.println("=================== appointments time======: "+appointments.getPasseddate()+"T"+appointments.getTime());

    }

}