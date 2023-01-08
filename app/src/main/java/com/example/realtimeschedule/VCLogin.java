package com.example.realtimeschedule;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.time.LocalDateTime;

public class VCLogin extends AppCompatActivity {
    private EditText edtEmail,edtPassword;
    private ProgressBar progressBar;
    private TextView resetPassword, tvRegister;
    private FirebaseAuth firebaseAuth;
    String email, password;
    Button btnLogin;

    FirebaseAuth mAuth;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vclogin);

            edtEmail = findViewById(R.id.email);
            edtPassword = findViewById(R.id.password);
            btnLogin = findViewById(R.id.login);
            progressBar = findViewById(R.id.progressbar);
            resetPassword = findViewById(R.id.resetPassword);
            firebaseAuth = FirebaseAuth.getInstance();
            tvRegister= findViewById(R.id.tvRegister);

        btnLogin.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            email = edtEmail.getText().toString().trim();
            password = edtPassword.getText().toString().trim();

            // validate inputs
            if(!validateInputs()){
                progressBar.setVisibility(View.GONE);
            }
            // all validations complete
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (!task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(VCLogin.this, "Unable to login. "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                // TODO - validate if logged in user is truly admin user

                // login was successful
                progressBar.setVisibility(View.GONE);
                Intent intent = new Intent(VCLogin.this, AdminTasks.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        });

            tvRegister.setOnClickListener(view -> {
                startActivity(new Intent(VCLogin.this, BookingsActivity.class));
            });

            resetPassword.setOnClickListener(v -> {
                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordDialog = new AlertDialog.Builder(v.getContext());
                passwordDialog.setTitle("Reset Password");
                passwordDialog.setMessage("Enter Your Email to Receive Reset Link");
                passwordDialog.setView(resetMail);

                passwordDialog.setPositiveButton("Yes", (dialog, which) -> {
                    String mail = resetMail.getText().toString();
                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(task -> Toast.makeText(VCLogin.this, "Reset Link sent To Your Email", Toast.LENGTH_LONG).show()).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(VCLogin.this, "Error! Reset Link is not sent" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                });

                passwordDialog.setNegativeButton("No", (dialog, which) -> { });

                passwordDialog.create().show();
            });
        }

    /**
     * Validate entered inputs
     */
    public boolean validateInputs(){
        if (email.isEmpty()) {
            edtEmail.setError("Email is required");
            edtEmail.requestFocus();
            return false;
        }
        if (password.isEmpty()) {
            edtPassword.setError("Password is required");
            edtPassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            edtPassword.setError("Password must be more than 6 characters");
            edtPassword.requestFocus();
            return false;
        }

        return  true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}