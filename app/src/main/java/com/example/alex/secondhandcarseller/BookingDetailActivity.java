package com.example.alex.secondhandcarseller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BookingDetailActivity extends AppCompatActivity {
    private Button btnBackMyBooking,btnAcceptRequest;
    private TextView tvCarName, tvAppDate, tvAppTime, tvPrice,tvCustName, tvCustContactNo, tvCusttEmail;
    private String carName, appDate, appTime, price, carPhoto, agentID, custID,bookingStatus;
    private ImageView ivCarPhoto;
    private ProgressBar downloadingAppDetail;
    SharedPreferences sharePref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);
        setTitle(R.string.title_booking_detail);

        sharePref = this.getSharedPreferences("My_Pref", Context.MODE_PRIVATE);
        agentID = sharePref.getString("ID", null);

        tvCarName = (TextView) findViewById(R.id.textViewCarName);
        tvAppDate = (TextView) findViewById(R.id.textViewAppDate);
        tvAppTime = (TextView) findViewById(R.id.textViewAppTime);
        tvPrice = (TextView) findViewById(R.id.textViewPrice);

        tvCustName = (TextView) findViewById(R.id.textViewCustomer);
        tvCustContactNo = (TextView) findViewById(R.id.textViewCustContactNo);
        tvCusttEmail = (TextView) findViewById(R.id.textViewCustEmail);
        ivCarPhoto = (ImageView) findViewById(R.id.imageViewCarPhoto);
        btnBackMyBooking = (Button) findViewById(R.id.buttonBackMyBooking);
        btnAcceptRequest= (Button) findViewById(R.id.buttonAccept);
        downloadingAppDetail = (ProgressBar) findViewById(R.id.downloadingAppDetail);

        downloadingAppDetail.setVisibility(View.GONE);
        Intent intent = getIntent();
        carName = intent.getStringExtra("CarName");
        appDate = intent.getStringExtra("appDate");
        appTime = intent.getStringExtra("appTime");
        price = intent.getStringExtra("price");
        carPhoto = intent.getStringExtra("carPhoto");
        custID = intent.getStringExtra("custID");
        bookingStatus=intent.getStringExtra("bookingStatus");

        //to let the agent accept the pending request
        if(bookingStatus.equals("Pending")){
            btnAcceptRequest.setVisibility(View.VISIBLE);
        }
        else{
            btnAcceptRequest.setVisibility(View.GONE);
        }


        btnBackMyBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
