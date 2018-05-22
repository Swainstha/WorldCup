package com.example.swainstha.worldcup.UI.UIFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.swainstha.worldcup.Adapters.PositionListAdapter;
import com.example.swainstha.worldcup.Classes.PositionData;
import com.example.swainstha.worldcup.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PositionFragment extends Fragment {


    ListView positionListView;
    PositionListAdapter positionListAdapter;
    public PositionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_position, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        positionListView = view.findViewById(R.id.position_list_view);

        ArrayList<PositionData> a = new ArrayList<>();
        a.add(new PositionData("1","Sajan Amatya","45"));
        a.add(new PositionData("2","Shovan Raj Shrestha","44"));
        a.add(new PositionData("3","Sushil Khadka","43"));
        a.add(new PositionData("4","Swain Shrestha","42"));
        a.add(new PositionData("5","Nischal Rai","41"));
        a.add(new PositionData("6","Sizu karmacharya","41"));
        a.add(new PositionData("7","Anjal Lakhey","40"));

        positionListAdapter = new PositionListAdapter(this.getContext(), a);

        // Add a header to the ListView
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.position_list,positionListView,false);
        positionListView.addHeaderView(header);

        positionListView.setAdapter(positionListAdapter);

    }

}
