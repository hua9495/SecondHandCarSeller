package com.example.alex.secondhandcarseller;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Alex on 11/23/2018.
 */

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.ViewHolder> {
    private static final String TAG = "PromotionAdapter";

    private ArrayList<String> CarID = new ArrayList<>();
    private ArrayList<String> CarName = new ArrayList<>();
    private ArrayList<String> CarPrice = new ArrayList<>();
    private ArrayList<String> Views = new ArrayList<>();
    private Context mContext;

    public PromotionAdapter(ArrayList<String> carID, ArrayList<String> carName, ArrayList<String> carPrice, ArrayList<String> views, Context mContext) {
        CarID = carID;
        CarName = carName;
        CarPrice = carPrice;
        Views = views;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public PromotionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_promotion, parent, false);
        PromotionAdapter.ViewHolder holder = new PromotionAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PromotionAdapter.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");


        holder.textViewProName.setText(CarName.get(position));
        holder.textViewProPrice.setText(CarPrice.get(position));
        holder.textViewProViews.setText(Views.get(position));
        holder.LayoutPromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });


    }

    @Override
    public int getItemCount() {
        return CarID.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewProName, textViewProPrice, textViewProViews;
        ConstraintLayout LayoutPromotion;
        ImageView imageViewPromotion;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewProName = (TextView) itemView.findViewById(R.id.textViewProName);
            textViewProPrice = (TextView) itemView.findViewById(R.id.textViewProPrice);
            textViewProViews = (TextView) itemView.findViewById(R.id.textViewProViews);
            LayoutPromotion = itemView.findViewById(R.id.LayoutPromotion);
            imageViewPromotion = (ImageView) itemView.findViewById(R.id.imageViewPromotion);
        }
    }
}
