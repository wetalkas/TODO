package com.keepsolid.wetalkas.keepsolid.activities;

import android.app.FragmentManager;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.keepsolid.wetalkas.keepsolid.fragments.AuthorisationFragment;
import com.keepsolid.wetalkas.keepsolid.fragments.SettingsFragment;
import com.keepsolid.wetalkas.keepsolid.fragments.SplashFragment;
import com.keepsolid.wetalkas.keepsolid.fragments.TasksFragment;
import com.keepsolid.wetalkas.keepsolid.sdk.CustomFragmentManager;
import com.keepsolid.wetalkas.keepsolid.sdk.CustomPreferenceManager;
import com.keepsolid.wetalkas.keepsolid.R;


public class MainActivity extends ActionBarActivity {



    boolean doubleBackToExitPressedOnce;

    private CustomFragmentManager customFragmentManager;
    private TasksFragment tasksFragment;
    private CustomPreferenceManager preferenceManager;

    private SplashFragment splashFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        CustomPreferenceManager.getInstance().init(getApplicationContext());

        preferenceManager = CustomPreferenceManager.getInstance();


        tasksFragment = new TasksFragment();

        splashFragment = new SplashFragment();





        CustomFragmentManager.getInstance().init(this);

        customFragmentManager = CustomFragmentManager.getInstance();

        customFragmentManager.setFragment(R.id.container, splashFragment, true);


    }


    @Override
    protected void onStart() {
        super.onStart();
        doubleBackToExitPressedOnce = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        switch (id) {
            //noinspection SimplifiableIfStatement
            case R.id.action_logout:
                preferenceManager.putBoolean("remembered", false);
                AuthorisationFragment authorisationFragment = new AuthorisationFragment();
                customFragmentManager.setFragment(R.id.container, authorisationFragment, false);
                return true;

            case R.id.action_create_new_database:
                tasksFragment.deleteAllTasksFromDataBase();
                break;

            case R.id.action_sorting:
                tasksFragment.showSortingDialog(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        FragmentManager fragmentManager = customFragmentManager.getFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();
        } else {

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

        }




    }


    public TasksFragment getTasksFragment() {
        return tasksFragment;
    }
}
