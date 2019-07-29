package com.sawankumarsingh.bookyourmeal.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sawankumarsingh.bookyourmeal.R;
import com.sawankumarsingh.bookyourmeal.activities.OrderFoodActivity;
import com.sawankumarsingh.bookyourmeal.classes.RestaurantNames;

import java.util.ArrayList;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

    private Context mContext;
    private ArrayList<RestaurantNames> restaurantNames;
    private String pnr, station_name;

    public RestaurantAdapter(Context mContext, ArrayList<RestaurantNames> restaurantNames, String pnr, String station_name) {
        this.mContext = mContext;
        this.restaurantNames = restaurantNames;
        this.pnr = pnr;
        this.station_name = station_name;
    }

    @NonNull
    @Override
    public RestaurantAdapter.RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.restaurant_names, viewGroup, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RestaurantAdapter.RestaurantViewHolder restaurantViewHolder, int position) {

        restaurantViewHolder.restauPlace.setText(restaurantNames.get(position).getAddress());
        restaurantViewHolder.restauFoodPrice.setText(restaurantNames.get(position).getAmount());
        Glide.with(mContext).load(restaurantNames.get(position).getImage()).into(restaurantViewHolder.image);
        restaurantViewHolder.restauName.setText(restaurantNames.get(position).getName());
        restaurantViewHolder.restauOrderTime.setText(restaurantNames.get(position).getTime());

        restaurantViewHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = restaurantViewHolder.restauName.getText().toString();
                Intent orderFoodIntent = new Intent(mContext, OrderFoodActivity.class);
                orderFoodIntent.putExtra("PNR_NUMBER", pnr);
                orderFoodIntent.putExtra("STATION_NAME", station_name);
                orderFoodIntent.putExtra("RESTAURANT_NAME", name);
                orderFoodIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(orderFoodIntent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurantNames.size();
    }

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout constraintLayout;
        ImageView image;
        TextView restauName, restauPlace, restauOrderTime, restauFoodPrice;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);

            constraintLayout = itemView.findViewById(R.id.constraintLayout);
            image = itemView.findViewById(R.id.restau_image);
            restauName = itemView.findViewById(R.id.restau_name);
            restauPlace = itemView.findViewById(R.id.restau_address);
            restauOrderTime = itemView.findViewById(R.id.restau_order_time);
            restauFoodPrice = itemView.findViewById(R.id.restau_min_order_amount);

        }
    }
}

