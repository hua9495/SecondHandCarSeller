package com.example.alex.secondhandcarseller;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
public class AppointFragment extends Fragment {
    private ListView lvBooking;
    private ProgressBar downloadingBooking;
    private TextView tvCaption,tvCaption1;
    private ArrayList<String> arrBookingStatus = new ArrayList<>();
    private ArrayList<String> arrCarNAMES = new ArrayList<>();
    private ArrayList<String> arrBookingDates = new ArrayList<>();
    private ArrayList<String> arrBookingTimes = new ArrayList<>();
    private ArrayList<String> arrPrice = new ArrayList<>();
    private ArrayList<String> arrCarPhoto = new ArrayList<>();
    private ArrayList<String> arrCustID = new ArrayList<>();
    private String agentID;

    public AppointFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_appoint, container, false);
        SharedPreferences myPref = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE);
        agentID = myPref.getString("ID", null);
        lvBooking = (ListView) v.findViewById(R.id.listViewBooking);
        downloadingBooking = (ProgressBar) v.findViewById(R.id.downloadBooking);
        tvCaption=(TextView)v.findViewById(R.id.tvNoBooking1);
        tvCaption1=(TextView)v.findViewById(R.id.tvNoBooking2);
        tvCaption1.setVisibility(View.GONE);
        tvCaption.setVisibility(View.GONE);
        downloadingBooking.setVisibility(View.GONE);
        getAppointment(getActivity(), getString(R.string.get_appointment_url));
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.action_bar_refresh, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void getAppointment(final Context context, String url) {

        clearView();
        downloadingBooking.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            //String message = jsonObject.getString("message");
                            JSONArray jsonArray = jsonObject.getJSONArray("BOOKING");
                            //if HAVE RECORD
                            if (success.equals("1")) {
                                //retrive the record
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject userResponse = jsonArray.getJSONObject(i);

                                    String carName = userResponse.getString("carName");
                                    String price = userResponse.getString("price");
                                    String appID = userResponse.getString("appID");
                                    String carID = userResponse.getString("carID");
                                    String custID = userResponse.getString("custID");
                                    String appDate = userResponse.getString("appDate");
                                    String appTime = userResponse.getString("appTime");
                                    String appStatus = userResponse.getString("appStatus");
                                    String carPhoto = userResponse.getString("car_photo");

                                    arrCarNAMES.add(carName);
                                    arrBookingDates.add(appDate);
                                    arrBookingTimes.add(appTime);
                                    arrBookingStatus.add(appStatus);
                                    //for showing booking detail purpose
                                    arrPrice.add(price);
                                    arrCarPhoto.add(carPhoto);
                                    arrCustID.add(custID);

                                }
                                AdapterMyBooking myBookingAdapter = new AdapterMyBooking(getActivity().getApplicationContext(), arrBookingStatus, arrCarNAMES, arrBookingDates, arrBookingTimes, arrPrice, arrCarPhoto,arrCustID);
                                lvBooking.setAdapter(myBookingAdapter);
                                Toast.makeText(getActivity(), "Done ! ", Toast.LENGTH_SHORT).show();

                                downloadingBooking.setVisibility(View.GONE);
                            } else {
                                tvCaption.setVisibility(View.VISIBLE);
                                tvCaption1.setVisibility(View.VISIBLE);

                                downloadingBooking.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), "Error:  " + e.toString(), Toast.LENGTH_LONG).show();
                            downloadingBooking.setVisibility(View.GONE);
                            e.printStackTrace();


                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        Toast.makeText(getActivity(), "Error: " + error.toString(), Toast.LENGTH_LONG).show();
                        downloadingBooking.setVisibility(View.GONE);
                        error.printStackTrace();
                        //btnSearch.setEnabled(true);

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //LHS is from php, RHS is getText there
//todo:change to agentID in php
                params.put("agentID", agentID);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(stringRequest);


    }

    private void clearView() {
        arrBookingStatus.clear();
        arrBookingTimes.clear();
        arrBookingDates.clear();
        arrCarNAMES.clear();
    }
}
