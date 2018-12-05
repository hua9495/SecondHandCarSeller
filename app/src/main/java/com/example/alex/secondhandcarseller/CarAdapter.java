package com.example.alex.secondhandcarseller;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 10/19/2018.
 */

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHolder> {
    private static final String TAG = "CarAdapter";

    private ArrayList<String> mCarName = new ArrayList<>();
    private ArrayList<String> mCarImage = new ArrayList<>();
    private ArrayList<String> mCarId = new ArrayList<>();
    private ArrayList<String> mCarBrand = new ArrayList<>();
    private ArrayList<String> mCarPrice = new ArrayList<>();
    private ArrayList<String> mCarColor = new ArrayList<>();
    private ArrayList<String> mCarDesc = new ArrayList<>();
    private ArrayList<String> mCarYear = new ArrayList<>();
    private ArrayList<String> mCarMile = new ArrayList<>();
    private ArrayList<String> mCarPlate = new ArrayList<>();
    private Context mContext;

    public CarAdapter(ArrayList<String> mCarName, ArrayList<String> mCarImage, ArrayList<String> mCarId, ArrayList<String> mCarBrand, ArrayList<String> mCarPrice, ArrayList<String> mCarColor, ArrayList<String> mCarDesc, ArrayList<String> mCarYear, ArrayList<String> mCarMile, ArrayList<String> mCarPlate, Context mContext) {
        this.mCarName = mCarName;
        this.mCarImage = mCarImage;
        this.mCarId = mCarId;
        this.mCarBrand = mCarBrand;
        this.mCarPrice = mCarPrice;
        this.mCarColor = mCarColor;
        this.mCarDesc = mCarDesc;
        this.mCarYear = mCarYear;
        this.mCarMile = mCarMile;
        this.mCarPlate = mCarPlate;
        this.mContext = mContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_car_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(mContext)
                .asBitmap()
                .load(mCarImage.get(position))
                .into(holder.imageViewCar);

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        Double dPrice = Double.parseDouble(mCarPrice.get(position));
        String price = formatter.format(dPrice);


        holder.textViewCar.setText(mCarBrand.get(position)+" "+mCarName.get(position));
        holder.textViewSHPrice.setText(price);
        holder.textViewYear.setText(mCarYear.get(position));

        holder.LayoutCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick:Clicked on: " + mCarName.get(position));
                Toast.makeText(mContext, mCarName.get(position), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(mContext, EditCarActivity.class);
                intent.putExtra("CarID", mCarId.get(position));
                intent.putExtra("CarName", mCarName.get(position));
                intent.putExtra("CarImg", mCarImage.get(position));
                intent.putExtra("CarBrand", mCarBrand.get(position));
                intent.putExtra("CarPrice", mCarPrice.get(position));
                intent.putExtra("CarColor", mCarColor.get(position));
                intent.putExtra("CarDesc", mCarDesc.get(position));
                intent.putExtra("CarYear", mCarYear.get(position));
                intent.putExtra("CarMile", mCarMile.get(position));
                intent.putExtra("CarPlate", mCarPlate.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCarName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCar,textViewYear,textViewSHPrice;
        ImageView imageViewCar;
        ConstraintLayout LayoutCar;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewCar = itemView.findViewById(R.id.textViewCar);
            textViewYear = itemView.findViewById(R.id.textViewYear);
            textViewSHPrice = itemView.findViewById(R.id.textViewSHPrice);
            imageViewCar = itemView.findViewById(R.id.imageViewCar);
            LayoutCar = itemView.findViewById(R.id.LayoutCar);
        }
    }


}

