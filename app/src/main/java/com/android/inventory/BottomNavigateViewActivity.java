package com.android.inventory;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.android.adapter.InventoryViewPagerAdapter;
import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;
import com.android.utils.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BottomNavigateViewActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottom_navigation;
    @BindView(R.id.view_pager)
    ViewPager viewPagerInventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigate_view);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            setTitle(getString(R.string.inventory));
        }
        callMaterialFragment();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void callMaterialFragment(){
        final InventoryViewPagerAdapter inventoryViewPagerAdapter=new InventoryViewPagerAdapter(getSupportFragmentManager());
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        bottom_navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_material:
                                viewPagerInventory.setCurrentItem(0);
                                break;
                            case R.id.action_assets:
                                viewPagerInventory.setCurrentItem(1);
                                break;

                        }
                        return false;
                    }
                });
        return false;
    }
}
