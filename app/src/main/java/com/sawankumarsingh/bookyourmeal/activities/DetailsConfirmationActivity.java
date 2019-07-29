package com.sawankumarsingh.bookyourmeal.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sawankumarsingh.bookyourmeal.R;

import java.util.HashMap;

public class DetailsConfirmationActivity extends AppCompatActivity {

    private EditText passenger_name, email_id, mobile_number;
    private TextView coach_name, seat_number;
    private String pnr;
    private Button save_continue;
    private int totalPrice = 0;

    private DatabaseReference detailsRef;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_confirmation);

        setTitle("Details Confirmation");

        pnr = getIntent().getExtras().getString("PNR_NUMBER");
        totalPrice = getIntent().getExtras().getInt("TOTAL_PRICE");

        progressDialog = new ProgressDialog(this);

        passenger_name = findViewById(R.id.passenger_name_editText);
        email_id = findViewById(R.id.email_id_editText);
        mobile_number = findViewById(R.id.mobile_number_editText);
        coach_name = findViewById(R.id.coach_name_textView);
        seat_number = findViewById(R.id.seat_number_textView);

        save_continue = findViewById(R.id.save_and_continue);

        detailsRef = FirebaseDatabase.getInstance().getReference().child("book_your_meal").child(pnr);

        updateData();

        save_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                confirmFoodOrder();
            }
        });
    }

    private void confirmFoodOrder() {

        String name = passenger_name.getText().toString();
        String id = email_id.getText().toString();
        String number = mobile_number.getText().toString();
        String coach = coach_name.getText().toString();
        String seatNum = seat_number.getText().toString();

        if (TextUtils.isEmpty(name)) {
            passenger_name.setError("Enter your name first");
            passenger_name.requestFocus();
            return;
        } else if (TextUtils.isEmpty(number)) {
            mobile_number.setError("Enter your mobile number");
            mobile_number.requestFocus();
            return;
        } else if (TextUtils.isEmpty(id)) {
            email_id.setError("Enter your email Id");
            email_id.requestFocus();
            return;
        } else {
            progressDialog.setMessage("Updating your Order");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            HashMap<String, Object> confirmOrder = new HashMap<>();
            confirmOrder.put("passengerName", name);
            confirmOrder.put("emailId", id);
            confirmOrder.put("mobileNumber", number);
            confirmOrder.put("coachName", coach);
            confirmOrder.put("seatNumber", seatNum);
            confirmOrder.put("orderStatus", "Preparing your order, Please wait...");
            confirmOrder.put("totalPrice", "Rs." + totalPrice);
            confirmOrder.put("deliveryBoyNumber", "not available now!");

            detailsRef.child("Your Order Details").updateChildren(confirmOrder)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(DetailsConfirmationActivity.this, "Your Order is confirmed.", Toast.LENGTH_LONG).show();
                                sendUserToHomePage();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(DetailsConfirmationActivity.this, "Something went wrong, Please try again later!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    private void sendUserToHomePage() {

        Intent intent = new Intent(DetailsConfirmationActivity.this, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void updateData() {

        detailsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String coach_nam = dataSnapshot.child("coach").getValue().toString();
                String seat_num = dataSnapshot.child("seat_number").getValue().toString();

                coach_name.setText(coach_nam);
                seat_number.setText(seat_num);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
