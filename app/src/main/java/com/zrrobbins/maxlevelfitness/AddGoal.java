package com.zrrobbins.maxlevelfitness;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.zrrobbins.maxlevelfitness.Abstracts.GoalType;
import com.zrrobbins.maxlevelfitness.Running.Distance;
import com.zrrobbins.maxlevelfitness.Running.RunningGoal;
import com.zrrobbins.maxlevelfitness.Running.Speed;
import com.zrrobbins.maxlevelfitness.database.DatabaseHelper;

public class AddGoal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);
    }

    public void addGoal(View v)
    {
        System.out.println("add goal called");
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(getApplicationContext());
        Spinner typeSpinner = (Spinner)findViewById(R.id.spinner1);
        Spinner frequencySpinner = (Spinner)findViewById(R.id.spinner2);
        EditText distanceVal = (EditText)findViewById(R.id.distanceVal);
        EditText speedVal = (EditText)findViewById(R.id.speedVal);

        GoalType goalType;
        int frequency;
        int distance;
        int speed;
        if (typeSpinner.getSelectedItem().toString() == "Running")
        {
            goalType = GoalType.RUNNING;
        }
        else
        {
            goalType = GoalType.GYM_VISITS;
        }
        if (frequencySpinner.getSelectedItem().toString() == "1 day/week")
            frequency = 1;
        else if (frequencySpinner.getSelectedItem().toString() == "2 days/week")
            frequency = 2;
        else if (frequencySpinner.getSelectedItem().toString() == "3 days/week")
            frequency = 3;
        else if (frequencySpinner.getSelectedItem().toString() == "4 days/week")
            frequency = 4;
        else if (frequencySpinner.getSelectedItem().toString() == "5 days/week")
            frequency = 5;
        else if (frequencySpinner.getSelectedItem().toString() == "6 days/week")
            frequency = 6;
        else
            frequency = 7;
        distance = Integer.parseInt(distanceVal.getText().toString());
        speed = Integer.parseInt(speedVal.getText().toString());

        Distance newDist = new Distance(distance, "miles");
        Speed newSpeed = new Speed(speed, "mph");
        RunningGoal newGoal = new RunningGoal(dbHelper.getNewRunningGoalID(), goalType, frequency, newDist, newSpeed);
        dbHelper.addRunningGoal(newGoal);
        finish();
    }
}
