package com.sawankumarsingh.bookyourmeal.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sawankumarsingh.bookyourmeal.R;
import com.sawankumarsingh.bookyourmeal.adapters.RestaurantAdapter;
import com.sawankumarsingh.bookyourmeal.classes.RestaurantNames;

import java.util.ArrayList;
import java.util.Objects;

public class RestaurantsActivity extends AppCompatActivity {

    private String stationName, pnrNumber;
    private RecyclerView recyclerView;
    private ArrayList<RestaurantNames> restaurantNames;
    private RestaurantAdapter adapter;
    private TextView emptyTextView2;

    private DatabaseReference restauRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);

        recyclerView = findViewById(R.id.restaurant_recycler_view);
        emptyTextView2 = findViewById(R.id.emptyTextView2);
        emptyTextView2.setVisibility(View.GONE);

        stationName = Objects.requireNonNull(getIntent().getExtras()).getString("stationName");
        pnrNumber = getIntent().getExtras().getString("PNR_NUMBER");

        setTitle(stationName);

        updateRestaurantDetails(pnrNumber, stationName);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        restaurantNames = new ArrayList<>();
    }

    public void updateRestaurantDetails(final String pnrNumber, final String stationName) {

        restauRef = FirebaseDatabase.getInstance().getReference().child("book_your_meal").child(pnrNumber).child("station_names")
                .child(stationName).child("restaurants");
        restauRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        RestaurantNames restauNames = ds.getValue(RestaurantNames.class);
                        restaurantNames.add(restauNames);
                    }
                    adapter = new RestaurantAdapter(RestaurantsActivity.this, restaurantNames, pnrNumber, stationName);
                    recyclerView.setAdapter(adapter);

                } else {
                    emptyTextView2.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
