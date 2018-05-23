package com.android.dashboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.dashboard.notification_model.NotificationCountSiteData;
import com.android.dashboard.notification_model.NotificationCountSiteResponse;
import com.android.dashboard.notification_model.ProjectsNotificationCountItem;
import com.android.firebase.counts_model.NotificationCountData;
import com.android.firebase.counts_model.NotificationCountResponse;
import com.android.login_mvp.LoginActivity;
import com.android.login_mvp.login_model.LoginResponseData;
import com.android.login_mvp.login_model.ModulesItem;
import com.android.login_mvp.login_model.PermissionsItem;
import com.android.login_mvp.login_model.ProjectsItem;
import com.android.login_mvp.login_model.SubModulesItem;
import com.android.utils.AppConstants;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import timber.log.Timber;

public class DashBoardActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.clientName)
    TextView mTextViewClientName;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.rv_task_selection)
    RecyclerView mRvTaskSelection;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.projectName)
    TextView mProjectName;
    @BindView(R.id.cart_badge)
    TextView cartBadge;
    @BindView(R.id.imageViewSiteCount)
    FrameLayout imageViewSiteCount;
    @BindView(R.id.imageViewToRefresh)
    ImageView imageViewToRefresh;
    @BindView(R.id.mainCoordinatorLayout)
    CoordinatorLayout mainCoordinatorLayout;
    private Context mContext;
    private OrderedRealmCollection<ModulesItem> modulesItemOrderedRealmCollection;
    private Realm realm;
    private Spinner projectSpinner;
    private TextView userName;
    private String strProjectName = "";
    private RealmResults<ProjectsItem> projectsItemRealmResults;
    private AlertDialog alert_Dialog;
    private TextView textViewSites, textViewSitesCount;
    private View dialogView;
    private View inflatedView = null;
    private LinearLayout linearLayoutOfSite;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutAndClearAllData() {
        realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.deleteAll();
                    AppUtils.getInstance().put(AppConstants.PREFS_IS_LOGGED_IN, false);
                    Intent intentLogin = new Intent(mContext, LoginActivity.class);
                    startActivity(intentLogin);
                    finish();
                }
            });
        } catch (Exception e) {
            Timber.d(e.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        ButterKnife.bind(this);
//        if (BuildConfig.DEBUG) {
        //Start Realm Browser
//        RealmBrowser.showRealmFilesNotification(getApplicationContext());
//        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);
        //Calling function to initialize required views.
        initializeViews();
        setUpDrawerData();
        if (AppUtils.getInstance().checkNetworkState()) {
            getCount();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }
        projectsItemRealmResults.removeAllChangeListeners();
    }

    /**
     * <b>private void initializeViews()</b>
     * <p>This function is used to initialize required views.</p>
     * Created by - Rohit
     */
    private void initializeViews() {
        mContext = DashBoardActivity.this;
        AppUtils.getInstance().initializeProgressBar(mainCoordinatorLayout,mContext);
        View headerLayout = navView.inflateHeaderView(R.layout.nav_header_dash_board);
        projectSpinner = headerLayout.findViewById(R.id.project_spinner);
        userName = headerLayout.findViewById(R.id.userName);
    }

    private void setUpDashboardAdapterData() {
        realm = Realm.getDefaultInstance();
        LoginResponseData loginResponseData = realm.where(LoginResponseData.class).findFirst();
        if (loginResponseData != null) {
            modulesItemOrderedRealmCollection = loginResponseData.getModules();
            NotificationCountData notificationCountData = realm.where(NotificationCountData.class)
                    .equalTo("currentSiteId", AppUtils.getInstance().getCurrentSiteId())
                    .findFirst();
            ModulesAdapter modulesAdapter = new ModulesAdapter(modulesItemOrderedRealmCollection, notificationCountData);
            mRvTaskSelection.setLayoutManager(new LinearLayoutManager(mContext));
            mRvTaskSelection.setAdapter(modulesAdapter);
            mRvTaskSelection.setHasFixedSize(true);
            modulesAdapter.setOnItemClickListener(new ModulesAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, int modulePosition) {
                    int subModuleIndex = itemView.getId();
                    RealmList<SubModulesItem> subModulesItemRealmList = modulesItemOrderedRealmCollection.get(modulePosition).getSubModules();
                    SubModulesItem subModulesItem = subModulesItemRealmList.get(subModuleIndex);
                    startCorrespondingAclActivity(subModulesItem, subModulesItemRealmList);
                }
            });
        }
    }

    private void setUpDrawerData() {
        realm = Realm.getDefaultInstance();
        LoginResponseData loginResponseData = realm.where(LoginResponseData.class).findFirst();
        userName.setText(loginResponseData.getFirstName() + " " + loginResponseData.getLastName());
        projectsItemRealmResults = realm.where(ProjectsItem.class).findAll();
        setUpProjectsSpinnerAdapter(projectsItemRealmResults);
        projectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int selectedId, long l) {
                setUpStaticValues(selectedId);
                if (AppUtils.getInstance().checkNetworkState()) {
                    requestNotificationCountData();
                } else {
                    AppUtils.getInstance().showOfflineMessage("DashBoardActivity");
                    setUpDashboardAdapterData();
                }
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
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
                    toolbar.setTitle(strProjectName);
                    isShow = true;
                } else if (isShow) {
                    toolbar.setTitle("");
                    isShow = false;
                }
            }
        });
    }

    private void setUpStaticValues(int selectedId) {
        ProjectsItem projectsItem = projectsItemRealmResults.get(selectedId);
        if (projectsItem != null) {
            mTextViewClientName.setText(projectsItem.getClient_company_name());
            int projectId = projectsItem.getProject_id();
            AppUtils.getInstance().put(getString(R.string.key_project_id), projectId);
            Timber.i("Current Site ID: " + AppUtils.getInstance().getInt(getString(R.string.key_project_id), -1));
            strProjectName = projectsItem.getProject_name();
            String strClientCompanyName = projectsItem.getClient_company_name();
            mProjectName.setText(strProjectName);
            mTextViewClientName.setText(strClientCompanyName);
        }
    }

    private void requestNotificationCountData() {
        JSONObject params = new JSONObject();
        try {
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
            Timber.d(String.valueOf(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_NOTIFICATION_DASHBOARD_COUNTS
                + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .setTag("requestNotificationCountData")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsObject(NotificationCountResponse.class,
                        new ParsedRequestListener<NotificationCountResponse>() {
                            @Override
                            public void onResponse(final NotificationCountResponse response) {
                                Timber.i("NotificationCountResponse: " + String.valueOf(response));
                                realm = Realm.getDefaultInstance();
                                try {
                                    realm.executeTransactionAsync(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            realm.delete(NotificationCountData.class);
                                            realm.insertOrUpdate(response);
                                        }
                                    }, new Realm.Transaction.OnSuccess() {
                                        @Override
                                        public void onSuccess() {
                                            setUpDashboardAdapterData();
                                        }
                                    }, new Realm.Transaction.OnError() {
                                        @Override
                                        public void onError(Throwable error) {
                                            AppUtils.getInstance().logRealmExecutionError(error);
                                        }
                                    });
                                } finally {
                                    if (realm != null) {
                                        realm.close();
                                    }
                                }
                            }

                            @Override
                            public void onError(ANError error) {
                                AppUtils.getInstance().logApiError(error, "requestNotificationCountData");
                            }
                        });
    }

    private void startCorrespondingAclActivity(SubModulesItem subModulesItem, RealmList<SubModulesItem> subModulesItemRealmList) {
        HashMap<String, String> aclKeyValuePair = retrieveAclKeyValueFromLocal();
        Intent intent = new Intent();
        String strClassName = aclKeyValuePair.get(subModulesItem.getSubModuleTag());
        Timber.d("Activity Started: " + strClassName);
        Realm realm = Realm.getDefaultInstance();
        List<PermissionsItem> permissionsItemList = realm.copyFromRealm(subModulesItem.getPermissions());
        intent.putExtra("permissionsItemList", new Gson().toJson(permissionsItemList));
        List<SubModulesItem> subModulesItemList = realm.copyFromRealm(subModulesItemRealmList);
        intent.putExtra("subModulesItemList", new Gson().toJson(subModulesItemList));
        intent.putExtra("subModuleTag", subModulesItem.getSubModuleTag());
        if (strClassName != null) {
            intent.setClassName(getApplicationContext(), strClassName);
            startActivity(intent);
        }
    }

    private void setUpProjectsSpinnerAdapter(RealmResults<ProjectsItem> projectsItems) {
        List<ProjectsItem> projectsItemList = realm.copyFromRealm(projectsItems);
        ArrayList<String> arrayOfUsers = new ArrayList<String>();
        for (ProjectsItem currentUser : projectsItemList) {
            String strProjectSiteName = currentUser.getProjectSiteName();
            arrayOfUsers.add(strProjectSiteName);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_item, arrayOfUsers);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        projectSpinner.setAdapter(arrayAdapter);
        if (AppUtils.getInstance().getCurrentSiteId() != -1) {
            int currentSiteId = AppUtils.getInstance().getCurrentSiteId();
            ProjectsItem projectsItem = realm.where(ProjectsItem.class).equalTo("project_id", currentSiteId).findFirst();
            if (projectsItem != null) {
//                String projectSiteName = projectsItem.getProjectSiteName();
//                int selectedIndex = arrayOfUsers.indexOf(projectSiteName);
                int selectedIndex = projectsItems.indexOf(projectsItem);
                projectSpinner.setSelection(selectedIndex);
            }
        }
    }

    private HashMap<String, String> retrieveAclKeyValueFromLocal() {
        Gson gson = new Gson();
        String hashMapString = AppUtils.getInstance().getString("aclKeyValuePair", "");
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        return gson.fromJson(hashMapString, type);
    }

    private void inflateLayoutForSites() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.layout_linear, null);
        alertDialogBuilder.setView(dialogView);
        LinearLayout linearLayout = dialogView.findViewById(R.id.linearLayout);
        alert_Dialog = alertDialogBuilder.create();
        realm = Realm.getDefaultInstance();
        RealmResults<ProjectsNotificationCountItem> siteCountListItemRealmResults = realm.where(ProjectsNotificationCountItem.class).findAll();
        for (int i = 0; i < siteCountListItemRealmResults.size(); i++) {
            final ProjectsNotificationCountItem siteCountListItem = siteCountListItemRealmResults.get(i);
            inflatedView = LayoutInflater.from(mContext).inflate(R.layout.inflate_layout_assigned_sites, null);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 20, 0, 20);
            inflatedView.setLayoutParams(layoutParams);
            textViewSites = inflatedView.findViewById(R.id.textViewSites);
            textViewSitesCount = inflatedView.findViewById(R.id.textViewSitesCount);
            linearLayoutOfSite = inflatedView.findViewById(R.id.linearLayoutOfSite);
            textViewSitesCount.setText("" + siteCountListItem.getNotificationCount());
            textViewSites.setText(siteCountListItem.getProjectSiteName());
            if (AppUtils.getInstance().getCurrentSiteId() == siteCountListItem.getProjectSiteId()) {
                textViewSites.setTextColor(ContextCompat.getColor(mContext,R.color.colorPrimaryDark));
                textViewSitesCount.setTextColor(getColor(R.color.colorPrimaryDark));
            }
            final int finalI = i;
            linearLayoutOfSite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mTextViewClientName.setText(siteCountListItem.getClientCompanyName());
                    int projectId = siteCountListItem.getProjectSiteId();
                    AppUtils.getInstance().put(getString(R.string.key_project_id), projectId);
                    Timber.i("Current Site ID: " + AppUtils.getInstance().getInt(getString(R.string.key_project_id), -1));
                    mProjectName.setText(siteCountListItem.getProjectName());
                    projectSpinner.setSelection(finalI);
                    alert_Dialog.dismiss();
                }
            });
            linearLayout.addView(inflatedView);
        }
        alert_Dialog = alertDialogBuilder.create();
        alert_Dialog.show();
    }

    private void getCount() {
        AndroidNetworking.post(AppURL.API_SITE_COUNT_URL + AppUtils.getInstance().getCurrentToken())
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("getCount")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(NotificationCountSiteResponse.class, new ParsedRequestListener<NotificationCountSiteResponse>() {
                    @Override
                    public void onResponse(final NotificationCountSiteResponse response) {
                        try {
                            realm = Realm.getDefaultInstance();
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(NotificationCountSiteResponse.class);
                                    realm.delete(NotificationCountSiteData.class);
                                    realm.delete(ProjectsNotificationCountItem.class);
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    AppUtils.getInstance().showProgressBar(mainCoordinatorLayout,false);
                                    int setSiteBadgeCount=0;
                                    for (int i = 0; i < response.getNotificationCountData().getProjectsNotificationCount().size(); i++) {
                                        setSiteBadgeCount = setSiteBadgeCount + response.getNotificationCountData().getProjectsNotificationCount().get(i).getNotificationCount();
                                    }
                                    cartBadge.setText(String.valueOf(setSiteBadgeCount));

                                }
                            }, new Realm.Transaction.OnError() {
                                @Override
                                public void onError(Throwable error) {
                                    AppUtils.getInstance().logRealmExecutionError(error);
                                }
                            });
                        } finally {
                            if (realm != null) {
                                realm.close();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logRealmExecutionError(anError);
                    }
                });
    }

    @OnClick(R.id.imageViewSiteCount)
    public void onViewClicked() {
        if (AppUtils.getInstance().checkNetworkState()) {
            inflateLayoutForSites();
        } else {
            AppUtils.getInstance().showOfflineMessage("DashBoardSiteSwitch");
        }
    }

    @OnClick(R.id.imageViewToRefresh)
    public void onViewClick() {
        if (AppUtils.getInstance().checkNetworkState()) {
            AppUtils.getInstance().showProgressBar(mainCoordinatorLayout,true);
            getCount();
            setUpDrawerData();
        }else {
            AppUtils.getInstance().showOfflineMessage("DashBoardActivity");
        }
    }
}