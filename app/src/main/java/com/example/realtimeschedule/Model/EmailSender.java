package com.example.realtimeschedule.Model;

import android.os.StrictMode;
import android.util.Log;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {
    private String sender = "ruthmuasya2000@gmail.com", receiver,
            subject = "Booking appointments",
            message = "Hello Sir/Madam, Please Check appointments that have been sent to you",
            password = "grcwtaoqrvhgwyox";//


    public EmailSender setReceiver(String receiver) {
        this.receiver = receiver;
        return this;
    }

    public EmailSender setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public EmailSender setMessage(String message) {
        this.message = message;
        return this;
    }


    // sending message
    public boolean send(){
        boolean emailSent = false;
        Properties properties = new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.ssl.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","465");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return  new PasswordAuthentication(sender, password);
                //return super.getPasswordAuthentication();
            }
        });
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(receiver));
            message.setSubject(subject);
            message.setText(this.message);
            //send email in a seperate thread
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(message);
                        Log.d("SEND_MAIL", "Email sent successfully");
                    } catch (MessagingException e) {
                        Log.d("SEND_MAIL", "Error sending email");
                        e.printStackTrace();
                    }
                }
            }).start();

            emailSent = true;

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return  emailSent;
    }

}
