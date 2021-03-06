package com.example.alex.secondhandcarseller;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


public class AdapterMyBooking extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> bookingStatus = new ArrayList<>();
    private ArrayList<String> carNames = new ArrayList<>();
    private ArrayList<String> dates = new ArrayList<>();
    private ArrayList<String> times = new ArrayList<>();
    private ArrayList<String> price = new ArrayList<>();
    private ArrayList<String> carPhoto = new ArrayList<>();
    private ArrayList<String> custID = new ArrayList<>();
    private ArrayList<String> appID = new ArrayList<>();
    private ArrayList<Date> acceptDateTime = new ArrayList<>();
    private String strPrice, strCarPhoto, strCustID, strBookingStatus,strAppID;

    public AdapterMyBooking(Context context, ArrayList<String> bookingStatus, ArrayList<String> carNames, ArrayList<String> dates, ArrayList<String> times, ArrayList<String> price, ArrayList<String> carPhoto, ArrayList<String> custID, ArrayList<String> appID,ArrayList<Date> acceptDateTime) {
        super(context, R.layout.content_appointment);
        this.bookingStatus = bookingStatus;
        this.carNames = carNames;
        this.dates = dates;
        this.times = times;
        this.price = price;
        this.carPhoto = carPhoto;
        this.custID = custID;
        this.appID=appID;
        this.acceptDateTime=acceptDateTime;
        this.context = context;
    }

    @Override
    public int getCount() {
        return carNames.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.content_appointment, null, true);
        final TextView carName = (TextView) v.findViewById(R.id.textViewBookingCarName);
        ImageView imBookingStatus = (ImageView) v.findViewById(R.id.imageViewBookingStatus);
        final TextView bookingDate = (TextView) v.findViewById(R.id.textViewBookingDate);
        final TextView bookingTime = (TextView) v.findViewById(R.id.textViewBookingTime);

        ConstraintLayout bookingLayout = (ConstraintLayout) v.findViewById(R.id.bookingLayout);


        carName.setText(carNames.get(position));
        bookingDate.setText(dates.get(position));
        bookingTime.setText(times.get(position));

        strBookingStatus = bookingStatus.get(position);

        //green-Met
        //Red-Booked
        //edit-Pending
        //cross-cancel

        if (strBookingStatus.equals("Pending")) {
            imBookingStatus.setImageResource(R.drawable.ic_action_edit_status);
        } else if (strBookingStatus.equals("Met")) {
            imBookingStatus.setImageResource(R.drawable.ic_action_green_status);
        } else if(strBookingStatus.equals("Booked")){
            imBookingStatus.setImageResource(R.drawable.ic_action_red_status);
        }
        else{
            imBookingStatus.setImageResource(R.drawable.ic_action_cancel_status);
        }

        bookingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strBookingStatus = bookingStatus.get(position);
                strPrice = price.get(position);
                strCarPhoto = carPhoto.get(position);
                strCustID = custID.get(position);
                strAppID=appID.get(position);

                Intent bookingDetailIntent = new Intent(context, BookingDetailActivity.class);
                bookingDetailIntent.putExtra("CarName", carName.getText().toString());
                bookingDetailIntent.putExtra("appDate", bookingDate.getText().toString());
                bookingDetailIntent.putExtra("appTime", bookingTime.getText().toString());
                bookingDetailIntent.putExtra("price", strPrice);
                bookingDetailIntent.putExtra("carPhoto", strCarPhoto);
                bookingDetailIntent.putExtra("custID", strCustID);
                bookingDetailIntent.putExtra("bookingStatus", strBookingStatus);
                bookingDetailIntent.putExtra("appID",strAppID);

                context.startActivity(bookingDetailIntent);
            }
        });


        return v;
    }
}
