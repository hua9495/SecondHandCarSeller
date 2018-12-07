package com.example.alex.secondhandcarseller;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class AppointFragment extends Fragment {
    private ListView lvBooking;
    private ProgressBar downloadingBooking;
    private TextView tvCaption;
    private TextView tvTips1Met, tvTips2Booked, tvTips3Cancelled, tvTips4Pending;
    private ImageView iv1Met, iv2Booked, iv3Cancelled, iv4Pending;
    private ConstraintLayout bookingLayout;
    private ArrayList<String> arrBookingStatus = new ArrayList<>();
    private ArrayList<String> arrCarNAMES = new ArrayList<>();
    private ArrayList<String> arrBookingDates = new ArrayList<>();
    private ArrayList<String> arrBookingTimes = new ArrayList<>();
    private ArrayList<String> arrPrice = new ArrayList<>();
    private ArrayList<String> arrCarPhoto = new ArrayList<>();
    private ArrayList<String> arrCustID = new ArrayList<>();
    private ArrayList<String> arrDateTime = new ArrayList<>();
    private ArrayList<Date> arrAcceptDateTime = new ArrayList<>();
    private ArrayList<String> arrAppID = new ArrayList<>();
    private ArrayList<Appointment> appointmentArrayList = new ArrayList<>();

    private String agentID, dealerID;


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

        tvTips1Met = (TextView) v.findViewById(R.id.textViewTips1);
        tvTips2Booked = (TextView) v.findViewById(R.id.textViewTips2);
        tvTips3Cancelled = (TextView) v.findViewById(R.id.textViewTips3);
        tvTips4Pending = (TextView) v.findViewById(R.id.textViewTips4);
        iv1Met = (ImageView) v.findViewById(R.id.imageViewG);
        iv2Booked = (ImageView) v.findViewById(R.id.imageViewR);
        iv3Cancelled = (ImageView) v.findViewById(R.id.imageViewCr);
        iv4Pending = (ImageView) v.findViewById(R.id.imageViewP);
        lvBooking = (ListView) v.findViewById(R.id.listViewBooking);
        downloadingBooking = (ProgressBar) v.findViewById(R.id.downloadBooking);
        tvCaption = (TextView) v.findViewById(R.id.tvNoBooking1);
        downloadingBooking.setVisibility(View.GONE);
        tvCaption.setVisibility(View.GONE);

        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.action_bar_refresh, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        getAppointment(getView(), getString(R.string.get_appointment_url));

        return super.onOptionsItemSelected(item);
    }

    private void getAppointment(final View v, String url) {
        clearView();

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
                                String appID="";
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject userResponse = jsonArray.getJSONObject(i);

                                    String carName = userResponse.getString("carName");
                                    String price = userResponse.getString("price");
                                    appID = userResponse.getString("appID");
                                    String carID = userResponse.getString("carID");
                                    String custID = userResponse.getString("custID");
                                    String appDate = userResponse.getString("appDate");
                                    String appTime = userResponse.getString("appTime");
                                    String appStatus = userResponse.getString("appStatus");
                                    String carPhoto = userResponse.getString("car_photo");
                                    String acceptDate = userResponse.getString("acceptDate");
                                    String acceptTime = userResponse.getString("acceptTime");
                                    //store the date time together,for checking purpose(used in booking detail)
                                    String bookingDateTime = appDate + " " + appTime;
                                    String acceptDateTime = acceptDate + " " + acceptTime;
                                    arrDateTime.add(bookingDateTime);

                                    arrCarNAMES.add(carName);
                                    arrBookingDates.add(appDate);
                                    arrBookingTimes.add(appTime);
                                    arrBookingStatus.add(appStatus);
                                    //for showing booking detail purpose
                                    arrPrice.add(price);
                                    arrCarPhoto.add(carPhoto);
                                    arrCustID.add(custID);
                                    arrAppID.add(appID);

                                    //for later check clash time
                                    Appointment newApp=new Appointment(appID,bookingDateTime,appStatus);
                                    appointmentArrayList.add(newApp);


                                }
                                SharedPreferences.Editor editor = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                                editor.putString("appID",appID);
                                Gson gson=new Gson();
                                String jsonApp=gson.toJson(appointmentArrayList);
                                editor.putString("jsonApp",jsonApp);
                                editor.commit();
                                initListVIew(v);
                                tvCaption.setVisibility(View.GONE);
                                showTips();

                            } else {
                                tvCaption.setVisibility(View.VISIBLE);

                            }
                            lvBooking.setVisibility(View.VISIBLE);
                            downloadingBooking.setVisibility(View.GONE);


                        } catch (JSONException e) {
                            //if no internet
                            if (!CarFragment.isConnected(v.getContext())) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                builder.setTitle("Connection Error");
                                builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();

                            } else
                                Toast.makeText(getActivity(), "Error:  " + e.toString(), Toast.LENGTH_LONG).show();
                            lvBooking.setVisibility(View.VISIBLE);
                            downloadingBooking.setVisibility(View.GONE);
                            tvCaption.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //if no internet
                        if (!CarFragment.isConnected(v.getContext())) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setTitle("Connection Error");
                            builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();

                        } else {
                            Toast.makeText(getActivity(), "Error: " + error.toString(), Toast.LENGTH_LONG).show();
                        }

                        lvBooking.setVisibility(View.VISIBLE);
                        downloadingBooking.setVisibility(View.GONE);
                        tvCaption.setVisibility(View.VISIBLE);
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

    private void clearView() {
        arrBookingStatus.clear();
        arrBookingTimes.clear();
        arrBookingDates.clear();
        arrCarNAMES.clear();
        lvBooking.setVisibility(View.GONE);
        arrAppID.clear();
        arrCustID.clear();
        arrPrice.clear();
        arrCarPhoto.clear();
        downloadingBooking.setVisibility(View.VISIBLE);
        tvCaption.setVisibility(View.VISIBLE);
        clearTips();
    }

    private void initListVIew(View v) {

        AdapterMyBooking myBookingAdapter = new AdapterMyBooking(getActivity(), arrBookingStatus, arrCarNAMES, arrBookingDates, arrBookingTimes, arrPrice, arrCarPhoto, arrCustID, arrAppID, arrAcceptDateTime);
        lvBooking.setAdapter(myBookingAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences myPref = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE);
        //past agentID and dealer ID as params to get booking list
        agentID = myPref.getString("ID", null);
        dealerID = myPref.getString("BelongDealer", null);

        getAppointment(getView(), getString(R.string.get_appointment_url));

    }

    private void showTips() {
        tvTips1Met.setVisibility(View.VISIBLE);
        tvTips2Booked.setVisibility(View.VISIBLE);
        tvTips3Cancelled.setVisibility(View.VISIBLE);
        tvTips4Pending.setVisibility(View.VISIBLE);
        iv1Met.setVisibility(View.VISIBLE);
        iv2Booked.setVisibility(View.VISIBLE);
        iv3Cancelled.setVisibility(View.VISIBLE);
        iv4Pending.setVisibility(View.VISIBLE);
    }

    private void clearTips() {
        tvTips1Met.setVisibility(View.GONE);
        tvTips2Booked.setVisibility(View.GONE);
        tvTips3Cancelled.setVisibility(View.GONE);
        tvTips4Pending.setVisibility(View.GONE);
        iv1Met.setVisibility(View.GONE);
        iv2Booked.setVisibility(View.GONE);
        iv3Cancelled.setVisibility(View.GONE);
        iv4Pending.setVisibility(View.GONE);
    }
}
