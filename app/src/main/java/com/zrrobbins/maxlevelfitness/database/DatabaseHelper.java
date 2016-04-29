package com.zrrobbins.maxlevelfitness.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.zrrobbins.maxlevelfitness.Abstracts.GoalType;
import com.zrrobbins.maxlevelfitness.Running.Distance;
import com.zrrobbins.maxlevelfitness.Running.RunningGoal;
import com.zrrobbins.maxlevelfitness.Running.RunningSession;
import com.zrrobbins.maxlevelfitness.Running.Speed;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexi on 4/24/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 4;

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
            + DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_DISTANCE_LENGTH + " integer not null,"
            + DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_DISTANCE_UNITS + " text not null, "
            + DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_FREQUENCY + " integer not null, "
            + DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_SPEED_VALUE + " integer not null,"
            + DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_SPEED_UNITS + " text not null,"
            + DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_TYPE + " text not null)";
    //SESSION_TABLE Create String
    private static final String SESSION_TABLE_CREATE = "create table "
            + SESSION_TABLE + " ("
            + DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_SESSION_ID + " integer primary key autoincrement, "
            + DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_PARENT_GOAL_ID + " integer, "
            + DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_START_TIME + " integer not null, "
            + DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_END_TIME + " integer not null,"
            + DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_SPEED_VALUE + " integer not null,"
            + DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_SPEED_UNITS + " text not null,"
            + DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_DISTANCE_LENGTH + " integer not null,"
            + DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_DISTANCE_UNITS + " text not null,"
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
        // creating required tables
        db.execSQL(CREATE_GOAL_TABLE);
        db.execSQL(SESSION_TABLE_CREATE);
        db.execSQL(PAIR_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + GOAL_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SESSION_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PAIR_TABLE);

        // create new tables
        onCreate(db);
    }

    public void dropAllTables(SQLiteDatabase db)
    {
        db.execSQL("DROP TABLE IF EXISTS " + GOAL_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SESSION_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PAIR_TABLE);
    }

    public void addRunningGoal(RunningGoal goal)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_DISTANCE_UNITS,
                goal.getDistance().getUnits());
        values.put(DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_DISTANCE_LENGTH,
                goal.getDistance().getLength());
        values.put(DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_FREQUENCY,
                goal.getGoalFrequency());
        values.put(DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_SPEED_VALUE,
                goal.getSpeed().getValue());
        values.put(DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_SPEED_UNITS,
                goal.getSpeed().getUnits());
        values.put(DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_TYPE,
                goal.getGoalType().toString());

        // insert row
        long todo_id = db.insert(GOAL_TABLE, null, values);
    }

    public void addRunningSession(RunningSession sess)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_PARENT_GOAL_ID,
                sess.getRunningGoal().getGoalID());
        values.put(DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_START_TIME,
                sess.getStartTime());
        values.put(DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_END_TIME,
                sess.getEndTime());
        values.put(DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_DISTANCE_LENGTH,
                sess.getDistSpeed().getDistance().getLength());
        values.put(DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_DISTANCE_UNITS,
                sess.getDistSpeed().getDistance().getUnits());
        values.put(DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_SPEED_VALUE,
                sess.getDistSpeed().getSpeed().getValue());
        values.put(DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_SPEED_UNITS,
                sess.getDistSpeed().getSpeed().getUnits());
        // insert row
        long todo_id = db.insert(SESSION_TABLE, null, values);
    }

    public RunningGoal getRunningGoal(long goalID) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + GOAL_TABLE + " WHERE "
                + DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_ID + " = " + goalID;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        int rgID = c.getInt((c.getColumnIndex(DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_ID)));
        GoalType rgType  = GoalType.valueOf(c.getString((
                c.getColumnIndex(DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_TYPE))));
        int rgFrequency = c.getInt((
                c.getColumnIndex(DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_FREQUENCY)));

        int distanceLength = c.getInt((c.getColumnIndex(DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_DISTANCE_LENGTH)));
        String distanceUnits = c.getString((c.getColumnIndex(DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_DISTANCE_UNITS)));
        Distance newDist = new Distance( distanceLength, distanceUnits);

        int rgSpeedVal = c.getInt((c.getColumnIndex(DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_SPEED_VALUE)));
        String rgSpeedUnits = c.getString((c.getColumnIndex(DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_SPEED_UNITS)));
        Speed newSpeed = new Speed(rgSpeedVal, rgSpeedUnits);

        RunningGoal rg = new RunningGoal(rgID, rgType, rgFrequency, newDist, newSpeed);

        return rg;
    }

    public List<RunningSession> getRunningSessionsWithParentID(long parentID) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + SESSION_TABLE + " WHERE "
                + DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_PARENT_GOAL_ID + " = " + parentID;

        Cursor c = db.rawQuery(selectQuery, null);

        List<RunningSession> RunningSessions = new ArrayList<RunningSession>();

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                long parentGoalID = c.getInt((c.getColumnIndex(DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_PARENT_GOAL_ID)));
                RunningGoal parentGoal = getRunningGoal(parentGoalID);

                int speedVal = c.getInt((c.getColumnIndex(DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_SPEED_VALUE)));
                String speedUnits = c.getString((c.getColumnIndex(DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_SPEED_UNITS)));
                Speed newSpeed = new Speed(speedVal, speedUnits);

                int distVal = c.getInt((c.getColumnIndex(DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_DISTANCE_LENGTH)));
                String distUnits = c.getString((c.getColumnIndex(DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_DISTANCE_UNITS)));
                Distance newDistance = new Distance (distVal, distUnits);

                long newId = c.getInt((c.getColumnIndex(DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_SESSION_ID)));
                double startTime = c.getInt((c.getColumnIndex(DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_START_TIME)));
                double endTime = c.getInt((c.getColumnIndex(DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_END_TIME)));
                RunningSession rs = new RunningSession(parentGoal, startTime, endTime, newId);

                rs.setDistSpeed(newDistance, newSpeed);
                RunningSessions.add(rs);
            } while (c.moveToNext());
        }

        return RunningSessions;
    }

    public List<RunningGoal> retrieveAllRunningGoals()
    {
        List<RunningGoal> RunningGoals = new ArrayList<RunningGoal>();
        String selectQuery = "SELECT  * FROM " + GOAL_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                int rgID = c.getInt((c.getColumnIndex(DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_ID)));
                GoalType rgType  = GoalType.valueOf(c.getString((
                        c.getColumnIndex(DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_TYPE))));
                int rgFrequency = c.getInt((
                        c.getColumnIndex(DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_FREQUENCY)));
                int distanceLength = c.getInt((c.getColumnIndex(DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_DISTANCE_LENGTH)));
                String distanceUnits = c.getString((c.getColumnIndex(DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_DISTANCE_UNITS)));
                Distance newDist = new Distance( distanceLength, distanceUnits);

                int rgSpeedVal = c.getInt((c.getColumnIndex(DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_SPEED_VALUE)));
                String rgSpeedUnits = c.getString((c.getColumnIndex(DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_SPEED_UNITS)));
                Speed newSpeed = new Speed(rgSpeedVal, rgSpeedUnits);

                RunningGoal rg = new RunningGoal(rgID, rgType, rgFrequency, newDist, newSpeed);

                //Retrieve all relevant sessions
                List<RunningSession> relevantSessions = getRunningSessionsWithParentID(rgID);
                for (RunningSession rs : relevantSessions)
                {
                    rg.addRunningSession(rs);
                }

                RunningGoals.add(rg);
            } while (c.moveToNext());
        }


        return RunningGoals;
    }

    public List<RunningSession> retrieveAllRunningSessions()
    {
        List<RunningSession> RunningSessions = new ArrayList<RunningSession>();
        String selectQuery = "SELECT  * FROM " + SESSION_TABLE;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                long parentGoalID = c.getInt((c.getColumnIndex(DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_PARENT_GOAL_ID)));
                RunningGoal parentGoal = getRunningGoal(parentGoalID);

                int speedVal = c.getInt((c.getColumnIndex(DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_SPEED_VALUE)));
                String speedUnits = c.getString((c.getColumnIndex(DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_SPEED_UNITS)));
                Speed newSpeed = new Speed(speedVal, speedUnits);

                int distVal = c.getInt((c.getColumnIndex(DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_DISTANCE_LENGTH)));
                String distUnits = c.getString((c.getColumnIndex(DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_DISTANCE_UNITS)));
                Distance newDistance = new Distance (distVal, distUnits);

                long newId = c.getInt((c.getColumnIndex(DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_SESSION_ID)));
                double startTime = c.getInt((c.getColumnIndex(DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_START_TIME)));
                double endTime = c.getInt((c.getColumnIndex(DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_END_TIME)));
                RunningSession rs = new RunningSession(parentGoal, startTime, endTime, newId);

                rs.setDistSpeed(newDistance, newSpeed);
                RunningSessions.add(rs);
            } while (c.moveToNext());
        }


        return RunningSessions;
    }

    public int getNewRunningGoalID()
    {
        List<RunningGoal> rgList = retrieveAllRunningGoals();
        return rgList.size()+1;
    }

    public int getNewRunningSessionID()
    {
        List<RunningSession> rsList = retrieveAllRunningSessions();
        return rsList.size()+1;
    }

}