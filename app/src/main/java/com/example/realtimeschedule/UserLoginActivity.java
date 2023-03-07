package com.example.realtimeschedule;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.realtimeschedule.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class UserLoginActivity extends Activity {
    private EditText email,password;
    private ProgressBar progressBar;
    private TextView tvRegister, tvResetPassword;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference usersRef;
    Button btnLogin;
    static SharedPreferences prefs;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.login);
        progressBar = findViewById(R.id.progressbar);
        tvResetPassword=findViewById(R.id.resetPassword);
        firebaseAuth = FirebaseAuth.getInstance();
        tvRegister = findViewById(R.id.register);
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        prefs = getSharedPreferences("user_details", Context.MODE_PRIVATE);

        // check  if user info is saved in shared preferences
        if (prefs.getString("uid", null) != null && prefs.getString("email", null) != null){
            progressBar.setVisibility(View.GONE);
            Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        tvRegister.setOnClickListener(view -> {
            Intent intent=new Intent(UserLoginActivity.this, UserRegisterActivity.class);
            startActivity(intent);
        });

        tvResetPassword.setOnClickListener(v -> {
            EditText resetMail = new EditText(v.getContext());
            AlertDialog.Builder passwordDialog = new AlertDialog.Builder(v.getContext());
            passwordDialog.setTitle("Reset Password");
            passwordDialog.setMessage("Enter Your Email to Receive Reset Link");
            passwordDialog.setView(resetMail);

            passwordDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String mail = resetMail.getText().toString();
                        firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(UserLoginActivity.this, "Reset Link sent To Your Email", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UserLoginActivity.this, "Error! Reset Link is not sent" + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                }
            });

            passwordDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            passwordDialog.create().show();
        });

        btnLogin.setOnClickListener(view -> {
            String  tex_email = email.getText().toString().trim();
            String tex_password = password.getText().toString().trim();

            if (tex_email.isEmpty()) {
                email.setError("Email is required");
                email.requestFocus();
                return;
            }
            if (tex_password.isEmpty()) {
                password.setError("Password is required");
                password.requestFocus();
                return;
            }

            if (tex_password.length() < 6) {
                password.setError("Password must be more than 6 characters");
                password.requestFocus();
                return;
            }  else {
                login(tex_email,tex_password);
            }

        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void login(String tex_email, String tex_password) {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(tex_email,tex_password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressBar.setVisibility(View.GONE);
                // get user information from firebase
                usersRef.child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            return;
                        }

                        User user = snapshot.getValue(User.class);
                        // save user info to shared preferences
                        saveUser(user);

                        Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(UserLoginActivity.this, "Error getting user information "+error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(UserLoginActivity.this, "Unable to log in. "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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
    }
}