package com.example.alex.secondhandcarseller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DealerRegisterActivity extends AppCompatActivity {
    private EditText editTextComName, editTextAddress, editTextLocation, editTextPic, editTextContact, ETemail, editTextPassword, editTextConfirmPw;
    private String name, address, location, pic, contact, email, password, conpw;
    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_register);
        setTitle("Register");
        editTextComName = (EditText) findViewById(R.id.editTextComName);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextLocation = (EditText) findViewById(R.id.editTextLocation);
        editTextPic = (EditText) findViewById(R.id.editTextPic);
        editTextContact = (EditText) findViewById(R.id.editTextContact);
        ETemail = (EditText) findViewById(R.id.ETemail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextConfirmPw = (EditText) findViewById(R.id.editTextConfirmPw);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = editTextComName.getText().toString();
                address = editTextAddress.getText().toString();
                location = editTextLocation.getText().toString();
                pic = editTextPic.getText().toString();
                contact = editTextContact.getText().toString();
                email = ETemail.getText().toString();
                password = editTextPassword.getText().toString();
                conpw = editTextConfirmPw.getText().toString();


            }
        });
    }
}
