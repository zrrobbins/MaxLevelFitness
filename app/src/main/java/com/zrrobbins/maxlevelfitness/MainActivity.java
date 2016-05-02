package com.zrrobbins.maxlevelfitness;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.zrrobbins.maxlevelfitness.Abstracts.Goal;
import com.zrrobbins.maxlevelfitness.Abstracts.GoalType;
import com.zrrobbins.maxlevelfitness.Running.DistSpeedPair;
import com.zrrobbins.maxlevelfitness.Running.Distance;
import com.zrrobbins.maxlevelfitness.Running.RunningGoal;
import com.zrrobbins.maxlevelfitness.Running.RunningSession;
import com.zrrobbins.maxlevelfitness.Running.Speed;
import com.zrrobbins.maxlevelfitness.ViewPager.GoalSessionFragment;
import com.zrrobbins.maxlevelfitness.ViewPager.ScreenSlidePageFragment;
import com.zrrobbins.maxlevelfitness.database.DatabaseHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ViewPager mPager;

    private PagerAdapter mPagerAdapter;
    private DatabaseHelper dbHelper;
    private GoalSessionFragment newGoalSessionFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(2);
        mPager.setOffscreenPageLimit(3);

        //Attach tablayout to view pager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mPager);

        // Initialize location tracking
        MyLocationListener locationListener = new MyLocationListener();
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);;
        } catch (Exception e) {
            System.err.println("ERROR in requesting location updates in Main Activity: " + e.getMessage());
            e.printStackTrace();
        }
        locationListener.updateSpeed(null);

        //

        //Instantiate dbHelper
        dbHelper = DatabaseHelper.getInstance(getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void testListUI(View view)
    {
        Intent intent = new Intent(this, UITest.class);
        startActivity(intent);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    SessionDisplay newGoalSearch =  SessionDisplay.create(0);
                    return newGoalSearch;
                case 1:
                    newGoalSessionFrame = GoalSessionFragment.create(1);
                    return newGoalSessionFrame;
                case 2:
                    LandingFragment newLandingFrame = LandingFragment.create(2);
                    return newLandingFrame;
                case 3:
                    StatsDisplay statsDisplay = StatsDisplay.create(3);
                    return statsDisplay;
                default:
                    return new ScreenSlidePageFragment();
            }
        }

        //Provides the titles of the fragments for use in tabLayout
        public CharSequence getPageTitle(int position) {
            String title=" ";
            switch (position){
                case 0:
                    title="Search";
                    break;
                case 1:
                    title="Session";
                    break;
                case 2:
                    title="Goals";
                    break;
                case 3:
                    title="Stats";
                    break;
                case 4:
                    title="Leaders";
                    break;
            }

            return title;
        }

        @Override
        public int getCount() {
            return 5;
        }
    }

    public void testDB(View v)
    {
        Distance testDist = new Distance(35, "meters");
        Speed testSpeed = new Speed(3, "m/s");
        int newID = dbHelper.getNewRunningGoalID();
        RunningGoal testGoal = new RunningGoal(newID, GoalType.RUNNING, 5, testDist, testSpeed);
        dbHelper.addRunningGoal(testGoal);
        List<RunningGoal> runningGoals = dbHelper.retrieveAllRunningGoals();
        System.out.println("Added goal with id: "+newID);
        System.out.println("Number of running goals found: "+runningGoals.size());
        for(RunningGoal rg : runningGoals)
        {
            System.out.println("Running Goal ID:"+rg.getGoalID());
        }
    }

    public void clearDB(View v)
    {
        dbHelper.clearDB();
    }


    public void updateGoalSessionInfo(Goal goal) {
        newGoalSessionFrame.updateGoalSessionInfo(goal);
    }

    public void addGoalsAndSessions(View v)
    {
        Calendar calendar = Calendar.getInstance();
        RunningGoal rg1 = new RunningGoal(dbHelper.getNewRunningGoalID(),
                GoalType.RUNNING, 5 , new Distance(4, "miles"), new Speed(6, "mph"));
        RunningGoal rg2 = new RunningGoal(dbHelper.getNewRunningGoalID(),
                GoalType.RUNNING, 3, new Distance(8, "miles"), new Speed(4, "mph"));
        DistSpeedPair testDistSpeed = new DistSpeedPair(new Distance (2, "miles"), new Speed (3, "mph"));
        RunningSession rs1 = new RunningSession(rg1,
                calendar.getTimeInMillis(), calendar.getTimeInMillis(), dbHelper.getNewRunningSessionID(), testDistSpeed);
        System.out.println("Current Time:" + (new Date(calendar.getTimeInMillis())));
                rg1.addRunningSession(rs1);
        dbHelper.addRunningGoal(rg1);
        dbHelper.addRunningGoal(rg2);
        dbHelper.addRunningSession(rs1);
    }



    /*


    Methods below are used for speed tracking via location.


     */

    /**
     * Created by zrrobbins on 4/30/16.
     */
    public class MyLocationListener implements LocationListener {

        public void updateSpeed(Location location) {
            // TODO Auto-generated method stub
            float nCurrentSpeed = 0;

            if(location != null)
            {
                nCurrentSpeed = location.getSpeed();
            }

            Formatter fmt = new Formatter(new StringBuilder());
            fmt.format(Locale.US, "%5.1f", nCurrentSpeed);
            String strCurrentSpeed = fmt.toString();
            strCurrentSpeed = strCurrentSpeed.replace(' ', '0');

            String strUnits = "miles/hour";
            System.out.println("-------------------in UpdateSpeed()");
            if (newGoalSessionFrame != null && newGoalSessionFrame.isGoalBeingTracked()) { //TODO: Implement this later:  && newGoalSessionFrame.getGoalBeingTracked() instanceof RunningGoal) {
                newGoalSessionFrame.updateRunningSpeed(strCurrentSpeed, strUnits);
            }
        }


        @Override
        public void onLocationChanged(Location location) {
            if(location != null) {
                this.updateSpeed(location);
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }
    }
}
