package com.sawankumarsingh.bookyourmeal.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sawankumarsingh.bookyourmeal.R;
import com.sawankumarsingh.bookyourmeal.classes.OrderFood;

import java.util.ArrayList;

public class OrderFoodAdapter extends RecyclerView.Adapter<OrderFoodAdapter.OrderFoodViewHolder> {

    private Context mContext;
    private ArrayList<OrderFood> orderFoods;

    private OnTotalPriceChangeListener mListener;

    // added listener...
    public OrderFoodAdapter(Context mContext, ArrayList<OrderFood> orderFoods, OnTotalPriceChangeListener listener) {
        this.mContext = mContext;
        this.orderFoods = orderFoods;
        mListener = listener;
    }


    @NonNull
    @Override
    public OrderFoodAdapter.OrderFoodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.food_lists, viewGroup, false);
        return new OrderFoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderFoodAdapter.OrderFoodViewHolder orderFoodViewHolder, final int position) {

        orderFoodViewHolder.name.setText(orderFoods.get(position).getName());
        orderFoodViewHolder.price.setText(orderFoods.get(position).getPrice());
        orderFoodViewHolder.extra.setText(orderFoods.get(position).getExtra_items());

        Glide.with(mContext).load(orderFoods.get(position).getFood_type()).placeholder(R.drawable.login_page_image).into(orderFoodViewHolder.imageView);

        orderFoodViewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int total_price = 0;

                String tempMoney = orderFoodViewHolder.price.getText().toString();
                int money = Integer.parseInt(tempMoney.substring(3));
                int count_item = Integer.parseInt(orderFoodViewHolder.count.getText().toString());

                count_item++;
                orderFoodViewHolder.count.setText(String.valueOf(count_item));
                total_price = total_price + money;

                mListener.onTotalPriceChange(total_price);

            }
        });

        orderFoodViewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int total_price = 0;

                String tempMoney = orderFoodViewHolder.price.getText().toString();
                int money = Integer.parseInt(tempMoney.substring(3));
                int count_item = Integer.parseInt(orderFoodViewHolder.count.getText().toString());

                if (count_item <= 0) {
                    Toast.makeText(mContext, "Items can't be less than 0", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    count_item--;
                    orderFoodViewHolder.count.setText(String.valueOf(count_item));
                    total_price = total_price - money;

                    mListener.onTotalPriceChange(total_price);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderFoods.size();
    }

    public interface OnTotalPriceChangeListener {
        void onTotalPriceChange(int total_price);
    }

    public static class OrderFoodViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name, price, extra, count;
        Button add, remove;

        public OrderFoodViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.food_type);
            name = itemView.findViewById(R.id.food_name);
            price = itemView.findViewById(R.id.food_price);
            extra = itemView.findViewById(R.id.extra_items);

            count = itemView.findViewById(R.id.item_count_textView);
            add = itemView.findViewById(R.id.add_item_button);
            remove = itemView.findViewById(R.id.remove_item_button);

        }
    }
}

