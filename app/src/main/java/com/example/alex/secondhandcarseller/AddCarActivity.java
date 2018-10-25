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


    private EditText editTextCarName,editTextCarPrice, editTextCarYear, editTextMileage;
    private Spinner spinnerCarBrand,spinnerColor,spinnerCarType;
    private Button buttonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        editTextCarName=findViewById(R.id.editTextCarName);
        editTextCarPrice=findViewById(R.id.editTextCarPrice);
        editTextCarYear=findViewById(R.id.editTextCarYear);
        editTextMileage=findViewById(R.id.editTextMileage);
        spinnerCarBrand=findViewById(R.id.spinnerCarBrand);
        spinnerColor=findViewById(R.id.spinnerColor);
        spinnerCarType=findViewById(R.id.spinnerCarType);
        buttonNext=findViewById(R.id.buttonNext);

        





    }

}
