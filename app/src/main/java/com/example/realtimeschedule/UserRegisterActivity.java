package com.example.realtimeschedule;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.realtimeschedule.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class UserRegisterActivity extends AppCompatActivity {
    private Button btnRegister;
    private TextView tvLogin;
    private EditText edtUsername, edtEmail, edtPhone, edtPassword, edtCPassword;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private static final int GalleryPick = 1;
    private static final String TAG = "UserRegisterActivity";
    private Uri imageUri;
    private StorageReference userImagesRef;
    private String username, email, phone, password, cPassword,
            userType,userRegisterRandomKey, downloadImageUrl;
    private ImageView profilePic;
    private Spinner spinner;
    private ArrayAdapter adapter;
    User user;
    static SharedPreferences prefs;
    ArrayList<String> userTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        profilePic = findViewById(R.id.profilePic);
        userImagesRef = FirebaseStorage.getInstance().getReference().child("User Images");
        mAuth = FirebaseAuth.getInstance();
        userRef= FirebaseDatabase.getInstance().getReference().child("Users");
        userRegisterRandomKey = userRef.push().getKey();
        edtUsername = findViewById(R.id.username);
        edtEmail = findViewById(R.id.email);
        edtPhone = findViewById(R.id.phone);
        edtPassword = findViewById(R.id.password);
        edtCPassword = findViewById(R.id.cPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);
        progressBar = findViewById(R.id.progressbar);
        spinner = findViewById(R.id.spUserTypes);
        prefs = getSharedPreferences("user_details", Context.MODE_PRIVATE);

        // initialize user types spinner, sorted in order of increasing priority
        userTypes = new ArrayList<>();
        userTypes.add("Student");
        userTypes.add("School President");
        userTypes.add("Lecturer");
        userTypes.add("COD");
        userTypes.add("Dean");
        userTypes.add("Registrar");

        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, userTypes);
        spinner.setAdapter(adapter);
        // make first item selected by default
        spinner.setSelection(0);
        // listen for user type selection
        spinner.setOnItemSelectedListener(onUserTypeSelected);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });

        btnRegister.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            username = edtUsername.getText().toString().trim();
            email = edtEmail.getText().toString().trim();
            phone = edtPhone.getText().toString().trim();
            password = edtPassword.getText().toString().trim();
            cPassword = edtCPassword.getText().toString().trim();
            // validate user inputs
            if(!validateInputs()){
                progressBar.setVisibility(View.GONE);
                return;
            };
            // upload profile picture and initialize downloadImageUrl variable
            uploadProfilePicture().addOnCompleteListener(uploadImageTask -> {
                if(!uploadImageTask.isSuccessful()) { // error uploading image
                    Toast.makeText(this, "Unable to upload your profile picture. Please try again", Toast.LENGTH_SHORT).show();
                }
                
                // proceed to register user
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                    if (!task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Error creating your account. Error : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        return;
                    }

                    // save created user to real time database
                    user = new User();
                    user.setUid(mAuth.getCurrentUser().getUid());
                    user.setUsername(username);
                    user.setEmail(email);
                    user.setPhone(phone);
                    user.setImage(downloadImageUrl);
                    user.setPriority(spinner.getSelectedItemPosition()+1); // make first priority '1' instead of '0'
                    user.setAdmin(false); // user not an admin
                    //save user
                    userRef.child(user.getUid()).setValue(user.toMap());
                    // save user info to shared preferences
                    saveUser(user);
                    Toast.makeText(getApplicationContext(), "Account Created Successfully",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                });

            });
        });

        // navigate to user login activity
        tvLogin.setOnClickListener(view -> {
            startActivity(new Intent(UserRegisterActivity.this, UserLoginActivity.class));
        });
    }

    /**
     * Validate all user inputs
     */
    private boolean validateInputs() {
        if (imageUri == null) {
            Toast.makeText(UserRegisterActivity.this, "Please select a profile picture", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (username.isEmpty()) {
            edtUsername.setError("Username is required");
            edtUsername.requestFocus();
            return false;
        }
        if (email.isEmpty()) {
            edtEmail.setError("Email is required");
            edtEmail.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Please provide valid email");
            edtEmail.requestFocus();
            return false;
        }
        if (phone.isEmpty()) {
            edtPhone.setError("phone is required");
            edtPhone.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            edtPassword.setError("Password is required");
            edtPassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            edtPassword.setError("Password should be more than 6 characters");
            edtPassword.requestFocus();
            return false;
        }
        if(!password.equals(cPassword)){
            edtCPassword.setError("Password and Confirm Password do not match");
            edtCPassword.requestFocus();
            return false;
        }
        if (userType.isEmpty()){
            Toast.makeText(this, "Please select user type", Toast.LENGTH_SHORT).show();
            return false;
        }

        return  true;
    }

    /**
     * Upload profile picture.
     * This will upload profile photo, get download image url and initialize it.
     * @return
     */
    private UploadTask uploadProfilePicture(){
        final StorageReference filePath = userImagesRef.child(imageUri.getLastPathSegment() + userRegisterRandomKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(imageUri);
        uploadTask.addOnFailureListener(e -> {
            Toast.makeText(UserRegisterActivity.this, "Error uploading profile picture. Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }).addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(UserRegisterActivity.this, "Profile photo uploaded Successfully...", Toast.LENGTH_SHORT).show();
            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return filePath.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    downloadImageUrl = task.getResult().toString();
                    Log.d(TAG, "Got download image url as "+downloadImageUrl);
                }
            });
        });

        return uploadTask;
    }

    /**
     * Handle selection of user type from spinner
     */
    private AdapterView.OnItemSelectedListener onUserTypeSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            // initialize userType
            userType = adapterView.getItemAtPosition(i).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            Log.d(TAG, "No item has been selected.");
        }
    };

    /**
     * Opens photo gallery
     */
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
            imageUri = data.getData();
            profilePic.setImageURI(imageUri);
        }
    }

    /**
     * Save user details to shared preferences
     * @param user Logged in user to save to shared preferences
     */
    private static void saveUser(User user){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("uid", user.getUid());
        editor.putString("username", user.getUsername());
        editor.putString("email", user.getEmail());
        editor.putString("image", user.getImage());
        editor.putString("userType", user.getUserType());
        editor.apply();
        editor.commit();
    }
}