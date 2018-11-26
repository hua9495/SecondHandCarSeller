package com.example.alex.secondhandcarseller;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.alex.secondhandcarseller.CarFragment.isConnected;

public class DealerRegisterActivity extends AppCompatActivity {
    private EditText editTextComName, editTextAddress, editTextLocation, editTextPic, editTextContact, ETemail, editTextPassword, editTextConfirmPw;
    private String name, address, location, pic, contact, email, password, conpw;
    private ProgressBar progressBarInsertDealer;
    private ArrayList<String> emails = new ArrayList<>();
    private Button buttonRegister;
    private String Url = "https://dewy-minuses.000webhostapp.com/InsertDealer.php";
    private String Url2 = "https://dewy-minuses.000webhostapp.com/checkDealerEmail.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_register);
        setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editTextComName = (EditText) findViewById(R.id.editTextComName);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextLocation = (EditText) findViewById(R.id.editTextLocation);
        editTextPic = (EditText) findViewById(R.id.editTextPic);
        editTextContact = (EditText) findViewById(R.id.editTextContact);
        ETemail = (EditText) findViewById(R.id.ETemail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextConfirmPw = (EditText) findViewById(R.id.editTextConfirmPw);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        progressBarInsertDealer = (ProgressBar) findViewById(R.id.progressBarInsertDealer);

        getEmail();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = editTextComName.getText().toString();
                address = editTextAddress.getText().toString();
                location = editTextLocation.getText().toString();
                pic = editTextPic.getText().toString();
                contact = editTextContact.getText().toString();
                email = ETemail.getText().toString();
                password = editTextPassword.getText().toString();
                conpw = editTextConfirmPw.getText().toString();

                if (name.isEmpty() || address.isEmpty() || location.isEmpty() || pic.isEmpty() || contact.isEmpty() || email.isEmpty() || password.isEmpty() || conpw.isEmpty()) {
                    if (name.isEmpty()) {
                        editTextComName.setError("Please fill in Company Name");
                    }

                    if (address.isEmpty()) {
                        editTextAddress.setError("Please fill in Company Address");
                    }

                    if (location.isEmpty()) {
                        editTextLocation.setError("Please fill in Location");
                    }

                    if (pic.isEmpty()) {
                        editTextPic.setError("Please fill in Person in Charge");
                    }

                    if (contact.isEmpty()) {
                        editTextContact.setError("Please fill in Company Contact Number");
                    }

                    if (email.isEmpty()) {
                        ETemail.setError("Please fill in Company Email");
                    }

                    if (password.isEmpty()) {
                        editTextPassword.setError("Please fill in Password");
                    }

                    if (conpw.isEmpty()) {
                        editTextConfirmPw.setError("Please fill in Confirm Password");
                    }

                    if (password.length() <= 6) {
                        editTextPassword.setError("Password length must more than 6");
                    }
                } else {
                    boolean checkemail = true;
                    for (int i = 0; i < emails.size(); i++) {
                        if (email.matches(emails.get(i))) {
                            checkemail = false;
                        }
                    }
                    if (checkemail) {
                        if (password.matches(conpw)) {
                            Proceed(false, View.VISIBLE);
                            InsertDealer(DealerRegisterActivity.this);
                        } else {
                            editTextConfirmPw.setError("Password Not Match");
                        }
                    } else {
                        ETemail.setError("Email Already Exist!");
                    }
                }

            }
        });
    }

    private void Proceed(boolean enabled, int visible) {
        editTextComName.setEnabled(enabled);
        editTextAddress.setEnabled(enabled);
        editTextLocation.setEnabled(enabled);
        editTextPic.setEnabled(enabled);
        editTextContact.setEnabled(enabled);
        ETemail.setEnabled(enabled);
        editTextPassword.setEnabled(enabled);
        editTextConfirmPw.setEnabled(enabled);
        buttonRegister.setEnabled(enabled);
        progressBarInsertDealer.setVisibility(visible);
    }

    private void getEmail() {
        Proceed(false, View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("dealerEmail");
                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String email1 = object.getString("email");
                            emails.add(email1);
                        }
                        Proceed(true, View.GONE);
                    } else {
                        Proceed(true, View.GONE);
                    }

                } catch (JSONException e) {
                    //if no internet
                    if (!isConnected(DealerRegisterActivity.this)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(DealerRegisterActivity.this);
                        builder.setTitle("Connection Error");
                        builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();

                    } else
                        Toast.makeText(DealerRegisterActivity.this, "Error", Toast.LENGTH_LONG).show();
                    Proceed(true, View.GONE);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //if no internet
                        if (!isConnected(DealerRegisterActivity.this)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(DealerRegisterActivity.this);
                            builder.setTitle("Connection Error");
                            builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();

                        } else
                            Toast.makeText(DealerRegisterActivity.this, "Error " + error.toString(), Toast.LENGTH_LONG).show();

                        Proceed(true, View.GONE);

                    }
                }) {

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void InsertDealer(Context context) {
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
                                    Proceed(true, View.GONE);
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Proceed(true, View.GONE);
                                    Toast.makeText(getApplicationContext(), message + " Please Try Again", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();

                                Toast.makeText(getApplicationContext(), "error" + e, Toast.LENGTH_LONG).show();
                                Proceed(true, View.GONE);

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error : " + error.toString(), Toast.LENGTH_LONG).show();
                            Proceed(true, View.GONE);

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", name);
                    params.put("address", address);
                    params.put("location", location);
                    params.put("pic", pic);
                    params.put("contact", contact);
                    params.put("email", email);
                    params.put("pw", password);
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
            Proceed(true, View.GONE);
        }
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
