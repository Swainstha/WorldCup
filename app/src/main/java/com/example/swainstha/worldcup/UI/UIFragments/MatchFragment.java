package com.example.swainstha.worldcup.UI.UIFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.swainstha.worldcup.Adapters.MatchListAdapter;
import com.example.swainstha.worldcup.Classes.MatchData;
import com.example.swainstha.worldcup.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatchFragment extends Fragment {


    MatchListAdapter matchListAdapter;
    ListView matchListview;
    public MatchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_match, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        matchListview= view.findViewById(R.id.match_list_view);
        matchListAdapter = new MatchListAdapter(this.getContext());

        for (int i = 1; i < 10; i++) {
            matchListAdapter.addItem(new MatchData("Chelsea","2","1","MU"));
            if (i % 4 == 0) {
                matchListAdapter.addSectionHeaderItem(new MatchData("Group 1"));
            }
        }

        matchListview.setAdapter(matchListAdapter);
    }

}
