package com.keepsolid.wetalkas.keepsolid.activities;

import android.app.FragmentManager;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.keepsolid.wetalkas.keepsolid.sdk.CustomSQLiteHelper;
import com.keepsolid.wetalkas.keepsolid.services.AlarmManagerHelper;
import com.keepsolid.wetalkas.keepsolid.todo_sdk.adapter.TabAdapter;


public class MainActivity extends AppCompatActivity{



    boolean doubleBackToExitPressedOnce;

    private CustomFragmentManager customFragmentManager;
    private TasksFragment tasksFragment;
    private CustomPreferenceManager preferenceManager;

    private AlarmManagerHelper alarmManagerHelper;

    private SplashFragment splashFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        CustomPreferenceManager.getInstance().init(getApplicationContext());

        preferenceManager = CustomPreferenceManager.getInstance();

        AlarmManagerHelper.getInstance().init(this);


        tasksFragment = new TasksFragment();

        splashFragment = new SplashFragment();





        CustomFragmentManager.getInstance().init(this);

        customFragmentManager = CustomFragmentManager.getInstance();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            Log.d("toolbar", "not null");
            toolbar.setTitle("TODO");
            toolbar.setTitleTextColor(getResources().getColor(R.color.gray_50));

            setSupportActionBar(toolbar);

            //toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_dots_vertical_white_24dp));
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Current"));
        tabLayout.addTab(tabLayout.newTab().setText("Done"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final TabAdapter adapter = new TabAdapter(getFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //customFragmentManager.setFragment(R.id.container, splashFragment, true);


    }


    @Override
    protected void onStart() {
        super.onStart();
        doubleBackToExitPressedOnce = false;
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
