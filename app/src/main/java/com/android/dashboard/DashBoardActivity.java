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

import com.android.constro360.BuildConfig;
import com.android.constro360.R;
import com.android.login_mvp.LoginActivity;
import com.android.models.login_acl.LoginResponseData;
import com.android.models.login_acl.ModulesItem;
import com.android.models.login_acl.SubModulesItem;
import com.android.utils.AppConstants;
import com.android.utils.AppUtils;
import com.android.utils.BaseActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

import de.jonasrottmann.realmbrowser.RealmBrowser;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import timber.log.Timber;

public class DashBoardActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Context mContext;
    private RecyclerView mRvTaskSelection;
    private OrderedRealmCollection<ModulesItem> modulesItemOrderedRealmCollection;
    private Realm realm;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        if (BuildConfig.DEBUG) {
            //Start Realm Browser
            RealmBrowser.showRealmFilesNotification(getApplicationContext());
        }
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

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
                SubModulesItem subModulesItem = modulesItemOrderedRealmCollection.get(modulePosition).getSubModules().get(subModuleIndex);
                if (BuildConfig.DEBUG) {
                    String strSubModuleTag = subModulesItem.getSubModuleTag();
                    Toast.makeText(mContext, "Hi: " + strSubModuleTag + " : " + modulePosition + " - " + subModuleIndex, Toast.LENGTH_SHORT).show();
                }
                startCorrespondingAclActivity(subModulesItem);
            }
        });
    }

    private void startCorrespondingAclActivity(SubModulesItem subModulesItem) {
        HashMap<String, String> aclKeyValuePair = retrieveAclKeyValueFromLocal();
        Intent intent = new Intent();
//        Gson gson = new Gson();
//        String strPermissions = gson.toJson(subModulesItem.getPermissions().toArray());
//        Bundle bundleExtras = new Bundle();
//        bundleExtras.putSerializable("strPermissions", strPermissions);
//        intent.putExtras(bundleExtras);
        String strClassName = aclKeyValuePair.get(subModulesItem.getSubModuleTag());
        Timber.d("Activity Started: " + strClassName);
        intent.setClassName(getApplicationContext(), strClassName);
        startActivity(intent);
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

    private void logoutAndClearAllData() {
        realm = Realm.getDefaultInstance();
        try {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.deleteAll();
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    AppUtils.getInstance().put(AppConstants.PREFS_IS_LOGGED_IN, false);
                    Intent intentLogin = new Intent(mContext, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();
                    startActivity(intentLogin);
                }
            });
        } catch (Exception e) {
            Timber.d(e.getMessage());
        }
    }

    private HashMap<String, String> retrieveAclKeyValueFromLocal() {
        Gson gson = new Gson();
        String hashMapString = AppUtils.getInstance().getString("aclKeyValuePair", "");
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        return gson.fromJson(hashMapString, type);
    }
}
