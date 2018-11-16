package com.example.alex.secondhandcarseller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {


    private ArrayList<String> DealerType = new ArrayList<>();
    private ArrayList<String> DealerNo = new ArrayList<>();
    private RecyclerView recycleViewAllDealer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        recycleViewAllDealer=(RecyclerView) findViewById(R.id.recycleViewAllDealer);

        DealerType.add("Pending Dealers");
        DealerNo.add("32");
        DealerType.add("Blacklisted Dealers");
        DealerNo.add("52");
        DealerType.add("Invalid Dealers");
        DealerNo.add("21");

        initRecyclerView();

        setTitle("Admin");
    }

    private void initRecyclerView() {

        AdminAdapter adapter = new AdminAdapter(DealerType,DealerNo,this);
        recycleViewAllDealer.setAdapter(adapter);
        recycleViewAllDealer.setLayoutManager(new LinearLayoutManager(this));

    }
    @Override
    public void onBackPressed() {
        finish();

    }
}
