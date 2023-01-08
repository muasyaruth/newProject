package com.example.realtimeschedule;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class UserLoginActivity extends Activity {
    private EditText email,password;
    private ProgressBar progressBar;
    private TextView tvRegister, tvResetPassword;
    private FirebaseAuth firebaseAuth;
    Button btnLogin;

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


        tvRegister.setOnClickListener(view -> {
            Intent intent=new Intent(UserLoginActivity.this,Register.class);
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

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void login(String tex_email, String tex_password) {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(tex_email,tex_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UserLoginActivity.this, "Unable to log in. Error: "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}