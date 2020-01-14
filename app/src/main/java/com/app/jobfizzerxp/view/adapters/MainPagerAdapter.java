package com.app.jobfizzerxp.view.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.jobfizzerxp.view.fragments.AccountsFragment;
import com.app.jobfizzerxp.view.fragments.ChatFragment;
import com.app.jobfizzerxp.view.fragments.HomeFragment;

public class MainPagerAdapter extends FragmentStatePagerAdapter {

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return HomeFragment.newInstance();
            case 2:
                return AccountsFragment.newInstance();
            case 0:
                return ChatFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}