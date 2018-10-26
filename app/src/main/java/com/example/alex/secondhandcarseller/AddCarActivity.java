package com.example.alex.secondhandcarseller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class AddCarActivity extends AppCompatActivity {


    private EditText editTextCarName, editTextCarPrice, editTextCarYear, editTextMileage;
    private Spinner spinnerCarBrand, spinnerColor, spinnerCarType;
    private Button buttonNext;
    ArrayAdapter<CharSequence> colorAdap, typeAdap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        editTextCarName = findViewById(R.id.editTextCarName);
        editTextCarPrice = findViewById(R.id.editTextCarPrice);
        editTextCarYear = findViewById(R.id.editTextCarYear);
        editTextMileage = findViewById(R.id.editTextMileage);
        spinnerCarBrand = findViewById(R.id.spinnerCarBrand);
        spinnerColor = findViewById(R.id.spinnerColor);
        spinnerCarType = findViewById(R.id.spinnerCarType);
        buttonNext = findViewById(R.id.buttonNext);

        colorAdap = ArrayAdapter.createFromResource(this, R.array.color, android.R.layout.simple_spinner_item);
        colorAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerColor.setAdapter(colorAdap);

        typeAdap = ArrayAdapter.createFromResource(this, R.array.car_type, android.R.layout.simple_spinner_item);
        typeAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCarType.setAdapter(typeAdap);



        spinnerColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + "selected", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

}
