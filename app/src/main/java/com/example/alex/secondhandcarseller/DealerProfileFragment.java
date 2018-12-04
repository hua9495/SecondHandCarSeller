package com.example.alex.secondhandcarseller;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class DealerProfileFragment extends Fragment {

    private Button buttonLogout;
    private TextView textViewWelcome, textViewEditProfile, textViewMakePro, textViewReport;

    public DealerProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_delaer_profile, container, false);
        textViewWelcome = (TextView) v.findViewById(R.id.textViewWelcome);
        textViewEditProfile = (TextView) v.findViewById(R.id.textViewEditProfile);
        textViewMakePro = (TextView) v.findViewById(R.id.textViewMakePro);
        textViewReport = (TextView) v.findViewById(R.id.textViewReport);
        buttonLogout = (Button) v.findViewById(R.id.buttonLogout);


        SharedPreferences myPref = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE);
        String Name = myPref.getString("Name", null);

        textViewWelcome.setText("Welcome " + Name);

        textViewEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), EditDealerProfile.class);
                startActivity(intent);
            }
        });

        textViewMakePro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), PromotionActivity.class);
                startActivity(intent);
            }
        });
        textViewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), ReportProblem.class);
                startActivity(intent);
            }
        });
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SharedPreferences.Editor user = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                user.putString("ID", null);
                user.apply();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

            }
        });
        return v;
    }

}
