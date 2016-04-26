package com.zrrobbins.maxlevelfitness.Running;

/**
 * Created by Alexi on 4/24/2016.
 */
public class DistSpeedPair {
    private double startTime;
    private double endTime;
    private Distance distance;
    private int speed;

    public DistSpeedPair(Distance distance, int speed, double startTime, double endTime)
    {
        this.distance = distance;
        this.speed = speed;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Distance getDistance()
    {
        return distance;
    }

    public int getSpeed()
    {
        return speed;
    }

    public double getStartTime()
    {
        return startTime;
    }

    public double getEndTime()
    {
        return endTime;
    }
}
