package com.app.jobfizzerxp.view.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.jobfizzerxp.view.fragments.AppointmentsFragment;

import org.json.JSONObject;

public class AppointmentAdapter extends FragmentStatePagerAdapter {
    private JSONObject date;

    public AppointmentAdapter(FragmentManager fm, JSONObject date) {
        super(fm);
        this.date = date;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return AppointmentsFragment.newInstance(date);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 1;
    }
}