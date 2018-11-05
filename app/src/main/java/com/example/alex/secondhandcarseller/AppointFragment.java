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
import java.util.ArrayList;
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
    private TextView tvCaption, tvCaption1;
    private ArrayList<String> arrBookingStatus = new ArrayList<>();
    private ArrayList<String> arrCarNAMES = new ArrayList<>();
    private ArrayList<String> arrBookingDates = new ArrayList<>();
    private ArrayList<String> arrBookingTimes = new ArrayList<>();
    private ArrayList<String> arrPrice = new ArrayList<>();
    private ArrayList<String> arrCarPhoto = new ArrayList<>();
    private ArrayList<String> arrCustID = new ArrayList<>();
    private ArrayList<String> arrDateTime = new ArrayList<>();
    private ArrayList<Appointment> appointmentArrayList=new ArrayList<>();
    private String agentID,dealerID;
    SharedPreferences sharePref;

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
        //past agentID and dealer ID as params to get booking list
        agentID = myPref.getString("ID", null);
        dealerID=myPref.getString("BelongDealer",null);

        lvBooking = (ListView) v.findViewById(R.id.listViewBooking);
        downloadingBooking = (ProgressBar) v.findViewById(R.id.downloadBooking);
        tvCaption = (TextView) v.findViewById(R.id.tvNoBooking1);
        tvCaption1 = (TextView) v.findViewById(R.id.tvNoBooking2);
        downloadingBooking.setVisibility(View.GONE);
        tvCaption1.setVisibility(View.GONE);
        tvCaption.setVisibility(View.GONE);


        getAppointment(v, getString(R.string.get_appointment_url));

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
        downloadingBooking.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String successA = jsonObject.getString("successA");
                            String successB=jsonObject.getString("successB");
                            //String message = jsonObject.getString("message");
                            JSONArray jsonArray = jsonObject.getJSONArray("BOOKING");
                            //if HAVE RECORD
                            if (successA.equals("1")||successB.equals("1")) {
                                //retrive the record
                                String appID="";
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

                                    //store the date time together,for checking purpose(used in booking detail)
                                    String bookingDateTime=appDate+" "+appTime;
                                    arrDateTime.add(bookingDateTime);

                                    arrCarNAMES.add(carName);
                                    arrBookingDates.add(appDate);
                                    arrBookingTimes.add(appTime);

                                    arrBookingStatus.add(appStatus);
                                    //for showing booking detail purpose
                                    arrPrice.add(price);
                                    arrCarPhoto.add(carPhoto);
                                    arrCustID.add(custID);

                                    Appointment newApp=new Appointment(appID,bookingDateTime,appStatus);
                                    appointmentArrayList.add(newApp);

                                }

                                SharedPreferences.Editor editor = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                                editor.putString("appID",appID);
                                Gson gson=new Gson();
                                String jsonApp=gson.toJson(appointmentArrayList);
                                editor.putString("jsonApp",jsonApp);
                                editor.commit();
                                downloadingBooking.setVisibility(View.GONE);
                                initListVIew(v);

                                Toast.makeText(getActivity(), "Done ! ", Toast.LENGTH_SHORT).show();


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
//10000 is the time in milliseconds adn is equal to 10 sec
      /*  stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(stringRequest);


    }

    private void clearView() {
        arrBookingStatus.clear();
        arrBookingTimes.clear();
        arrBookingDates.clear();
        arrCarNAMES.clear();
    }

    private void initListVIew(View v) {
        AdapterMyBooking myBookingAdapter = new AdapterMyBooking(getActivity(), arrBookingStatus, arrCarNAMES, arrBookingDates, arrBookingTimes, arrPrice, arrCarPhoto, arrCustID);
        lvBooking.setAdapter(myBookingAdapter);
    }
}
