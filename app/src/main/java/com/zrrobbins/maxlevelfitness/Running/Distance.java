package com.zrrobbins.maxlevelfitness.Running;

/**
 * Created by Alexi on 4/24/2016.
 */
public class Distance {
    private int length;
    private String units;

    public Distance(int length, String units)
    {
        this.length = length;
        this.units = units;
    }

    public int getLength()
    {
        return length;
    }

    public String getUnits()
    {
        return units;
    }
}
