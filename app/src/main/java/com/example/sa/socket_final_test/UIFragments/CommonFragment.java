package com.example.sa.socket_final_test.UIFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sa.socket_final_test.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommonFragment extends Fragment {


    public CommonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_common, container, false);

        return rootView;
    }

}
