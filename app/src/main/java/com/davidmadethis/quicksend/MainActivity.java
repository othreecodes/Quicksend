package com.davidmadethis.quicksend;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.davidmadethis.quicksend.fragments.CVFragment;
import com.davidmadethis.quicksend.fragments.HomeFragment;
import com.davidmadethis.quicksend.fragments.TemplatesFragment;

public class MainActivity extends AppCompatActivity implements CVFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener,TemplatesFragment.OnFragmentInteractionListener {

    private TextView mTextMessage;
    Fragment selectedFragment = null;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = HomeFragment.newInstance();
                    getSupportActionBar().setTitle("Quicksend");
                    break;
                case R.id.navigation_dashboard:
                    selectedFragment = CVFragment.newInstance();
                    getSupportActionBar().setTitle("Upload CV");
                    break;
                case R.id.navigation_notifications:
                    getSupportActionBar().setTitle("Email Templates");
                    selectedFragment = TemplatesFragment.newInstance();
                    break;
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content, selectedFragment);
            transaction.commit();
            return true;
        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, HomeFragment.newInstance());
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
