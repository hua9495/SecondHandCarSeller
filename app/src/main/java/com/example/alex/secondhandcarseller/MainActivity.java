package com.example.alex.secondhandcarseller;

import android.content.Intent;
import android.content.SharedPreferences;
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
    private TextView textViewResetPw;
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
        loading = (ProgressBar) findViewById(R.id.loading);


        SharedPreferences chkuser = getSharedPreferences("My_Pref", MODE_PRIVATE);
        String chkus = chkuser.getString("LoginEmail", null);

        if (chkus != null) {
            Intent login = new Intent(MainActivity.this, DealerActivity.class);
            startActivity(login);
            finish();
        }


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw = editTextPw.getText().toString().trim();
                email = editTextEmail.getText().toString().trim();


                if (!pw.isEmpty() || !email.isEmpty()) {
                    Login(email, pw);
                } else {
                    editTextEmail.setError("Please enter Email!");
                    editTextPw.setError("Please enter Password!");
                }


            }
        });


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
                            String email = object.getString("email").trim();
                            loading.setVisibility(View.GONE);
                            buttonLogin.setVisibility(View.VISIBLE);

                            Toast.makeText(MainActivity.this, "Login success", Toast.LENGTH_LONG).show();

                            //SharedPreferences.Editor user = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                            // user.putString("LoginEmail", email);
                            //  user.apply();

                            // Intent login = new Intent(MainActivity.this, DealerActivity.class);
                            //startActivity(login);
                            //finish();

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error" + e.toString(), Toast.LENGTH_LONG).show();
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
