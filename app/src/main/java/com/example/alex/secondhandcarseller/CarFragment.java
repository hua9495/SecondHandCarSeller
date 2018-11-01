package com.example.alex.secondhandcarseller;


import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class CarFragment extends Fragment {


    public CarFragment() {
        // Required empty public constructor
    }

    private ArrayList<String> mCarName = new ArrayList<>();
    private ArrayList<String> mCarImage = new ArrayList<>();
    private ArrayList<String> mCarId = new ArrayList<>();
    private ArrayList<String> mCarBrand = new ArrayList<>();
    private ArrayList<String> mCarPrice = new ArrayList<>();
    private ArrayList<String> mCarColor = new ArrayList<>();
    private ArrayList<String> mCarDesc = new ArrayList<>();
    private ArrayList<String> mCarYear = new ArrayList<>();
    private ArrayList<String> mCarMile = new ArrayList<>();
    private ProgressBar progressBarLoadCar;
    private RecyclerView recyclerViewCar;
    private String Url = "https://dewy-minuses.000webhostapp.com/sellerCar.php";
    private String subid, dealerid;
    private FloatingActionButton fabAddCar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_car, container, false);
        recyclerViewCar = (RecyclerView) v.findViewById(R.id.recyclerViewCar);
        progressBarLoadCar = (ProgressBar) v.findViewById(R.id.progressBarLoadCar);
        fabAddCar = (FloatingActionButton) v.findViewById(R.id.fabAddCar);

        SharedPreferences myPref = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE);
        String checkid = myPref.getString("ID", null);
        subid = checkid.substring(0, 1);
        if (subid.equals("A")) {
            dealerid = myPref.getString("BelongDealer", null);
        } else {
            dealerid = myPref.getString("ID", null);
        }


        fabAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddCarActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }


    @Override
    public void onResume() {

        recyclerViewCar.setEnabled(false);
        mCarName.clear();
        mCarImage.clear();
        mCarId.clear();
        mCarBrand.clear();
        mCarPrice.clear();
        mCarColor.clear();
        mCarDesc.clear();
        mCarYear.clear();
        mCarMile.clear();

        LoadPic(getView());

        super.onResume();
    }

    private void initRecyclerView(View v) {

        CarAdapter adapter = new CarAdapter(mCarName, mCarImage, mCarId, mCarBrand, mCarPrice, mCarColor, mCarDesc, mCarYear, mCarMile, getActivity());
        recyclerViewCar.setAdapter(adapter);
        recyclerViewCar.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    private void LoadPic(final View v) {
        progressBarLoadCar.setVisibility(View.VISIBLE);
        recyclerViewCar.setEnabled(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("car");
                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            //follow index
                            String carID = object.getString("id");
                            String name = object.getString("name");
                            String image_data = object.getString("imageUrl");
                            String brand = object.getString("brand");
                            String price = object.getString("price");
                            String color = object.getString("color");
                            String desc = object.getString("desc");
                            String year = object.getString("year");
                            String mileage = object.getString("mileage");

                            mCarName.add(name);
                            mCarImage.add(image_data);
                            mCarId.add(carID);
                            mCarBrand.add(brand);
                            mCarPrice.add(price);
                            mCarColor.add(color);
                            mCarDesc.add(desc);
                            mCarYear.add(year);
                            mCarMile.add(mileage);
                        }
                        initRecyclerView(v);

                        progressBarLoadCar.setVisibility(View.GONE);
                        recyclerViewCar.setEnabled(true);
                    } else {
                        progressBarLoadCar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "No car in your list", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                    progressBarLoadCar.setVisibility(View.GONE);
                    recyclerViewCar.setEnabled(true);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getActivity(), "Error " + error.toString(), Toast.LENGTH_LONG).show();
                        progressBarLoadCar.setVisibility(View.GONE);
                        recyclerViewCar.setEnabled(true);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("dealerid", dealerid);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);


    }

}
