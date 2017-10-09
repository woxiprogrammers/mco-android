package com.android.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.BaseActivity;
import com.android.constro360.BuildConfig;
import com.android.constro360.R;
import com.android.login_mvp.LoginActivity;
import com.android.models.login_acl.LoginResponseData;
import com.android.models.login_acl.ModulesItem;
import com.android.models.login_acl.PermissionsItem;
import com.android.models.login_acl.ProjectsItem;
import com.android.models.login_acl.SubModulesItem;
import com.android.utils.AppConstants;
import com.android.utils.AppUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.jonasrottmann.realmbrowser.RealmBrowser;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import timber.log.Timber;

public class DashBoardActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.builderName)
    TextView projectSiteName;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.rv_task_selection)
    RecyclerView rvTaskSelection;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private Context mContext;
    private RecyclerView mRvTaskSelection;
    private OrderedRealmCollection<ModulesItem> modulesItemOrderedRealmCollection;
    private Realm realm;
    private Spinner projectSpinner;
    private TextView userName;
    private RealmResults<ProjectsItem> projectsItemRealmResults;
    private List<ProjectsItem> projectsItemList;

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
        ButterKnife.bind(this);
        if (BuildConfig.DEBUG) {
            //Start Realm Browser
            RealmBrowser.showRealmFilesNotification(getApplicationContext());
        }
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("");
//        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);
        //Calling function to initialize required views.
        initializeViews();
        getSiteName();
        setSpinnerListener();
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
        View headerLayout = navView.inflateHeaderView(R.layout.nav_header_dash_board);
        projectSpinner = headerLayout.findViewById(R.id.project_spinner);
        userName = headerLayout.findViewById(R.id.userName);
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
                /*if (BuildConfig.DEBUG) {
                    String strSubModuleTag = subModulesItem.getSubModuleTag();
                    Toast.makeText(mContext, "Hi: " + strSubModuleTag + " : " + modulePosition + " - " + subModuleIndex, Toast.LENGTH_SHORT).show();
                }*/
                startCorrespondingAclActivity(subModulesItem);
            }
        });
        projectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(mContext,"Only  " + projectSpinner.getSelectedItem().toString()  + "can be selected",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    toolbar.setTitle("Kunal Aspiree, Balewadi");
                    isShow = true;
                } else if (isShow) {
                    toolbar.setTitle("");
                    isShow = false;
                }
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
        Realm realm = Realm.getDefaultInstance();
        List<PermissionsItem> permissionsItemList = realm.copyFromRealm(subModulesItem.getPermissions());
        intent.putExtra("permissionsItemList", new Gson().toJson(permissionsItemList));
        intent.putExtra("subModuleTag", subModulesItem.getSubModuleTag());
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

    private void getSiteName() {
        realm = Realm.getDefaultInstance();
        ProjectsItem projectsItem = realm.where(ProjectsItem.class).equalTo("project_id", 2).findFirst();
        if (projectsItem != null) {
            projectSiteName.setText(projectsItem.getClient_company_name());
        }
    }

    private void setSpinnerListener() {
        realm = Realm.getDefaultInstance();
        projectsItemRealmResults = realm.where(ProjectsItem.class).findAll();
        setUpSpinnerAdapter(projectsItemRealmResults);
        if (projectsItemRealmResults != null) {
            Timber.d("availableUsersRealmResults change listener added.");
            projectsItemRealmResults.addChangeListener(new RealmChangeListener<RealmResults<ProjectsItem>>() {
                @Override
                public void onChange(RealmResults<ProjectsItem> availableUsersItems) {
                    Timber.d("Size of availableUsersItems: " + String.valueOf(availableUsersItems.size()));
                    setUpSpinnerAdapter(projectsItemRealmResults);
                }
            });
        } else {
            AppUtils.getInstance().showOfflineMessage("PurchaseMaterialListActivity");
        }
    }

    private void setUpSpinnerAdapter(RealmResults<ProjectsItem> availableUsersItems) {
        projectsItemList = realm.copyFromRealm(availableUsersItems);
        ArrayList<String> arrayOfUsers = new ArrayList<String>();
        for (ProjectsItem currentUser : projectsItemList) {
            String strMaterialName = currentUser.getProjectName();
            arrayOfUsers.add(strMaterialName);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrayOfUsers);
        if (arrayAdapter != null) {
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            projectSpinner.setAdapter(arrayAdapter);
        }
        userName.setText("Test User");
    }
}
