package com.example.alex.secondhandcarseller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdminDealerDetail extends AppCompatActivity {
    private TextView textViewdealername, tvdealerloc, tvdealeremail, tvdealercont, tvpersonic, tvdealerstatus;
    private String id, name, loc, email, contact, pic, status;
    private String Url="https://dewy-minuses.000webhostapp.com/changeDealerStatus.php";
    private Button buttonBlackList, buttonApprove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dealer_detail);

        setTitle("");
        textViewdealername = (TextView) findViewById(R.id.textViewdealername);
        tvdealerloc = (TextView) findViewById(R.id.tvdealerloc);
        tvdealeremail = (TextView) findViewById(R.id.tvdealeremail);
        tvdealercont = (TextView) findViewById(R.id.tvdealercont);
        tvpersonic = (TextView) findViewById(R.id.tvpersonic);
        tvdealerstatus = (TextView) findViewById(R.id.tvdealerstatus);
        buttonBlackList = (Button) findViewById(R.id.buttonBlackList);
        buttonApprove = (Button) findViewById(R.id.buttonApprove);

        Intent intent = getIntent();
        id = intent.getStringExtra("DealerID");
        name = intent.getStringExtra("DealerName");
        loc = intent.getStringExtra("DealerLoctaion");
        email = intent.getStringExtra("DealerEmail");
        contact = intent.getStringExtra("DealerContact");
        pic = intent.getStringExtra("Pic");
        status = intent.getStringExtra("DealerStatus");


        textViewdealername.setText(name);
        tvdealerloc.setText(loc);
        tvdealercont.setText(contact);
        tvdealeremail.setText(email);
        tvpersonic.setText(pic);
        tvdealerstatus.setText(status);



        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                changeStatus(AdminDealerDetail.this);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        if (status.matches("Blacklisted")) {
            tvdealerstatus.setTextColor(ContextCompat.getColor(this, R.color.Red));
            builder.setMessage("Are you sure to remove this dealer from Blacklist?");
            buttonApprove.setText("Remove From Blacklist");
            buttonBlackList.setVisibility(View.GONE);

        } else if (status.matches("Approved")) {
            tvdealerstatus.setTextColor(ContextCompat.getColor(this, R.color.Green));
            builder.setMessage("Are you sure to blacklist this dealer?");
            buttonApprove.setVisibility(View.GONE);
        } else {
            tvdealerstatus.setTextColor(ContextCompat.getColor(this, R.color.Orange));
            builder.setMessage("Are you sure to approve this dealer?");
            buttonBlackList.setVisibility(View.GONE);
        }

        buttonApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        buttonBlackList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

    }

    private void changeStatus(Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        //Send data
        try {
            StringRequest postRequest = new StringRequest(
                    Request.Method.POST,
                    Url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                String message = jsonObject.getString("message");

                                if (success.equals("1")) {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), message + " Please Try Again", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "error" + e, Toast.LENGTH_LONG).show();

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error : " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", id);
                    params.put("status", status);

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
            e.printStackTrace();
        }
    }
}
