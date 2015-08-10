package com.keepsolid.wetalkas.keepsolid.todo_sdk.adapter;



import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.keepsolid.wetalkas.keepsolid.fragments.SplashFragment;
import com.keepsolid.wetalkas.keepsolid.fragments.TasksFragment;

/**
 * Created by wetalkas on 08.08.15.
 */
public class TabAdapter extends FragmentStatePagerAdapter {

    private int numberOfTabs;


    public TabAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;

    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TasksFragment tasksFragment = new TasksFragment();
                return tasksFragment;
            case 1:
                SplashFragment splashFragment = new SplashFragment();
                return splashFragment;
            default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
