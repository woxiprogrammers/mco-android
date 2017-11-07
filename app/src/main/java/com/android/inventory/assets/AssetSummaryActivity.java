package com.android.inventory.assets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.android.constro360.R;
import com.android.inventory.asset_models.AssetReadingsListResponse;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import timber.log.Timber;

public class AssetSummaryActivity extends AppCompatActivity {
    private int inventoryComponentId;
    private Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_summary);
        getSupportActionBar().setTitle("Assets Summary");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundleArgs = getIntent().getExtras();
        if (bundleArgs != null) {
            inventoryComponentId = bundleArgs.getInt("inventoryComponentId");
        }
        requestAssetSummaryList();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void requestAssetSummaryList() {
        JSONObject params = new JSONObject();
        try {
            params.put("inventory_component_id", inventoryComponentId);
            params.put("date", "2017-11-04");
//            params.put("month", 11);
//            params.put("year", 2017);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Timber.d(AppURL.API_ASSET_READINGS_DAY_MONTHWISE_LIST_URL + AppUtils.getInstance().getCurrentToken());
        AndroidNetworking.post(AppURL.API_ASSET_READINGS_DAY_MONTHWISE_LIST_URL + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .setTag("requestAssetSummaryList")
                .build()
                .getAsObject(AssetReadingsListResponse.class, new ParsedRequestListener<AssetReadingsListResponse>() {
                    @Override
                    public void onResponse(final AssetReadingsListResponse response) {
                        realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    Timber.d(String.valueOf(response));
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
                        AppUtils.getInstance().logApiError(anError, "requestAssetSummaryList");
                    }
                });
    }
}
