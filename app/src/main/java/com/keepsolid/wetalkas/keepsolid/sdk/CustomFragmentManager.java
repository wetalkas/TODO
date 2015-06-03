package com.keepsolid.wetalkas.keepsolid.sdk;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;

/**
 * Created by wetalkas on 02.06.15.
 */
public class CustomFragmentManager {

    private static CustomFragmentManager instance;

    private Activity activity;
    private FragmentManager fragmentManager;


    private CustomFragmentManager() {

    }



    public static CustomFragmentManager getInstance() {

        if (instance == null) {
            instance = new CustomFragmentManager();
        }

        return instance;
    }


    public void init(Activity activity){
        this.activity = activity;
        fragmentManager = activity.getFragmentManager();
    }


    public void setFragment(int containerViewId, Fragment fragment, boolean addToBackStack) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment);

        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.commit();
    }


    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }
}
