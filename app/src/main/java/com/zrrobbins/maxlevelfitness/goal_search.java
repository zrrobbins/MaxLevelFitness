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
import android.widget.ListView;

import com.zrrobbins.maxlevelfitness.Abstracts.Goal;
import com.zrrobbins.maxlevelfitness.Abstracts.GoalType;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link goal_search.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link goal_search# newInstance} factory method to
 * create an instance of this fragment.
 */
public class goal_search extends Fragment {
    //// TODO: Rename parameter arguments, choose names that match
    //// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

   // // TODO: Rename and change types of parameters
   // private String mParam1;
   // private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static final String ARG_PAGE = "page";

    private ListView lv;
    ArrayAdapter<String> adapter;
    EditText searchInput;
    ArrayList<Goal> goalList;
    //ArrayList<HashMap<String,String>> goalList;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param //param1 Parameter 1.
     * @param //param2 Parameter 2.
     * @return A new instance of fragment goal_search.

    // TODO: Rename and change types and number of parameters
    public static goal_search newInstance(String param1, String param2) {
        goal_search fragment = new goal_search();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
*/

    public static goal_search create(int pageNumber)
    {

        goal_search fragment = new goal_search();
        Bundle args = new Bundle();
        System.out.println("page number: " + pageNumber);
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public goal_search() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private void fetchGoals() {
        goalList = new ArrayList<Goal>();
        /*
        goalList.add(new Goal("Running Goal 1", GoalType.RUNNING));
        goalList.add(new Goal("Walking Goal 1", GoalType.WALKING));
        goalList.add(new Goal("Gym Visits Goal 1", GoalType.GYM_VISITS));
        goalList.add(new Goal("Running Goal 2", GoalType.RUNNING));*/
    }

    private ArrayList<String> fetchGoalNames() {
        ArrayList<String> goalNameList = new ArrayList<String>();
        for(Goal g : goalList) {
            goalNameList.add(Integer.toString(g.getGoalID()));
        }
        return goalNameList;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflated =  inflater.inflate(R.layout.fragment_goal_search, container, false);

        fetchGoals();

        ArrayList<String> goalNames = fetchGoalNames();

        lv = (ListView) inflated.findViewById(R.id.goalListView);
        searchInput = (EditText) inflated.findViewById(R.id.searchFragmentSearchBar);

        // Add items to list view
        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                R.layout.group_item, R.id.goal_name, goalNames);
        lv.setAdapter(adapter);

        searchInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                goal_search.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
