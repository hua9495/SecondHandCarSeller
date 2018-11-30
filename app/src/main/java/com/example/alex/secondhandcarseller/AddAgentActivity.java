package com.example.alex.secondhandcarseller;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.view.View.GONE;

public class AddAgentActivity extends AppCompatActivity {
    private EditText etAgentName, etAgentIC, etAgentEmail, etAgentContact, etPassword;
    private TextView tvChooseDate;
    private Button buttonAddAgent;
    private String aName, aIC, aEmail, aContact, aWork, aPassword, subid, dealerid;
    private int mYear, mMonth, mDay;
    private String Url = "https://dewy-minuses.000webhostapp.com/InsertAgent.php";
    private ProgressBar progressBarInsertA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_agent);
        setTitle("Add New Agent");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences myPref = getSharedPreferences("My_Pref", MODE_PRIVATE);
        String checkid = myPref.getString("ID", null);
        subid = checkid.substring(0, 1);
        if (subid.equals("A")) {
            dealerid = myPref.getString("BelongDealer", null);
        } else {
            dealerid = myPref.getString("ID", null);
        }


        etAgentName = (EditText) findViewById(R.id.etAgentName);
        etAgentIC = (EditText) findViewById(R.id.etAgentIC);
        etAgentEmail = (EditText) findViewById(R.id.etAgentEmail);
        etAgentContact = (EditText) findViewById(R.id.etAgentContact);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvChooseDate = (TextView) findViewById(R.id.tvChooseDate);
        buttonAddAgent = (Button) findViewById(R.id.buttonAddAgent);
        progressBarInsertA = (ProgressBar) findViewById(R.id.progressBarInsertA);

        tvChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(AddAgentActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                        String myFormat = "dd/MM/yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
                        tvChooseDate.setText(sdf.format(myCalendar.getTime()));

                        mDay = selectedday;
                        mMonth = selectedmonth;
                        mYear = selectedyear;
                    }
                }, mYear, mMonth, mDay);
                //mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });

        buttonAddAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aName = etAgentName.getText().toString();
                aIC = etAgentIC.getText().toString();
                aEmail = etAgentEmail.getText().toString();
                aContact = etAgentContact.getText().toString();
                aWork = tvChooseDate.getText().toString();
                aPassword = etPassword.getText().toString();

                if (aName.isEmpty() || aIC.isEmpty() || aEmail.isEmpty() || aContact.isEmpty() || aWork.matches("Select A Date") || aPassword.isEmpty()) {
                    if (aName.isEmpty())
                        etAgentName.setError("Cannot Be Blank!");
                    if (aIC.isEmpty())
                        etAgentIC.setError("Cannot Be Blank!");
                    if (aEmail.isEmpty())
                        etAgentEmail.setError("Cannot Be Blank!");
                    if (aContact.isEmpty())
                        etAgentContact.setError("Cannot Be Blank!");
                    if (aWork.matches("Select A Date"))
                        tvChooseDate.setError("Please Select A Date");
                    if (aPassword.isEmpty())
                        etPassword.setError("Cannot Be Blank!");
                } else {
                    progressBarInsertA.setVisibility(View.VISIBLE);
                    etAgentName.setEnabled(false);
                    etAgentIC.setEnabled(false);
                    etAgentEmail.setEnabled(false);
                    etAgentContact.setEnabled(false);
                    tvChooseDate.setEnabled(false);
                    etPassword.setEnabled(false);
                    buttonAddAgent.setEnabled(false);

                    InsertAgent(AddAgentActivity.this);
                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void proceed() {
        progressBarInsertA.setVisibility(GONE);
        etAgentName.setEnabled(true);
        etAgentIC.setEnabled(true);
        etAgentEmail.setEnabled(true);
        etAgentContact.setEnabled(true);
        tvChooseDate.setEnabled(true);
        etPassword.setEnabled(true);
        buttonAddAgent.setEnabled(true);
    }


    private void InsertAgent(Context context) {
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
                                    proceed();
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    proceed();
                                    Toast.makeText(getApplicationContext(), message + " Please Try Again", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();

                                Toast.makeText(getApplicationContext(), "error" + e, Toast.LENGTH_LONG).show();
                                proceed();

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error : " + error.toString(), Toast.LENGTH_LONG).show();
                            proceed();

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("did", dealerid);
                    params.put("name", aName);
                    params.put("ic", aIC);
                    params.put("date", aWork);
                    params.put("email", aEmail);
                    params.put("contact", aContact);
                    params.put("pw", aPassword);

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
            proceed();
        }
    }

}
