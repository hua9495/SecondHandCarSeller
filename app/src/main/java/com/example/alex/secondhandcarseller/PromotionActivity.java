package com.example.alex.secondhandcarseller;

import android.content.SharedPreferences;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
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
    private String Url = "https://dewy-minuses.000webhostapp.com/PromotionRecommended.php", dealerid;
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
                discountRate = Integer.parseInt(editTextDiscount.getText().toString());

                if (discountRate.equals(null)) {
                    editTextDiscount.setError("Cannot be Blank!");
                } else {
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
                buttonProBack.setText(getChecked[0].toString() + getChecked[1].toString() + getChecked[2].toString() + getChecked[3].toString() + getChecked[4].toString());

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
        progressBarLoadPro.setVisibility(View.VISIBLE);

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


}
