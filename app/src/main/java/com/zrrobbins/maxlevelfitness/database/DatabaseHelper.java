package com.zrrobbins.maxlevelfitness.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Alexi on 4/24/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "RunningDatabase";

    // Table Names
    public static final String GOAL_TABLE = "RunGoals";
    public static final String SESSION_TABLE = "RunningSessions";
    public static final String PAIR_TABLE = "DistSpeedPairs";

    //GOAL_TABLE Create String
    private static String CREATE_GOAL_TABLE = "create table "
            + GOAL_TABLE + " ("
            + DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_ID+ " integer primary key autoincrement, "
            + DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_DISTANCE + " text not null, "
            + DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_FREQUENCY + " integer not null, "
            + DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_SPEED + " integer not null,"
            + DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_TYPE + " text not null)";
    //SESSION_TABLE Create String
    private static final String SESSION_TABLE_CREATE = "create table "
            + SESSION_TABLE + " ("
            + DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_SESSION_ID + " integer primary key autoincrement, "
            + DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_PARENT_GOAL_ID + " integer, "
            + DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_START_TIME + " integer not null, "
            + DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_END_TIME + " integer not null,"
            + " FOREIGN KEY ("+DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_PARENT_GOAL_ID+") REFERENCES "+
                GOAL_TABLE+"("+DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_ID+"));";

    //DistSpeedPairs Table Create String
    private static final String PAIR_TABLE_CREATE = "create table "
            + PAIR_TABLE + " ("
            + DatabaseContract.DatabaseEntry.DistSpeedPairEntry.COLUMN_NAME_PAIR_ID + " integer primary key autoincrement, "
            + DatabaseContract.DatabaseEntry.DistSpeedPairEntry.COLUMN_NAME_PARENT_SESSION_ID + " integer, "
            + DatabaseContract.DatabaseEntry.DistSpeedPairEntry.COLUMN_NAME_DISTANCE_LENGTH+ " integer not null, "
            + DatabaseContract.DatabaseEntry.DistSpeedPairEntry.COLUMN_NAME_DISTANCE_UNITS + " text not null,"
            + DatabaseContract.DatabaseEntry.DistSpeedPairEntry.COLUMN_NAME_SPEED + " integer not null,"
            + DatabaseContract.DatabaseEntry.DistSpeedPairEntry.COLUMN_NAME_START_TIME + " integer not null,"
            + DatabaseContract.DatabaseEntry.DistSpeedPairEntry.COLUMN_NAME_END_TIME + " integer not null,"
            + " FOREIGN KEY ("+DatabaseContract.DatabaseEntry.DistSpeedPairEntry.COLUMN_NAME_PARENT_SESSION_ID+") REFERENCES "+
            SESSION_TABLE+"("+DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_SESSION_ID+"));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
