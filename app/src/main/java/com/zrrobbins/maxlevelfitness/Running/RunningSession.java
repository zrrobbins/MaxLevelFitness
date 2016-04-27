package com.zrrobbins.maxlevelfitness.Running;

import com.zrrobbins.maxlevelfitness.Abstracts.WorkoutSession;

/**
 * Created by Alexi on 4/24/2016.
 */
public class RunningSession extends WorkoutSession {
    private RunningGoal runningGoal;
    private DistSpeedPair[] sections;

    public RunningSession(RunningGoal runningGoal)
    {
        this.runningGoal = runningGoal;
    }

    public int getAverageSpeed()
    {
        return 0;
    }

    public int getTopSpeed()
    {
        return 0;
    }

    public int getSlowestSpeed()
    {
        return 0;
    }

    public Distance getTotalDistance()
    {
        return null;
    }
}
