package com.android.drawings;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.models.awarenessmodels.AwarenessMainCategoryResponse;
import com.android.models.awarenessmodels.AwarenessSubCategoriesItem;
import com.android.models.awarenessmodels.MainCategoriesData;
import com.android.models.awarenessmodels.MainCategoriesItem;
import com.android.models.awarenessmodels.SubCatedata;
import com.android.models.awarenessmodels.SubCategoriesResponse;
import com.android.models.drawing.DrawingImagesResponse;
import com.android.models.drawing.ImageListDrawing;
import com.android.models.drawing.ImagesListDrawingItem;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.RecyclerItemClickListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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
    @BindView(R.id.rv_image_list)
    RecyclerView rvImageList;

    private Realm realm;
    private Context mContext;
    private List<MainCategoriesItem> categoryList;
    RealmResults<MainCategoriesItem> mainCategoriesItems;

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
        spinnerDrawingCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int selectedItemIndex, long l) {
                realm = Realm.getDefaultInstance();
                mainCategoriesItems = realm.where(MainCategoriesItem.class).findAll();
                requestToGetSubCatData(mainCategoriesItems.get(selectedItemIndex).getId());
                rvSubcatdrawingList.setVisibility(View.VISIBLE);
                rvImageList.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        requestToGetCategoryData();
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
//            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_AWARENES_CATEGORY_DATA + AppUtils.getInstance().getCurrentToken())
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

    private void requestToGetSubCatData(final int mainCategoryId) {
        JSONObject params = new JSONObject();
        try {
            params.put("page", 0);
            params.put("main_category_id", mainCategoryId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_AWARENES_SUB_CATEGORY_DATA + AppUtils.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("requestToGetSubCatData")
                .build()
                .getAsObject(SubCategoriesResponse.class, new ParsedRequestListener<SubCategoriesResponse>() {
                    @Override
                    public void onResponse(final SubCategoriesResponse response) {
                        Timber.i(String.valueOf(response));
                        realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(SubCategoriesResponse.class);
                                    realm.delete(SubCatedata.class);
                                    realm.delete(AwarenessSubCategoriesItem.class);
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    setUpSubCatListAdapter();

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

    private void requestToGetImageData() {
        AndroidNetworking.get(AppURL.API_IMAGE_LIST_DRAWING)
                .setTag("requestToGetImageData")
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(DrawingImagesResponse.class, new ParsedRequestListener<DrawingImagesResponse>() {
                    @Override
                    public void onResponse(final DrawingImagesResponse response) {
                        Timber.i(String.valueOf(response));
                        realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(DrawingImagesResponse.class);
                                    realm.delete(ImageListDrawing.class);
                                    realm.delete(ImagesListDrawingItem.class);
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    rvSubcatdrawingList.setVisibility(View.GONE);
                                    rvImageList.setVisibility(View.VISIBLE);
                                    setUpSubImageListAdapter();

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
    private void setUpSubCatListAdapter() {
        realm = Realm.getDefaultInstance();
        final RealmResults<AwarenessSubCategoriesItem> assetsListItems = realm.where(AwarenessSubCategoriesItem.class).findAll();
        Timber.d(String.valueOf(assetsListItems));
        SubCategoryAdapter subCategoryAdapter = new SubCategoryAdapter(assetsListItems, true, true);
        rvSubcatdrawingList.setLayoutManager(new GridLayoutManager(mContext, 2));
        rvSubcatdrawingList.setHasFixedSize(true);
        rvSubcatdrawingList.setAdapter(subCategoryAdapter);
        rvSubcatdrawingList.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                rvSubcatdrawingList,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        requestToGetImageData();

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                }));
        if (assetsListItems != null) {
            assetsListItems.addChangeListener(new RealmChangeListener<RealmResults<AwarenessSubCategoriesItem>>() {
                @Override
                public void onChange(RealmResults<AwarenessSubCategoriesItem> assetsListItemRealmResults) {
                }
            });
        } else {
            AppUtils.getInstance().showOfflineMessage("AssetsListFragment");
        }
    }
    private void setUpSubImageListAdapter() {
        realm = Realm.getDefaultInstance();
        final RealmResults<ImagesListDrawingItem> assetsListItems = realm.where(ImagesListDrawingItem.class).findAll();
        Timber.d(String.valueOf(assetsListItems));
        ImageListAdapter subCategoryAdapter = new ImageListAdapter(assetsListItems, true, true);
        rvImageList.setLayoutManager(new GridLayoutManager(mContext, 2));
        rvImageList.setHasFixedSize(true);
        rvImageList.setAdapter(subCategoryAdapter);
        rvImageList.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                rvImageList,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                }));
        if (assetsListItems != null) {
            assetsListItems.addChangeListener(new RealmChangeListener<RealmResults<ImagesListDrawingItem>>() {
                @Override
                public void onChange(RealmResults<ImagesListDrawingItem> assetsListItemRealmResults) {
                }
            });
        } else {
            AppUtils.getInstance().showOfflineMessage("AssetsListFragment");
        }
    }
    public class SubCategoryAdapter extends RealmRecyclerViewAdapter<AwarenessSubCategoriesItem, SubCategoryAdapter.MyViewHolder> {
        private OrderedRealmCollection<AwarenessSubCategoriesItem> awarenessSubCategoriesItemOrderedRealmCollection;
        private AwarenessSubCategoriesItem awarenessSubCategoriesItem;

        public SubCategoryAdapter(@Nullable OrderedRealmCollection<AwarenessSubCategoriesItem> data, boolean autoUpdate, boolean updateOnModification) {
            super(data, autoUpdate, updateOnModification);
            Timber.d(String.valueOf(data));
            awarenessSubCategoriesItemOrderedRealmCollection = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drawing_sub_cat_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            awarenessSubCategoriesItem = awarenessSubCategoriesItemOrderedRealmCollection.get(position);
            holder.textviewSubCatName.setText(awarenessSubCategoriesItem.getName());

        }

        @Override
        public long getItemId(int index) {
            return awarenessSubCategoriesItemOrderedRealmCollection.get(index).getId();

        }

        @Override
        public int getItemCount() {
            return awarenessSubCategoriesItemOrderedRealmCollection == null ? 0 : awarenessSubCategoriesItemOrderedRealmCollection.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.textviewSubCatName)
            TextView textviewSubCatName;

            private MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    public class ImageListAdapter extends RealmRecyclerViewAdapter<ImagesListDrawingItem, ImageListAdapter.MyViewHolder> {
        private OrderedRealmCollection<ImagesListDrawingItem> imagesListDrawingItemOrderedRealmCollection;
        private ImagesListDrawingItem imagesListDrawingItem;

        public ImageListAdapter(@Nullable OrderedRealmCollection<ImagesListDrawingItem> data, boolean autoUpdate, boolean updateOnModification) {
            super(data, autoUpdate, updateOnModification);
            Timber.d(String.valueOf(data));
            imagesListDrawingItemOrderedRealmCollection = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drawing_images, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            imagesListDrawingItem = imagesListDrawingItemOrderedRealmCollection.get(position);
            Glide.with(mContext).load(imagesListDrawingItem.getImageUrl())
                    .thumbnail(0.1f)
                    .crossFade()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.imageViewDrawing);


        }

        @Override
        public long getItemId(int index) {
            return imagesListDrawingItemOrderedRealmCollection.get(index).getId();

        }

        @Override
        public int getItemCount() {
            return imagesListDrawingItemOrderedRealmCollection == null ? 0 : imagesListDrawingItemOrderedRealmCollection.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.imageViewDrawing)
            ImageView imageViewDrawing;
            private MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

}
