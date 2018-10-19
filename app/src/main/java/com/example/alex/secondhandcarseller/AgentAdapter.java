package com.example.alex.secondhandcarseller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Alex on 10/19/2018.
 */

public class AgentAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] AgentNames, AgentContact, AgentEmail, AgentWorkDate, AgentStatus;

    public AgentAdapter(@NonNull Context context, String[] agentNames, String[] agentContact, String[] agentEmail, String[] agentWorkDate, String[] agentStatus) {
        super(context, R.layout.adapter_agent_layout);

        this.AgentNames = agentNames;
        this.AgentContact = agentContact;
        this.AgentEmail = agentEmail;
        this.AgentWorkDate = agentWorkDate;
        this.AgentStatus = agentStatus;
        this.context = context;
    }

    @Override
    public int getCount() {
        return AgentNames.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.adapter_agent_layout, null, true);
        TextView textViewAgentName = (TextView) v.findViewById(R.id.textViewAgentName);
        TextView textViewAgentEmail = (TextView) v.findViewById(R.id.textViewAgentEmail);
        TextView textViewAgentNo = (TextView) v.findViewById(R.id.textViewAgentNo);
        TextView textViewStatus = (TextView) v.findViewById(R.id.textViewStatus);
        TextView textViewWorkDate = (TextView) v.findViewById(R.id.textViewWorkDate);

        textViewAgentName.setText(AgentNames[position]);
        textViewAgentEmail.setText(AgentEmail[position]);
        textViewAgentNo.setText(AgentContact[position]);
        textViewStatus.setText(AgentStatus[position]);
        textViewWorkDate.setText(AgentWorkDate[position]);

        return v;
    }

}