package com.example.alex.secondhandcarseller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPw;
    private Button buttonLogin;
    private TextView textViewResetPw;

    private String pw, email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPw = (EditText) findViewById(R.id.editTextPw);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        textViewResetPw = (TextView) findViewById(R.id.textViewResetPw);


        SharedPreferences chkuser = getSharedPreferences("My_Pref", MODE_PRIVATE);
        String chkus = chkuser.getString("username", null);

        if (chkus != null) {
            Intent login = new Intent(MainActivity.this, DealerActivity.class);
            startActivity(login);
            finish();
        }


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw = editTextPw.getText().toString();
                email = editTextEmail.getText().toString();

                SharedPreferences.Editor user = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                user.putString("username", email);
                user.apply();

                if (email.matches("abc")) {
                    Intent login = new Intent(MainActivity.this, DealerActivity.class);
                    startActivity(login);
                    finish();
                } else if (email.matches("123")) {
                    Intent login = new Intent(MainActivity.this, AgentActivity.class);
                    startActivity(login);
                    finish();
                }


            }
        });


    }
}
