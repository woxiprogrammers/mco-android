package com.android.purchase;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;
import com.android.models.purchase_request.PurchaseRequestResponse;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import io.realm.Realm;
import timber.log.Timber;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class PurchaseRequestListFragment extends Fragment implements FragmentInterface {
    private Context mContext;
    private View mParentView;

    public PurchaseRequestListFragment() {
        // Required empty public constructor
    }

    public static PurchaseRequestListFragment newInstance() {
        Bundle args = new Bundle();
        PurchaseRequestListFragment fragment = new PurchaseRequestListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void fragmentBecameVisible() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.fragment_purchase_request_list, container, false);
        //Initialize Views
        initializeViews();
        return mParentView;
    }

    /**
     * <b>private void initializeViews()</b>
     * <p>This function is used to initialize required views.</p>
     * Created by - Rohit
     */
    public void initializeViews() {
        mContext = getActivity();
        functionForGettingData();
    }

    private void functionForGettingData() {
        if (AppUtils.getInstance().checkNetworkState()) {
            //Get data from Server
            requestPrListOnline();
        } else {
            //Get data from local DB
        }
    }

    private void requestPrListOnline() {
        AndroidNetworking.get(AppURL.API_PURCHASE_REQUEST_LIST)
                .setPriority(Priority.MEDIUM)
                .setTag("requestPrListOnline")
                .build()
                .getAsObject(PurchaseRequestResponse.class, new ParsedRequestListener<PurchaseRequestResponse>() {
                    @Override
                    public void onResponse(final PurchaseRequestResponse response) {
                        Realm realm = AppUtils.getInstance().getRealmInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.copyToRealm(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    setUpPrAdapter();
                                    Timber.d("Success");
                                }
                            }, new Realm.Transaction.OnError() {
                                @Override
                                public void onError(Throwable error) {
                                    Timber.d("Error");
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
                        Timber.d(String.valueOf(anError.getErrorCode()));
                        Timber.d(String.valueOf(anError.getErrorBody()));
                    }
                });

                /*.getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.d(String.valueOf(response));
                        final String tempResp = "{\n" +
                                "  \"purchaseRequestRespData\": {\n" +
                                "    \"purchase_request_list\": [\n" +
                                "      {\n" +
                                "        \"purchase_request_id\": \"PR1516\",\n" +
                                "        \"date\": \"Monday, 22 July 2017\",\n" +
                                "        \"materials\": \"Steel, Sand, Stone, Crush, Cement\",\n" +
                                "        \"status\": \"admin_approved\"\n" +
                                "      },\n" +
                                "      {\n" +
                                "        \"purchase_request_id\": \"PR1516645\",\n" +
                                "        \"date\": \"Tuesday, 12 July 2017\",\n" +
                                "        \"materials\": \"Steel, Sand, Stone, Crush, Cement\",\n" +
                                "        \"status\": \"admin_disapproved\"\n" +
                                "      },\n" +
                                "      {\n" +
                                "        \"purchase_request_id\": \"PR151646\",\n" +
                                "        \"date\": \"Friday, 22 July 2017\",\n" +
                                "        \"materials\": \"Steel, Sand, Stone, Crush, Cement\",\n" +
                                "        \"status\": \"manager_approved\"\n" +
                                "      }\n" +
                                "    ]\n" +
                                "  },\n" +
                                "  \"next_url\": \"\",\n" +
                                "  \"message\": \"Success\"\n" +
                                "}";
                        Gson gson = new Gson();
                        final PurchaseRequestResponse purchaseRequestResponse = gson.fromJson(tempResp, PurchaseRequestResponse.class);
                        Realm realm = AppUtils.getInstance().getRealmInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.copyToRealm(purchaseRequestResponse);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    setUpPrAdapter();
                                    Timber.d("Success");
                                }
                            }, new Realm.Transaction.OnError() {
                                @Override
                                public void onError(Throwable error) {
                                    Timber.d("Error");
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
                        Timber.d(String.valueOf(anError.getErrorCode()));
                        Timber.d(String.valueOf(anError.getErrorBody()));
                    }
                });*/
    }

    private void setUpPrAdapter() {
    }
}
