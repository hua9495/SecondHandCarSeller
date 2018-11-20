package com.example.alex.secondhandcarseller;

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
import java.util.zip.Inflater;

import static com.example.alex.secondhandcarseller.CarFragment.isConnected;

public class AdminActivity extends AppCompatActivity {


    private ArrayList<String> DealerType = new ArrayList<>();
    private ArrayList<String> DealerNo = new ArrayList<>();
    private ArrayList<String> DealerStatus = new ArrayList<>();
    private RecyclerView recycleViewAllDealer;
    private ProgressBar progressBarloadCOunt;
    private String Url = "https://dewy-minuses.000webhostapp.com/getDealerCount.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        recycleViewAllDealer = (RecyclerView) findViewById(R.id.recycleViewAllDealer);
        progressBarloadCOunt = (ProgressBar) findViewById(R.id.progressBarloadCOunt);

        getCount();


        setTitle("Admin");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_refresh, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        getCount();
        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerView() {

        AdminAdapter adapter = new AdminAdapter(DealerType, DealerNo, DealerStatus, this);
        recycleViewAllDealer.setAdapter(adapter);
        recycleViewAllDealer.setLayoutManager(new LinearLayoutManager(this));

    }


    @Override
    public void onBackPressed() {
        finish();

    }

    private void getCount() {
        clearall();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("count");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        //follow index
                        String approved = object.getString("approved");
                        String pending = object.getString("pending");
                        String blacklist = object.getString("blacklist");

                        DealerType.add("Pending Dealers");
                        DealerNo.add(pending);
                        DealerStatus.add("Pending");

                        DealerType.add("Blacklisted Dealers");
                        DealerNo.add(blacklist);
                        DealerStatus.add("Blacklisted");

                        DealerType.add("Approved Dealers");
                        DealerNo.add(approved);
                        DealerStatus.add("Approved");
                    }
                    progressBarloadCOunt.setVisibility(View.GONE);
                    initRecyclerView();


                } catch (JSONException e) {

                    progressBarloadCOunt.setVisibility(View.GONE);
                    //if no internet
                    if (!isConnected(AdminActivity.this)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
                        builder.setTitle("Connection Error");
                        builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();
                    } else
                        Toast.makeText(AdminActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressBarloadCOunt.setVisibility(View.GONE);
                        //if no internet
                        if (!isConnected(AdminActivity.this)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
                            builder.setTitle("Connection Error");
                            builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();
                        } else
                            Toast.makeText(AdminActivity.this, "Error " + error.toString(), Toast.LENGTH_LONG).show();


                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
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

    private void clearall() {
        progressBarloadCOunt.setVisibility(View.VISIBLE);
        DealerType.clear();
        DealerNo.clear();
        DealerStatus.clear();
    }
}
