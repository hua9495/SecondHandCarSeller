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


/**
 * A simple {@link Fragment} subclass.
 */
public class DealerProfileFragment extends Fragment {

    private Button buttonLogout;

    public DealerProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_delaer_profile, container, false);
        buttonLogout = (Button) v.findViewById(R.id.buttonLogout);

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SharedPreferences.Editor user = getActivity().getSharedPreferences("My_Pref", Context.MODE_PRIVATE).edit();
                user.putString("ID", null);
                user.apply();
                Intent intent=new Intent(getActivity(),MainActivity.class);
                startActivity(intent);

            }
        });
        return v;
    }

}
