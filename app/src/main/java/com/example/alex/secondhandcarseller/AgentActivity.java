package com.example.alex.secondhandcarseller;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class AgentActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavView1;
    private FrameLayout mainFrame1;

    private CarFragment carFragment;
    private AppointFragment appointFragment;
    private AgentProfileFragment agentprofileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent);

        mainFrame1 = (FrameLayout) findViewById(R.id.mainFrame1);
        bottomNavView1 = (BottomNavigationView) findViewById(R.id.bottomNavView1);

        carFragment = new CarFragment();
        appointFragment = new AppointFragment();
        agentprofileFragment = new AgentProfileFragment();
        setFragment(carFragment);


        bottomNavView1.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_Car:
                        setFragment(carFragment);
                        setTitle(R.string.title_car_management);
                        return true;
                    case R.id.nav_Appoint:
                        setFragment(appointFragment);
                        setTitle(R.string.title_booking);
                        return true;
                    case R.id.nav_Profile:
                        setTitle(R.string.title_my_profile);
                        setFragment(agentprofileFragment);
                        return true;

                    default:
                        return false;


                }
            }
        });


    }


    private void setFragment(Fragment fragement) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame1, fragement);
        fragmentTransaction.commit();
    }
}
