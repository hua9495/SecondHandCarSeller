package com.example.alex.secondhandcarseller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ResetPwActivity extends AppCompatActivity {
    private Button btnReset;
    private EditText etEmailAddr;
    private ProgressBar checkingEmail;
    private String email;
    public static final String TAG = "my.edu.tarc.secondhandcar";
    List<String> allEmailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pw);
        setTitle(R.string.title_reset_pw);
        allEmailList = new ArrayList<>();
        btnReset = (Button) findViewById(R.id.buttonReset);
        etEmailAddr = (EditText) findViewById(R.id.editTextEmail);
        checkingEmail = (ProgressBar) findViewById(R.id.checkingEmail);
        checkingEmail.setVisibility(View.GONE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getAgentDealerEmail(getApplicationContext(), getString(R.string.get_allEmail_url));
    }
    public void OnReset(View v) {
        email = etEmailAddr.getText().toString();
        btnReset.setEnabled(false);
        etEmailAddr.setEnabled(false);
        checkingEmail.setVisibility(View.VISIBLE);
        if (!isConnected(ResetPwActivity.this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ResetPwActivity.this);
            builder.setTitle("Connection Error");
            builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();

        } else if (etEmailAddr.getText().toString().isEmpty()) {
            etEmailAddr.setError("Please fill in your email address");
        } else {
            if (foundEmail(email)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ResetPwActivity.this);
                builder.setTitle("Email Sent");
                builder.setMessage("Email sent!Please check your mailbox to reset password").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent loginIntent = new Intent(ResetPwActivity.this, MainActivity.class);
                        startActivity(loginIntent);
                    }
                }).create().show();

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(ResetPwActivity.this);
                builder.setTitle("Incorrect Email");
                builder.setMessage("The email entered is not in the record.\nPlease try again").setNegativeButton("Retry", null).create().show();
            }

        }
        btnReset.setEnabled(true);
        etEmailAddr.setEnabled(true);
        checkingEmail.setVisibility(View.GONE);


    }

    //retrieve all the agent and dealer email to check the email is exist or not
    private void getAgentDealerEmail(Context context, String url) {
        allEmailList.clear();
        // Instantiate the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String successA = jsonObject.getString("successA");
                    String successB = jsonObject.getString("successB");
                    JSONArray jsonArrayDealer = jsonObject.getJSONArray("EMAIL");
                    JSONArray jsonArrayAgent = jsonObject.getJSONArray("AEMAIL");
                    //if have dealer record
                    if (successA.equals("1")) {
                        for (int i = 0; i < jsonArrayDealer.length(); i++) {
                            JSONObject objectDealer = jsonArrayDealer.getJSONObject(i);
                            //follow index
                            String dealerEmail = objectDealer.getString("dealerEmail").trim();
                            allEmailList.add(dealerEmail);
                        }
                    }
                    //if have agent record
                    if (successB.equals("1")) {
                        for (int i = 0; i < jsonArrayAgent.length(); i++) {
                            JSONObject objectAgent = jsonArrayAgent.getJSONObject(i);
                            String agentEmail = objectAgent.getString("agentEmail").trim();
                            allEmailList.add(agentEmail);

                        }
                    }

                } catch (JSONException e) {
                    checkError(e, ResetPwActivity.this);

                }
            }
        },
                new Response.ErrorListener()

                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        checkError( error, ResetPwActivity.this);

                    }
                });

        // Set the tag on the request.
        stringRequest.setTag(TAG);

        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest);
    }

    private void checkError(Exception e, Context context) {
        if (!isConnected(context)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Connection Error");
            builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();

        } else {
            Toast.makeText(context, "Send email failed. \n" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    //to check if the email entered is repeated or not
    public boolean foundEmail(String emails) {
        //check whether the username exist or not

        boolean found = false;
        for (int i = 0; i < allEmailList.size(); ++i) {
            if (allEmailList.get(i).equals(emails)) {
                found = true;

            }
        }
        return found;
    }

    //check internet
    public static boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
