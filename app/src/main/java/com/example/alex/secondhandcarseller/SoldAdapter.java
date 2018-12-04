package com.example.alex.secondhandcarseller;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by Alex on 12/5/2018.
 */

public class SoldAdapter extends RecyclerView.Adapter<SoldAdapter.ViewHolder> {
    private static final String TAG = "SoldAdapter";

    private ArrayList<String> mCarName = new ArrayList<>();
    private ArrayList<String> mCarImage = new ArrayList<>();
    private ArrayList<String> mCarPrice = new ArrayList<>();
    private ArrayList<String> mCarYear = new ArrayList<>();
    private ArrayList<String> mCarMile = new ArrayList<>();
    private Context mContext;

    public SoldAdapter(ArrayList<String> mCarName, ArrayList<String> mCarImage, ArrayList<String> mCarPrice, ArrayList<String> mCarYear, ArrayList<String> mCarMile, Context mContext) {
        this.mCarName = mCarName;
        this.mCarImage = mCarImage;
        this.mCarPrice = mCarPrice;
        this.mCarYear = mCarYear;
        this.mCarMile = mCarMile;
        this.mContext = mContext;
    }


    @Override
    public SoldAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_sold, parent, false);
        SoldAdapter.ViewHolder holder = new SoldAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(SoldAdapter.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###,###");
        String newPrice = decimalFormat.format(Double.parseDouble(mCarPrice.get(position)));
        Glide.with(mContext)
                .asBitmap()
                .load(mCarImage.get(position))
                .into(holder.imageViewSold);
;
        Double Mile = Double.parseDouble(mCarMile.get(position));
        String mile = decimalFormat.format(Mile)+" KM";


        holder.textViewSoldName.setText(mCarName.get(position));
        holder.textViewSoldPrice.setText("RM " +newPrice);
        holder.textViewSoldYear.setText(mCarYear.get(position));
        holder.textViewSoldMile.setText(mile);

    }

    @Override
    public int getItemCount() {
        return mCarName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewSoldName, textViewSoldYear, textViewSoldPrice, textViewSoldMile;
        ImageView imageViewSold;
        ConstraintLayout LayoutSold;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewSoldName = itemView.findViewById(R.id.textViewSoldName);
            textViewSoldYear = itemView.findViewById(R.id.textViewSoldYear);
            textViewSoldMile = itemView.findViewById(R.id.textViewSoldMile);
            textViewSoldPrice = itemView.findViewById(R.id.textViewSoldPrice);
            imageViewSold = itemView.findViewById(R.id.imageViewSold);
            LayoutSold = itemView.findViewById(R.id.LayoutSold);
        }
    }


}

