package com.example.alex.secondhandcarseller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;

public class AgentDetailActivity extends AppCompatActivity {
    private EditText editTextAgentName, editTextAgentIC, editTextAgentEmail, editTextAgentContact, editTextAgentWork;
    private Button buttonBack, buttonUpdateAgent;
    private CheckBox checkBoxRetired;
    private String name, ic, id, contact, email, workdate, status;
    private String Url = "https://dewy-minuses.000webhostapp.com/UpdateAgent.php";
    private ProgressBar progressBarUpdateA;
    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_detail);
        setTitle("Edit Agent");

        editTextAgentName = (EditText) findViewById(R.id.editTextAgentName);
        editTextAgentIC = (EditText) findViewById(R.id.editTextAgentIC);
        editTextAgentEmail = (EditText) findViewById(R.id.editTextAgentEmail);
        editTextAgentContact = (EditText) findViewById(R.id.editTextAgentContact);
        editTextAgentWork = (EditText) findViewById(R.id.editTextAgentWork);
        buttonBack = (Button) findViewById(R.id.buttonBack);
        buttonUpdateAgent = (Button) findViewById(R.id.buttonUpdateAgent);
        checkBoxRetired = (CheckBox) findViewById(R.id.checkBoxRetired);
        progressBarUpdateA = (ProgressBar) findViewById(R.id.progressBarUpdateA);

        Intent intent = getIntent();
        name = intent.getStringExtra("Aname");
        ic = intent.getStringExtra("Aic");
        id = intent.getStringExtra("Aid");
        contact = intent.getStringExtra("Acontact");
        email = intent.getStringExtra("Aemail");
        workdate = intent.getStringExtra("Awork");
        status = intent.getStringExtra("Astatus");

        editTextAgentName.setText(name);
        editTextAgentIC.setText(ic);
        editTextAgentEmail.setText(email);
        editTextAgentContact.setText(contact);
        editTextAgentWork.setText(workdate);

        if (status.equals("Deleted")) {
            checkBoxRetired.setChecked(true);
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure to delete this Agent?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                status = "Deleted";
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkBoxRetired.setChecked(false);
                dialog.dismiss();
            }
        });

        checkBoxRetired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBoxRetired.isChecked()) {
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    status = "On";
                }

            }
        });


        buttonUpdateAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = editTextAgentName.getText().toString();
                ic = editTextAgentIC.getText().toString();
                email = editTextAgentEmail.getText().toString();
                contact = editTextAgentContact.getText().toString();
                workdate = editTextAgentWork.getText().toString();

                progressBarUpdateA.setVisibility(View.VISIBLE);
                editTextAgentName.setEnabled(false);
                editTextAgentIC.setEnabled(false);
                editTextAgentEmail.setEnabled(false);
                editTextAgentContact.setEnabled(false);
                editTextAgentWork.setEnabled(false);
                checkBoxRetired.setEnabled(false);
                buttonBack.setEnabled(false);
                buttonUpdateAgent.setEnabled(false);

                updateAgent(AgentDetailActivity.this);

            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void updateAgent(Context context) {
        queue = Volley.newRequestQueue(context);
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
                    params.put("name", name);
                    params.put("ic", ic);
                    params.put("id", id);
                    params.put("date", workdate);
                    params.put("email", email);
                    params.put("contact", contact);
                    params.put("status", status);

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

    private void proceed() {
        progressBarUpdateA.setVisibility(GONE);
        editTextAgentName.setEnabled(true);
        editTextAgentIC.setEnabled(true);
        editTextAgentEmail.setEnabled(true);
        editTextAgentContact.setEnabled(true);
        editTextAgentWork.setEnabled(true);
        checkBoxRetired.setEnabled(true);
        buttonBack.setEnabled(true);
        buttonUpdateAgent.setEnabled(true);
    }


}
