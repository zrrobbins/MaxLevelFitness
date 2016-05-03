package com.zrrobbins.maxlevelfitness;

import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Fiona on 5/1/2016.
 */
public class LeaderboardDisplay extends Fragment {
    public static final String ARG_PAGE = "page";


    public LeaderboardDisplay() {
        // Required empty public constructor
    }

    public static LeaderboardDisplay newInstance(String param1, String param2) {
        LeaderboardDisplay fragment = new LeaderboardDisplay();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static LeaderboardDisplay create(int pageNumber)
    {
        LeaderboardDisplay fragment = new LeaderboardDisplay();
        Bundle args = new Bundle();
        System.out.println("page number: " + pageNumber);
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflated =  inflater.inflate(R.layout.leaderboard_page, container, false);

        return inflated;
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

