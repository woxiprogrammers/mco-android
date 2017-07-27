package com.android.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.android.adapter.TaskSelectionRvAdapter;
import com.android.constro360.NewActivity;
import com.android.constro360.R;
import com.android.models.AssignedTaskItem;
import com.android.peticash.PetiCashListActivity;
import com.android.utils.BaseActivity;
import com.android.utils.RecyclerItemClickListener;

import java.util.ArrayList;

public class DashBoardActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Context mContext;
    private RecyclerView mRvTaskSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        collapsingToolbar.setTitle("Kunal Aspiree \nBalewadi \nKunal Builders");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Kunal Aspiree, Balewadi");
        setSupportActionBar(toolbar);
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //Calling function to initialize required views.
        initializeViews();
    }

    /**
     * <b>private void initializeViews()</b>
     * <p>This function is used to initialize required views.</p>
     * Created by - Rohit
     */
    private void initializeViews() {
        mContext = DashBoardActivity.this;
        mRvTaskSelection = (RecyclerView) findViewById(R.id.rv_task_selection);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        ArrayList<AssignedTaskItem> mArrAssignedTaskItem = new ArrayList<>();
        mArrAssignedTaskItem = getDummyData();
        TaskSelectionRvAdapter selectionRvAdapter = new TaskSelectionRvAdapter(mArrAssignedTaskItem);
        mRvTaskSelection.setLayoutManager(layoutManager);
        mRvTaskSelection.setAdapter(selectionRvAdapter);
        mRvTaskSelection.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext, mRvTaskSelection, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (position == 2) {
                            startActivity(new Intent(mContext, PetiCashListActivity.class));
                        } else {
                            startActivity(new Intent(mContext, NewActivity.class));
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
//                        Toast.makeText(mContext, "Item Long Clicked", Toast.LENGTH_SHORT).show();
                    }
                })
        );
    }

    private ArrayList<AssignedTaskItem> getDummyData() {
        ArrayList<AssignedTaskItem> mArrAssignedTaskItem = new ArrayList<>();
        AssignedTaskItem assignedTaskItem;
        //New
        assignedTaskItem = new AssignedTaskItem();
        assignedTaskItem.setStrName("Purchase");
        assignedTaskItem.setStrDescription("Create and manage purchases");
        mArrAssignedTaskItem.add(assignedTaskItem);
        //New
        assignedTaskItem = new AssignedTaskItem();
        assignedTaskItem.setStrName("Inventory");
        assignedTaskItem.setStrDescription("Create and manage inventories");
        mArrAssignedTaskItem.add(assignedTaskItem);
        //New
        assignedTaskItem = new AssignedTaskItem();
        assignedTaskItem.setStrName("Peticash");
        assignedTaskItem.setStrDescription("Create and manage peticash");
        mArrAssignedTaskItem.add(assignedTaskItem);
        //New
        assignedTaskItem = new AssignedTaskItem();
        assignedTaskItem.setStrName("Checklist");
        assignedTaskItem.setStrDescription("Create and manage checklists");
        mArrAssignedTaskItem.add(assignedTaskItem);
        //New
        assignedTaskItem = new AssignedTaskItem();
        assignedTaskItem.setStrName("Workforce");
        assignedTaskItem.setStrDescription("Create and manage workforce");
        //New
        assignedTaskItem = new AssignedTaskItem();
        assignedTaskItem.setStrName("Site Report 5");
        assignedTaskItem.setStrDescription("Daily Site Report 5");
        mArrAssignedTaskItem.add(assignedTaskItem);
        mArrAssignedTaskItem.add(assignedTaskItem);
        //New
        assignedTaskItem = new AssignedTaskItem();
        assignedTaskItem.setStrName("Site Report 7");
        assignedTaskItem.setStrDescription("Daily Site Report 7");
        mArrAssignedTaskItem.add(assignedTaskItem);
        //New
        assignedTaskItem = new AssignedTaskItem();
        assignedTaskItem.setStrName("Site Report 8");
        assignedTaskItem.setStrDescription("Daily Site Report 8");
        mArrAssignedTaskItem.add(assignedTaskItem);
        //New
        assignedTaskItem = new AssignedTaskItem();
        assignedTaskItem.setStrName("Site Report 9");
        assignedTaskItem.setStrDescription("Daily Site Report 9");
        mArrAssignedTaskItem.add(assignedTaskItem);
        //New
        assignedTaskItem = new AssignedTaskItem();
        assignedTaskItem.setStrName("Site Report 10");
        assignedTaskItem.setStrDescription("Daily Site Report 10");
        mArrAssignedTaskItem.add(assignedTaskItem);
        //New
        assignedTaskItem = new AssignedTaskItem();
        assignedTaskItem.setStrName("Site Report 11");
        assignedTaskItem.setStrDescription("Daily Site Report 11");
        mArrAssignedTaskItem.add(assignedTaskItem);
        //New
        assignedTaskItem = new AssignedTaskItem();
        assignedTaskItem.setStrName("Site Report 12");
        assignedTaskItem.setStrDescription("Daily Site Report 12");
        mArrAssignedTaskItem.add(assignedTaskItem);
        //New
        assignedTaskItem = new AssignedTaskItem();
        assignedTaskItem.setStrName("Site Report 13");
        assignedTaskItem.setStrDescription("Daily Site Report 13");
        mArrAssignedTaskItem.add(assignedTaskItem);
        //New
        assignedTaskItem = new AssignedTaskItem();
        assignedTaskItem.setStrName("Site Report 14");
        assignedTaskItem.setStrDescription("Daily Site Report 14");
        mArrAssignedTaskItem.add(assignedTaskItem);
        //New
        assignedTaskItem = new AssignedTaskItem();
        assignedTaskItem.setStrName("Site Report 15");
        assignedTaskItem.setStrDescription("Daily Site Report 15");
        mArrAssignedTaskItem.add(assignedTaskItem);
        return mArrAssignedTaskItem;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}