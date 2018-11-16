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
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Alex on 11/11/2018.
 */

public class AdminAdapter  extends RecyclerView.Adapter<AdminAdapter.ViewHolder> {
    private static final String TAG = "AdminAdapter";

    private ArrayList<String> DealerType = new ArrayList<>();
    private ArrayList<String> DealerNo = new ArrayList<>();
    private ArrayList<String> DealerStatus = new ArrayList<>();
    private Context mContext;

    public AdminAdapter(ArrayList<String> dealerType, ArrayList<String> dealerNo,ArrayList<String> dealerStatus, Context mContext) {
        DealerType = dealerType;
        DealerNo = dealerNo;
        DealerStatus=dealerStatus;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public AdminAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_dealer, parent, false);
        AdminAdapter.ViewHolder holder =new AdminAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminAdapter.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");


        holder.tvNumberDealer.setText(DealerNo.get(position));
        holder.tvDealerTitle.setText(DealerType.get(position));

        holder.Dealer_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(DealerNo.get(position).equals(0)){

                }else if(DealerNo.get(position).equals(1)){

                }
                else {

                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return DealerType.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDealerTitle, tvNumberDealer;
        ConstraintLayout Dealer_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNumberDealer = (TextView) itemView.findViewById(R.id.tvNumberDealer);
            tvDealerTitle = (TextView) itemView.findViewById(R.id.tvDealerTitle);
            Dealer_layout = itemView.findViewById(R.id.Dealer_layout);
        }
    }
}
