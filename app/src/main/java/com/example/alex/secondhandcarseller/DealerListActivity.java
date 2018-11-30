package com.example.alex.secondhandcarseller;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.alex.secondhandcarseller.CarFragment.isConnected;

public class DealerListActivity extends AppCompatActivity {
    private RecyclerView rvDealerList;
    private String Status;
    private ArrayList<String> Dealerid = new ArrayList<>();
    private ArrayList<String> DealerName = new ArrayList<>();
    private ArrayList<String> DealerStatus = new ArrayList<>();
    private ArrayList<String> DealerEmail = new ArrayList<>();
    private ArrayList<String> DealerLoctaion = new ArrayList<>();
    private ArrayList<String> DealerContact = new ArrayList<>();
    private ArrayList<String> Pic = new ArrayList<>();
    private ArrayList<String> Reason = new ArrayList<>();
    private ProgressBar progressBarLoadDeal;
    private String Url = "https://dewy-minuses.000webhostapp.com/getSelectedDealer.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rvDealerList = (RecyclerView) findViewById(R.id.rvDealerList);
        progressBarLoadDeal = (ProgressBar) findViewById(R.id.progressBarLoadDeal);

        Intent intent = getIntent();
        Status = intent.getStringExtra("DealerStatus");
        setTitle(Status + " Dealers List");

        getDealer();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_refresh, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh)
            getDealer();

        else
            super.onBackPressed();

        return super.onOptionsItemSelected(item);
    }

    private void getDealer() {
        clearAll();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("dealer");
                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);


                            //follow index
                            String id = object.getString("id");
                            String name = object.getString("name");
                            String email = object.getString("email");
                            String location = object.getString("location");
                            String contact = object.getString("contact");
                            String pic = object.getString("pic");
                            String reason = object.getString("reason");

                            Dealerid.add(id);
                            DealerName.add(name);
                            DealerStatus.add(Status);
                            DealerEmail.add(email);
                            DealerLoctaion.add(location);
                            DealerContact.add(contact);
                            Pic.add(pic);
                            Reason.add(reason);

                        }

                        progressBarLoadDeal.setVisibility(View.GONE);
                        initRecyclerView();
                    } else {
                        progressBarLoadDeal.setVisibility(View.GONE);
                        Toast.makeText(DealerListActivity.this, "There are no " + Status + " dealer in your list!", Toast.LENGTH_LONG).show();
                        finish();
                    }


                } catch (JSONException e) {

                    progressBarLoadDeal.setVisibility(View.GONE);
                    //if no internet
                    if (!isConnected(DealerListActivity.this)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(DealerListActivity.this);
                        builder.setTitle("Connection Error");
                        builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();
                    } else
                        Toast.makeText(DealerListActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressBarLoadDeal.setVisibility(View.GONE);
                        //if no internet
                        if (!isConnected(DealerListActivity.this)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(DealerListActivity.this);
                            builder.setTitle("Connection Error");
                            builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();
                        } else
                            Toast.makeText(DealerListActivity.this, "Error " + error.toString(), Toast.LENGTH_LONG).show();


                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("status", Status);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void clearAll() {
        Dealerid.clear();
        DealerName.clear();
        DealerStatus.clear();
        DealerEmail.clear();
        DealerLoctaion.clear();
        DealerContact.clear();
        Pic.clear();
        progressBarLoadDeal.setVisibility(View.VISIBLE);
    }


    private void initRecyclerView() {
        DealerListAdap adapter = new DealerListAdap(Dealerid, DealerName, DealerStatus, DealerEmail, DealerLoctaion, DealerContact, Pic, Reason, this);
        rvDealerList.setAdapter(adapter);
        rvDealerList.setLayoutManager(new LinearLayoutManager(this));
    }
}
