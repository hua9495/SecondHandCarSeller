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
    private Context mContext;

    public CarAdapter(Context context, ArrayList<String> CarName, ArrayList<String> CarImage, ArrayList<String> CarId) {
        mCarName = CarName;
        mCarImage = CarImage;
        mContext = context;
        mCarId = CarId;
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

        holder.textViewCar.setText(mCarName.get(position));
        holder.LayoutCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick:Clicked on: " + mCarName.get(position));
                Toast.makeText(mContext, mCarName.get(position), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(mContext, EditCarActivity.class);
                intent.putExtra("CarID", mCarId.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCarName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCar;
        ImageView imageViewCar;
        ConstraintLayout LayoutCar;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewCar = itemView.findViewById(R.id.textViewCar);
            imageViewCar = itemView.findViewById(R.id.imageViewCar);
            LayoutCar = itemView.findViewById(R.id.LayoutCar);
        }
    }


}

