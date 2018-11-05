package com.android.purchase_module.purchase_request;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.constro360.R;
import com.android.purchase_module.purchase_request.purchase_request_model.purchase_request.PurchaseRequestListItem;
import com.android.purchase_module.purchase_request.purchase_request_model.purchase_request.PurchaseRequestResponse;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.FragmentInterface;
import com.android.utils.RecyclerItemClickListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import timber.log.Timber;

/**
 * Created by Sharvari on 6/1/18.
 */

public class PurchaseOrderApproveListFragment extends Fragment implements FragmentInterface {

    @BindView(R.id.rv_material_list)
    RecyclerView rvMaterialList;
    @BindView(R.id.mainRelativeList)
    RelativeLayout mainRelativeList;
    private Unbinder unbinder;
    private Context mContext;
    private Realm realm;
    private RealmResults<PurchaseRequestListItem> purchaseRequestListItems;
    private int pageNumber = 0;
    private int oldPageNumber;
    private int passYear, passMonth;

    public PurchaseOrderApproveListFragment() {

    }

    public static PurchaseOrderApproveListFragment newInstance() {

        Bundle args = new Bundle();
        PurchaseOrderApproveListFragment fragment = new PurchaseOrderApproveListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mParentView = inflater.inflate(R.layout.layout_common_recycler_view_listing, container, false);
        unbinder = ButterKnife.bind(this, mParentView);
        Bundle bundle = getArguments();

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        passMonth = calendar.get(Calendar.MONTH) + 1;
        passYear = calendar.get(Calendar.YEAR);
        //Initialize Views
        initializeViews();
        return mParentView;
    }

    private void initializeViews() {
        mContext = getActivity();

    }






    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }



    @Override
    public void fragmentBecameVisible() {
    }
}
