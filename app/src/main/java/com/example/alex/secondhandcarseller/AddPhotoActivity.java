package com.example.alex.secondhandcarseller;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class AddPhotoActivity extends AppCompatActivity {

    private Button buttonSelectImg, buttonUpload, buttonCamera;
    private ImageView imageViewAddCar;
    private String dealerid, brand, name, color, price, year, type, desc, mileage, ConvertImage, subid, plate;
    private Bitmap FixBitmap;
    private String ServerUploadPath = "http://dewy-minuses.000webhostapp.com/upload.php";
    private ProgressDialog progressDialog;
    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] byteArray;
    private HttpURLConnection httpURLConnection;
    private URL url;
    private Uri imageUri;
    private OutputStream outputStream;
    private BufferedWriter bufferedWriter;
    private int RC;
    private BufferedReader bufferedReader;
    private StringBuilder stringBuilder;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    boolean check = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);
        setTitle("Choose Image for Car");


        SharedPreferences myPref = this.getSharedPreferences("My_Pref", MODE_PRIVATE);
        String checkid = myPref.getString("ID", null);
        subid = checkid.substring(0, 1);
        if (subid.equals("A")) {
            dealerid = myPref.getString("BelongDealer", null);
        } else {
            dealerid = myPref.getString("ID", null);
        }

        brand = getIntent().getStringExtra("brand");
        name = getIntent().getStringExtra("name");
        color = getIntent().getStringExtra("color");
        price = getIntent().getStringExtra("price");
        year = getIntent().getStringExtra("year");
        type = getIntent().getStringExtra("type");
        mileage = getIntent().getStringExtra("mileage");
        desc = getIntent().getStringExtra("desc");
        plate = getIntent().getStringExtra("plate");

        buttonSelectImg = (Button) findViewById(R.id.buttonSelectImg);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        imageViewAddCar = (ImageView) findViewById(R.id.imageViewAddCar);
        byteArrayOutputStream = new ByteArrayOutputStream();
        buttonCamera = (Button) findViewById(R.id.buttonStartCamera);
        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getPermission first
                if (ActivityCompat.checkSelfPermission(AddPhotoActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddPhotoActivity.this, new String[] {Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
                } else {
                   // Intent intent = new Intent(getActivity(), ScanActivity.class);
                    //startActivityForResult(intent, REQUEST_CODE);
                }
                //start camera
                Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photo));
                    imageUri = Uri.fromFile(photo);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }

            }
        });
        buttonSelectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 1);
                buttonUpload.setEnabled(true);
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadImageToServer();


            }
        });
    }

    @Override
    protected void onActivityResult(int RC, int RQC, Intent I) {
        super.onActivityResult(RC, RQC, I);
        if (RC == 1 && RQC == RESULT_OK && I != null && I.getData() != null) {
            Uri uri = I.getData();
            try {
                FixBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageViewAddCar.setImageBitmap(FixBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //i add this le
        else if (RC == REQUEST_IMAGE_CAPTURE && RQC == RESULT_OK) {
            Bundle extras = I.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageViewAddCar.setImageBitmap(imageBitmap);
        }

    }

    private void UploadImageToServer() {
        FixBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byteArray = byteArrayOutputStream.toByteArray();
        ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(AddPhotoActivity.this, "Car Information is Uploading", "Please Wait", false, false);
            }

            @Override
            protected void onPostExecute(String string1) {
                super.onPostExecute(string1);
                progressDialog.dismiss();
                Toast.makeText(AddPhotoActivity.this, string1, Toast.LENGTH_LONG).show();

                if (!string1.equals("ERROR")) {
                    if (subid.equals("A")) {
                        Intent backtomain = new Intent(AddPhotoActivity.this, AgentActivity.class);
                        startActivity(backtomain);
                    } else {
                        Intent backtomain = new Intent(AddPhotoActivity.this, DealerActivity.class);
                        startActivity(backtomain);
                    }

                }
            }

            @Override
            protected String doInBackground(Void... params) {
                AddPhotoActivity.ImageProcessClass imageProcessClass = new AddPhotoActivity.ImageProcessClass();
                HashMap<String, String> HashMapParams = new HashMap<String, String>();

                HashMapParams.put("brand", brand);
                HashMapParams.put("name", name);
                HashMapParams.put("dealer", dealerid);
                HashMapParams.put("price", price);
                HashMapParams.put("color", color);
                HashMapParams.put("desc", desc);
                HashMapParams.put("year", year);
                HashMapParams.put("type", type);
                HashMapParams.put("mileage", mileage);
                HashMapParams.put("plate", plate);
                HashMapParams.put("image_data", ConvertImage);
                String FinalData = imageProcessClass.ImageHttpRequest(ServerUploadPath, HashMapParams);
                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();

    }

    public class ImageProcessClass {
        public String ImageHttpRequest(String requestURL, HashMap<String, String> PData) {
            StringBuilder stringBuilder = new StringBuilder();

            try {
                url = new URL(requestURL);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(20000);
                httpURLConnection.setConnectTimeout(20000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                outputStream = httpURLConnection.getOutputStream();
                bufferedWriter = new BufferedWriter(

                        new OutputStreamWriter(outputStream, "UTF-8"));

                bufferedWriter.write(bufferedWriterDataFN(PData));
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                RC = httpURLConnection.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {
                    bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    stringBuilder = new StringBuilder();
                    String RC2;

                    while ((RC2 = bufferedReader.readLine()) != null) {
                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {
            stringBuilder = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
                if (check)
                    check = false;
                else
                    stringBuilder.append("&");
                stringBuilder.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));
                stringBuilder.append("=");
                stringBuilder.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilder.toString();
        }

    }

}
