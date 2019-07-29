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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.sawankumarsingh.bookyourmeal.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailID, password_editText;
    private Button signUp;
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        signUp = findViewById(R.id.sign_Up);
        emailID = findViewById(R.id.EMAIL_editText);
        password_editText = findViewById(R.id.PASSWORD_editText);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerNewUser();

            }
        });
    }

    private void registerNewUser() {

        String email = emailID.getText().toString();
        String password = password_editText.getText().toString();

        if (email.isEmpty()) {
            emailID.setError("Email is required");
            emailID.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailID.setError("Please enter a valid email");
            emailID.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            password_editText.setError("Password is required");
            password_editText.requestFocus();
            return;
        }

        if (password.length() < 8) {
            password_editText.setError("Minimum password length should be 8");
            password_editText.requestFocus();
            return;
        } else {

            progressDialog.setMessage("Registering User...Please wait!");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                        sendUserToHomePageActivity();
                        finish();
                    } else {
                        progressDialog.dismiss();
                        if (task.getException() instanceof FirebaseNetworkException) {
                            Toast.makeText(RegisterActivity.this, "Check your Internet Connection!", Toast.LENGTH_SHORT).show();
                        } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(RegisterActivity.this, "You are already registered!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });
        }

    }

    private void sendUserToHomePageActivity() {

        Intent loginIntent = new Intent(RegisterActivity.this, HomePageActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(loginIntent);
        finish();
    }
}

