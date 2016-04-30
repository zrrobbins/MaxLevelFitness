package com.zrrobbins.maxlevelfitness.Running;

import com.zrrobbins.maxlevelfitness.Abstracts.Goal;
import com.zrrobbins.maxlevelfitness.Abstracts.GoalType;

import java.util.ArrayList;

/**
 * Created by Alexi on 4/25/2016.
 */
public class RunningGoal extends Goal {

    private Distance distance;
    private Speed speed;
    private ArrayList<RunningSession> sessions = new ArrayList<RunningSession>();

    public RunningGoal(int goalID, GoalType goalType, int frequency, Distance distance, Speed speed) {
        super(goalID, goalType, frequency);
        this.distance = distance;
        this.speed = speed;
    }

    public void addRunningSession(RunningSession sess)
    {
        sessions.add(sess);
    }

    public Distance getDistance(){ return distance;}

    public long getTotalDistanceRan()
    {
        long retVal = 0;
        for (RunningSession sess: sessions)
        {
            retVal += sess.getDistSpeed().getDistance().getLength();
        }
        return retVal;
    }

    public Speed getSpeed() {return speed; }


}
