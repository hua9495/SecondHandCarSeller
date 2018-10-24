package com.example.alex.secondhandcarseller;


import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CarFragment extends Fragment {


    public CarFragment() {
        // Required empty public constructor
    }

    private ListView listViewCar;
    private FloatingActionButton fabAddCar;
    private List<Car> carList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_car, container, false);

        carList.add(new Car("Proton Saga", R.drawable.test1));
        carList.add(new Car("Proton Vira", R.drawable.test3));
        carList.add(new Car("Honda Civic", R.drawable.test4));
        carList.add(new Car("Honda City", R.drawable.test5));
        carList.add(new Car("Myvi", R.drawable.test6));

        fabAddCar = (FloatingActionButton) v.findViewById(R.id.fabAddCar);
        listViewCar = (ListView) v.findViewById(R.id.listViewCar);

        CarAdapter carAdapter = new CarAdapter(getActivity(), R.layout.adapter_car_layout, carList);
        listViewCar.setAdapter(carAdapter);

        fabAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddCarActivity.class);
                startActivity(intent);
            }
        });


        return v;
    }

}
