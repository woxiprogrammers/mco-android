package com.android.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.android.adapter.ModulesAdapter;
import com.android.constro360.R;
import com.android.login_mvp.LoginActivity;
import com.android.models.AssignedTaskItem;
import com.android.models.LoginResponseData;
import com.android.models.ModulesItem;
import com.android.utils.AppConstants;
import com.android.utils.AppUtils;
import com.android.utils.BaseActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import timber.log.Timber;

public class DashBoardActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Context mContext;
    private RecyclerView mRvTaskSelection;
    private Realm realm;
    private OrderedRealmCollection<ModulesItem> modulesItemOrderedRealmCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        collapsingToolbar.setTitle("Kunal Aspiree \nBalewadi \nKunal Builders");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Kunal Aspiree, Balewadi");
        setSupportActionBar(toolbar);
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
        realm = Realm.getDefaultInstance();
        modulesItemOrderedRealmCollection = realm.where(LoginResponseData.class).findFirst().getModules();
        ModulesAdapter modulesAdapter = new ModulesAdapter(modulesItemOrderedRealmCollection);
        mRvTaskSelection.setLayoutManager(new LinearLayoutManager(mContext));
        mRvTaskSelection.setAdapter(modulesAdapter);
        mRvTaskSelection.setHasFixedSize(true);
        modulesAdapter.setOnItemClickListener(new ModulesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int modulePosition) {
                int subModuleIndex = itemView.getId();
                String strSubModuleName = modulesItemOrderedRealmCollection.get(modulePosition).getSubModules().get(subModuleIndex).getSubModuleName();
                Toast.makeText(mContext, "Hi: " + strSubModuleName + " : " + modulePosition + " - " + subModuleIndex, Toast.LENGTH_SHORT).show();
                HashMap<String, String> aclKeyValuePair = retrieveAclKeyValueFromLocal();
                Timber.d(aclKeyValuePair.get("create_purchase_request"));
//                String string = "com.android.constro360.NewActivity";
                Intent intent = new Intent();
                intent.setClassName(getApplicationContext(), aclKeyValuePair.get("create_purchase_request"));
                startActivity(intent);
            }
        });
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.actionLogout:
                logoutAndClearAllData();
                break;
            case R.id.actionSetting:
            case R.id.actionAbout:
            case R.id.actionProfile:
                Toast.makeText(mContext, "In Progress", Toast.LENGTH_SHORT).show();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutAndClearAllData() {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.deleteAll();
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
                AppUtils.getInstance().put(AppConstants.PREFS_IS_LOGGED_IN, false);
                Intent intentLogin = new Intent(mContext, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                startActivity(intentLogin);
            }
        }
    }

    /*
     * It is good practice to null the reference from the view to the adapter when it is no longer needed.
     * Because the <code>RealmRecyclerViewAdapter</code> registers itself as a <code>RealmResult.ChangeListener</code>
     * the view may still be reachable if anybody is still holding a reference to the <code>RealmResult>.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRvTaskSelection.setAdapter(null);
        realm.close();
    }

    private HashMap<String, String> retrieveAclKeyValueFromLocal() {
        Gson gson = new Gson();
        String hashMapString = AppUtils.getInstance().getString("aclKeyValuePair", "");
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        HashMap<String, String> aclKeyValuePair = gson.fromJson(hashMapString, type);
        Timber.d(String.valueOf(aclKeyValuePair));
        return aclKeyValuePair;
    }
}
