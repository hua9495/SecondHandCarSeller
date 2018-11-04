package com.example.alex.secondhandcarseller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class BookingDetailActivity extends AppCompatActivity {
    private Button btnBackMyBooking, btnAcceptRequest;
    private TextView tvCarName, tvAppDate, tvAppTime, tvPrice, tvCustName, tvCustContactNo, tvCusttEmail, tvDealerLoc;
    private String carName, appDate, appTime, price, carPhoto, agentID, appID, custID, bookingStatus;
    private ImageView ivCarPhoto;
    private ProgressBar downloadingAppDetail;
    SharedPreferences sharePref;
    private ArrayList<String> dateTime = new ArrayList<>();
    private ArrayList<String> status = new ArrayList<>();
    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);
        setTitle(R.string.title_booking_detail);

        sharePref = this.getSharedPreferences("My_Pref", Context.MODE_PRIVATE);
        agentID = sharePref.getString("ID", null);
        appID = sharePref.getString("appID", null);


        tvCarName = (TextView) findViewById(R.id.textViewCarName);
        tvAppDate = (TextView) findViewById(R.id.textViewAppDate);
        tvAppTime = (TextView) findViewById(R.id.textViewAppTime);
        tvPrice = (TextView) findViewById(R.id.textViewPrice);
        tvDealerLoc = (TextView) findViewById(R.id.textViewDealerLocation);
        tvCustName = (TextView) findViewById(R.id.textViewCustomer);
        tvCustContactNo = (TextView) findViewById(R.id.textViewCustContactNo);
        tvCusttEmail = (TextView) findViewById(R.id.textViewCustEmail);
        ivCarPhoto = (ImageView) findViewById(R.id.imageViewCarPhoto);
        btnBackMyBooking = (Button) findViewById(R.id.buttonBackMyBooking);
        btnAcceptRequest = (Button) findViewById(R.id.buttonAccept);
        downloadingAppDetail = (ProgressBar) findViewById(R.id.downloadingAppDetail);

        downloadingAppDetail.setVisibility(View.GONE);
        Intent intent = getIntent();
        carName = intent.getStringExtra("CarName");
        appDate = intent.getStringExtra("appDate");
        appTime = intent.getStringExtra("appTime");
        price = intent.getStringExtra("price");
        carPhoto = intent.getStringExtra("carPhoto");
        custID = intent.getStringExtra("custID");
        bookingStatus = intent.getStringExtra("bookingStatus");


        //Todo: get the custID and agentID,make a function call with params(custID,agentId)
        //to let the agent accept the pending request
        if (bookingStatus.equals("Pending")) {
            btnAcceptRequest.setVisibility(View.VISIBLE);
        } else {
            btnAcceptRequest.setVisibility(View.GONE);
        }

        getAgentAppointmentDetail(this, getString(R.string.get_appointment_detail_url), custID, agentID);

        btnBackMyBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //Todo:have to try
    private void getAgentAppointmentDetail(Context context, String url, final String custID, final String agentID) {
        downloadingAppDetail.setVisibility(View.VISIBLE);
        btnBackMyBooking.setEnabled(false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("DETAIL");
                            //if HAVE RECORD
                            if (success.equals("1")) {
                                String custContact = "";
                                String custName = "";
                                String custEmail = "";
                                String dealerLocation = "";
                                //retrive the record
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject userResponse = jsonArray.getJSONObject(i);


                                    custContact = userResponse.getString("agentContactNo");
                                    custName = userResponse.getString("agentName");
                                    custEmail = userResponse.getString("agentEmail");
                                    dealerLocation = userResponse.getString("dealerLocation");


                                }

                                tvCarName.setText(carName.toString());
                                tvAppDate.setText(appDate.toString());
                                tvAppTime.setText(appTime.toString());
                                tvPrice.setText("RM " + price.toString() + ".00");
                                Glide.with(getApplicationContext()).asBitmap().load(carPhoto).into(ivCarPhoto);
                                tvDealerLoc.setText(dealerLocation.toString());
                                tvCustName.setText(custName.toString());
                                tvCusttEmail.setText(custEmail.toString());
                                tvCustContactNo.setText(custContact.toString());
                                downloadingAppDetail.setVisibility(View.GONE);
                                btnBackMyBooking.setEnabled(true);


                            } else {
                                Toast.makeText(BookingDetailActivity.this, "No record", Toast.LENGTH_LONG).show();
                                downloadingAppDetail.setVisibility(View.GONE);
                                btnBackMyBooking.setEnabled(true);
                            }

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Error:  " + e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                            downloadingAppDetail.setVisibility(View.GONE);
                            btnBackMyBooking.setEnabled(true);

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error: " + error.toString(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                        downloadingAppDetail.setVisibility(View.GONE);
                        btnBackMyBooking.setEnabled(true);

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //LHS is from php, RHS is getText there
                params.put("custID", custID);
                params.put("agentID", agentID);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);
    }

    //if the agent accept the appointment request
    public void onAcceptRequest(View v) {
        //Todo: check got crash datetime or not(datetime,status)
        String appointmentDateTime = appDate + " " + appTime;


        //else reply to customer, update the appointment status to "booked"(DONE)
        makeServiceCall(this,getString(R.string.update_Status_url),appID);

    }
    //Todo: have to try
    private  void makeServiceCall(final Context context,String url, final String id){
        downloadingAppDetail.setVisibility(View.VISIBLE);
        btnBackMyBooking.setEnabled(false);
        btnAcceptRequest.setEnabled(false);
        queue = Volley.newRequestQueue(context);
        //Send data
        try {
            StringRequest postRequest = new StringRequest(
                    Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                String message = jsonObject.getString("message");

                                if (success.equals("1")) {//UPDATED success
                                    Toast.makeText(BookingDetailActivity.this, message, Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(BookingDetailActivity.this, message, Toast.LENGTH_LONG).show();

                                }
                                proceed();
                            }
                            catch (JSONException e) {
                                Toast.makeText(BookingDetailActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                                proceed();

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(BookingDetailActivity.this, "Error  " + error.toString(), Toast.LENGTH_LONG).show();
                            proceed();

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("appID", id);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }
            };
            queue.add(postRequest);
        } catch (Exception e) {
            Toast.makeText(BookingDetailActivity.this, "Error : " + e.toString(), Toast.LENGTH_LONG).show();
            proceed();
        }
    }

    private void proceed(){
        downloadingAppDetail.setVisibility(View.GONE);
        btnBackMyBooking.setEnabled(true);
        btnAcceptRequest.setEnabled(true);
    }

}
