package com.example.alex.secondhandcarseller;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AgentFragment extends Fragment {
    private ListView listViewAgent;


    public AgentFragment() {
        // Required empty public constructor
    }


    private String[] AgentNames = {"Chan Yong Hua", "Ng Poh LI", "Loh JJ"};
    private String[] AgentContact = {"01554455", "0155745646", "456461315"};
    private String[] AgentEmail = {"C_hua9495@hotmail.com", "C_hua9495@gmail.com", "C_hua9495@hotmail.co.uk"};
    private String[] AgentWorkDate = {"22/11/08", "2/11/08", "22/1/08"};
    private String[] AgentStatus = {"On", "Off", "Semi"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_agent, container, false);
        listViewAgent = (ListView) v.findViewById(R.id.listViewAgent);

        AgentAdapter agentAdapter = new AgentAdapter(getActivity(), AgentNames, AgentContact, AgentEmail, AgentWorkDate, AgentStatus);
        listViewAgent.setAdapter(agentAdapter);

        return v;
    }

}
