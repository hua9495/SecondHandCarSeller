package com.example.alex.secondhandcarseller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class EditCarActivity extends AppCompatActivity {
private Button buttonTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_car);

        buttonTest=(Button)findViewById(R.id.buttonTest);
        int INT =getIntent().getIntExtra("Item",0);


        buttonTest.setText(INT);

    }
}
