package com.zrrobbins.maxlevelfitness.Abstracts;

/**
 * Created by zrrobbins on 4/24/16.
 */
public abstract class Goal
{
    private GoalType goalType;
    private int goalID;
    private int frequency;


    public Goal(int goalName, GoalType goalType, int frequency) {
        this.goalID = goalName;
        this.goalType = goalType;
        this.frequency = frequency;
    }

    public int getGoalID() {
        return goalID;
    }

    public  int getGoalFrequency()
    {
        return frequency;
    }

    public GoalType getGoalType()
    {
        return goalType;
    }
}
