package com.android.purchase_module.purchase_request;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.constro360.R;
import com.android.utils.FragmentInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class PurchaseHistoryFragment extends Fragment implements FragmentInterface {
    public PurchaseHistoryFragment() {
        // Required empty public constructor
    }

    public static PurchaseHistoryFragment newInstance() {
        Bundle args = new Bundle();
        PurchaseHistoryFragment fragment = new PurchaseHistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_purchase_history, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.action_approve);
        item.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void fragmentBecameVisible() {
    }
}
