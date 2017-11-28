package com.android.checklisthome;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckListActionActivity extends BaseActivity {
    @BindView(R.id.frameLayoutChecklistAction)
    FrameLayout frameLayoutChecklistAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_checklist_action_activity);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Checklist Details");
        }
        getCHeckListFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getCHeckListFragment() {
        CheckListTitleFragment checkListTitleFragment = CheckListTitleFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frameLayoutChecklistAction, checkListTitleFragment, "checkListTitleFragment");
        fragmentTransaction.commit();
    }

    public void getChckListVerificationFragment(int id) {
        FragmentCheckListVerification fragmentCheckListVerification = FragmentCheckListVerification.newInstance(id);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutChecklistAction, fragmentCheckListVerification, "FragmentCheckListVerification");
        fragmentTransaction.addToBackStack("checkListTitleFragment");
        fragmentTransaction.commit();
    }
}
