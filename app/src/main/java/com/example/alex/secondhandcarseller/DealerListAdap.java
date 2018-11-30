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
 * Created by Alex on 11/18/2018.
 */

public class DealerListAdap  extends RecyclerView.Adapter<DealerListAdap.ViewHolder> {
    private static final String TAG = "DealerListAdap";

    private ArrayList<String> Dealerid = new ArrayList<>();
    private ArrayList<String> DealerName = new ArrayList<>();
    private ArrayList<String> DealerStatus = new ArrayList<>();
    private ArrayList<String> DealerEmail = new ArrayList<>();
    private ArrayList<String> DealerLoctaion = new ArrayList<>();
    private ArrayList<String> DealerContact = new ArrayList<>();
    private ArrayList<String> Reason = new ArrayList<>();
    private ArrayList<String> Pic = new ArrayList<>();
    private Context mContext;

    public DealerListAdap(ArrayList<String> dealerid, ArrayList<String> dealerName, ArrayList<String> dealerStatus, ArrayList<String> dealerEmail, ArrayList<String> dealerLoctaion, ArrayList<String> dealerContact, ArrayList<String> pic,  ArrayList<String> reason,Context mContext) {
        Dealerid = dealerid;
        DealerName = dealerName;
        DealerStatus = dealerStatus;
        DealerEmail = dealerEmail;
        DealerLoctaion = dealerLoctaion;
        DealerContact = dealerContact;
        Reason = reason;
        Pic = pic;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public DealerListAdap.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dealer_list_adap, parent, false);
        DealerListAdap.ViewHolder holder =new DealerListAdap.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DealerListAdap.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");


        holder.tvDealername.setText(DealerName.get(position));
        holder.tvPic.setText(Pic.get(position));
        holder.tvDealerlocation.setText(DealerLoctaion.get(position));
        holder.dealerListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext,AdminDealerDetail.class);
                intent.putExtra("DealerID", Dealerid.get(position));
                intent.putExtra("DealerName", DealerName.get(position));
                intent.putExtra("DealerStatus", DealerStatus.get(position));
                intent.putExtra("DealerEmail", DealerEmail.get(position));
                intent.putExtra("DealerLoctaion", DealerLoctaion.get(position));
                intent.putExtra("DealerContact", DealerContact.get(position));
                intent.putExtra("Pic", Pic.get(position));
                intent.putExtra("Reason", Reason.get(position));
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return DealerName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDealername, tvPic,tvDealerlocation;
        ConstraintLayout dealerListLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDealername = (TextView) itemView.findViewById(R.id.tvDealername);
            tvPic = (TextView) itemView.findViewById(R.id.tvPic);
            tvDealerlocation = (TextView) itemView.findViewById(R.id.tvDealerlocation);
            dealerListLayout = itemView.findViewById(R.id.dealerListLayout);
        }
    }
}
