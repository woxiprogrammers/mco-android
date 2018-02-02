package com.android.drawings_module;

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

import com.android.constro360.R;
import com.android.utils.FragmentInterface;
import com.android.drawings_module.drawing_model.CommentsData;
import com.android.drawings_module.drawing_model.CommentsListItem;
import com.android.drawings_module.drawing_model.DrawingCommentsResponse;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.RecyclerItemClickListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class DrawingCommentFragment extends Fragment  implements FragmentInterface{

    @BindView(R.id.rvCommonList)
    RecyclerView rvCommonList;
    private Unbinder unbinder;
    private Realm realm;
    private Context mContext;
    private int drawingId;
    public DrawingCommentFragment() {
        // Required empty public constructor
    }


    public static DrawingCommentFragment newInstance(int id) {
        Bundle args = new Bundle();
        DrawingCommentFragment fragment = new DrawingCommentFragment();
        args.putInt("drawingVersionId",id);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_drawing_comment, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext=getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            drawingId = bundle.getInt("drawingVersionId");
        }
        requestToGetComments();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }



    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = getArguments();
        if (bundle != null) {
            drawingId = bundle.getInt("drawingVersionId");
        }
    }

    public void requestToGetComments() {
        final JSONObject params = new JSONObject();
        try {
            params.put("drawing_image_version_id", drawingId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_DRAWING_COMMENTS_LIST + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .setTag("requestToGetComments")
                .build()
                .getAsObject(DrawingCommentsResponse.class, new ParsedRequestListener<DrawingCommentsResponse>() {
                    @Override
                    public void onResponse(final DrawingCommentsResponse response) {

                        realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(DrawingCommentsResponse.class);
                                    realm.delete(CommentsData.class);
                                    realm.delete(CommentsListItem.class);
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    setUpCommentAdapter();

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

    private void setUpCommentAdapter() {
        realm = Realm.getDefaultInstance();
        final RealmResults<CommentsListItem> commentsListItemRealmResults = realm.where(CommentsListItem.class).findAll();
        Timber.d(String.valueOf(commentsListItemRealmResults));
        CommentListAdapter commentListAdapter = new CommentListAdapter(commentsListItemRealmResults, true, true);
        rvCommonList.setLayoutManager(new LinearLayoutManager(mContext));
        rvCommonList.setHasFixedSize(true);
        rvCommonList.setAdapter(commentListAdapter);
        rvCommonList.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                rvCommonList,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                }));
    }

    @Override
    public void fragmentBecameVisible() {

    }

    public class CommentListAdapter extends RealmRecyclerViewAdapter<CommentsListItem, CommentListAdapter.MyViewHolder> {
        private OrderedRealmCollection<CommentsListItem> commentsListItemOrderedRealmCollection;
        private CommentsListItem commentsListItem;
        int counter=0;

        public CommentListAdapter(@Nullable OrderedRealmCollection<CommentsListItem> data, boolean autoUpdate, boolean updateOnModification) {
            super(data, autoUpdate, updateOnModification);
            Timber.d(String.valueOf(data));
            commentsListItemOrderedRealmCollection = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drawing_comment_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            commentsListItem = commentsListItemOrderedRealmCollection.get(position);
            holder.textViewCommentNo.setText(String.valueOf(counter=counter + 1) + ".");
            holder.textViewCommentList.setText(commentsListItem.getName());

        }

        @Override
        public long getItemId(int index) {
            return commentsListItemOrderedRealmCollection.get(index).getId();
        }

        @Override
        public int getItemCount() {
            return commentsListItemOrderedRealmCollection == null ? 0 : commentsListItemOrderedRealmCollection.size();
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
