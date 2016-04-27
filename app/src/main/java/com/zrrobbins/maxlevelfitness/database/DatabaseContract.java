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
            public static final String COLUMN_NAME_GOAL_SPEED = "goalSpeed";
        }

        public static abstract class RunningSessionEntry implements BaseColumns
        {
            public static final String TABLE_NAME = "RunningSessions";
            public static final String COLUMN_NAME_SESSION_ID = "sessionID";
            public static final String COLUMN_NAME_PARENT_GOAL_ID = "parenRunGoalID";
            public static final String COLUMN_NAME_START_TIME = "startTime";
            public static final String COLUMN_NAME_END_TIME = "endTime";
        }

        public static abstract class DistSpeedPairEntry implements BaseColumns
        {
            public static final String TABLE_NAME = "DistSpeedPairs";
            public static final String COLUMN_NAME_PAIR_ID = "pairID";
            public static final String COLUMN_NAME_PARENT_SESSION_ID = "parentSessionID";
            public static final String COLUMN_NAME_DISTANCE_LENGTH = "distanceLength";
            public static final String COLUMN_NAME_DISTANCE_UNITS = "distanceUnits";
            public static final String COLUMN_NAME_SPEED = "speed";
            public static final String COLUMN_NAME_START_TIME = "startTime";
            public static final String COLUMN_NAME_END_TIME = "endTime";
        }
    }
}
