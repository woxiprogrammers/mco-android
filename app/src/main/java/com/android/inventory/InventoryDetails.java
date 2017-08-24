package com.android.inventory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.adapter.InventoryViewPagerAdapter;
import com.android.adapter.SelectedMaterialListAdapter;
import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;
import com.android.inventory.material.AssetListFragment;
import com.android.inventory.material.InventoryDetailsMoveFragment;
import com.android.inventory.material.MaterialHistoryFragment;
import com.android.inventory.material.MaterialListFragment;
import com.android.models.inventory.MaterialListItem;
import com.android.utils.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmList;
import timber.log.Timber;

public class InventoryDetails extends BaseActivity {




    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottom_navigation;

    @BindView(R.id.view_pager)
    ViewPager viewPagerInventory;

    MenuItem prevMenuItem;
    private ArrayList<Integer> arrayList = new ArrayList<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_view);
        ButterKnife.bind(this);
        Intent intent =getIntent();
        if(intent !=null) {
            arrayList = intent.getIntegerArrayListExtra("Array");
        }
        callFragment();
        viewPagerInventory.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottom_navigation.getMenu().getItem(0).setChecked(false);
                }
                bottom_navigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottom_navigation.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_move:
                        viewPagerInventory.setCurrentItem(0);
                        break;
                    case R.id.action_history:
                        viewPagerInventory.setCurrentItem(1);
                        break;

                }
                return false;
            }
        });
    }

    private void callFragment(){
        final InventoryDetailsViewPagerAdapter inventoryViewPagerAdapter=new InventoryDetailsViewPagerAdapter(getSupportFragmentManager());
        viewPagerInventory.setAdapter(inventoryViewPagerAdapter);
        viewPagerInventory.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                FragmentInterface fragment = (FragmentInterface) inventoryViewPagerAdapter.instantiateItem(viewPagerInventory, position);
                if (fragment != null) {
                    fragment.fragmentBecameVisible();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    private class InventoryDetailsViewPagerAdapter extends FragmentPagerAdapter {
        private String[] arrBottomTitle={"Bottom1","Bottom2"};

        public InventoryDetailsViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return InventoryDetailsMoveFragment.newInstance(arrayList);
                case 1:
                    return MaterialHistoryFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return arrBottomTitle.length;
        }
    }
}
