package com.zrrobbins.maxlevelfitness;

import java.util.Enumeration;

/**
 * Created by zrrobbins on 4/24/16.
 */
public class Goal
{
    private GoalType goalType;
    private String goalName;


    Goal(String goalName, GoalType goalType) {
        this.goalName = goalName;
        this.goalType = goalType;
    }

    public String getGoalName() {
        return goalName;
    }
}
