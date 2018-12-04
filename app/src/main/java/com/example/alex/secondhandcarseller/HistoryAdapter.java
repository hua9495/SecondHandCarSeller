package com.example.alex.secondhandcarseller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Alex on 12/4/2018.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private static final String TAG = "HistoryAdapter";

    private ArrayList<String> CarName = new ArrayList<>();
    private ArrayList<String> CarImage = new ArrayList<>();
    private ArrayList<Float> NewPrice = new ArrayList<>();
    private ArrayList<String> Date = new ArrayList<>();
    private ArrayList<String> Rate = new ArrayList<>();
    private Context mContext;

    public HistoryAdapter(ArrayList<String> carName, ArrayList<String> carImage, ArrayList<Float> newPrice, ArrayList<String> date, ArrayList<String> rate, Context mContext) {
        CarName = carName;
        CarImage = carImage;
        NewPrice = newPrice;
        Date = date;
        Rate = rate;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_history, parent, false);
        HistoryAdapter.ViewHolder holder = new HistoryAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final HistoryAdapter.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###,###");
        String newPrice = decimalFormat.format(NewPrice.get(position));

        holder.textViewHisName.setText(CarName.get(position));
        holder.textViewHisPrice.setText("RM " + newPrice);
        holder.textViewUntil.setText(Date.get(position));
        holder.textViewDisRate.setText(Rate.get(position) + " %");
        Glide.with(mContext)
                .asBitmap()
                .load(CarImage.get(position))
                .into(holder.imageViewHistory);


    }

    @Override
    public int getItemCount() {
        return CarName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewHisName, textViewHisPrice, textViewUntil, textViewDisRate;
        ConstraintLayout LayoutHistory;
        ImageView imageViewHistory;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewHisName = (TextView) itemView.findViewById(R.id.textViewHisName);
            textViewHisPrice = (TextView) itemView.findViewById(R.id.textViewHisPrice);
            textViewUntil = (TextView) itemView.findViewById(R.id.textViewUntil);
            textViewDisRate = (TextView) itemView.findViewById(R.id.textViewDisRate);
            LayoutHistory = itemView.findViewById(R.id.LayoutHistory);
            imageViewHistory = (ImageView) itemView.findViewById(R.id.imageViewHistory);
        }
    }
}
