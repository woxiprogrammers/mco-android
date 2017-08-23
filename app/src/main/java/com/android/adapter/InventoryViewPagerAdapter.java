package com.android.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android.inventory.material.MaterialListFragment;

/**
 * Created by Sharvari on 23/8/17.
 */

public class InventoryViewPagerAdapter extends FragmentPagerAdapter {

    public InventoryViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position) {
            case 0:
                return new  MaterialListFragment().newInstance();
            case 1:
                return null;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 0;
    }
}
