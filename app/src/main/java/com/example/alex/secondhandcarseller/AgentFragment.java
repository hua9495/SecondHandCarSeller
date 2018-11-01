package com.example.alex.secondhandcarseller;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class AgentFragment extends Fragment {

    private ArrayList<String> AgentID = new ArrayList<>();
    private ArrayList<String> AgentNames = new ArrayList<>();
    private ArrayList<String> AgentIC = new ArrayList<>();
    private ArrayList<String> AgentContact = new ArrayList<>();
    private ArrayList<String> AgentEmail = new ArrayList<>();
    private ArrayList<String> AgentWorkDate = new ArrayList<>();
    private ArrayList<String> AgentStatus = new ArrayList<>();

    private ProgressBar loadAgent;
    private RecyclerView recycleViewAgent;
    private FloatingActionButton fabAddAgent;
    private String Url = "https://dewy-minuses.000webhostapp.com/AgentOfD.php";
    private String subid, dealerid;

    public AgentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_agent, container, false);
        recycleViewAgent = (RecyclerView) v.findViewById(R.id.recycleViewAgent);
        loadAgent = (ProgressBar) v.findViewById(R.id.loadAgent);
        fabAddAgent = (FloatingActionButton) v.findViewById(R.id.fabAddAgent);

        SharedPreferences myPref = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE);
        String checkid = myPref.getString("ID", null);
        subid = checkid.substring(0, 1);
        if (subid.equals("A")) {
            dealerid = myPref.getString("BelongDealer", null);
        } else {
            dealerid = myPref.getString("ID", null);
        }



        fabAddAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),AddAgentActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        AgentNames.clear();
        AgentContact.clear();
        AgentEmail.clear();
        AgentWorkDate.clear();
        AgentStatus.clear();
        AgentID.clear();
        AgentIC.clear();
        loadAgent(getView());
        super.onResume();

    }

    private void initRecyclerView(View v) {

        AgentAdapter adapter = new AgentAdapter(getActivity(), AgentID, AgentNames, AgentIC, AgentContact, AgentEmail, AgentWorkDate, AgentStatus);
        recycleViewAgent.setAdapter(adapter);
        recycleViewAgent.setLayoutManager(new LinearLayoutManager(getActivity()));

    }


    private void loadAgent(final View v) {
        loadAgent.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("agent");
                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            //follow index
                            String agentid = object.getString("id");
                            String agentname = object.getString("name");
                            String agentic = object.getString("ic");
                            String agentemail = object.getString("email");
                            String agentcontact = object.getString("contact");
                            String agentdate = object.getString("date");
                            String Status = object.getString("status");

                            AgentNames.add(agentname);
                            AgentContact.add(agentcontact);
                            AgentEmail.add(agentemail);
                            AgentWorkDate.add(agentdate);
                            AgentStatus.add(Status);
                            AgentID.add(agentid);
                            AgentIC.add(agentic);
                            ;
                        }
                        initRecyclerView(v);

                        loadAgent.setVisibility(View.GONE);
                    } else {
                        loadAgent.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "No Agent in your list", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                    loadAgent.setVisibility(View.GONE);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getActivity(), "Error " + error.toString(), Toast.LENGTH_LONG).show();
                        loadAgent.setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("dealerid", dealerid);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);


    }

}
