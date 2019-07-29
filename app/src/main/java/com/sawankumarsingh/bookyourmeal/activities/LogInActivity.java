package com.sawankumarsingh.bookyourmeal.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.sawankumarsingh.bookyourmeal.R;

public class LogInActivity extends AppCompatActivity {

    private TextView registerActivity;
    private EditText emailId, passwordEditText;
    private Button signIn;

    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        registerActivity = findViewById(R.id.goToRegisterActivity);
        emailId = findViewById(R.id.email_id);
        passwordEditText = findViewById(R.id.password);
        signIn = findViewById(R.id.sign_inButton);

        registerActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendToRegisterActivity();
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verifyingEmailAndSigningIn();
            }
        });

    }

    private void sendToRegisterActivity() {
        Intent registerIntent = new Intent(LogInActivity.this, RegisterActivity.class);
        startActivity(registerIntent);

    }

    private void verifyingEmailAndSigningIn() {

        String email = emailId.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty()) {
            emailId.setError("Email is required");
            emailId.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailId.setError("Please enter a valid email");
            emailId.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return;
        }

        if (password.length() < 8) {
            passwordEditText.setError("Minimum password length should be 8");
            passwordEditText.requestFocus();
            return;
        } else {

            progressDialog.setMessage("Signing User...Please wait!");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(LogInActivity.this, "Sign In Successful!", Toast.LENGTH_SHORT).show();
                        sendUserToHomePageActivity();
                        finish();
                    } else {
                        progressDialog.dismiss();
                        if (task.getException() instanceof FirebaseNetworkException) {
                            Toast.makeText(LogInActivity.this, "Check your Internet Connection!", Toast.LENGTH_SHORT).show();
                        } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(LogInActivity.this, "You are already registered!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LogInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });
        }

    }

    private void sendUserToHomePageActivity() {

        Intent intent = new Intent(LogInActivity.this, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}

