package com.sajednt.arzalarm.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.sajednt.arzalarm.fragment.fragment_conditions;
import com.sajednt.arzalarm.fragment.fragment_list;

public class adapterTimeframe extends FragmentStatePagerAdapter {


    public adapterTimeframe(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Fragment fragment = new fragment_conditions();
                return fragment;
            case 1:
                Fragment fragment2 = new fragment_list();
                return fragment2;
        }

        return  null;

    }

    @Override
    public int getCount() {
        return 2;
    }
}
