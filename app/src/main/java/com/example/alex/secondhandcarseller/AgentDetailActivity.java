package com.example.alex.secondhandcarseller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AgentDetailActivity extends AppCompatActivity {
    EditText editTextAgentName, editTextAgentIC, editTextAgentEmail, editTextAgentContact, editTextAgentWork;
    Button buttonBack,buttonUpdateAgent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_detail);
        setTitle("Edit Agent");

        editTextAgentName = (EditText) findViewById(R.id.editTextAgentName);
        editTextAgentIC = (EditText) findViewById(R.id.editTextAgentIC);
        editTextAgentEmail = (EditText) findViewById(R.id.editTextAgentEmail);
        editTextAgentContact = (EditText) findViewById(R.id.editTextAgentContact);
        editTextAgentWork = (EditText) findViewById(R.id.editTextAgentWork);
        buttonBack=(Button)findViewById(R.id.buttonBack);
        buttonUpdateAgent=(Button)findViewById(R.id.buttonUpdateAgent);

        Intent intent = getIntent();
        String name = intent.getStringExtra("Aname");
        String ic = intent.getStringExtra("Aic");
        String id = intent.getStringExtra("Aid");
        String contact = intent.getStringExtra("Acontact");
        String email = intent.getStringExtra("Aemail");
        String workdate = intent.getStringExtra("Awork");
        String status = intent.getStringExtra("Astatus");

        editTextAgentName.setText(name);
        editTextAgentIC.setText(ic);
        editTextAgentEmail.setText(email);
        editTextAgentContact.setText(contact);
        editTextAgentWork.setText(workdate);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });




    }
}
