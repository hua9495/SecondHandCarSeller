package com.example.alex.secondhandcarseller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPw;
    private Button buttonLogin;
    private TextView textViewResetPw,textViewRegister;
    private String pw, email;
    private ProgressBar loading;
    private static String Url = "http://dewy-minuses.000webhostapp.com/dealerlogin.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPw = (EditText) findViewById(R.id.editTextPw);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        textViewResetPw = (TextView) findViewById(R.id.textViewResetPw);
        textViewRegister = (TextView) findViewById(R.id.textViewRegister);
        loading = (ProgressBar) findViewById(R.id.loading);

        //skip this activity if already logged in
        SharedPreferences myPref = getSharedPreferences("My_Pref", MODE_PRIVATE);
        String checkUser = myPref.getString("ID", null);


        if (checkUser != null) {
            String checkD = checkUser.substring(0, 1);
            if (checkD.matches("D")) {
                Intent login = new Intent(MainActivity.this, DealerActivity.class);
                startActivity(login);
                finish();
            } else {
                Intent login = new Intent(MainActivity.this, AgentActivity.class);
                startActivity(login);
                finish();
            }
        }

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,DealerRegisterActivity.class);
                startActivity(intent);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw = editTextPw.getText().toString().trim();
                email = editTextEmail.getText().toString().trim();

                if (pw.isEmpty()) {
                    editTextPw.setError("Please enter Password!");
                } else if (email.isEmpty()) {
                    editTextEmail.setError("Please enter Email!");
                } else {
                    if (email.equals("Adminlogin") && pw.equals("admin")) {
                        Intent login = new Intent(MainActivity.this, AdminActivity.class);
                        startActivity(login);
                        login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        finish();
                    } else
                        Login(email, pw);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
    }

    private void Login(final String DealerEmail, final String Dealerpw) {
        loading.setVisibility(View.VISIBLE);
        buttonLogin.setVisibility(View.GONE);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("login");
                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            //follow index
                            String demail = object.getString("email").trim();
                            String dname = object.getString("name").trim();
                            String did = object.getString("id").trim();
                            String dlocation = object.getString("location").trim();
                            String dcontact = object.getString("contact").trim();
                            String dpic = object.getString("personIC").trim();

                            loading.setVisibility(View.GONE);
                            buttonLogin.setVisibility(View.VISIBLE);

                            Toast.makeText(MainActivity.this, "Dealer", Toast.LENGTH_LONG).show();

                            SharedPreferences.Editor user = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                            user.putString("Email", demail);
                            user.putString("ID", did);
                            user.putString("Name", dname);
                            user.putString("Location", dlocation);
                            user.putString("Contact", dcontact);
                            user.putString("PersonIC", dpic);
                            user.apply();

                            Intent login = new Intent(MainActivity.this, DealerActivity.class);
                            login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(login);
                            finish();

                        }
                    } else if (success.equals("2")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            //follow index
                            String aemail = object.getString("email").trim();
                            String aid = object.getString("id").trim();
                            String did = object.getString("dealerID").trim();
                            String aic = object.getString("ic").trim();
                            String aname = object.getString("name").trim();
                            String acontact = object.getString("contact").trim();
                            String workstart = object.getString("workstart").trim();
                            loading.setVisibility(View.GONE);
                            buttonLogin.setVisibility(View.VISIBLE);

                            Toast.makeText(MainActivity.this, "Agent", Toast.LENGTH_LONG).show();

                            SharedPreferences.Editor user = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                            user.putString("Email", aemail);
                            user.putString("ID", aid);
                            user.putString("BelongDealer", did);
                            user.putString("ICno", aic);
                            user.putString("Name", aname);
                            user.putString("Contact", acontact);
                            user.putString("WorkStart", workstart);
                            user.apply();

                            Intent login = new Intent(MainActivity.this, AgentActivity.class);
                            login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(login);
                            finish();

                        }
                    } else {
                        loading.setVisibility(View.GONE);
                        buttonLogin.setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this, "Incorrect Password", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Invalid Email \nPlease make sure u enter the correct Email", Toast.LENGTH_LONG).show();
                    loading.setVisibility(View.GONE);
                    buttonLogin.setVisibility(View.VISIBLE);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(MainActivity.this, "Error" + error.toString(), Toast.LENGTH_LONG).show();
                        loading.setVisibility(View.GONE);
                        buttonLogin.setVisibility(View.VISIBLE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("dealerEmail", DealerEmail);
                params.put("password", Dealerpw);


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}
