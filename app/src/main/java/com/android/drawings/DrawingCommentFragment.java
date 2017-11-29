package com.android.drawings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.constro360.R;
import com.android.inventory.assets.AssetListResponse;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class DrawingCommentFragment extends Fragment {

    @BindView(R.id.rvCommonList)
    RecyclerView rvCommonList;
    private Unbinder unbinder;
    private Realm realm;

    public DrawingCommentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_drawing_comment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    public static DrawingCommentFragment newInstance() {
        Bundle args = new Bundle();
        DrawingCommentFragment fragment = new DrawingCommentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void requestToGetComments(){
        /*final JSONObject params = new JSONObject();
        try {
            params.put("page", pageId);
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        AndroidNetworking.post(AppURL.API_DRAWING_COMMENTS_LIST + AppUtils.getInstance().getCurrentToken())
//                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .setTag("requestToGetComments")
                .build()
                .getAsObject(AssetListResponse.class, new ParsedRequestListener<AssetListResponse>() {
                    @Override
                    public void onResponse(final AssetListResponse response) {

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
                        AppUtils.getInstance().logApiError(anError, "requestAssetsListOnline");
                    }
                });
    }
}
