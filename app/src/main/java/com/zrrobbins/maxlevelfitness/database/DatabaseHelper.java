package com.zrrobbins.maxlevelfitness.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.zrrobbins.maxlevelfitness.Abstracts.GoalType;
import com.zrrobbins.maxlevelfitness.Running.DistSpeedPair;
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

    private static DatabaseHelper instance;

    public static DatabaseHelper getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 7;

    // Database Name
    private static final String DATABASE_NAME = "RunningDatabase";

    // Table Names
    public static final String GOAL_TABLE = DatabaseContract.DatabaseEntry.RunGoalEntry.TABLE_NAME;
    public static final String SESSION_TABLE = DatabaseContract.DatabaseEntry.RunningSessionEntry.TABLE_NAME;

    //GOAL_TABLE Create String
    private static String CREATE_GOAL_TABLE = "create table "
            + GOAL_TABLE + " ("
            + DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_ID+ " integer primary key autoincrement, "
            + DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_DISTANCE_LENGTH + " integer not null,"
            + DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_DISTANCE_UNITS + " text not null, "
            + DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_FREQUENCY + " integer not null, "
            + DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_SPEED_VALUE + " integer not null,"
            + DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_SPEED_UNITS + " text not null,"
            + DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_COMPLETED + " integer not null,"
            + DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_IS_ACTIVE + " integer not null,"
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

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_GOAL_TABLE);
        db.execSQL(SESSION_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + GOAL_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SESSION_TABLE);

        // create new tables
        onCreate(db);
    }

    public void clearDB()
    {
        this.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + GOAL_TABLE);
        this.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + SESSION_TABLE);

        onCreate(this.getWritableDatabase());
    }

    public void addRunningGoal(RunningGoal goal)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int completedState = 0;
        int isActive = 0;

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
        if (goal.isActive())
        {
            isActive = 1;
        }
        if (goal.isCompleted())
        {
            completedState = 1;
        }
        values.put(DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_IS_ACTIVE,
                isActive);
        values.put(DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_COMPLETED,
                completedState);
        // insert row
        long goal_id = db.insert(GOAL_TABLE, null, values);

        //Add sessions
        for (RunningSession sess: goal.getSessions())
        {
            addRunningSession(sess);
        }
    }

    public void addRunningSession(RunningSession sess)
    {
        if (checkExists(SESSION_TABLE, DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_SESSION_ID, sess.getId()))
        {
            updateRunningSession(sess);
        }
        else
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
    }

    public void updateRunningSession (RunningSession sess)
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
        String equivalenceString = DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_SESSION_ID + "=" + Long.toString(sess.getId());
        long todo_id = db.update(SESSION_TABLE, values, equivalenceString, null);
    }

    public RunningGoal getRunningGoal(long goalID) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + GOAL_TABLE + " WHERE "
                + DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_ID + " = " + goalID;

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

        int isActive = c.getInt((c.getColumnIndex(DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_IS_ACTIVE)));
        int completedState = c.getInt((c.getColumnIndex(DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_COMPLETED)));

        RunningGoal rg = new RunningGoal(rgID, rgType, rgFrequency, newDist, newSpeed);
        if (isActive > 0)
        {
            rg.setIsActive(true);
        }
        if (completedState > 0)
        {
            rg.setCompleted(true);
        }

        c.close();

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
                RunningSession rs = new RunningSession(parentGoal, startTime, endTime, newId, new DistSpeedPair(newDistance, newSpeed));
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

                int isActive = c.getInt((c.getColumnIndex(DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_IS_ACTIVE)));
                int completedState = c.getInt((c.getColumnIndex(DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_COMPLETED)));

                RunningGoal rg = new RunningGoal(rgID, rgType, rgFrequency, newDist, newSpeed);
                if (isActive > 0)
                {
                    rg.setIsActive(true);
                }
                if (completedState > 0)
                {
                    rg.setCompleted(true);
                }
                //Retrieve all relevant sessions
                List<RunningSession> relevantSessions = getRunningSessionsWithParentID(rgID);
                for (RunningSession rs : relevantSessions)
                {
                    rg.addRunningSession(rs);
                }

                RunningGoals.add(rg);
            } while (c.moveToNext());
        }

        c.close();

        return RunningGoals;
    }

    public List<RunningSession> retrieveAllRunningSessions()
    {
        List<RunningSession> RunningSessions = new ArrayList<RunningSession>();
        String selectQuery = "SELECT  * FROM " + SESSION_TABLE;

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
                RunningSession rs = new RunningSession(parentGoal, startTime, endTime, newId, new DistSpeedPair(newDistance, newSpeed));

                RunningSessions.add(rs);
            } while (c.moveToNext());
        }

        c.close();

        return RunningSessions;
    }

    private boolean checkExists(String TableName, String fieldName, long fieldValue) {
        SQLiteDatabase sqldb = this.getWritableDatabase();
        String Query = "Select * from " + TableName + " where " + fieldName + " = " + fieldValue;
        Cursor cursor = sqldb.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public int getNewRunningGoalID()
    {
        List<RunningGoal> rgList = retrieveAllRunningGoals();
        if (rgList.size() == 0)
        {
            return 1;
        }
        else
        {
            SQLiteDatabase sqldb = this.getWritableDatabase();

            String Query = "SELECT MAX("+ DatabaseContract.DatabaseEntry.RunGoalEntry.COLUMN_NAME_GOAL_ID+") AS max_id FROM "+GOAL_TABLE;
            Cursor cursor = sqldb.rawQuery(Query, null);
            if (cursor != null)
                cursor.moveToFirst();
            int retVal = (cursor.getInt((cursor.getColumnIndex("max_id"))))+1;
            cursor.close();
            return retVal;
        }
    }

    public int getNewRunningSessionID()
    {
        List<RunningGoal> rsList = retrieveAllRunningGoals();
        if (rsList.size() == 0)
        {
            return 1;
        }
        else
        {
            SQLiteDatabase sqldb = this.getWritableDatabase();

            String Query = "SELECT MAX("+ DatabaseContract.DatabaseEntry.RunningSessionEntry.COLUMN_NAME_SESSION_ID+") AS max_id FROM "+SESSION_TABLE;
            Cursor cursor = sqldb.rawQuery(Query, null);
            if (cursor != null)
                cursor.moveToFirst();

            int retVal = (cursor.getInt((cursor.getColumnIndex("max_id"))))+1;
            cursor.close();
            return retVal;
        }
    }

}