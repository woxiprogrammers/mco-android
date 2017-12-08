package com.android.checklisthome;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class CheckListActionActivity extends BaseActivity {
    @BindView(R.id.frameLayoutChecklistAction)
    FrameLayout frameLayoutChecklistAction;
    private int projectSiteUserChecklistAssignmentId;
    private int projectSiteChecklistId;
    private String isFromState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_checklist_action_activity);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Checklist Details");
        }
        Bundle bundleExtras = getIntent().getExtras();
        if (bundleExtras != null) {
            projectSiteUserChecklistAssignmentId = bundleExtras.getInt("projectSiteUserChecklistAssignmentId");
            projectSiteChecklistId = Integer.parseInt(bundleExtras.getString("projectSiteChecklistId"));
            isFromState = bundleExtras.getString("isFromState");
            CheckListTitleFragment checkListTitleFragment = CheckListTitleFragment.newInstance(projectSiteUserChecklistAssignmentId, projectSiteChecklistId, isFromState);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.frameLayoutChecklistAction, checkListTitleFragment, "checkListTitleFragment");
            fragmentTransaction.commit();
        } else {
            Timber.d("bundleExtras: null");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getCheckListVerificationFragment(int projectSiteUserCheckpointId) {
        CheckListVerificationFragment checkListVerificationFragment = CheckListVerificationFragment.newInstance(projectSiteUserCheckpointId, isFromState);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutChecklistAction, checkListVerificationFragment, "checkListVerificationFragment");
        fragmentTransaction.addToBackStack("checkListTitleFragment");
        fragmentTransaction.commit();
    }
}
