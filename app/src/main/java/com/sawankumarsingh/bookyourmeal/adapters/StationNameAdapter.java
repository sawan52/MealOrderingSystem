package com.sawankumarsingh.bookyourmeal.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sawankumarsingh.bookyourmeal.R;
import com.sawankumarsingh.bookyourmeal.activities.RestaurantsActivity;
import com.sawankumarsingh.bookyourmeal.classes.StationNames;

import java.util.ArrayList;

public class StationNameAdapter extends RecyclerView.Adapter<StationNameAdapter.StationViewHolder> {

    private Context mContext;
    private ArrayList<StationNames> stationNames;
    private String pnrNumber;

    public StationNameAdapter(Context mContext, ArrayList<StationNames> stationNames, String pnrNumber) {
        this.mContext = mContext;
        this.stationNames = stationNames;
        this.pnrNumber = pnrNumber;
    }

    @NonNull
    @Override
    public StationNameAdapter.StationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.train_stations_names, viewGroup, false);
        return new StationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StationNameAdapter.StationViewHolder stationViewHolder, int position) {

        stationViewHolder.arrival_time.setText(stationNames.get(position).getArrival_time());
        stationViewHolder.halt_time.setText(stationNames.get(position).getHalt_time());
        stationViewHolder.station_name.setText(stationNames.get(position).getStation_name());

        stationViewHolder.add_station_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String stationName = stationViewHolder.station_name.getText().toString();

                Intent restauIntent = new Intent(mContext, RestaurantsActivity.class);
                restauIntent.putExtra("stationName", stationName);
                restauIntent.putExtra("PNR_NUMBER", pnrNumber);
                restauIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(restauIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return stationNames.size();
    }

    public static class StationViewHolder extends RecyclerView.ViewHolder {

        TextView station_name, halt_time, arrival_time;
        ImageButton add_station_button;

        public StationViewHolder(@NonNull View itemView) {
            super(itemView);

            station_name = itemView.findViewById(R.id.station_name);
            halt_time = itemView.findViewById(R.id.halt_time);
            arrival_time = itemView.findViewById(R.id.arrival_time);

            add_station_button = itemView.findViewById(R.id.add_imageButton);

        }
    }
}

