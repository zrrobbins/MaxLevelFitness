package com.zrrobbins.maxlevelfitness.Running;

import com.zrrobbins.maxlevelfitness.Abstracts.WorkoutSession;

/**
 * Created by Alexi on 4/24/2016.
 */
public class RunningSession extends WorkoutSession {
    private RunningGoal runningGoal;

    public DistSpeedPair getDistSpeed() {
        return distSpeed;
    }

    private DistSpeedPair distSpeed;

    public RunningSession(RunningGoal runningGoal, double startTime, double endTime, long id, DistSpeedPair distSpeed)
    {
        super(startTime, endTime, id);
        this.runningGoal = runningGoal;
        this.distSpeed = distSpeed;
    }

    public RunningGoal getRunningGoal() {return runningGoal;}

    public void setDistSpeed( Distance dist, Speed speed)
    {
        distSpeed = new DistSpeedPair(dist, speed);
    }
}
