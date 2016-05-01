package com.zrrobbins.maxlevelfitness;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.zrrobbins.maxlevelfitness.Abstracts.Goal;
import com.zrrobbins.maxlevelfitness.Abstracts.GoalType;
import com.zrrobbins.maxlevelfitness.Running.Distance;
import com.zrrobbins.maxlevelfitness.Running.RunningGoal;
import com.zrrobbins.maxlevelfitness.Running.RunningSession;
import com.zrrobbins.maxlevelfitness.Running.Speed;
import com.zrrobbins.maxlevelfitness.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SessionDisplay.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SessionDisplay# newInstance} factory method to
 * create an instance of this fragment.
 */
public class SessionDisplay extends Fragment {
    public static final String ARG_PAGE = "page";

    private DatabaseHelper dbHelper;
    private OnFragmentInteractionListener mListener;
    List<String> groupList;
    List<String> childList;
    ExpandableListView expListView;

    List<RunningSession> sessionList;
    List<String> sessionStringList;
    List<String> sessionChildList;
    Map<String, List<String>> sessionCollection;
    //ArrayList<HashMap<String,String>> sessionList;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param //param1 Parameter 1.
     * @param //param2 Parameter 2.
     * @return A new instance of fragment SessionDisplay.

    // TODO: Rename and change types and number of parameters
    public static SessionDisplay newInstance(String param1, String param2) {
        SessionDisplay fragment = new SessionDisplay();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
*/

    public static SessionDisplay create(int pageNumber)
    {

        SessionDisplay fragment = new SessionDisplay();
        Bundle args = new Bundle();
        System.out.println("page number: " + pageNumber);
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public SessionDisplay() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(this.getContext());
        createGroupList();
        createCollection();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflated =  inflater.inflate(R.layout.fragment_goal_search, container, false);
        final View inflatedCopy = inflated;
        expListView = (ExpandableListView) inflated.findViewById(R.id.laptop_list);
        //final ExpandableListAdapter expListAdapter = new CustomExpandableAdapter(
        //        this.getActivity(), groupList, laptopCollection);
        final ExpandableListAdapter expListAdapter = new CustomExpandableAdapter(
                this.getActivity(), sessionStringList, sessionCollection);
        expListView.setAdapter(expListAdapter);

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



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void createGroupList() {
        sessionList = dbHelper.retrieveAllRunningSessions();

        List<RunningSession> allSessions = dbHelper.retrieveAllRunningSessions();
        for (RunningSession sess: allSessions)
        {

        }


        sessionStringList = new ArrayList<String>();
        for (RunningSession sess : sessionList) {
            sessionStringList.add("Workout Session " + sess.getId());
        }

        groupList = new ArrayList<String>();
        groupList.add("Running Goal 1");
    }

    private void createCollection() {
        String[][] allChildArrays = new String[sessionList.size()][5];
        // preparing goals collection
        int counter = 0;
        for (RunningSession sess: sessionList)
        {
            allChildArrays[counter] = runningSessionToStringArray(sess);
            counter++;
        }

        sessionCollection = new LinkedHashMap<String, List<String>>();

        for (String sessionName : sessionStringList) {
            if (sessionName.equals("Workout Session 1")) {
                loadGoalChildInfo(allChildArrays[0]);
            }
            else if (sessionName.equals("Workout Session 2")) {
                loadGoalChildInfo(allChildArrays[1]);
            }
            sessionCollection.put(sessionName, sessionChildList);
        }
    }

    private String[] runningSessionToStringArray(RunningSession session)
    {
        String[] retArray = new String[5];
        retArray[0] = "Session Type: "+session.getRunningGoal().getGoalType();
        retArray[1] = "Start time: "+(new Date((long)session.getStartTime()));
        retArray[2] = "End time: "+(new Date((long)session.getEndTime()));
        retArray[3] = "Distance: "+session.getDistSpeed().getDistance().getLength()
                +" "+session.getDistSpeed().getDistance().getUnits();
        retArray[4] = "Target speed: "+session.getDistSpeed().getSpeed().getValue()
                +" "+session.getDistSpeed().getSpeed().getUnits();
        return retArray;
    }

    private void loadGoalChildInfo(String[] goalChildInfo) {
        sessionChildList = new ArrayList<String>();
        for (String info : goalChildInfo)
            sessionChildList.add(info);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
