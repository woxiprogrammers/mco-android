package com.android.checklist_module;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;

public class ChecklistHomeActivity extends BaseActivity {
    private String strSubModuleTag, permissionsItemList, subModulesItemList;
    public static boolean isFromAssigned=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_activity_checklist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            strSubModuleTag = bundle.getString("subModuleTag");
            permissionsItemList = bundle.getString("permissionsItemList");
            subModulesItemList = bundle.getString("subModulesItemList");
        }
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return ChecklistList_AssignedFragment.newInstance(strSubModuleTag, permissionsItemList, subModulesItemList);
                case 1:
                    return ChecklistList_InProgressFragment.newInstance(strSubModuleTag, permissionsItemList, subModulesItemList);
                case 2:
                    return ChecklistList_ReviewFragment.newInstance(strSubModuleTag, permissionsItemList, subModulesItemList);
                case 3:
                    return ChecklistList_CompleteFragment.newInstance(strSubModuleTag, permissionsItemList, subModulesItemList);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}