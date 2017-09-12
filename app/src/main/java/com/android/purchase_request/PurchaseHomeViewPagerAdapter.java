package com.android.purchase_request;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
class PurchaseHomeViewPagerAdapter extends FragmentStatePagerAdapter {
    private String[] arrTabTitles = {
            "Purchase \nRequest", "Purchase \nOrder", "Purchase \nBill"
    };

    PurchaseHomeViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return arrTabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return PurchaseRequestListFragment.newInstance();
            case 1:
                return PurchaseOrderListFragment.newInstance();
            case 2:
                return PurchaseBillListFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return arrTabTitles[position];
    }
}

