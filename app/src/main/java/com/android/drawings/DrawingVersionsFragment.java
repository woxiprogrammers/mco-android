package com.android.drawings;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;
import com.android.models.drawing.CommentsListItem;
import com.android.models.drawing.DrawingVersionsResponse;
import com.android.models.drawing.VersionsItem;
import com.android.models.drawing.Versionsdata;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.RecyclerItemClickListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class DrawingVersionsFragment extends Fragment implements FragmentInterface {

    @BindView(R.id.rvVersionList)
    RecyclerView rvVersionList;
    Unbinder unbinder;
    private Realm realm;
    private Context mContext;
    private int drawingId,getSubCatId;
    private String imagePath,versionUrl;

    public DrawingVersionsFragment() {
        // Required empty public constructor
    }

    public static DrawingVersionsFragment newInstance(int drawingVersionId,int subCatId) {
        Bundle args = new Bundle();
        DrawingVersionsFragment fragment = new DrawingVersionsFragment();
        args.putInt("imageId",drawingVersionId);
        args.putInt("currentSubCatId",subCatId);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_version_comment, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext=getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            drawingId = bundle.getInt("imageId");
            getSubCatId=bundle.getInt("currentSubCatId");
        }
        requestToGetVersionList();
        return view;

    }


    @Override
    public void fragmentBecameVisible() {

    }

    public void requestToGetVersionList() {
        final JSONObject params = new JSONObject();
        try {
            params.put("image_id", drawingId);
            params.put("project_site_id",AppUtils.getInstance().getCurrentSiteId());
            params.put("sub_category_id",getSubCatId);
            Log.i("@SP", params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_DRAWING_VERSIONS_LIST + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .setTag("requestToGetVersionList")
                .build()
                .getAsObject(DrawingVersionsResponse.class, new ParsedRequestListener<DrawingVersionsResponse>() {
                    @Override
                    public void onResponse(final DrawingVersionsResponse response) {

                        realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(DrawingVersionsResponse.class);
                                    realm.delete(Versionsdata.class);
                                    realm.delete(VersionsItem.class);
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    imagePath=response.getVersionsdata().getPath();
                                    setUpVersionAdapter();
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

    private void setUpVersionAdapter() {
        realm = Realm.getDefaultInstance();
        final RealmResults<VersionsItem> versionsItemRealmResults = realm.where(VersionsItem.class).findAll();
        Timber.d(String.valueOf(versionsItemRealmResults));
        VersionListAdapter commentListAdapter = new VersionListAdapter(versionsItemRealmResults, true, true);
        rvVersionList.setLayoutManager(new LinearLayoutManager(mContext));
        rvVersionList.setHasFixedSize(true);
        rvVersionList.setAdapter(commentListAdapter);
        rvVersionList.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                rvVersionList,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        VersionsItem versionsItem=versionsItemRealmResults.get(position);
                        try {
                            versionUrl =java.net.URLEncoder.encode(versionsItem.getName(), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        ((DrawingDetailsActivity)mContext).call(versionsItem.getId(),imagePath + "/" +versionUrl,true);
                        DrawingDetailsActivity.drawingVersionId=versionsItem.getId();
                        DrawingDetailsActivity.imageName=versionsItem.getTitle();
                        DrawingDetailsActivity.imageUrl=imagePath + "/" +versionUrl;
                        ((DrawingDetailsActivity)mContext).setTitle();

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                }));
        if (versionsItemRealmResults != null) {
            versionsItemRealmResults.addChangeListener(new RealmChangeListener<RealmResults<VersionsItem>>() {
                @Override
                public void onChange(RealmResults<VersionsItem> assetsListItemRealmResults) {
                }
            });
        } else {
            AppUtils.getInstance().showOfflineMessage("versionsItemRealmResults");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public class VersionListAdapter extends RealmRecyclerViewAdapter<VersionsItem, VersionListAdapter.MyViewHolder> {
        private OrderedRealmCollection<VersionsItem> versionsItemOrderedRealmCollection;
        private VersionsItem versionsItem;
        int counter = 0;

        public VersionListAdapter(@Nullable OrderedRealmCollection<VersionsItem> data, boolean autoUpdate, boolean updateOnModification) {
            super(data, autoUpdate, updateOnModification);
            Timber.d(String.valueOf(data));
            versionsItemOrderedRealmCollection = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drawing_comment_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            versionsItem = versionsItemOrderedRealmCollection.get(position);
            holder.textViewCommentNo.setText(String.valueOf(counter = counter + 1) + ".");
            holder.textViewCommentList.setText(versionsItem.getTitle());
            if(DrawingDetailsActivity.drawingVersionId == position){
                holder.textViewCommentList.setTextColor(getActivity().getColor(R.color.colorAccent));
            }

        }

        @Override
        public long getItemId(int index) {
            return versionsItemOrderedRealmCollection.get(index).getId();
        }

        @Override
        public int getItemCount() {
            return versionsItemOrderedRealmCollection == null ? 0 : versionsItemOrderedRealmCollection.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.textViewCommentNo)
            TextView textViewCommentNo;
            @BindView(R.id.textViewCommentList)
            TextView textViewCommentList;

            private MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

}
