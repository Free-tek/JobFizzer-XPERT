package com.app.jobfizzerxp.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.jobfizzerxp.utilities.helpers.Constants;
import com.app.jobfizzerxp.view.adapters.AppointmentAdapter;
import com.app.jobfizzerxp.R;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailedAppointmentsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_appointments);

        ImageView backButton = findViewById(R.id.backButton);
        TextView toolBarTitle = findViewById(R.id.toolBarTitle);
        toolBarTitle.setText(getString(R.string.bookings));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            try {
                JSONObject date = new JSONObject(intent.getStringExtra(Constants.INTENT_KEYS.DATE));
                ViewPager viewPager = findViewById(R.id.viewPager);
                AppointmentAdapter appointmentAdapter = new AppointmentAdapter(getSupportFragmentManager(), date);
                viewPager.setAdapter(appointmentAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //bookings adapter
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}