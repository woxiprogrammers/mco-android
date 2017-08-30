package com.android.inventory;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android.inventory.assets.AssetListFragment;
import com.android.inventory.material.MaterialListFragment;

/**
 * Created by Sharvari on 23/8/17.
 */

public class InventoryViewPagerAdapter extends FragmentPagerAdapter {

    private String[] arrBottomTitle={"Bottom1","Bottom2"};
    public InventoryViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position) {
            case 0:
                return MaterialListFragment.newInstance();
            case 1:
                return AssetListFragment.newInstance();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return arrBottomTitle.length;
    }
}
