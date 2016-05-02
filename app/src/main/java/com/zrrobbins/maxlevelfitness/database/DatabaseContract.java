package com.zrrobbins.maxlevelfitness.database;

import android.provider.BaseColumns;

/**
 * Created by Alexi on 4/24/2016.
 */
public class DatabaseContract {
    public DatabaseContract() {
    }

    /*Define table contents*/
    public static abstract class DatabaseEntry implements BaseColumns
    {
        public static abstract class RunGoalEntry implements BaseColumns
        {
            public static final String TABLE_NAME = "RunGoals";
            public static final String COLUMN_NAME_GOAL_ID = "runGoalID";
            public static final String COLUMN_NAME_GOAL_TYPE = "goalType";
            public static final String COLUMN_NAME_GOAL_FREQUENCY = "goalFrequency";
            public static final String COLUMN_NAME_GOAL_DISTANCE_LENGTH = "goalDistanceLength";
            public static final String COLUMN_NAME_GOAL_DISTANCE_UNITS = "goalDistanceUnits";
            public static final String COLUMN_NAME_GOAL_SPEED_VALUE = "goalSpeedValue";
            public static final String COLUMN_NAME_GOAL_SPEED_UNITS = "goalSpeedUnits";
            public static final String COLUMN_NAME_IS_ACTIVE = "isActive";
            public static final String COLUMN_NAME_COMPLETED = "completed";
        }

        public static abstract class RunningSessionEntry implements BaseColumns
        {
            public static final String TABLE_NAME = "RunningSessions";
            public static final String COLUMN_NAME_SESSION_ID = "sessionID";
            public static final String COLUMN_NAME_PARENT_GOAL_ID = "parenRunGoalID";
            public static final String COLUMN_NAME_START_TIME = "startTime";
            public static final String COLUMN_NAME_END_TIME = "endTime";
            public static final String COLUMN_NAME_DISTANCE_LENGTH = "goalDistanceLength";
            public static final String COLUMN_NAME_DISTANCE_UNITS = "goalDistanceUnits";
            public static final String COLUMN_NAME_SPEED_VALUE = "goalSpeedValue";
            public static final String COLUMN_NAME_SPEED_UNITS = "goalSpeedUnits";
        }

    }
}
