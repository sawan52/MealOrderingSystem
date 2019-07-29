package com.sawankumarsingh.bookyourmeal.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sawankumarsingh.bookyourmeal.R;

public class HomePageActivity extends AppCompatActivity {

    private EditText pnrNumber;
    private String pnr;
    private ProgressDialog progressDialog;

    private DatabaseReference rootRef;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Book Your Meal");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (!isConnected()) buildDialog(this).show();
        else {
            setContentView(R.layout.activity_home_page);

            progressDialog = new ProgressDialog(this);

            pnrNumber = findViewById(R.id.pnr_editText);
            Button pnrVerify = findViewById(R.id.pnr_verify_button);

            rootRef = FirebaseDatabase.getInstance().getReference().child("book_your_meal");

            pnrVerify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    verifyPNR();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.send_feedback) {
            sendFeedbackMail();
        }
        if (item.getItemId() == R.id.sign_out) {
            signOut();
        }
        return true;
    }

    private void signOut() {

        mAuth.signOut();
        finish();
        sendUserToLogInActivity();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser == null) {
            sendUserToLogInActivity();
        }
    }

    private void sendFeedbackMail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:gfirebase86@gmail.com"));// only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback on Book your Meal App.");
        startActivity(intent);

    }


    private void sendUserToLogInActivity() {

        Intent loginIntent = new Intent(HomePageActivity.this, LogInActivity.class);
        startActivity(loginIntent);
        finish();
    }

    public boolean isConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) {
                return true;
            } else return false;

        } else
            return false;
    }

    public AlertDialog.Builder buildDialog(Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or Wifi to access this. Press OK to Exit");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        return builder;
    }

    public void verifyPNR() {
        pnr = pnrNumber.getText().toString();

        if (TextUtils.isEmpty(pnr)) {
            pnrNumber.setError("Enter PNR number first");
            return;
        }
        if (!(pnr.length() == 10)) {
            pnrNumber.setError("PNR number should be 10 digits");
            return;
        } else {

            progressDialog.setMessage("Verifying PNR...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            rootRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(pnr).exists()) {

                        progressDialog.dismiss();
                        sendToStationDetailsActivity(pnr);
                    } else {

                        progressDialog.dismiss();
                        Toast.makeText(HomePageActivity.this, "PNR number is incorrect!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }


    public void sendToStationDetailsActivity(String pnr) {

        Intent intent = new Intent(HomePageActivity.this, StationDetailsActivity.class);
        //Create the bundle
        Bundle bundle = new Bundle();

        //Add your data to bundle
        bundle.putString("PNR_number", pnr);

        //Add the bundle to the intent
        intent.putExtras(bundle);

        startActivity(intent);
    }
}

