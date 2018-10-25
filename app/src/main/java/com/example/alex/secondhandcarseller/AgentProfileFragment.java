package com.example.alex.secondhandcarseller;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class AgentProfileFragment extends Fragment {


    public AgentProfileFragment() {
        // Required empty public constructor
    }

    private TextView tvAgentName, tvAgentIC, tvAgentContact, tvAgentEmail, tvBelongTo,tvWorkStart;
    private Button buttonReport;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_agent_profile, container, false);

        tvAgentName=v.findViewById(R.id.tvAgentName);
        tvAgentIC=v.findViewById(R.id.tvAgentIC);
        tvAgentContact=v.findViewById(R.id.tvAgentContact);
        tvAgentEmail=v.findViewById(R.id.tvAgentEmail);
        tvBelongTo=v.findViewById(R.id.tvBelongTo);
        tvWorkStart=v.findViewById(R.id.tvWorkStart);
        buttonReport=v.findViewById(R.id.buttonReport);

        SharedPreferences myPref = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE);
        String email = myPref.getString("Email", null);
        String name = myPref.getString("Name", null);
        String belong = myPref.getString("BelongDealer", null);
        String ic = myPref.getString("ICno", null);
        String contact = myPref.getString("Contact", null);
        String workstart = myPref.getString("WorkStart", null);


        tvAgentName.setText("Name: \t" + name.toString());
        tvAgentIC.setText("IC No: \t" + ic.toString());
        tvAgentContact.setText("Contact No: \t" + contact.toString());
        tvAgentEmail.setText("Email: \t" + email.toString());
        tvBelongTo.setText("Company: \t" + belong.toString());
        tvWorkStart.setText("Work Since: \t" + workstart.toString());
        return v;


    }

}
