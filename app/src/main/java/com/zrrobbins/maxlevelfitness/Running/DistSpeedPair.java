package com.zrrobbins.maxlevelfitness.Running;

/**
 * Created by Alexi on 4/24/2016.
 */
public class DistSpeedPair {
    private Distance distance;
    private Speed speed;

    public DistSpeedPair(Distance distance, Speed speed)
    {
        this.distance = distance;
        this.speed = speed;
    }

    public Distance getDistance()
    {
        return distance;
    }

    public Speed getSpeed()
    {
        return speed;
    }
}
