package com.zrrobbins.maxlevelfitness.Abstracts;

/**
 * Created by Alexi on 4/24/2016.
 */
public abstract class WorkoutSession {
    double startTime;
    double endTime;

    public double getElapsedTime()
    {
        return endTime-startTime;
    }

    public double getStartTime() { return startTime;}

    public double getEndTime() { return endTime; }
}
