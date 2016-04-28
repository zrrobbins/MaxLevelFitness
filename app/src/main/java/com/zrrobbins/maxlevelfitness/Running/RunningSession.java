package com.zrrobbins.maxlevelfitness.Running;

import com.zrrobbins.maxlevelfitness.Abstracts.WorkoutSession;

/**
 * Created by Alexi on 4/24/2016.
 */
public class RunningSession extends WorkoutSession {
    private RunningGoal runningGoal;
    private DistSpeedPair distSpeed;

    public RunningSession(RunningGoal runningGoal)
    {
        this.runningGoal = runningGoal;
    }

    public int getAverageSpeed()
    {
        return 0;
    }

    public RunningGoal getRunningGoal() {return runningGoal;}

    public Distance getTotalDistance()
    {
        return null;
    }
}
