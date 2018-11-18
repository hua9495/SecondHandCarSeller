package com.example.alex.secondhandcarseller;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AdminDealerDetail extends AppCompatActivity {
    private TextView textViewdealername, tvdealerloc, tvdealeremail, tvdealercont, tvpersonic, tvdealerstatus;
    private String id, name, loc, email, contact, pic, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dealer_detail);

        setTitle("");
        textViewdealername = (TextView) findViewById(R.id.textViewdealername);
        tvdealerloc = (TextView) findViewById(R.id.tvdealerloc);
        tvdealeremail = (TextView) findViewById(R.id.tvdealeremail);
        tvdealercont = (TextView) findViewById(R.id.tvdealercont);
        tvpersonic = (TextView) findViewById(R.id.tvpersonic);
        tvdealerstatus = (TextView) findViewById(R.id.tvdealerstatus);

        Intent intent = getIntent();
        id = intent.getStringExtra("DealerID");
        name = intent.getStringExtra("DealerName");
        loc = intent.getStringExtra("DealerLoctaion");
        email = intent.getStringExtra("DealerEmail");
        contact = intent.getStringExtra("DealerContact");
        pic = intent.getStringExtra("Pic");
        status = intent.getStringExtra("DealerStatus");



        textViewdealername.setText(name);
        tvdealerloc.setText(loc);
        tvdealercont.setText(contact);
        tvdealeremail.setText(email);
        tvpersonic.setText(pic);
        tvdealerstatus.setText(status);
        if (status.matches("Blacklisted")){
            tvdealerstatus.setTextColor(ContextCompat.getColor(this,R.color.Red));
        }else if (status.matches("Approved")){
            tvdealerstatus.setTextColor(ContextCompat.getColor(this,R.color.Green));
        }else {
            tvdealerstatus.setTextColor(ContextCompat.getColor(this,R.color.Orange));
        }

    }
}
