package com.example.alex.secondhandcarseller;

import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import static com.example.alex.secondhandcarseller.CarFragment.isConnected;

public class SoldCar extends AppCompatActivity {
    private String dealerid, Url = "https://dewy-minuses.000webhostapp.com/getSold.php";


    private ArrayList<String> mCarName = new ArrayList<>();
    private ArrayList<String> mCarImage = new ArrayList<>();
    private ArrayList<String> mCarPrice = new ArrayList<>();
    private ArrayList<String> mCarYear = new ArrayList<>();
    private ArrayList<String> mCarMile = new ArrayList<>();
    private ProgressBar pbLoadSold;
    private RecyclerView rvSoldCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sold_car);
        setTitle("Sold Car");
        rvSoldCar = (RecyclerView) findViewById(R.id.rvSoldCar);
        pbLoadSold = (ProgressBar) findViewById(R.id.pbLoadSold);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences myPref = getSharedPreferences("My_Pref", MODE_PRIVATE);
        String checkid = myPref.getString("ID", null).substring(0, 1);

        if (checkid.equals("A")) {
            dealerid = myPref.getString("BelongDealer", null);
        } else {
            dealerid = myPref.getString("ID", null);
        }
        LoadSld();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerView() {

        SoldAdapter adapter = new SoldAdapter(mCarName, mCarImage, mCarPrice, mCarYear, mCarMile, this);
        rvSoldCar.setAdapter(adapter);
        rvSoldCar.setLayoutManager(new LinearLayoutManager(this));

    }

    private void LoadSld() {
        pbLoadSold.setVisibility(View.VISIBLE);
        rvSoldCar.setEnabled(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("sold");
                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            //follow index
                            String name = object.getString("name");
                            String image_data = object.getString("imageUrl");
                            String brand = object.getString("brand");
                            String price = object.getString("price");
                            String year = object.getString("year");
                            String mileage = object.getString("mileage");

                            mCarName.add(brand + " " + name);
                            mCarImage.add(image_data);
                            mCarPrice.add(price);
                            mCarYear.add(year);
                            mCarMile.add(mileage);
                        }

                        initRecyclerView();

                        pbLoadSold.setVisibility(View.GONE);
                        rvSoldCar.setEnabled(true);
                    } else {
                        pbLoadSold.setVisibility(View.GONE);
                        Toast.makeText(SoldCar.this, "No car in your list", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    //if no internet
                    if (!isConnected(SoldCar.this)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SoldCar.this);
                        builder.setTitle("Connection Error");
                        builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();

                    } else
                        Toast.makeText(SoldCar.this, "Error", Toast.LENGTH_LONG).show();

                    pbLoadSold.setVisibility(View.GONE);
                    rvSoldCar.setEnabled(true);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //if no internet
                        if (!isConnected(SoldCar.this)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SoldCar.this);
                            builder.setTitle("Connection Error");
                            builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();

                        } else
                            Toast.makeText(SoldCar.this, "Error " + error.toString(), Toast.LENGTH_LONG).show();

                        pbLoadSold.setVisibility(View.GONE);
                        rvSoldCar.setEnabled(true);

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("dealerid", dealerid);
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

}
