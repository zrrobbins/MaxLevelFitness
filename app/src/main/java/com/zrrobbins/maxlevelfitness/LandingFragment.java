package com.zrrobbins.maxlevelfitness;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zrrobbins.maxlevelfitness.Abstracts.Goal;
import com.zrrobbins.maxlevelfitness.Abstracts.GoalType;
import com.zrrobbins.maxlevelfitness.Running.Distance;
import com.zrrobbins.maxlevelfitness.Running.RunningGoal;
import com.zrrobbins.maxlevelfitness.Running.Speed;
import com.zrrobbins.maxlevelfitness.ViewPager.GoalSessionFragment;
import com.zrrobbins.maxlevelfitness.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LandingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LandingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LandingFragment extends Fragment {
    public static final String ARG_PAGE = "page";

    private DatabaseHelper dbHelper;
    private OnFragmentInteractionListener mListener;
    List<String> groupList;
    List<String> childList;
    Map<String, List<String>> laptopCollection;
    ExpandableListView expListView;

    List<RunningGoal> goalList;
    List<String> goalStringList;
    List<String> goalChildList;
    Map<String, List<String>> goalCollection;

    public LandingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LandingFragment.
     */
    public static LandingFragment newInstance() {
        LandingFragment fragment = new LandingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static LandingFragment create(int pageNumber)
    {
        LandingFragment fragment = new LandingFragment();
        Bundle args = new Bundle();
        System.out.println("page number: " + pageNumber);
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createGroupList();
        createCollection();
        dbHelper = DatabaseHelper.getInstance(getContext());
        for (RunningGoal goal:goalList)
        {
            dbHelper.addRunningGoal(goal);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflated =  inflater.inflate(R.layout.landing_page, container, false);
        final View inflatedCopy = inflated;
        expListView = (ExpandableListView) inflated.findViewById(R.id.laptop_list);
        //final ExpandableListAdapter expListAdapter = new CustomExpandableAdapter(
        //        this.getActivity(), groupList, laptopCollection);
        final ExpandableListAdapter expListAdapter = new CustomExpandableAdapter(
                this.getActivity(), goalStringList, goalCollection);
        expListView.setAdapter(expListAdapter);

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                final String selected = (String) expListAdapter.getGroup(groupPosition);
                Toast.makeText(inflatedCopy.getContext(), selected + " selected for session tracking"
                        , Toast.LENGTH_LONG).show();
                final String lastInt = selected.substring(selected.length() - 1);
                ((MainActivity) getActivity()).updateGoalSessionInfo(goalList.get(Integer.parseInt(lastInt) - 1));

            }
        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                final String selected = (String) expListAdapter.getChild(
                        groupPosition, childPosition);
                Toast.makeText(inflatedCopy.getContext(), selected, Toast.LENGTH_LONG)
                        .show();

                return true;
            }
        });

        FloatingActionButton FAB = (FloatingActionButton)inflated.findViewById(R.id.addNewGoal);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity() ,AddGoal.class);
                startActivity(i);
            }
        });
        return inflated;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void createGroupList() {
        goalList = new ArrayList<RunningGoal>();
        goalList.add(new RunningGoal(1, GoalType.RUNNING, 5, new Distance(4, "miles"), new Speed(6, "mph")));
        goalList.add(new RunningGoal(2, GoalType.RUNNING, 3, new Distance(8, "miles"), new Speed(4, "mph")));
        goalStringList = new ArrayList<String>();
        for (RunningGoal goal : goalList) {
            goalStringList.add("Current Goal "+goal.getGoalID());
        }

        groupList = new ArrayList<String>();
        groupList.add("Running Goal 1");
    }

    private void createCollection() {

        // preparing goals collection
        String[] runningGoal1Info = runningGoalToStringArray(goalList.get(0));
        String[] runningGoal2Info = runningGoalToStringArray(goalList.get(1));

        goalCollection = new LinkedHashMap<String, List<String>>();

        for (String goalName : goalStringList) {
            if (goalName.equals("Current Goal 1")) {
                loadGoalChildInfo(runningGoal1Info);
            }
            else if (goalName.equals("Current Goal 2")) {
                loadGoalChildInfo(runningGoal2Info);
            }
            goalCollection.put(goalName, goalChildList);
        }
    }

    private String[] runningGoalToStringArray(RunningGoal goal)
    {
        String[] retArray = new String[4];
        retArray[0] = "Goal Type: "+goal.getGoalType();
        retArray[1] = "Target frequency: "+goal.getGoalFrequency()+" days/week";
        retArray[2] = "Target distance: "+goal.getDistance().getLength()+" "+goal.getDistance().getUnits();
        retArray[3] = "Target speed: "+goal.getSpeed().getValue()+" "+goal.getSpeed().getUnits();
        return retArray;
    }

    private void loadGoalChildInfo(String[] goalChildInfo) {
        goalChildList = new ArrayList<String>();
        for (String info : goalChildInfo)
            goalChildList.add(info);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
