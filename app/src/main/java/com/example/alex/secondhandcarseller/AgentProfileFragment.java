package com.example.alex.secondhandcarseller;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class AgentProfileFragment extends Fragment {

    private Button buttonLogout;
    public static final int PERMISSION_REQUEST = 200;
    public static final int REQUEST_CODE = 100;
    private ProgressBar pbScanning;

    public AgentProfileFragment() {
        // Required empty public constructor
    }

    private TextView tvAgentName, tvAgentIC, tvAgentContact, tvAgentEmail, tvBelongTo, tvWorkStart;
    private Button buttonReport;
    private String agentID,dealerID, appID;
    private ArrayList<String> arrAppID=new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_agent_profile, container, false);
        buttonLogout = (Button) v.findViewById(R.id.buttonLogout);
        tvAgentName = v.findViewById(R.id.tvAgentName);
        tvAgentIC = v.findViewById(R.id.tvAgentIC);
        tvAgentContact = v.findViewById(R.id.tvCustContact);
        tvAgentEmail = v.findViewById(R.id.tvCustEmail);
        tvBelongTo = v.findViewById(R.id.tvBelongTo);
        tvWorkStart = v.findViewById(R.id.tvWorkStart);
        buttonReport = v.findViewById(R.id.buttonReport);
        pbScanning = v.findViewById(R.id.pbScanning);
        pbScanning.setVisibility(View.INVISIBLE);
        getAppointment(getString(R.string.get_appointment_url));
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SharedPreferences.Editor user = getActivity().getSharedPreferences("My_Pref", Context.MODE_PRIVATE).edit();
                user.putString("ID", null);
                user.apply();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });

        SharedPreferences myPref;
        myPref = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE);
        agentID = myPref.getString("ID", null);
        dealerID = myPref.getString("BelongDealer", null);
        String email = myPref.getString("Email", null);
        String name = myPref.getString("Name", null);
        String belong = myPref.getString("BelongDealer", null);
        String ic = myPref.getString("ICno", null);
        String contact = myPref.getString("Contact", null);
        String workstart = myPref.getString("WorkStart", null);


        tvAgentName.setText(getString(R.string.Name) + "\t" + name);
        tvAgentIC.setText(getString(R.string.ic) + "\t" + ic);
        tvAgentContact.setText(getString(R.string.contact_no) + "\t" + contact);
        tvAgentEmail.setText(getString(R.string.email_dot) + "\t" + email);
        tvBelongTo.setText(getString(R.string.company) + "\t" + belong);
        tvWorkStart.setText(getString(R.string.start_date) + "\t" + workstart);
        return v;


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.scan_qrcode, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        } else {
            Intent intent = new Intent(getActivity(), ScanActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onPause() {

        super.onPause();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

        }
    }

    //to get QR code
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                final Barcode barcode = data.getParcelableExtra("barcode");
                String codeAppID = barcode.displayValue.substring(0, 5);
                String codeAgentID = barcode.displayValue.substring(6, 12);
                updateApp(codeAppID, codeAgentID);

            }
        }
    }
    private void getAppointment( String url) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String successA = jsonObject.getString("successA");
                            String successB = jsonObject.getString("successB");
                            //String message = jsonObject.getString("message");
                            JSONArray jsonArray = jsonObject.getJSONArray("BOOKING");
                            //if HAVE RECORD
                            if (successA.equals("1") || successB.equals("1")) {
                                //retrive the record

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject userResponse = jsonArray.getJSONObject(i);
                                    appID = userResponse.getString("appID");
                                    arrAppID.add(appID);

                                }
                            }

                        } catch (JSONException e) {
                            //if no internet
                            if (!CarFragment.isConnected(getContext())) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("Connection Error");
                                builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();

                            } else
                                Toast.makeText(getActivity(), "Error:  " + e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //if no internet
                        if (!CarFragment.isConnected(getContext())) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Connection Error");
                            builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();

                        } else {
                            Toast.makeText(getActivity(), "Error: " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                        error.printStackTrace();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //LHS is from php, RHS is getText there
                params.put("agentID", agentID);
                params.put("dealerID", dealerID);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(stringRequest);


    }
    private void updateApp(String codeAppID, String codeAgentID) {
        //check agentID is the same with this agent or not, then check appID
        if (codeAgentID.equals(agentID)){
            for(int i=0; i<arrAppID.size();i++){
                //if codeAppID is same with one of the appID from arrAppID, updateStatus
                if(codeAppID.equals(arrAppID.get(i))){
                    appID=arrAppID.get(i);
                    makeServiceCall(getString(R.string.met_update_status_url),codeAgentID,appID);

                }
            }
        }else{
            AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
            builder.setTitle("Scan failed");
            builder.setMessage("Invalid agent. Please make sure you are the right agent for this appointment").setNegativeButton("Retry", null).create().show();

        }
    }

    private void makeServiceCall(String url, final String strAgentID, final String strAppID) {
        pbScanning.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            //if UPDATE SUCCESS
                            if (success.equals("1")){
                                Toast.makeText(getContext(),message+"\nStatus Updated",Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            //if no internet
                            if (!CarFragment.isConnected(getContext())) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("Connection Error");
                                builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();

                            } else
                                Toast.makeText(getActivity(), "Error:  " + e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                        pbScanning.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //if no internet
                        if (!CarFragment.isConnected(getContext())) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Connection Error");
                            builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();

                        } else {
                            Toast.makeText(getActivity(), "Error: " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                        error.printStackTrace();
                        pbScanning.setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //LHS is from php, RHS is getText there
                params.put("agentID", strAgentID);
                params.put("appID", strAppID);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(stringRequest);

    }
}
