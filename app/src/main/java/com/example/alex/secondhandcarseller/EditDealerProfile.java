package com.example.alex.secondhandcarseller;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class EditDealerProfile extends AppCompatActivity {
    private Button buttonSave, buttonEdit, buttonback;
    private EditText eTcompanyName, eTcompanyAddress, eTcompanyLocation, eTcompanyEmail, eTcompanyContact, eTcompanyPic;
    private String name, email, id, location, contact, pic, address;
    private String Url = "https://dewy-minuses.000webhostapp.com/UpdateDealer.php";
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dealer_profile);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonEdit = (Button) findViewById(R.id.buttonEdit);
        buttonback = (Button) findViewById(R.id.buttonback);
        eTcompanyName = (EditText) findViewById(R.id.eTcompanyName);
        eTcompanyAddress = (EditText) findViewById(R.id.eTcompanyAddress);
        eTcompanyLocation = (EditText) findViewById(R.id.eTcompanyLocation);
        eTcompanyEmail = (EditText) findViewById(R.id.eTcompanyEmail);
        eTcompanyContact = (EditText) findViewById(R.id.eTcompanyContact);
        eTcompanyPic = (EditText) findViewById(R.id.eTcompanyPic);

        SharedPreferences myPref = this.getSharedPreferences("My_Pref", MODE_PRIVATE);
        String Name = myPref.getString("Name", null);
        String Email = myPref.getString("Email", null);
        id = myPref.getString("ID", null);
        String Location = myPref.getString("Location", null);
        String Contact = myPref.getString("Contact", null);
        String PersonIC = myPref.getString("PersonIC", null);
        String Address = myPref.getString("Address", null);

        eTcompanyName.setText(Name);
        eTcompanyLocation.setText(Location);
        eTcompanyEmail.setText(Email);
        eTcompanyContact.setText(Contact);
        eTcompanyPic.setText(PersonIC);
        eTcompanyAddress.setText(Address);

        buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableAll(true);
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = eTcompanyName.getText().toString();
                email = eTcompanyEmail.getText().toString();
                location = eTcompanyLocation.getText().toString();
                contact = eTcompanyContact.getText().toString();
                pic = eTcompanyPic.getText().toString();
                address = eTcompanyAddress.getText().toString();
                if (name.isEmpty() || email.isEmpty() || location.isEmpty() || contact.isEmpty() || pic.isEmpty() || address.isEmpty()) {
                    if (name.isEmpty())
                        eTcompanyName.setError("Cannot Be Blank!");
                    if (email.isEmpty())
                        eTcompanyEmail.setError("Cannot Be Blank!");
                    if (location.isEmpty())
                        eTcompanyLocation.setError("Cannot Be Blank!");
                    if (contact.isEmpty())
                        eTcompanyContact.setError("Cannot Be Blank!");
                    if (pic.isEmpty())
                        eTcompanyPic.setError("Cannot Be Blank!");
                    if (address.isEmpty())
                        eTcompanyAddress.setError("Cannot Be Blank!");
                } else {
                    updateDealer(EditDealerProfile.this);
                }
            }
        });
    }

    private void updateDealer(Context context) {
        enableAll(false);
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
                                    enableAll(true);
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    enableAll(true);
                                    Toast.makeText(getApplicationContext(), message + " Please Try Again", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();

                                Toast.makeText(getApplicationContext(), "error" + e, Toast.LENGTH_LONG).show();
                                enableAll(true);

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error : " + error.toString(), Toast.LENGTH_LONG).show();
                            enableAll(true);

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", name);
                    params.put("location", location);
                    params.put("id", id);
                    params.put("address", address);
                    params.put("email", email);
                    params.put("contact", contact);
                    params.put("pic", pic);

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
            enableAll(true);
        }
    }

    private void enableAll(boolean Enable) {
        eTcompanyName.setEnabled(Enable);
        eTcompanyAddress.setEnabled(Enable);
        eTcompanyLocation.setEnabled(Enable);
        eTcompanyEmail.setEnabled(Enable);
        eTcompanyContact.setEnabled(Enable);
        eTcompanyPic.setEnabled(Enable);
        buttonSave.setEnabled(Enable);
        buttonEdit.setEnabled(!Enable);

    }
}
