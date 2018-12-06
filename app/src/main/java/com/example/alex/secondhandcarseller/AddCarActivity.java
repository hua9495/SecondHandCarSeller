package com.example.alex.secondhandcarseller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
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


    private EditText editTextCarName, editTextCarPrice, editTextCarYear, editTextMileage, editTextDesc,editTextPlateNumber;
    private Spinner spinnerCarBrand, spinnerColor, spinnerCarType;
    private ProgressBar progressBarLoadBrand;
    private Button buttonNext;
    private static String Url = "http://dewy-minuses.000webhostapp.com/brand.php";
    private ArrayList<String> brand = new ArrayList<>();

    ArrayAdapter<CharSequence> colorAdap, typeAdap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        setTitle("Add Car");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editTextCarName = (EditText) findViewById(R.id.editTextCarName);
        editTextPlateNumber = (EditText) findViewById(R.id.editTextPlateNumber);
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
                String plate = editTextPlateNumber.getText().toString();
                String brand = spinnerCarBrand.getSelectedItem().toString();


                if (name.isEmpty() || price.isEmpty() || year.isEmpty() || mileage.isEmpty() || desc.isEmpty()||plate.isEmpty()) {
                    if (name.isEmpty()) {
                        editTextCarName.setError("Please enter Car Name");
                        editTextCarName.requestFocus();
                    }
                    if (price.isEmpty()) {
                        editTextCarPrice.setError("Please enter Car Price");
                        editTextCarPrice.requestFocus();
                    }
                    if (year.isEmpty()) {
                        editTextCarYear.setError("Please enter Car Publish Year");
                        editTextCarYear.requestFocus();
                    }
                    if (mileage.isEmpty()) {
                        editTextMileage.setError("Please enter Mileage of car");
                        editTextMileage.requestFocus();
                    }
                    if (desc.isEmpty()) {
                        editTextDesc.setError("Please enter Car Description");
                        editTextDesc.requestFocus();
                    }
                    if (plate.isEmpty()) {
                        editTextPlateNumber.setError("Please enter Car Plate Number");
                        editTextPlateNumber.requestFocus();
                    }

                } else {
                    if (Double.parseDouble(price) <= 0 || Double.parseDouble(price) > 10000000) {
                        editTextCarPrice.setError("Invalid Price Range!");
                        editTextCarPrice.requestFocus();
                    } else if (Integer.parseInt(year) < 1940 || Integer.parseInt(year) > 2019) {
                        editTextCarYear.setError("Invalid Year!");
                        editTextCarYear.requestFocus();
                    } else if (Integer.parseInt(mileage) < 0 || Integer.parseInt(mileage) > 100000000) {
                        editTextMileage.setError("Invalid Mileage!");
                        editTextMileage.requestFocus();
                    } else {

                        Intent addinfo = new Intent(AddCarActivity.this, AddPhotoActivity.class);
                        addinfo.putExtra("brand", brand);
                        addinfo.putExtra("name", name);
                        addinfo.putExtra("color", spinnerColor.getSelectedItem().toString());
                        addinfo.putExtra("price", price);
                        addinfo.putExtra("year", year);
                        addinfo.putExtra("type", spinnerCarType.getSelectedItem().toString());
                        addinfo.putExtra("mileage", mileage);
                        addinfo.putExtra("plate", plate);
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
                            String bname = object.getString("name");
                            brand.add(bname);
                        }
                        progressBarLoadBrand.setVisibility(View.GONE);
                        buttonNext.setEnabled(true);
                        ArrayAdapter<String> brandAdap = new ArrayAdapter<>(AddCarActivity.this, android.R.layout.simple_spinner_dropdown_item, brand);
                        spinnerCarBrand.setAdapter(brandAdap);

                    } else {
                        progressBarLoadBrand.setVisibility(View.GONE);
                        buttonNext.setEnabled(true);
                        Toast.makeText(AddCarActivity.this, "Error", Toast.LENGTH_LONG).show();
                        finish();
                    }
                } catch (JSONException e) {
                    if (!CarFragment.isConnected(AddCarActivity.this)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddCarActivity.this);
                        builder.setTitle("Connection Error");
                        builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();

                    } else
                        Toast.makeText(AddCarActivity.this, "Error" + e.toString(), Toast.LENGTH_LONG).show();
                    progressBarLoadBrand.setVisibility(View.GONE);
                    buttonNext.setEnabled(true);
                    finish();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (!CarFragment.isConnected(AddCarActivity.this)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddCarActivity.this);
                            builder.setTitle("Connection Error");
                            builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();

                        } else
                            Toast.makeText(AddCarActivity.this, "Error " + error.toString(), Toast.LENGTH_LONG).show();
                        progressBarLoadBrand.setVisibility(View.GONE);
                        buttonNext.setEnabled(true);
                        finish();
                    }
                }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();

        return super.onOptionsItemSelected(item);
    }
}
