package com.zrrobbins.maxlevelfitness.ViewPager;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zrrobbins.maxlevelfitness.Abstracts.Goal;
import com.zrrobbins.maxlevelfitness.Abstracts.WorkoutSession;
import com.zrrobbins.maxlevelfitness.AddGoal;
import com.zrrobbins.maxlevelfitness.R;


/**
 * Created by zrrobbins on 4/29/16.
 */
public class GoalSessionFragment extends Fragment {
    public static final String ARG_PAGE = "page";

    private OnFragmentInteractionListener mListener;

    // The current goal session we are tracking
    WorkoutSession currentSession;
    Boolean goalBeingTracked = false;

    TextView isGoalBeingTrackedView;
    TextView goalIdView;
    TextView goalTypeView;
    TextView goalFrequencyView;
    TextView userSpeedView;

    public GoalSessionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LandingFragment.
     */
    public static GoalSessionFragment newInstance() {
        GoalSessionFragment fragment = new GoalSessionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static GoalSessionFragment create(int pageNumber)
    {
        GoalSessionFragment fragment = new GoalSessionFragment();
        Bundle args = new Bundle();
        System.out.println("page number: " + pageNumber);
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // FIXME: Idk what to put here
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inflated =  inflater.inflate(R.layout.goal_session_fragment, container, false);
        final View inflatedCopy = inflated;

        isGoalBeingTrackedView = (TextView) inflated.findViewById(R.id.isGoalBeingTracked);
        goalIdView = (TextView) inflated.findViewById(R.id.goalID);
        goalTypeView = (TextView) inflated.findViewById(R.id.goalType);
        goalFrequencyView = (TextView) inflated.findViewById(R.id.goalFrequency);
        userSpeedView = (TextView) inflated.findViewById(R.id.userSpeed);

        Button stopSessionButton = (Button)inflated.findViewById(R.id.stopSessionButton);
        stopSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSession();
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

    public void updateGoalSessionInfo(Goal goal) {
        goalBeingTracked = true;
        isGoalBeingTrackedView.setText(("A goal is being tracked!"));
        goalIdView.setText("" + goal.getGoalID());
        goalTypeView.setText("" + goal.getGoalType().toString());
        goalFrequencyView.setText("" + goal.getGoalFrequency());
    }

    public void updateRunningSpeed(String speed, String units) {
        System.out.println("-------------------Updating running speed with" + speed + " " + units);
        userSpeedView.setText(speed + " " + units);
    }

    public void stopSession() {
        goalBeingTracked = false;
        isGoalBeingTrackedView.setText(("A goal is NOT being tracked currently."));
        goalIdView.setText("");
        goalTypeView.setText("");
        goalFrequencyView.setText("");
        userSpeedView.setText("No goal being tracked");
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // Convert pixel to dip
    public int getDipsFromPixel(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    public boolean isGoalBeingTracked() {
        return goalBeingTracked;
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
