package com.example.realtimeschedule;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    private Button register;
    private TextView signup;
    private EditText editTextFullname, editTextEmail, editTextPassword, Phone, location, address;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String UserRegisterRandomKey, downloadImageUrl;
    private StorageReference userImagesRef;
    private String username, email;
    private ImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        profile= findViewById(R.id.profilePic);
        userImagesRef = FirebaseStorage.getInstance().getReference().child("User Images");


        mAuth = FirebaseAuth.getInstance();
        userRef= FirebaseDatabase.getInstance().getReference();
        register =  findViewById(R.id.register);
        signup = findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, UserLoginActivity.class);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String username = editTextFullname.getText().toString().trim();
                final String email = editTextEmail.getText().toString().trim();
                final String phone = Phone.getText().toString().trim();
                final String password = editTextPassword.getText().toString().trim();
                final String Location =location.getText().toString().trim();
                final String Address = address.getText().toString().trim();

                if (username.isEmpty()) {
                    editTextEmail.setError("Email is required");
                    editTextEmail.requestFocus();
                    return;
                } else if (email.isEmpty()) {
                    editTextEmail.setError("Email is required");
                    editTextEmail.requestFocus();
                    return;
                } else if (password.isEmpty()) {
                    editTextPassword.setError("Password is required");
                    editTextPassword.requestFocus();
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    editTextEmail.setError("Please provide valid email");
                    editTextEmail.requestFocus();
                    return;

                } else if (password.length() < 6) {
                    editTextPassword.setError("Password should be more than 6 characters");
                    editTextPassword.requestFocus();
                    return;
                } else if (phone.isEmpty()) {
                    Phone.setError("phone is required");
                    Phone.requestFocus();
                    return;
                } else if (Address.isEmpty()) {
                    address.setError("Address is required");
                    address.requestFocus();
                    return;
                }
                else if (Location.isEmpty()) {
                    location.setError("Location is required");
                    location.requestFocus();
                    return;
                }
                if (ImageUri == null) {
                    Toast.makeText(Register.this, "User image is mandatory...", Toast.LENGTH_SHORT).show();
                }
                else {
                    final StorageReference filePath = userImagesRef.child(ImageUri.getLastPathSegment() + UserRegisterRandomKey + ".jpg");

                    final UploadTask uploadTask = filePath.putFile(ImageUri);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String message = e.toString();
                            Toast.makeText(Register.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(Register.this, "Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

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
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        downloadImageUrl = task.getResult().toString();

                                        Toast.makeText(Register.this, "got the user image Url Successfully...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });

                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressBar.setVisibility(View.VISIBLE);
                                String currentUserId=mAuth.getCurrentUser().getUid();

                                /*RootRef.child("Users").child(currentUserId).setValue("");*/
                                HashMap<String, String> users=new HashMap<>();
                                users.put("uid", currentUserId);
                                users.put("location", Location);
                                users.put("address", Address);
                                users.put("name", username);
                                users.put("email", email);
                                users.put("phone", phone);
                                users.put("password",password);
                                users.put("image", downloadImageUrl);

                                //specify if user is admin or normal user
                                users.put("isUser", "1");

                                //set path
                                userRef.child("Users").child(currentUserId).setValue(users);
                                Toast.makeText(getApplicationContext(), "Account Created Successfully",Toast.LENGTH_SHORT).show();
                                //loadingbar.dismiss();
                                progressBar.setVisibility(View.GONE);
                                Intent intent = new Intent(Register.this, MainActivity.class);
                                intent.putExtra("currentPhone", phone);
                                intent.putExtra("currentName", username);
                                intent.putExtra("currentEmail", email);
                                startActivity(intent);
                                //startActivity(new Intent(getApplicationContext(),Login.class));

                            }
                            else{
                                String message=task.getException().toString();
                                Toast.makeText(getApplicationContext(), "Error : " + message, Toast.LENGTH_SHORT).show();
                                //loadingbar.dismiss();

                                progressBar.setVisibility(View.GONE);
                            }

                        }
                    });

                }

            }
        });
        editTextFullname = (EditText) findViewById(R.id.fullname);
        editTextEmail    = (EditText) findViewById(R.id.email);
        Phone=findViewById(R.id.phone);
        editTextPassword = (EditText) findViewById(R.id.password);
        location=(EditText)findViewById(R.id.Location);
        address=(EditText)findViewById(R.id.Address);

//        username= editTextFullname.getText().toString();
//        email= editTextEmail.getText().toString();

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
    }
    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();
            profile.setImageURI(ImageUri);
        }
    }
}