package com.example.alex.secondhandcarseller;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class DealerActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavView;
    private FrameLayout mainFrame;

    private CarFragment carFragment;
    private AgentFragment agentFragment;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer);

        mainFrame = (FrameLayout) findViewById(R.id.mainFrame);
        bottomNavView = (BottomNavigationView) findViewById(R.id.bottomNavView);

        carFragment = new CarFragment();
        agentFragment = new AgentFragment();
        profileFragment = new ProfileFragment();
        setFragment(carFragment);


        bottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_Car:
                        setFragment(carFragment);
                        return true;
                    case R.id.nav_Agent:
                        setFragment(agentFragment);
                        return true;
                    case R.id.nav_Profile:
                        setFragment(profileFragment);
                        return true;

                    default:
                        return false;


                }
            }
        });


    }

    private void setFragment(Fragment fragement) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame, fragement);
        fragmentTransaction.commit();
    }
}
