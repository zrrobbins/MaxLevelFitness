package com.zrrobbins.maxlevelfitness.Running;

import com.zrrobbins.maxlevelfitness.Abstracts.Goal;
import com.zrrobbins.maxlevelfitness.Abstracts.GoalType;

import java.util.ArrayList;

/**
 * Created by Alexi on 4/25/2016.
 */
public class RunningGoal extends Goal {

    private Distance distance;
    private int speed;
    private ArrayList<RunningSession> sessions;

    public RunningGoal(int goalID, GoalType goalType, int frequency, Distance distance, int speed) {
        super(goalID, goalType, frequency);
        this.distance = distance;
        this.speed = speed;
    }

    public void addRunningSession(RunningSession sess)
    {
        sessions.add(sess);
    }

    public Distance getDistance(){ return distance;}

    public int getSpeed() {return speed; }


}
