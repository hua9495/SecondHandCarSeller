package com.example.alex.secondhandcarseller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditCarActivity extends AppCompatActivity {
    private Button buttonSold;
    private TextView TvCarName, TvColor, TvPrice, TvYear, TvMile, TvDesc;
    private String id, name, img, brand, price, color, desc, year, mile;
    private ImageView imageViewShCar;
    private String Url = "https://dewy-minuses.000webhostapp.com/deleteCar.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_car);
        setTitle(R.string.title_edit_car);
        buttonSold = (Button) findViewById(R.id.buttonSold);
        TvCarName = (TextView) findViewById(R.id.TvCarName);
        TvColor = (TextView) findViewById(R.id.TvColor);
        TvPrice = (TextView) findViewById(R.id.TvPrice);
        TvYear = (TextView) findViewById(R.id.TvYear);
        TvMile = (TextView) findViewById(R.id.TvMile);
        TvDesc = (TextView) findViewById(R.id.TvDesc);
        imageViewShCar = (ImageView) findViewById(R.id.imageViewShCar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        id = intent.getStringExtra("CarID");
        name = intent.getStringExtra("CarName");
        img = intent.getStringExtra("CarImg");
        brand = intent.getStringExtra("CarBrand");
        price = intent.getStringExtra("CarPrice");
        color = intent.getStringExtra("CarColor");
        desc = intent.getStringExtra("CarDesc");
        year = intent.getStringExtra("CarYear");
        mile = intent.getStringExtra("CarMile");

        TvCarName.setText(name);
        TvColor.setText(color);
        TvPrice.setText(price);
        TvYear.setText(year);
        TvMile.setText(mile);
        TvDesc.setText(desc);
        Glide.with(this).load(img).into(imageViewShCar);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure to Mark this as Sold ?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                deleteCar(EditCarActivity.this);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        
        buttonSold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }


    private void deleteCar(Context context) {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
