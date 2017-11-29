package com.android.drawings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.constro360.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DrawingVersionsFragment extends Fragment {

    public DrawingVersionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_drawing_comment, container, false);

    }

    public static DrawingVersionsFragment newInstance() {

        Bundle args = new Bundle();

        DrawingVersionsFragment fragment = new DrawingVersionsFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
