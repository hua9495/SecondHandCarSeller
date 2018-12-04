package com.example.alex.secondhandcarseller;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Arrays;
import java.util.List;

public class ReportProblem extends AppCompatActivity {

    private EditText editTextProblem;
    private Button buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_problem);
        buttonSend = (Button) findViewById(R.id.buttonSend);
        editTextProblem = (EditText) findViewById(R.id.editTextProblem);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Report Problem");
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Recipient = "qbc4572@gmail.com";
                List<String> recipients = Arrays.asList(Recipient.split("\\s*,\\s*"));
                String strID, strSender, strSenderEmail, strSenderPw, Content = editTextProblem.getText().toString();

                SharedPreferences sharePref = getSharedPreferences("My_Pref", Context.MODE_PRIVATE);

                strSenderEmail = sharePref.getString("Email", null);
                strSenderPw = sharePref.getString("password", null);
                strID = sharePref.getString("ID", null);
                strSender = sharePref.getString("Name", null);
                String subject = "Problem Report";
                String body = strSender + " ID (" + strID + "):\n\n" + Content;

                SendMailTask smt = new SendMailTask(ReportProblem.this);
                smt.execute(strSenderEmail, strSenderPw, recipients, subject, body);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
