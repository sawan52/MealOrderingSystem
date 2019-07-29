package com.sawankumarsingh.bookyourmeal.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sawankumarsingh.bookyourmeal.R;

public class CheckYourOrderActivity extends AppCompatActivity {

    private TextView food_price, food_status, delivery_boy_number, warning,
            quote, totalCost, orderStatus, deliveryBoyNumber, orderConfirm;
    private ImageView orderCnfImage;
    private String pnr;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_your_order);

        setTitle("Your Order");

        pnr = getIntent().getExtras().getString("pnr_number");

        orderCnfImage = findViewById(R.id.orderConfirmImageView);
        quote = findViewById(R.id.quoteTextView);
        totalCost = findViewById(R.id.totalCostTextView);
        orderStatus = findViewById(R.id.orderStatusTextView);
        deliveryBoyNumber = findViewById(R.id.deliveryBoyNumberTextView);
        orderConfirm = findViewById(R.id.orderConfirmTextView);
        food_price = findViewById(R.id.food_price);
        food_status = findViewById(R.id.food_status);
        delivery_boy_number = findViewById(R.id.delivery_boy_number);
        warning = findViewById(R.id.warning_text);
        warning.setVisibility(View.GONE);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("book_your_meal").child(pnr);

        updateYourOrder(pnr);
    }

    private void updateYourOrder(final String pnr) {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Your Order Details").exists()) {

                    String price = dataSnapshot.child("Your Order Details").child("totalPrice").getValue().toString();
                    String status = dataSnapshot.child("Your Order Details").child("orderStatus").getValue().toString();
                    String number = dataSnapshot.child("Your Order Details").child("deliveryBoyNumber").getValue().toString();

                    food_price.setText(price);
                    food_status.setText(status);
                    delivery_boy_number.setText(number);
                } else {
                    quote.setVisibility(View.INVISIBLE);
                    totalCost.setVisibility(View.INVISIBLE);
                    orderConfirm.setVisibility(View.INVISIBLE);
                    deliveryBoyNumber.setVisibility(View.INVISIBLE);
                    food_price.setVisibility(View.INVISIBLE);
                    orderStatus.setVisibility(View.INVISIBLE);
                    food_status.setVisibility(View.INVISIBLE);
                    orderCnfImage.setVisibility(View.INVISIBLE);
                    food_price.setVisibility(View.INVISIBLE);
                    food_status.setVisibility(View.INVISIBLE);
                    delivery_boy_number.setVisibility(View.INVISIBLE);
                    warning.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
