package com.example.alex.secondhandcarseller;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class AgentProfileFragment extends Fragment implements ZXingScannerView.ResultHandler {

    private Button buttonLogout;
    private ZXingScannerView zxingScannerView;

    public AgentProfileFragment() {
        // Required empty public constructor
    }

    private TextView tvAgentName, tvAgentIC, tvAgentContact, tvAgentEmail, tvBelongTo, tvWorkStart;
    private Button buttonReport;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_agent_profile, container, false);
        buttonLogout = (Button) v.findViewById(R.id.buttonLogout);
        tvAgentName = v.findViewById(R.id.tvAgentName);
        tvAgentIC = v.findViewById(R.id.tvAgentIC);
        tvAgentContact = v.findViewById(R.id.tvCustContact);
        tvAgentEmail = v.findViewById(R.id.tvCustEmail);
        tvBelongTo = v.findViewById(R.id.tvBelongTo);
        tvWorkStart = v.findViewById(R.id.tvWorkStart);
        buttonReport = v.findViewById(R.id.buttonReport);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SharedPreferences.Editor user = getActivity().getSharedPreferences("My_Pref", Context.MODE_PRIVATE).edit();
                user.putString("ID", null);
                user.apply();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });

        SharedPreferences myPref;
        myPref = getActivity().getSharedPreferences("My_Pref", MODE_PRIVATE);
        String email = myPref.getString("Email", null);
        String name = myPref.getString("Name", null);
        String belong = myPref.getString("BelongDealer", null);
        String ic = myPref.getString("ICno", null);
        String contact = myPref.getString("Contact", null);
        String workstart = myPref.getString("WorkStart", null);


        tvAgentName.setText(getString(R.string.Name) + "\t" + name);
        tvAgentIC.setText(getString(R.string.ic) + "\t" + ic);
        tvAgentContact.setText(getString(R.string.contact_no) + "\t" + contact);
        tvAgentEmail.setText(getString(R.string.email_dot) + "\t" + email);
        tvBelongTo.setText(getString(R.string.company) + "\t" + belong);
        tvWorkStart.setText(getString(R.string.start_date) + "\t" + workstart);
        return v;


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.scan_qrcode, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        zxingScannerView = new ZXingScannerView(getActivity());
        //Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        //intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes
        //Intent intent = new Intent(zxingScannerView.toString());
        //intent.putExtra("SCAN_MODE", "QR_CODE_MODE");

        //startActivityForResult(intent, 0);
        //zxingScannerView.setResultHandler(this);
        //zxingScannerView.startCamera();
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onPause() {

        super.onPause();
        // zxingScannerView.stopCamera();
    }


    @Override
    public void handleResult(Result result) {
        Toast.makeText(getContext(), "Test", Toast.LENGTH_LONG).show();
        //zxingScannerView.resumeCameraPreview(this);
    }

}
