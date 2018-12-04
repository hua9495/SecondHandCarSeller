package com.example.alex.secondhandcarseller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
    private Button buttonSold, buttonChange;
    private EditText edCarName, edCarPrice, edCarYear, edMile, edCarDesc;
    private Spinner spinnerEditColor;
    private String id, name, img, brand, price, color, desc, year, mile;
    private ImageView imageViewShCar;
    private ProgressBar progressBarEditcar;
    private String Url = "https://dewy-minuses.000webhostapp.com/deleteCar.php";
    private String Url2 = "https://dewy-minuses.000webhostapp.com/editCar.php";
    ArrayAdapter<CharSequence> colorAdap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_car);
        setTitle(R.string.title_edit_car);
        spinnerEditColor = (Spinner) findViewById(R.id.spinnerEditColor);
        buttonSold = (Button) findViewById(R.id.buttonSold);
        buttonChange = (Button) findViewById(R.id.buttonChange);
        edCarName = (EditText) findViewById(R.id.edCarName);
        edCarPrice = (EditText) findViewById(R.id.edCarPrice);
        edCarYear = (EditText) findViewById(R.id.edCarYear);
        edMile = (EditText) findViewById(R.id.edMile);
        edCarDesc = (EditText) findViewById(R.id.edCarDesc);
        imageViewShCar = (ImageView) findViewById(R.id.imageViewShCar);
        progressBarEditcar = (ProgressBar) findViewById(R.id.progressBarEditcar);

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


        colorAdap = ArrayAdapter.createFromResource(this, R.array.color, android.R.layout.simple_spinner_item);
        colorAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEditColor.setAdapter(colorAdap);

        int spinnerPosition = colorAdap.getPosition(color);
        spinnerEditColor.setSelection(spinnerPosition);
        spinnerEditColor.setEnabled(false);

        edCarName.setText(name);
        edCarPrice.setText(price);
        edCarYear.setText(year);
        edMile.setText(mile);
        edCarDesc.setText(desc);
        Glide.with(this).load(img).into(imageViewShCar);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");

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
                builder.setMessage("Are you sure to mark this as Sold ?");
                AlertDialog alert = builder.create();
                alert.show();
            }
        });


        final AlertDialog.Builder builder2 = new AlertDialog.Builder(this);

        builder2.setTitle("Confirm");

        builder2.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                UpdateCar(EditCarActivity.this);

                dialog.dismiss();
            }
        });

        builder2.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        buttonChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getChange = buttonChange.getText().toString();

                if (getChange.equals("Edit")) {
                    changeAll(true, view.GONE);
                    buttonChange.setText("Save");
                } else {

                    String Name = edCarName.getText().toString();
                    String Year = edCarYear.getText().toString();
                    String Mile = edMile.getText().toString();
                    String Color = spinnerEditColor.getSelectedItem().toString();
                    String Desc = edCarDesc.getText().toString();
                    String Price = edCarPrice.getText().toString();

                    int chkYear=Integer.parseInt(Year);

                    if (Name.isEmpty() || Year.isEmpty() || Mile.isEmpty() || Desc.isEmpty() || Price.isEmpty()) {
                        if (Name.isEmpty())
                            edCarName.setError("Cannot Be Blank");
                        if (Year.isEmpty())
                            edCarYear.setError("Cannot Be Blank");
                        if (Mile.isEmpty())
                            edMile.setError("Cannot Be Blank");
                        if (Desc.isEmpty())
                            edCarDesc.setError("Cannot Be Blank");
                        if (Price.isEmpty())
                            edCarPrice.setError("Cannot Be Blank");

                    } else {
                        if (chkYear >= 1950 && chkYear <= 2018) {
                            name = Name;
                            year = Year;
                            mile = Mile;
                            color = Color;
                            desc = Desc;
                            price = Price;
                            builder2.setMessage("Information of this car will change. \nAre you sure?");
                            AlertDialog alert = builder2.create();
                            alert.show();
                        }
                     else {
                            edCarYear.setError("Range Only can between 1950 and 2018");
                        }


                    }
                }
            }
        });
    }


    private void changeAll(boolean enabled, int gone) {
        edCarDesc.setEnabled(enabled);
        edMile.setEnabled(enabled);
        edCarYear.setEnabled(enabled);
        edCarPrice.setEnabled(enabled);
        edCarName.setEnabled(enabled);
        spinnerEditColor.setEnabled(enabled);
        buttonSold.setEnabled(!enabled);
        progressBarEditcar.setVisibility(gone);
    }


    private void deleteCar(Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        progressBarEditcar.setVisibility(View.VISIBLE);
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

                                    progressBarEditcar.setVisibility(View.GONE);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), message + " Please Try Again", Toast.LENGTH_LONG).show();
                                    progressBarEditcar.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "error" + e, Toast.LENGTH_LONG).show();
                                progressBarEditcar.setVisibility(View.GONE);

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error : " + error.toString(), Toast.LENGTH_LONG).show();
                            progressBarEditcar.setVisibility(View.GONE);
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
            progressBarEditcar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }


    private void UpdateCar(Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        buttonChange.setText("Edit");
        changeAll(false, View.VISIBLE);
        //Send data
        try {
            StringRequest postRequest = new StringRequest(
                    Request.Method.POST,
                    Url2,
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

                                    progressBarEditcar.setVisibility(View.GONE);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Update Failed,Please Try Again", Toast.LENGTH_LONG).show();

                                    progressBarEditcar.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "error" + e, Toast.LENGTH_LONG).show();
                                progressBarEditcar.setVisibility(View.GONE);

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error : " + error.toString(), Toast.LENGTH_LONG).show();
                            progressBarEditcar.setVisibility(View.GONE);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", id);
                    params.put("name", name);
                    params.put("price", price);
                    params.put("color", color);
                    params.put("desc", desc);
                    params.put("mile", mile);
                    params.put("year", year);

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
