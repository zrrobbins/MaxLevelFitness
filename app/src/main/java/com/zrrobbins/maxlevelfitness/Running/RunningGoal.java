package com.zrrobbins.maxlevelfitness.Running;

import com.zrrobbins.maxlevelfitness.Abstracts.Goal;
import com.zrrobbins.maxlevelfitness.Abstracts.GoalType;

import java.util.ArrayList;

/**
 * Created by Alexi on 4/25/2016.
 */
public class RunningGoal extends Goal {

    private Distance distance;
    private int Speed;
    private ArrayList<RunningSession> sessions;

    public RunningGoal(int goalName, GoalType goalType, int frequency) {
        super(goalName, goalType, frequency);
    }

    public void addRunningSession(RunningSession sess)
    {
        sessions.add(sess);
    }
}
