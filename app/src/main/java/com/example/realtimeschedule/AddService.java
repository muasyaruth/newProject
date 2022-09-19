package com.example.realtimeschedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class AddService extends AppCompatActivity {
    private String Sname, saveCurrentDate, saveCurrentTime, Semail;
    private Button AddNewOfficerButton;
    private ImageView InputOfficerImage;
    private EditText InputOfficerName, InputOfficerEmail;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_service);


        AddNewOfficerButton = (Button) findViewById(R.id.add_new_officer);
        InputOfficerImage = (ImageView) findViewById(R.id.select_officer_image);
        InputOfficerName = (EditText) findViewById(R.id.nameOfOfficer);
        InputOfficerEmail=(EditText) findViewById(R.id.emailOfOfficer);
//        InputProductDescription = (EditText) findViewById(R.id.product_description);
//        InputProductPrice = (EditText) findViewById(R.id.product_price);
        loadingBar = new ProgressDialog(this);

        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Service Images");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Services");


        //Toast.makeText(this, CategoryName, Toast.LENGTH_LONG).show();

        InputOfficerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });

        AddNewOfficerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateProductData();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(AddService.this, MainActivity.class);

        startActivity(intent);
    }

    private void ValidateProductData()
    {

//        Description = InputProductDescription.getText().toString();
//        Price = InputProductPrice.getText().toString();
        Sname = InputOfficerName.getText().toString();
        Semail = InputOfficerEmail.getText().toString();


        if (ImageUri == null)
        {
            Toast.makeText(this, "Officer image is mandatory...", Toast.LENGTH_SHORT).show();
        }
//        else if (TextUtils.isEmpty(Description))
//        {
//            Toast.makeText(this, "Please write product description...", Toast.LENGTH_SHORT).show();
//        }
//        else if (TextUtils.isEmpty(Price))
//        {
//            Toast.makeText(this, "Please write product Price...", Toast.LENGTH_SHORT).show();
//        }
        else if (TextUtils.isEmpty(Sname))
        {
            Toast.makeText(this, "Please write officer name...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreProductInformation();
        }
    }

    private void StoreProductInformation()
    {

        loadingBar.setTitle("Adding New Service");
        loadingBar.setMessage("Dear Admin, please wait while we are adding the new service.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        java.util.Calendar calender = Calendar.getInstance();

        java.text.SimpleDateFormat currentDate = new java.text.SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calender.getTime());

        java.text.SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime= currentTime.format((calender.getTime()));

        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(AddService.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(AddService.this, "Service Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(AddService.this, "got the Service image Url Successfully...", Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfoToDatabase() {

        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();

        HashMap<String, Object> officerMap = new HashMap<>();
        officerMap.put("pid", productRandomKey);
        officerMap.put("date", saveCurrentDate);
        officerMap.put("time", saveCurrentTime);
//        productMap.put("description", Description);
        officerMap.put("image", downloadImageUrl);
        //productMap.put("category", CategoryName);
//        productMap.put("price", Price);
        officerMap.put("sname", Sname);
        officerMap.put("semail", Semail);

        ProductsRef.child(saltStr).updateChildren(officerMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(AddService.this, ServiceActivity.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(AddService.this, "Service is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AddService.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            ImageUri = data.getData();
            InputOfficerImage.setImageURI(ImageUri);
        }
    }
}