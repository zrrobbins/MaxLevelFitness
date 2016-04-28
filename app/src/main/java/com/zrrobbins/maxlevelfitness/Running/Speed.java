package com.zrrobbins.maxlevelfitness.Running;

/**
 * Created by Alexi on 4/28/2016.
 */
public class Speed {
    private int value;
    private String units;

    public Speed(int value, String units)
    {
        this.value = value;
        this.units = units;
    }

    public int getValue()
    {
        return value;
    }

    public String getUnits()
    {
        return units;
    }
}
