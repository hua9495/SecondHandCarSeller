package com.example.alex.secondhandcarseller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.transition.Visibility;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.example.alex.secondhandcarseller.CarFragment.isConnected;

public class PromotionActivity extends AppCompatActivity {

    private ArrayList<String> CarID = new ArrayList<>();
    private ArrayList<String> CarName = new ArrayList<>();
    private ArrayList<String> CarPrice = new ArrayList<>();
    private ArrayList<Float> NewPrice = new ArrayList<>();
    private ArrayList<String> CarImage = new ArrayList<>();
    private ArrayList<String> Views = new ArrayList<>();
    private String[] getChecked;
    private Integer discountRate;
    private String Url = "https://dewy-minuses.000webhostapp.com/PromotionRecommended.php", Url1 = "https://dewy-minuses.000webhostapp.com/InsertPromotion.php", dealerid, today, after, carids;
    private RecyclerView RecyclerViewPromotion;
    private Button buttonProm, buttonProBack, buttonApply;
    private EditText editTextDiscount;
    private TextView textViewTop;
    private ProgressBar progressBarLoadPro;
    private PromotionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);
        progressBarLoadPro = (ProgressBar) findViewById(R.id.progressBarLoadPro);
        textViewTop = (TextView) findViewById(R.id.textViewTop);
        RecyclerViewPromotion = (RecyclerView) findViewById(R.id.RecyclerViewPromotion);
        buttonProm = (Button) findViewById(R.id.buttonProm);
        buttonProBack = (Button) findViewById(R.id.buttonProBack);
        buttonApply = (Button) findViewById(R.id.buttonApply);
        editTextDiscount = (EditText) findViewById(R.id.editTextDiscount);

        SharedPreferences myPref = this.getSharedPreferences("My_Pref", MODE_PRIVATE);
        dealerid = myPref.getString("ID", null);


        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String checkRate=editTextDiscount.getText().toString();
                if (checkRate.isEmpty()) {
                    editTextDiscount.setError("Cannot be Blank!");
                } else {
                    discountRate=Integer.parseInt(editTextDiscount.getText().toString());
                    if (discountRate <= 0 || discountRate >= 100)
                        editTextDiscount.setError("Range only Can be 1 to 99");
                    else {
                        textViewTop.setVisibility(View.VISIBLE);
                        RecyclerViewPromotion.setVisibility(View.VISIBLE);
                        progressBarLoadPro.setVisibility(View.VISIBLE);
                        clearAll();
                        loadPro();
                        buttonApply.setEnabled(false);
                        editTextDiscount.setEnabled(false);
                        buttonProm.setEnabled(true);
                    }
                }
            }
        });

        buttonProm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChecked = adapter.getAllChecked();

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");

                today = mdformat.format(calendar.getTime());
                calendar.add(Calendar.DATE, 30);
                after = mdformat.format(calendar.getTime());

                final AlertDialog.Builder builder = new AlertDialog.Builder(PromotionActivity.this);
                builder.setTitle("Confirm?");
                builder.setMessage("Selected cars will have " + discountRate + "% discount.\nThe promotion cannot be removed, it will end on " + after);

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        InsertProm(PromotionActivity.this);
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                carids = getChecked[0].toString() + "," + getChecked[1].toString() + "," + getChecked[2].toString() + "," + getChecked[3].toString() + "," + getChecked[4].toString();
                if (carids.equals(" , , , , "))
                    Toast.makeText(PromotionActivity.this, "No Car Selected for Promotion!", Toast.LENGTH_LONG).show();
                else {
                    AlertDialog alert = builder.create();
                    alert.show();
                }


            }
        });
        buttonProBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void clearAll() {
        CarID.clear();
        CarName.clear();
        CarPrice.clear();
        Views.clear();
    }

    private void loadPro() {
        closeAll(View.VISIBLE,true);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("promotion");
                    if (success.equals("1")) {
                        if (jsonArray.length() >= 5) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                //follow index
                                String carID = object.getString("id");
                                String name = object.getString("name");
                                String image_data = object.getString("imageUrl");
                                String brand = object.getString("brand");
                                String price = object.getString("price");
                                String color = object.getString("color");
                                String desc = object.getString("desc");
                                String year = object.getString("year");
                                String mileage = object.getString("mileage");
                                String views = object.getString("views");

                                Float Conv_Price = Float.parseFloat(price) - (Float.parseFloat(price) * discountRate / 100);

                                CarID.add(carID);
                                CarName.add(brand + " " + name);
                                CarPrice.add(price);
                                Views.add(views);
                                CarImage.add(image_data);
                                NewPrice.add(Conv_Price);
                            }
                            adapter = new PromotionAdapter(CarID, CarName, CarPrice, CarImage, NewPrice, Views, PromotionActivity.this);
                            RecyclerViewPromotion.setAdapter(adapter);
                            RecyclerViewPromotion.setLayoutManager(new LinearLayoutManager(PromotionActivity.this));
                        } else {
                            Toast.makeText(PromotionActivity.this, "You must have more than 5 cars in ur list to do promotion!", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        progressBarLoadPro.setVisibility(View.GONE);
                    } else {
                        progressBarLoadPro.setVisibility(View.GONE);
                        Toast.makeText(PromotionActivity.this, "No car in your list!", Toast.LENGTH_LONG).show();
                        finish();
                    }

                } catch (JSONException e) {
                    //if no internet
                    if (!isConnected(PromotionActivity.this)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PromotionActivity.this);
                        builder.setTitle("Connection Error");
                        builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();

                    } else
                        Toast.makeText(PromotionActivity.this, "Error", Toast.LENGTH_LONG).show();

                    progressBarLoadPro.setVisibility(View.GONE);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //if no internet
                        if (!isConnected(PromotionActivity.this)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(PromotionActivity.this);
                            builder.setTitle("Connection Error");
                            builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();

                        } else
                            Toast.makeText(PromotionActivity.this, "Error " + error.toString(), Toast.LENGTH_LONG).show();

                        progressBarLoadPro.setVisibility(View.GONE);

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

    private void closeAll(int v,boolean boo) {
        progressBarLoadPro.setVisibility(v);
        buttonProm.setEnabled(boo);
        RecyclerViewPromotion.setEnabled(boo);
    }


    private void InsertProm(Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        closeAll(View.VISIBLE,false);
        //Send data
        try {
            StringRequest postRequest = new StringRequest(
                    Request.Method.POST,
                    Url1,
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
                                    closeAll(View.GONE,true);
                                } else {
                                    Toast.makeText(getApplicationContext(), message + " Please Try Again", Toast.LENGTH_LONG).show();
                                    closeAll(View.GONE,true);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();

                                Toast.makeText(getApplicationContext(), "error" + e, Toast.LENGTH_LONG).show();
                                closeAll(View.GONE,true);

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error : " + error.toString(), Toast.LENGTH_LONG).show();
                            closeAll(View.GONE,true);

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("carids", carids);
                    params.put("start", today);
                    params.put("end", after);
                    params.put("rate", discountRate.toString());


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
            closeAll(View.GONE,true);
        }
    }

}
