package com.zrrobbins.maxlevelfitness;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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

    private OnFragmentInteractionListener mListener;
    List<String> groupList;
    List<String> childList;
    Map<String, List<String>> laptopCollection;
    ExpandableListView expListView;

    List<Goal> goalList;
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

                ((MainActivity)getActivity()).updateGoalSessionInfo(goalList.get(Integer.parseInt(selected)-1));

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
        goalList = new ArrayList<Goal>();
        goalList.add(new RunningGoal(1, GoalType.RUNNING, 5, new Distance(4, "mile"), new Speed(6, "mph")));
        goalList.add(new RunningGoal(2, GoalType.RUNNING, 10, new Distance(8, "mile"), new Speed(4, "mph")));
        goalStringList = new ArrayList<String>();
        for (Goal goal : goalList) {
            goalStringList.add(""+goal.getGoalID());
        }

        groupList = new ArrayList<String>();
        groupList.add("Running Goal 1");
        //groupList.add("HP");
        //groupList.add("Dell");
        //groupList.add("Lenovo");
        //groupList.add("Sony");
        //groupList.add("HCL");
        //groupList.add("Samsung");
    }

    private void createCollection() {

        // preparing goals collection
        String[] runningGoal1Info = {"Goal ID: " + goalList.get(0).getGoalID(),
                "Goal Type: " + goalList.get(0).getGoalType(),
                "Goal Frequency: " + goalList.get(0).getGoalFrequency()};
        String[] runningGoal2Info = {"Goal ID: " + goalList.get(1).getGoalID(),
                "Goal Type: " + goalList.get(1).getGoalType(),
                "Goal Frequency: " + goalList.get(1).getGoalFrequency()};

        goalCollection = new LinkedHashMap<String, List<String>>();

        for (String goalName : goalStringList) {
            if (goalName.equals("1")) {
                loadGoalChildInfo(runningGoal1Info);
            }
            else if (goalName.equals("2")) {
                loadGoalChildInfo(runningGoal2Info);
            }
            goalCollection.put(goalName, goalChildList);
        }





        // preparing laptops collection(child)
        String[] hpModels = { "HP Pavilion G6-2014TX", "ProBook HP 4540",
                "HP Envy 4-1025TX" };
        String[] hclModels = { "HCL S2101", "HCL L2102", "HCL V2002" };
        String[] lenovoModels = { "IdeaPad Z Series", "Essential G Series",
                "ThinkPad X Series", "Ideapad Z Series" };
        String[] sonyModels = { "VAIO E Series", "VAIO Z Series",
                "VAIO S Series", "VAIO YB Series" };
        String[] dellModels = { "Inspiron", "Vostro", "XPS" };
        String[] samsungModels = { "NP Series", "Series 5", "SF Series" };

        laptopCollection = new LinkedHashMap<String, List<String>>();

        for (String laptop : groupList) {
            if (laptop.equals("HP")) {
                loadChild(hpModels);
            } else if (laptop.equals("Dell"))
                loadChild(dellModels);
            else if (laptop.equals("Sony"))
                loadChild(sonyModels);
            else if (laptop.equals("HCL"))
                loadChild(hclModels);
            else if (laptop.equals("Samsung"))
                loadChild(samsungModels);
            else if (laptop.equals("Lenovo"))
                loadChild(lenovoModels);
            else
                loadChild(runningGoal1Info);

            laptopCollection.put(laptop, childList);
        }
    }

    private void loadGoalChildInfo(String[] goalChildInfo) {
        goalChildList = new ArrayList<String>();
        for (String info : goalChildInfo)
            goalChildList.add(info);
    }

    private void loadChild(String[] laptopModels) {
        childList = new ArrayList<String>();
        for (String model : laptopModels)
            childList.add(model);
    }

    // Convert pixel to dip
    public int getDipsFromPixel(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
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
