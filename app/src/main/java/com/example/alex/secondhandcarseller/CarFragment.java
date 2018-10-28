package com.example.alex.secondhandcarseller;


import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    private ArrayList<String> mCarName = new ArrayList<>();
    private ArrayList<String> mCarImage = new ArrayList<>();
    RecyclerView recyclerViewCar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_car, container, false);
        recyclerViewCar = (RecyclerView) v.findViewById(R.id.recyclerViewCar);
        mCarName.clear();
        mCarImage.clear();
        initImageBitmaps(v);


        return v;
    }

    private void initImageBitmaps(View v) {

        mCarImage.add("http://dewy-minuses.000webhostapp.com/images/C0001.png");
        mCarName.add("Test1");
        mCarImage.add("http://dewy-minuses.000webhostapp.com/images/C0002.png");
        mCarName.add("Test2");

        initRecyclerView(v);
    }

    private void initRecyclerView(View v) {

        CarAdapter adapter = new CarAdapter(getActivity(), mCarName, mCarImage);
        recyclerViewCar.setAdapter(adapter);
        recyclerViewCar.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

}
