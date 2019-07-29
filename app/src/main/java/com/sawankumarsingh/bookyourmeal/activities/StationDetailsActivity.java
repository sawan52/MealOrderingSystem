package com.sawankumarsingh.bookyourmeal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sawankumarsingh.bookyourmeal.R;
import com.sawankumarsingh.bookyourmeal.adapters.StationNameAdapter;
import com.sawankumarsingh.bookyourmeal.classes.StationNames;

import java.util.ArrayList;

public class StationDetailsActivity extends AppCompatActivity {

    private TextView dateTextView, pnrTextView, trainNumberTextView, boardingTextView, emptyTextView;
    private RecyclerView listOfStationsRecyclerView;
    private String pnr_number;

    private ArrayList<StationNames> stationNames;
    private StationNameAdapter adapter;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_details);

        listOfStationsRecyclerView = findViewById(R.id.station_name_recycler_view);

        emptyTextView = findViewById(R.id.emptyTextView);
        dateTextView = findViewById(R.id.date);
        pnrTextView = findViewById(R.id.pnr);
        trainNumberTextView = findViewById(R.id.train_number);
        boardingTextView = findViewById(R.id.boarding);

        emptyTextView.setVisibility(View.GONE);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("book_your_meal");

        //Get the bundle
        Bundle bundle = getIntent().getExtras();

        //Extract the data…
        pnr_number = bundle.getString("PNR_number");

        updateData(pnr_number);
        fetchStationDetails(pnr_number);
        listOfStationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        stationNames = new ArrayList<>();

    }

    public void fetchStationDetails(final String pnr_number) {

        databaseReference = FirebaseDatabase.getInstance().getReference().child("book_your_meal").child(pnr_number).child("station_names");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        StationNames names = ds.getValue(StationNames.class);
                        stationNames.add(names);
                    }

                    adapter = new StationNameAdapter(StationDetailsActivity.this, stationNames, pnr_number);
                    listOfStationsRecyclerView.setAdapter(adapter);
                } else {
                    emptyTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void updateData(final String pnr_number) {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(pnr_number).exists()) {

                    String boarding = dataSnapshot.child(pnr_number).child("boarding").getValue().toString();
                    String date = dataSnapshot.child(pnr_number).child("date").getValue().toString();
                    String pnr_num = dataSnapshot.child(pnr_number).child("pnr_number").getValue().toString();
                    String train_name = dataSnapshot.child(pnr_number).child("train_name").getValue().toString();
                    String train_number = dataSnapshot.child(pnr_number).child("train_number").getValue().toString();

                    boardingTextView.setText(boarding);
                    dateTextView.setText(date);
                    pnrTextView.setText(pnr_num);
                    trainNumberTextView.setText(train_number);

                    setTitle(train_name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu_order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.check_my_order) {
            sendUserToCheckYourOrderActivity();
        }
        return true;
    }

    private void sendUserToCheckYourOrderActivity() {

        Intent orderIntent = new Intent(StationDetailsActivity.this, CheckYourOrderActivity.class);
        orderIntent.putExtra("pnr_number", pnr_number);
        startActivity(orderIntent);
    }
}

