package com.technotronicsolutions.app.manualstepper.Class;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.technotronicsolutions.app.manualstepper.Fragment.DummyFragment;
import com.technotronicsolutions.app.manualstepper.MainActivity;

/**
 * Created by novat on 6/29/2017.
 */

public class StepperViewAdapter extends FragmentPagerAdapter {

    private MainActivity mainActivity = new MainActivity();

    public StepperViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        //I've set position + 1 as i've arrange colors from 1 to n
        //replace it with position if the application requires a call from 0 to n-1
        return DummyFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return MainActivity.totalSteps;
    }
}