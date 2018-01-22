package com.android.inventory_module.assets;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.constro360.R;
import com.android.inventory_module.MaitenanceFormActivity;
import com.android.inventory_module.MaterialListAdapter;
import com.android.inventory_module.inventory_model.MaterialListItem;
import com.android.utils.FragmentInterface;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import timber.log.Timber;


/**
 * A simple {@link Fragment} subclass.
 */
public class AssetMaintainaceListFragment extends Fragment implements FragmentInterface{


   /* @BindView(R.id.rv_asset_maintaianance_list)
    RecyclerView rvAssetMaintaiananceList;
    @BindView(R.id.mainRelativeMaintList)
    RelativeLayout mainRelativeMaintList;*/
    private View mParentView;


    @BindView(R.id.cardView)
    CardView cardView;

    public AssetMaintainaceListFragment() {
        // Required empty public constructor
    }

    public static AssetMaintainaceListFragment newInstance() {

        Bundle args = new Bundle();

        AssetMaintainaceListFragment fragment = new AssetMaintainaceListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mParentView= inflater.inflate(R.layout.item_asset_maint_list, container, false);

        ButterKnife.bind(this, mParentView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),MaitenanceFormActivity.class);
                startActivity(intent);


            }
        });
        return mParentView;

    }

    @Override
    public void fragmentBecameVisible() {

    }

    public class AssetMaintenanceListAdapter extends RealmRecyclerViewAdapter<MaterialListItem, AssetMaintenanceListAdapter.MyViewHolder> {
        //ToDo Item class
        private OrderedRealmCollection<MaterialListItem> materialListItemCollection;
        private DecimalFormat df;

        //ToDo Item class
        public AssetMaintenanceListAdapter(@Nullable OrderedRealmCollection<MaterialListItem> data, boolean autoUpdate, boolean updateOnModification) {
            super(data, autoUpdate, updateOnModification);
            materialListItemCollection = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_asset_maint_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            //ToDO Item class
            final MaterialListItem materialListItem = materialListItemCollection.get(position);

        }

        @Override
        public long getItemId(int index) {
            return materialListItemCollection.get(index).getId();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.textViewMaintNumber)
            TextView textViewMaintNumber;
            @BindView(R.id.textViewReqName)
            TextView textViewReqName;
            @BindView(R.id.textViewStatus)
            TextView textViewStatus;

            private Context context;

            private MyViewHolder(View itemView) {
                super(itemView);
                context = itemView.getContext();
                ButterKnife.bind(this, itemView);
            }
        }

    }
}
