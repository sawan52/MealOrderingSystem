package com.sawankumarsingh.bookyourmeal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sawankumarsingh.bookyourmeal.R;
import com.sawankumarsingh.bookyourmeal.adapters.OrderFoodAdapter;
import com.sawankumarsingh.bookyourmeal.classes.OrderFood;

import java.util.ArrayList;

public class OrderFoodActivity extends AppCompatActivity {

    private String restau_name, pnr, station_name;
    private RecyclerView foodRecyclerView;
    private ArrayList<OrderFood> orderFoods;
    private OrderFoodAdapter adapter;
    private TextView emptyFoodTextView, final_price;
    private Button _continue;

    private DatabaseReference foodRef;
    private int total = 0;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_food);

        setTitle("Order Food");

        foodRecyclerView = findViewById(R.id.order_food_recycler_view);
        emptyFoodTextView = findViewById(R.id.emptyTextView5);
        _continue = findViewById(R.id.continue_button);
        final_price = findViewById(R.id.final_price_textView);
        constraintLayout = findViewById(R.id.linearLayout);

        restau_name = getIntent().getExtras().getString("RESTAURANT_NAME");
        pnr = getIntent().getExtras().getString("PNR_NUMBER");
        station_name = getIntent().getExtras().getString("STATION_NAME");

        emptyFoodTextView.setVisibility(View.GONE);
        _continue.setVisibility(View.INVISIBLE);
        updateFoodDetails(pnr, restau_name, station_name);

        foodRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderFoods = new ArrayList<>();

        _continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                moveToDetailsConfirmationActivity();
            }
        });
    }

    private void moveToDetailsConfirmationActivity() {

        Intent detailsIntent = new Intent(OrderFoodActivity.this, DetailsConfirmationActivity.class);
        detailsIntent.putExtra("TOTAL_PRICE", total);
        detailsIntent.putExtra("PNR_NUMBER", pnr);
        startActivity(detailsIntent);
    }

    private void updateFoodDetails(String pnr, String restau_name, String station_name) {

        foodRef = FirebaseDatabase.getInstance().getReference().child("book_your_meal").child(pnr).child("station_names")
                .child(station_name).child("restaurants").child(restau_name).child("foods");

        foodRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        OrderFood food = ds.getValue(OrderFood.class);
                        orderFoods.add(food);
                    }
                    adapter = new OrderFoodAdapter(OrderFoodActivity.this, orderFoods, new OrderFoodAdapter.OnTotalPriceChangeListener() {
                        @Override
                        public void onTotalPriceChange(int total_price) {

                            total = total + total_price;
                            if (total > 0) {
                                final_price.setText("Rs." + total);
                                _continue.setVisibility(View.VISIBLE);
                            } else {
                                final_price.setText("Rs.0");
                                _continue.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                    foodRecyclerView.setAdapter(adapter);

                } else {
                    emptyFoodTextView.setVisibility(View.VISIBLE);
                    constraintLayout.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

