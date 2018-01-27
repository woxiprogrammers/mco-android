package com.android.dpr_module;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.dpr_module.dpr_model.DprListItem;
import com.android.dpr_module.dpr_model.DprListingResponse;
import com.android.dpr_module.dpr_model.DprUsersItem;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.RecyclerItemClickListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.Sort;
import timber.log.Timber;

public class DPRListActivity extends BaseActivity {
    @BindView(R.id.rv_subContCatList)
    RecyclerView rvSubContCatList;
    @BindView(R.id.mainRelativeDprList)
    LinearLayout mainRelativeDprList;
    @BindView(R.id.floating_add_button_peticash)
    FloatingActionButton floatingAddButtonPeticash;
    @BindView(R.id.textView_purchaseHome_appBarTitle)
    TextView textViewPurchaseHomeAppBarTitle;
    @BindView(R.id.relative_layout_datePicker_purchaseRequest)
    RelativeLayout relativeLayoutDatePickerPurchaseRequest;
    @BindView(R.id.toolbarPurchaseHome)
    Toolbar toolbarPurchaseHome;
    private Context mContext;
    private Realm realm;
    private Date date;
    private String strCurrentDate;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.floating_add_button_peticash)
    public void onViewClicked() {
        Intent intent = new Intent(mContext, DPRHomeActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.relative_layout_datePicker_purchaseRequest)
    public void onCLicked() {
        openDatePickerDialog();
    }

    private void openDatePickerDialog() {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog pickDatePickerDialog = new DatePickerDialog(this, R.style.MyDialogTheme, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int month, int day) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, day);
                String monthString = String.valueOf(month + 1);
                String strDay = String.valueOf(day);
                String strYear = String.valueOf(year);
//                String formatDate;
                if (strDay.length() == 1) {
                    strDay = "0" + strDay;
                }
                if (monthString.length() == 1) {
                    monthString = "0" + monthString;
                }
                strCurrentDate = strYear + "-" + monthString + "-" + strDay;
                setUpDPRListAdapter(strCurrentDate);
                requestToGetDprListing(strCurrentDate);
                setDateInAppBar(month + 1, year, strDay, true);
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        pickDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        pickDatePickerDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dprlist);
        ButterKnife.bind(this);
        initializeViews();
    }

    private void initializeViews() {
        mContext = DPRListActivity.this;
        toolbarPurchaseHome.setTitle("");
        setSupportActionBar(toolbarPurchaseHome);
//        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        date = new Date();
        String format = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        strCurrentDate = simpleDateFormat.format(date);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setUpDPRListAdapter(strCurrentDate);
        requestToGetDprListing(strCurrentDate);
        setDateInAppBar(0, 0, "", false);
    }

    public void setDateInAppBar(int passMonth, int passYear, String passDay, boolean isDateSelected) {
        String format = "dd-MMMM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        String strGetDate = simpleDateFormat.format(date);
        textViewPurchaseHomeAppBarTitle.setText(strGetDate);
        if (isDateSelected) {
            String strMonth = new DateFormatSymbols().getMonths()[passMonth - 1];
            textViewPurchaseHomeAppBarTitle.setText(passDay + "," + strMonth + ", " + passYear);
        }
    }

    private void setUpDPRListAdapter(String strCurrentDate) {
        realm = Realm.getDefaultInstance();
        RealmResults<DprUsersItem> dprListItemRealmResults = realm.where(DprUsersItem.class)
                .equalTo("strDate", strCurrentDate)
                .equalTo("currentSiteId", AppUtils.getInstance().getCurrentSiteId())
                .findAllSortedAsync("strSubConName", Sort.ASCENDING);
        DprListAdapter purchaseRequestRvAdapter = new DprListAdapter(dprListItemRealmResults, true, true);
        rvSubContCatList.setLayoutManager(new LinearLayoutManager(mContext));
        rvSubContCatList.setHasFixedSize(true);
        rvSubContCatList.setAdapter(purchaseRequestRvAdapter);
        rvSubContCatList.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                rvSubContCatList,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                }));
    }

    private void requestToGetDprListing(final String strDate) {
        JSONObject params = new JSONObject();
        try {
            params.put("date", strDate);
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_DPR_LISTING + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .setTag("requestToGetDprListing")
                .build()
                .getAsObject(DprListingResponse.class,
                        new ParsedRequestListener<DprListingResponse>() {
                            @Override
                            public void onResponse(final DprListingResponse response) {
                                realm = Realm.getDefaultInstance();
                                try {
                                    realm.executeTransactionAsync(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
//                                            realm.delete(DprListingResponse.class);
//                                            realm.delete(DprListingData.class);
//                                            realm.delete(DprListItem.class);
//                                            realm.delete(DprUsersItem.class);
                                            for (int i = 0; i < response.getDprListingData().getDprList().size(); i++) {
                                                DprListItem dprListItem = response.getDprListingData().getDprList().get(i);
                                                int intId = dprListItem.getId();
                                                String strSubConName = dprListItem.getName();
                                                for (DprUsersItem userItem : dprListItem.getUsers()) {
                                                    userItem.setIntPrimaryKey(intId);
                                                    userItem.setStrSubConName(strSubConName);
                                                    userItem.setStrDate(strDate);
                                                }
                                            }
                                            realm.insertOrUpdate(response);
                                        }
                                    }, new Realm.Transaction.OnSuccess() {
                                        @Override
                                        public void onSuccess() {
                                            Timber.d("Realm Execution Successful");
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
                                AppUtils.getInstance().logApiError(anError, "requestToGetDprListing");
                            }
                        });
    }

    @SuppressWarnings("WeakerAccess")
    protected class DprListAdapter extends RealmRecyclerViewAdapter<DprUsersItem, DprListAdapter.MyViewHolder> {
        private Realm realm;
        //ToDo Item class
        private OrderedRealmCollection<DprUsersItem> usersItemOrderedRealmCollection;

        DprListAdapter(@Nullable OrderedRealmCollection<DprUsersItem> data, boolean autoUpdate, boolean updateOnModification) {
            super(data, autoUpdate, updateOnModification);
            realm = Realm.getDefaultInstance();
            usersItemOrderedRealmCollection = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dpr_listing, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            //ToDo Item class
            Timber.d(String.valueOf(usersItemOrderedRealmCollection.size()));
            DprUsersItem usersItem = usersItemOrderedRealmCollection.get(position);
            String strSubConName = usersItem.getStrSubConName();
            DprUsersItem firstUsersItem = realm.where(DprUsersItem.class)
                    .equalTo("strSubConName", strSubConName).findFirst();
            if (firstUsersItem.getId() == usersItem.getId()) {
                holder.textViewSubConName.setText(strSubConName);
            } else holder.textViewSubConName.setVisibility(View.GONE);
            holder.textViewCatName.setText(usersItem.getCat());
        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            super.onDetachedFromRecyclerView(recyclerView);
            if (realm != null) {
                realm.close();
            }
        }

        @Override
        public long getItemId(int index) {
            return usersItemOrderedRealmCollection.get(index).getId();
        }

        @Override
        public int getItemCount() {
            return usersItemOrderedRealmCollection == null ? 0 : usersItemOrderedRealmCollection.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.textViewSubConName)
            TextView textViewSubConName;
            @BindView(R.id.linearLayoutSubContName)
            LinearLayout linearLayoutSubContName;
            @BindView(R.id.textViewCatName)
            TextView textViewCatName;
            @BindView(R.id.textViewTotalCatCount)
            TextView textViewTotalCatCount;

            MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
