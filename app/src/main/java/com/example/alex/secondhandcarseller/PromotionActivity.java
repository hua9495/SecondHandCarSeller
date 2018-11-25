package com.example.alex.secondhandcarseller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

public class PromotionActivity extends AppCompatActivity {

    private ArrayList<String> CarID = new ArrayList<>();
    private ArrayList<String> CarName = new ArrayList<>();
    private ArrayList<String> CarPrice = new ArrayList<>();
    private ArrayList<String> Views = new ArrayList<>();
    private RecyclerView RecyclerViewPromotion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);
        RecyclerViewPromotion=(RecyclerView)findViewById(R.id.RecyclerViewPromotion);
        CarID.add("1");
        CarName.add("One");
        CarPrice.add("1111");
        Views.add("12312");

        CarID.add("1");
        CarName.add("2");
        CarPrice.add("1111");
        Views.add("12312");

        CarID.add("1");
        CarName.add("3");
        CarPrice.add("1111");
        Views.add("12312");

        CarID.add("1");
        CarName.add("4");
        CarPrice.add("1111");
        Views.add("12312");

        CarID.add("1");
        CarName.add("5");
        CarPrice.add("1111");
        Views.add("12312");
initRecyclerView();
    }

    private void initRecyclerView() {

        PromotionAdapter adapter = new PromotionAdapter(CarID,CarName,CarPrice,Views,this);
        RecyclerViewPromotion.setAdapter(adapter);
        RecyclerViewPromotion.setLayoutManager(new LinearLayoutManager(this));

    }
}
