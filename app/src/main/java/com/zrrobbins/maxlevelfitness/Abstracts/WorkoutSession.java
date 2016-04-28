package com.zrrobbins.maxlevelfitness.Abstracts;

/**
 * Created by Alexi on 4/24/2016.
 */
public abstract class WorkoutSession {
    protected double startTime;
    protected double endTime;
    protected long id;

    public WorkoutSession(double startTime, double endTime, long id)
    {
        this.startTime = startTime;
        this.endTime = endTime;
        this.id=id;
    }
    public double getElapsedTime()
    {
        return endTime-startTime;
    }

    public double getStartTime() { return startTime;}

    public double getEndTime() { return endTime; }
}
