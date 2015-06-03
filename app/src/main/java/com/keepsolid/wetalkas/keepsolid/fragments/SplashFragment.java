package com.keepsolid.wetalkas.keepsolid.fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keepsolid.wetalkas.keepsolid.R;
import com.keepsolid.wetalkas.keepsolid.activities.MainActivity;
import com.keepsolid.wetalkas.keepsolid.sdk.CustomFragmentManager;

import java.util.concurrent.TimeUnit;


public class SplashFragment extends Fragment {





    CustomFragmentManager customFragmentManager;

    MainActivity activity;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        AsyncTaskCustom asyncTaskCustom = new AsyncTaskCustom();

        asyncTaskCustom.execute();


        customFragmentManager = CustomFragmentManager.getInstance();

        if (getActivity() != null) {
            activity = (MainActivity) getActivity();
        }



        return inflater.inflate(R.layout.fragment_splash, container, false);
    }




    class AsyncTaskCustom extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            customFragmentManager.setFragment(R.id.container, activity.getTasksFragment(), false);
        }
    }





}
