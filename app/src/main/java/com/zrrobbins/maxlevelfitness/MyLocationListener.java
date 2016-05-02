package com.zrrobbins.maxlevelfitness;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.TextView;

import com.zrrobbins.maxlevelfitness.ViewPager.GoalSessionFragment;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by zrrobbins on 4/30/16.
 */
public class MyLocationListener implements LocationListener {

    GoalSessionFragment goalSessionFragment;

    MyLocationListener(GoalSessionFragment goalSessionFragment) {
        this.goalSessionFragment = goalSessionFragment;
    }

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
        goalSessionFragment.updateRunningSpeed(strCurrentSpeed, strUnits);
        if (goalSessionFragment != null && goalSessionFragment.isGoalBeingTracked()) { //TODO: Implement this later:  && newGoalSessionFrame.getGoalBeingTracked() instanceof RunningGoal) {
            goalSessionFragment.updateRunningSpeed(strCurrentSpeed, strUnits);
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
