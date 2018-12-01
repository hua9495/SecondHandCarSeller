package com.example.alex.secondhandcarseller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
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
    private EditText editTextAgentName, editTextAgentIC, editTextAgentEmail, editTextAgentContact, editTextAgentWork, editTextReasonAgent;
    private Button buttonBack, buttonUpdateAgent;
    private CheckBox checkBoxRetired;
    private String name, ic, id, contact, email, workdate, status, reason = "";
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
        editTextReasonAgent = (EditText) findViewById(R.id.editTextReasonAgent);
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
        reason = intent.getStringExtra("Reason");

        editTextAgentName.setText(name);
        editTextAgentIC.setText(ic);
        editTextAgentEmail.setText(email);
        editTextAgentContact.setText(contact);
        editTextAgentWork.setText(workdate);
        editTextReasonAgent.setEnabled(false);

        if (status.equals("Resigned")) {
            checkBoxRetired.setChecked(true);
            checkBoxRetired.setEnabled(false);
            editTextAgentName.setEnabled(false);
            editTextAgentIC.setEnabled(false);
            editTextAgentEmail.setEnabled(false);
            editTextAgentContact.setEnabled(false);
            editTextAgentWork.setEnabled(false);
            editTextReasonAgent.setEnabled(false);
            buttonUpdateAgent.setEnabled(false);

            editTextReasonAgent.setText(reason);
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure to delete this Agent?\nYou cannot retrieve back this account after you delete.");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                status = "Resigned";
                editTextReasonAgent.setEnabled(true);
                editTextReasonAgent.requestFocus();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkBoxRetired.setChecked(false);
                editTextReasonAgent.setEnabled(false);
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
                    status = "Active";
                    editTextReasonAgent.setEnabled(false);
                    editTextAgentName.requestFocus();
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
                reason = editTextReasonAgent.getText().toString();
                if (name.isEmpty() || ic.isEmpty() || email.isEmpty() || contact.isEmpty() || workdate.isEmpty()) {
                    if (name.isEmpty())
                        editTextAgentName.setError("Cannot Be Blank!");
                    if (ic.isEmpty())
                        editTextAgentIC.setError("Cannot Be Blank!");
                    if (email.isEmpty())
                        editTextAgentEmail.setError("Cannot Be Blank!");
                    if (contact.isEmpty())
                        editTextAgentContact.setError("Cannot Be Blank!");
                    if (workdate.isEmpty())
                        editTextAgentWork.setError("Cannot Be Blank!");

                } else {
                    if (checkBoxRetired.isChecked()) {
                        if (reason.isEmpty())
                            editTextReasonAgent.setError("Cannot be blank if you want to delete account");
                        else {
                            proceedAll(View.VISIBLE, false);
                            updateAgent(AgentDetailActivity.this);
                        }
                    }
                    else {
                        reason="";
                        proceedAll(View.VISIBLE, false);
                        updateAgent(AgentDetailActivity.this);
                    }
                }


            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void proceedAll(int visible, boolean enabled) {
        progressBarUpdateA.setVisibility(visible);
        editTextAgentName.setEnabled(enabled);
        editTextAgentIC.setEnabled(enabled);
        editTextAgentEmail.setEnabled(enabled);
        editTextAgentContact.setEnabled(enabled);
        editTextAgentWork.setEnabled(enabled);
        checkBoxRetired.setEnabled(enabled);
        buttonBack.setEnabled(enabled);
        buttonUpdateAgent.setEnabled(enabled);
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
                                    proceedAll(View.GONE, true);
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    proceedAll(View.GONE, true);
                                    Toast.makeText(getApplicationContext(), message + " Please Try Again", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();

                                Toast.makeText(getApplicationContext(), "error" + e, Toast.LENGTH_LONG).show();
                                proceedAll(View.GONE, true);

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error : " + error.toString(), Toast.LENGTH_LONG).show();
                            proceedAll(View.GONE, true);

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
                    params.put("reason",reason);

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
            proceedAll(View.GONE, true);
        }
    }


}
