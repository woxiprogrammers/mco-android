package com.android.drawings;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.inventory.assets.AssetsListAdapter;
import com.android.inventory.assets.AssetsListItem;
import com.android.models.awarenessmodels.AwarenessMainCategoryResponse;
import com.android.models.awarenessmodels.MainCategoriesData;
import com.android.models.awarenessmodels.MainCategoriesItem;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import timber.log.Timber;

public class DrawingHomeActivity extends BaseActivity {

    @BindView(R.id.spinner_drawing_categories)
    Spinner spinnerDrawingCategories;
    @BindView(R.id.rv_subcatdrawing_list)
    RecyclerView rvSubcatdrawingList;

    private Realm realm;
    private Context mContext;
    private List<MainCategoriesItem> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing_home);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Drawings Management");
        }
        mContext = DrawingHomeActivity.this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestToGetCategoryData() {
        JSONObject params = new JSONObject();
        try {
            params.put("page", 0);
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_DRAWING_CATEGORY_DATA + AppUtils.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("requestToGetCategoryData")
                .build()
                .getAsObject(AwarenessMainCategoryResponse.class, new ParsedRequestListener<AwarenessMainCategoryResponse>() {
                    @Override
                    public void onResponse(final AwarenessMainCategoryResponse response) {
                        Timber.i(String.valueOf(response));
                        realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(AwarenessMainCategoryResponse.class);
                                    realm.delete(MainCategoriesData.class);
                                    realm.delete(MainCategoriesItem.class);
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    Timber.d("Success");
                                    setUpUsersSpinnerValueChangeListener();

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
                        AppUtils.getInstance().logApiError(anError, "requestUsersWithApproveAcl");
                    }
                });
    }

    private void setUpUsersSpinnerValueChangeListener() {
        realm = Realm.getDefaultInstance();
        RealmResults<MainCategoriesItem> mainCategoriesItemRealmResults = realm.where(MainCategoriesItem.class).findAll();
        setUpSpinnerAdapter(mainCategoriesItemRealmResults);
        if (mainCategoriesItemRealmResults != null) {
            mainCategoriesItemRealmResults.addChangeListener(new RealmChangeListener<RealmResults<MainCategoriesItem>>() {
                @Override
                public void onChange(RealmResults<MainCategoriesItem> availableUsersItems) {
                    setUpSpinnerAdapter(availableUsersItems);
                }
            });
        } else {
            AppUtils.getInstance().showOfflineMessage("Drawing");
        }
    }

    private void setUpSpinnerAdapter(RealmResults<MainCategoriesItem> mainCategoriesItems) {
        categoryList = realm.copyFromRealm(mainCategoriesItems);
        ArrayList<String> arrayOfUsers = new ArrayList<String>();
        for (MainCategoriesItem currentUser : categoryList) {
            String strUserName = currentUser.getName();
            arrayOfUsers.add(strUserName);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrayOfUsers);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDrawingCategories.setAdapter(arrayAdapter);
    }

    public class SubCategoryAdapter extends RealmRecyclerViewAdapter<AssetsListItem, SubCategoryAdapter.MyViewHolder> {
        private OrderedRealmCollection<AssetsListItem> assetsListItemCollection;
        private AssetsListItem assetsListItem;

        public SubCategoryAdapter(@Nullable OrderedRealmCollection<AssetsListItem> data, boolean autoUpdate, boolean updateOnModification) {
            super(data, autoUpdate, updateOnModification);
            Timber.d(String.valueOf(data));
            assetsListItemCollection = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_assets_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            assetsListItem = assetsListItemCollection.get(position);

        }

        @Override
        public long getItemId(int index) {
            return assetsListItemCollection.get(index).getId();
        }

        @Override
        public int getItemCount() {
            return assetsListItemCollection == null ? 0 : assetsListItemCollection.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {


            private MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
