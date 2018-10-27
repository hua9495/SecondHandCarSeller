package com.example.alex.secondhandcarseller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class AddCarActivity extends AppCompatActivity {


    private EditText editTextCarName, editTextCarPrice, editTextCarYear, editTextMileage, editTextDesc;
    private Spinner spinnerCarBrand, spinnerColor, spinnerCarType;
    private ProgressBar progressBarLoadBrand;
    private Button buttonNext;
    private static String Url = "http://dewy-minuses.000webhostapp.com/brand.php";
    private ArrayList<String> brand = new ArrayList<String>();

    ArrayAdapter<CharSequence> colorAdap, typeAdap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        setTitle("Add Car");

        editTextCarName = (EditText) findViewById(R.id.editTextCarName);
        editTextDesc = (EditText) findViewById(R.id.editTextDesc);
        editTextCarPrice = (EditText) findViewById(R.id.editTextCarPrice);
        editTextCarYear = (EditText) findViewById(R.id.editTextCarYear);
        editTextMileage = (EditText) findViewById(R.id.editTextMileage);
        spinnerCarBrand = (Spinner) findViewById(R.id.spinnerCarBrand);
        spinnerColor = (Spinner) findViewById(R.id.spinnerColor);
        spinnerCarType = (Spinner) findViewById(R.id.spinnerCarType);
        buttonNext = (Button) findViewById(R.id.buttonNext);
        progressBarLoadBrand = (ProgressBar) findViewById(R.id.progressBarLoadBrand);

        colorAdap = ArrayAdapter.createFromResource(this, R.array.color, android.R.layout.simple_spinner_item);
        colorAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerColor.setAdapter(colorAdap);

        typeAdap = ArrayAdapter.createFromResource(this, R.array.car_type, android.R.layout.simple_spinner_item);
        typeAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCarType.setAdapter(typeAdap);

        getBrand();


        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextCarName.getText().toString();
                String price = editTextCarPrice.getText().toString();
                String year = editTextCarYear.getText().toString();
                String mileage = editTextMileage.getText().toString();
                String desc = editTextDesc.getText().toString();

                if (name.isEmpty() || price.isEmpty() || year.isEmpty() || mileage.isEmpty() || desc.isEmpty()) {
                    if (name.isEmpty()) {
                        editTextCarName.setError("Please enter Car Name");
                    }
                    if (price.isEmpty()) {
                        editTextCarPrice.setError("Please enter Car Price");
                    }
                    if (year.isEmpty()) {
                        editTextCarYear.setError("Please enter Car Publish Year");
                    }
                    if (mileage.isEmpty()) {
                        editTextMileage.setError("Please enter Mileage of car");
                    }
                    if (desc.isEmpty()) {
                        editTextDesc.setError("Please enter Car Description");
                    }

                } else {
                    if (Integer.parseInt(year) < 1940 || Integer.parseInt(year) > 2019) {
                        editTextCarYear.setError("Invalid Year!");
                    } else if (Double.parseDouble(price) <= 0 || Double.parseDouble(price) > 10000000) {
                        editTextCarPrice.setError("Invalid Price Range!");
                    } else if (Integer.parseInt(mileage) < 0 || Integer.parseInt(mileage) > 100000000) {
                        editTextMileage.setError("Invalid Mileage!");
                    } else {

                        Intent addinfo = new Intent(AddCarActivity.this, AddPhotoActivity.class);
                        addinfo.putExtra("brand", spinnerCarBrand.getSelectedItem().toString());
                        addinfo.putExtra("name", name);
                        addinfo.putExtra("color", spinnerColor.getSelectedItem().toString());
                        addinfo.putExtra("price", price);
                        addinfo.putExtra("year", year);
                        addinfo.putExtra("type", spinnerCarType.getSelectedItem().toString());
                        addinfo.putExtra("mileage", mileage);
                        addinfo.putExtra("desc", desc);
                        startActivity(addinfo);
                    }
                }
            }
        });
    }


    private void getBrand() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("brand");
                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            //follow index
                            String bid = object.getString("id").trim();
                            String bname = object.getString("name").trim();
                            String bdesc = object.getString("desc").trim();
                            String byear = object.getString("year").trim();
                            brand.add(bname);
                        }
                        progressBarLoadBrand.setVisibility(View.GONE);
                        buttonNext.setEnabled(true);
                        ArrayAdapter<String> brandAdap = new ArrayAdapter<String>(AddCarActivity.this, android.R.layout.simple_spinner_dropdown_item, brand);
                        spinnerCarBrand.setAdapter(brandAdap);

                    } else {
                        progressBarLoadBrand.setVisibility(View.GONE);
                        buttonNext.setEnabled(true);
                        Toast.makeText(AddCarActivity.this, "Error", Toast.LENGTH_LONG).show();
                        finish();
                    }
                } catch (JSONException e) {
                    progressBarLoadBrand.setVisibility(View.GONE);
                    buttonNext.setEnabled(true);
                    Toast.makeText(AddCarActivity.this, "Error" + e.toString(), Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBarLoadBrand.setVisibility(View.GONE);
                        buttonNext.setEnabled(true);
                        Toast.makeText(AddCarActivity.this, "Error " + error.toString(), Toast.LENGTH_LONG).show();
                        finish();
                    }
                }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
