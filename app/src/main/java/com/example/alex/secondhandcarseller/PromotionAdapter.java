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
 * Created by Alex on 11/23/2018.
 */

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.ViewHolder> {
    private static final String TAG = "PromotionAdapter";

    private ArrayList<String> CarID = new ArrayList<>();
    private ArrayList<String> CarName = new ArrayList<>();
    private ArrayList<String> CarPrice = new ArrayList<>();
    private ArrayList<String> CarImage = new ArrayList<>();
    private ArrayList<Float> NewPrice = new ArrayList<>();
    private ArrayList<String> Views = new ArrayList<>();
    private String[] getchecked = {" ", " ", " ", " ", " "};
    private Context mContext;

    public PromotionAdapter(ArrayList<String> carID, ArrayList<String> carName, ArrayList<String> carPrice, ArrayList<String> carImage, ArrayList<Float> newPrice, ArrayList<String> views, Context mContext) {
        CarID = carID;
        CarName = carName;
        CarPrice = carPrice;
        CarImage = carImage;
        NewPrice = newPrice;
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
    public void onBindViewHolder(@NonNull final PromotionAdapter.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###,###");
        String newProPrice = decimalFormat.format(Float.parseFloat(CarPrice.get(position)));
        String newPrice = decimalFormat.format(NewPrice.get(position));

        holder.textViewProName.setText(CarName.get(position));
        holder.textViewProPrice.setText("RM " + newProPrice);
        holder.textViewProViews.setText(Views.get(position));
        holder.textViewNewPrice.setText("RM " + newPrice);
        Glide.with(mContext)
                .asBitmap()
                .load(CarImage.get(position))
                .into(holder.imageViewPromotion);

        holder.LayoutPromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkBoxProm.isChecked()) {
                    holder.checkBoxProm.setChecked(false);
                    getchecked[position] = " ";

                } else {
                    holder.checkBoxProm.setChecked(true);
                    getchecked[position] = CarID.get(position);
                }
            }
        });

    }


    public String[] getAllChecked() {
        return getchecked;
    }

    @Override
    public int getItemCount() {
        return CarID.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewProName, textViewProPrice, textViewProViews, textViewNewPrice;
        ConstraintLayout LayoutPromotion;
        ImageView imageViewPromotion;
        CheckBox checkBoxProm;

        public ViewHolder(View itemView) {
            super(itemView);
            checkBoxProm = (CheckBox) itemView.findViewById(R.id.checkBoxProm);
            textViewProName = (TextView) itemView.findViewById(R.id.textViewProName);
            textViewProPrice = (TextView) itemView.findViewById(R.id.textViewProPrice);
            textViewProViews = (TextView) itemView.findViewById(R.id.textViewProViews);
            textViewNewPrice = (TextView) itemView.findViewById(R.id.textViewNewPrice);
            LayoutPromotion = itemView.findViewById(R.id.LayoutPromotion);
            imageViewPromotion = (ImageView) itemView.findViewById(R.id.imageViewPromotion);
        }
    }
}
