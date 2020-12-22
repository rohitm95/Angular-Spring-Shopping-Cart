package com.persistent.covidinventory.aggregator.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.persistent.covidinventory.R;
import com.persistent.covidinventory.aggregator.ui.NewCompletedOrdersFragment;
import com.persistent.covidinventory.aggregator.ui.NewPendingOrdersFragment;
import com.persistent.covidinventory.aggregator.ui.NewReadyOrdersFragment;
import com.persistent.covidinventory.common.Constants;
import com.persistent.covidinventory.common.LoginActivity;
import com.persistent.covidinventory.common.UpdatePassword;
import com.persistent.covidinventory.customer.ui.SectionsPagerAdapter;

import java.util.Objects;

public class NewDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dashboard);

        // Define ActionBar object
        ActionBar actionBar;
        actionBar = getSupportActionBar();
//        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("##FF6800"));

        // Set BackgroundDrawable

//        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        actionBar.setTitle("Golden Katar URC");

        @SuppressLint({"NewApi", "LocalSuppress"})
        String token = Objects.requireNonNull(Constants.token);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        sectionsPagerAdapter.addFragment(new NewPendingOrdersFragment(token), "Pending");
        sectionsPagerAdapter.addFragment(new NewReadyOrdersFragment(token), "Ready");
        sectionsPagerAdapter.addFragment(new NewCompletedOrdersFragment(token), "Completed");

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

    }


    private void killActivity() {
        this.finishAffinity();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_dashbord, menu);
//        getMenuInflater().inflate(R.menu.menu_dashbord, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_logout:
                Log.v("sign out ","sign out is clicked");
                Intent logout = new Intent(getApplicationContext(), LoginActivity.class);
                logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

//                Log.v("TAG","Yes coming with id :: "+Constants.START_ACTIVITY_FOR_RESULT_LOGOUT);
//                logout.putExtra("START_ACTIVITY_FOR_RESULT_LOGOUT",Constants.START_ACTIVITY_FOR_RESULT_LOGOUT);


                startActivity(logout);
                killActivity();
                return true;

            case R.id.menu_update_password:

                SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
                //SharedPreferences.Editor myEdit  = sharedPreferences.edit();

                int aggregatorId  = sharedPreferences.getInt(Constants.SHARED_PREFERENCE_FIELD_AGGREGATOR_ID,0);
                String id = Integer.toString(aggregatorId);


                Intent updatePasswordIntent = new Intent(getApplicationContext(), UpdatePassword.class);
                updatePasswordIntent.putExtra("id",id);
                startActivity(updatePasswordIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
