package com.example.alex.secondhandcarseller;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CarFragment extends Fragment {


    public CarFragment() {
        // Required empty public constructor
    }

    private ListView listViewCar;

    private Integer [] IMAGES={R.drawable.test1,R.drawable.test3,R.drawable.test4,R.drawable.test5,R.drawable.test6};
    private String[] NAMES ={"Proton Saga","Proton Vira","Honda Civic", "Honda City","Myvi"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_car, container, false);
        listViewCar=(ListView)v.findViewById(R.id.listViewCar);

        final CarAdapter carAdapter=new CarAdapter(getActivity(),IMAGES,NAMES);
        listViewCar.setAdapter(carAdapter);


        listViewCar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View view, int position, long id) {

                Intent login = new Intent(getActivity(), EditCarActivity.class);
                login.putExtra("Item",position);
                startActivity(login);
            }
        });



        return v;
    }

}
