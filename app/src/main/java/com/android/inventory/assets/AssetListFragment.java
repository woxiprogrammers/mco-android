package com.android.inventory.assets;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssetListFragment extends Fragment implements FragmentInterface {

    private Context mContext;
    private View mParentView;
    public AssetListFragment() {
        // Required empty public constructor
    }

    public static AssetListFragment newInstance() {
        Bundle args = new Bundle();
        AssetListFragment fragment = new AssetListFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mParentView=inflater.inflate(R.layout.fragment_asset_list, container, false);
        mContext=getActivity();
        return mParentView;
    }

    @Override
    public void fragmentBecameVisible() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);//Make sure you have this line of code.
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.options_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_request_maintaianance:
                startRequestMaintainanceActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startRequestMaintainanceActivity(){
        Intent startIntent=new Intent(mContext, ActivityRequestMaintanance.class);
        startActivity(startIntent);
    }
}
