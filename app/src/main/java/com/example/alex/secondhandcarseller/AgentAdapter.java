package com.example.alex.secondhandcarseller;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Alex on 10/19/2018.
 */

public class AgentAdapter extends RecyclerView.Adapter<AgentAdapter.ViewHolder> {
    private static final String TAG = "CarAdapter";

    private ArrayList<String> AgentID = new ArrayList<>();
    private ArrayList<String> AgentNames = new ArrayList<>();
    private ArrayList<String> AgentIC = new ArrayList<>();
    private ArrayList<String> AgentContact = new ArrayList<>();
    private ArrayList<String> AgentEmail = new ArrayList<>();
    private ArrayList<String> AgentWorkDate = new ArrayList<>();
    private ArrayList<String> AgentStatus = new ArrayList<>();
    private Context mContext;


    public AgentAdapter(Context context, ArrayList<String> agentID, ArrayList<String> agentNames, ArrayList<String> agentIC, ArrayList<String> agentContact, ArrayList<String> agentEmail, ArrayList<String> agentWorkDate, ArrayList<String> agentStatus) {
        AgentNames = agentNames;
        AgentID = agentID;
        AgentIC = agentIC;
        AgentContact = agentContact;
        AgentEmail = agentEmail;
        AgentWorkDate = agentWorkDate;
        AgentStatus = agentStatus;
        mContext = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_agent_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");


        holder.textViewAgentName.setText(AgentNames.get(position));
        holder.textViewAgentNo.setText(AgentContact.get(position));
        holder.textViewStatus.setText(AgentStatus.get(position));
        if (!AgentStatus.get(position).matches("Active"))
            holder.textViewStatus.setTextColor(ContextCompat.getColor(mContext, R.color.Red));
        holder.AgentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, AgentDetailActivity.class);
                intent.putExtra("Aname", AgentNames.get(position));
                intent.putExtra("Aid", AgentID.get(position));
                intent.putExtra("Aic", AgentIC.get(position));
                intent.putExtra("Acontact", AgentContact.get(position));
                intent.putExtra("Aemail", AgentEmail.get(position));
                intent.putExtra("Awork", AgentWorkDate.get(position));
                intent.putExtra("Astatus", AgentStatus.get(position));

                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return AgentNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAgentName, textViewAgentNo, textViewStatus;
        ConstraintLayout AgentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewAgentName = (TextView) itemView.findViewById(R.id.textViewAgentName);
            textViewAgentNo = (TextView) itemView.findViewById(R.id.textViewAgentNo);
            textViewStatus = (TextView) itemView.findViewById(R.id.textViewStatus);
            AgentLayout = itemView.findViewById(R.id.AgentLayout);
        }
    }
}