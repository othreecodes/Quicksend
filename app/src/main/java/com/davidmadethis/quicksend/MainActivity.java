package com.davidmadethis.quicksend;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.davidmadethis.quicksend.fragments.CVFragment;
import com.davidmadethis.quicksend.fragments.HomeFragment;
import com.davidmadethis.quicksend.fragments.TemplatesFragment;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements CVFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener, TemplatesFragment.OnFragmentInteractionListener {

    private FirebaseAnalytics mFirebaseAnalytics;

    Fragment selectedFragment = null;
    private TextView mTextMessage;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = HomeFragment.newInstance(recievedEmail);
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
    String recievedEmail = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent n = getIntent();
        if (!n.getAction().equals("android.intent.action.MAIN")) {
            try {
                this.recievedEmail = n.getDataString().substring(7);
            } catch (Exception e) {

            }
        }
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, HomeFragment.newInstance(recievedEmail));
        transaction.commit();

        permissionAsk();


        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "App opened");
        bundle.putString(FirebaseAnalytics.Param.VALUE, new Date().toString());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_about) {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://github.com/othreecodes/quicksend"));
            startActivity(browserIntent);
        } else if (item.getItemId() == R.id.menu_settings) {
            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void permissionAsk() {

        int permission = ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);


        ArrayList<String> permStrings = new ArrayList<>();

        if (permission == PackageManager.PERMISSION_DENIED) {
            permStrings.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permission2 == PackageManager.PERMISSION_DENIED) {
            permStrings.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        String[] stockArr = new String[permStrings.size()];
        stockArr = permStrings.toArray(stockArr);


        if (permStrings.size() > 0) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    stockArr,
                    1);

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {

        Log.e("emailaddress", intent.getData().toString());
        super.onNewIntent(intent);
    }

}
