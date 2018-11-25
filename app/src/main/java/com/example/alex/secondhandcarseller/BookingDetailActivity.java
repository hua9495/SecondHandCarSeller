package com.example.alex.secondhandcarseller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class BookingDetailActivity extends AppCompatActivity {
    private Button btnAcceptRequest;
    private TextView tvCarName, tvAppDate, tvAppTime, tvPrice, tvCustName, tvCustContactNo, tvCusttEmail;
    private String carName, appDate, appTime, price, carPhoto, agentID, appID, custID, bookingStatus;
    private ImageView ivCarPhoto;
    private ProgressBar downloadingAppDetail;
    SharedPreferences sharePref;
    private ArrayList<String> dateTime = new ArrayList<>();
    private ArrayList<String> status = new ArrayList<>();
    ArrayList<Appointment> appList = new ArrayList<>();
    RequestQueue queue;
    private Date date;
    private String currentDate = "";
    private String currentTime = "";
    SimpleDateFormat shFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
    NumberFormat formatter = NumberFormat.getCurrencyInstance();
    private Double dPrice;
    private String custName, custEmail, custContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);
        setTitle(R.string.title_booking_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharePref = this.getSharedPreferences("My_Pref", Context.MODE_PRIVATE);
        agentID = sharePref.getString("ID", null);


        //     Gson gson = new Gson();
        //     String json = sharePref.getString("jsonApp", null);
        //appList is to check got crashTime or not
        //   Type type = new TypeToken<ArrayList<Appointment>>() {
        //    }.getType();
        //   appList = gson.fromJson(json, type);


        tvCarName = (TextView) findViewById(R.id.textViewCarName);
        tvAppDate = (TextView) findViewById(R.id.textViewAppDate);
        tvAppTime = (TextView) findViewById(R.id.textViewAppTime);
        tvPrice = (TextView) findViewById(R.id.textViewPrice);
        tvCustName = (TextView) findViewById(R.id.textViewCustomer);
        tvCustContactNo = (TextView) findViewById(R.id.textViewCustContactNo);
        tvCusttEmail = (TextView) findViewById(R.id.textViewCustEmail);
        ivCarPhoto = (ImageView) findViewById(R.id.imageViewCarPhoto);

        btnAcceptRequest = (Button) findViewById(R.id.buttonAccept);
        downloadingAppDetail = (ProgressBar) findViewById(R.id.downloadingAppDetail);


        downloadingAppDetail.setVisibility(View.GONE);
        Intent intent = getIntent();
        carName = intent.getStringExtra("CarName");
        appDate = intent.getStringExtra("appDate");
        appTime = intent.getStringExtra("appTime");
        price = intent.getStringExtra("price");
        appID = intent.getStringExtra("appID");

        dPrice = Double.parseDouble(price);
        price = formatter.format(dPrice);

        carPhoto = intent.getStringExtra("carPhoto");
        custID = intent.getStringExtra("custID");
        bookingStatus = intent.getStringExtra("bookingStatus");

        //to let the agent accept the pending request
        if (bookingStatus.equals("Pending")) {
            btnAcceptRequest.setVisibility(View.VISIBLE);
        } else {
            btnAcceptRequest.setVisibility(View.GONE);
        }

        getAgentAppointmentDetail(this, getString(R.string.get_appointment_detail_url), custID);


    }

    private void getAgentAppointmentDetail(Context context, String url, final String custID) {
        downloadingAppDetail.setVisibility(View.VISIBLE);
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

                                //retrive the record
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject userResponse = jsonArray.getJSONObject(i);

                                    custName = userResponse.getString("custName");
                                    custEmail = userResponse.getString("custEmail");
                                    custContact = userResponse.getString("custContactNo");

                                }
                                loadData();


                            } else {
                                Toast.makeText(BookingDetailActivity.this, "No record", Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Error:  " + e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();

                        }

                        downloadingAppDetail.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error: " + error.toString(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                        downloadingAppDetail.setVisibility(View.GONE);

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //LHS is from php, RHS is getText there
                params.put("custID", custID);


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
        /*stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);
    }

    private void loadData() {
        tvCarName.setText(carName);
        tvAppDate.setText(appDate);
        tvAppTime.setText(appTime);
        tvPrice.setText(price);
        Glide.with(getApplicationContext()).asBitmap().load(carPhoto).into(ivCarPhoto);
        tvCustName.setText(custName);
        tvCusttEmail.setText(custEmail);
        tvCustContactNo.setText(custContact);
    }

    //if the agent accept the appointment request
    public void onAcceptRequest(View v) {

        Boolean clashTime = checkClashTime(appList);
        if (clashTime) {
            AlertDialog.Builder buider = new AlertDialog.Builder(BookingDetailActivity.this);
            buider.setTitle(R.string.retry);
            buider.setMessage("Can not accept the appointment.\nReason: Date time is clashed ").setNegativeButton(R.string.cancel, null).create().show();

        } else {
            //else reply to customer, update the appointment status to "booked"(DONE)
            Date cDate = new Date();
            Date cTime = new Date();
            SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm a");
            currentDate = sdfDate.format(cDate);
            currentTime = sdfTime.format(cTime);
            makeServiceCall(this, getString(R.string.update_Status_url), appID, currentDate, currentTime);
        }


    }

    private Boolean checkClashTime(ArrayList<Appointment> appList) {
        String dateTime, status;
        Boolean crashTime = false;
        Date aDate;

        ParsePosition aPos = new ParsePosition(0);
        aDate = shFormatter.parse(appDate + " " + appTime, aPos);
        for (int i = 0; i < appList.size(); i++) {
            dateTime = appList.get(i).getAppDateNTime();
            status = appList.get(i).getAppStatus();
            //convert dateTime to date format
            ParsePosition pos = new ParsePosition(0);
            date = shFormatter.parse(dateTime, pos);
            //check status equals "Booked" AND dateTime is equal to the app datetime;
            if (status.equals("Booked") && date.equals(aDate)) {
                crashTime = true;
            }

        }
        return crashTime;
    }


    private void makeServiceCall(final Context context, String url, final String id, final String cDate, final String cTime) {
        downloadingAppDetail.setVisibility(View.VISIBLE);
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
                                    finish();
                                } else {
                                    Toast.makeText(BookingDetailActivity.this, message, Toast.LENGTH_LONG).show();

                                }
                            } catch (JSONException e) {
                                Toast.makeText(BookingDetailActivity.this, e.toString(), Toast.LENGTH_LONG).show();


                            }
                            proceed();
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
                    params.put("agentID", agentID);
                    params.put("acceptDate", cDate);
                    params.put("acceptTime", cTime);
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

    private void proceed() {
        downloadingAppDetail.setVisibility(View.GONE);
        btnAcceptRequest.setEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
