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

public class PromotionHistory extends AppCompatActivity {
    private String Url="https://dewy-minuses.000webhostapp.com/getPromotion.php",dealerid;
    private ArrayList<String> CarName = new ArrayList<>();
    private ArrayList<String> CarImage = new ArrayList<>();
    private ArrayList<Float> NewPrice = new ArrayList<>();
    private ArrayList<String> Date = new ArrayList<>();
    private ArrayList<String> Rate = new ArrayList<>();
    private RecyclerView RecycleHistory;
    private ProgressBar progressBarHis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_history);
        setTitle("Cars On Promotion");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecycleHistory=(RecyclerView)findViewById(R.id.RecycleHistory);
        progressBarHis=(ProgressBar) findViewById(R.id.progressBarHis);

        SharedPreferences myPref = this.getSharedPreferences("My_Pref", MODE_PRIVATE);
        dealerid = myPref.getString("ID", null);
        loadDis();

    }

    private void loadDis() {
        progressBarHis.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("car");
                    if (success.equals("1")) {
                        if (jsonArray.length() >= 1) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                //follow index
                                String name = object.getString("name");
                                String image_data = object.getString("imageUrl");
                                String brand = object.getString("brand");
                                String price = object.getString("price");
                                String end = object.getString("end");
                                String discount = object.getString("discount");

                                Float rate=Float.parseFloat(discount);

                                Float Conv_Price = Float.parseFloat(price) - (Float.parseFloat(price) * rate / 100);

                                CarName.add(brand + " " + name);
                                Rate.add(discount);
                                Date.add(end);
                                CarImage.add(image_data);
                                NewPrice.add(Conv_Price);
                            }
                            HistoryAdapter adapter = new HistoryAdapter( CarName,  CarImage, NewPrice, Date,Rate, PromotionHistory.this);
                            RecycleHistory.setAdapter(adapter);
                            RecycleHistory.setLayoutManager(new LinearLayoutManager(PromotionHistory.this));
                        }

                        progressBarHis.setVisibility(View.GONE);
                    } else {
                        progressBarHis.setVisibility(View.GONE);
                        Toast.makeText(PromotionHistory.this, "You do not have any promoting cars.", Toast.LENGTH_LONG).show();
                        finish();
                    }

                } catch (JSONException e) {
                    //if no internet
                    if (!isConnected(PromotionHistory.this)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PromotionHistory.this);
                        builder.setTitle("Connection Error");
                        builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();

                    } else
                        Toast.makeText(PromotionHistory.this, "Error", Toast.LENGTH_LONG).show();

                    progressBarHis.setVisibility(View.GONE);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //if no internet
                        if (!isConnected(PromotionHistory.this)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(PromotionHistory.this);
                            builder.setTitle("Connection Error");
                            builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();

                        } else
                            Toast.makeText(PromotionHistory.this, "Error " + error.toString(), Toast.LENGTH_LONG).show();

                        progressBarHis.setVisibility(View.GONE);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
