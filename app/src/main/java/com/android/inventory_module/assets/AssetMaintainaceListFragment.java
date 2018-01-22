package com.android.inventory_module.assets;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.constro360.R;
import com.android.inventory_module.MaterialListAdapter;
import com.android.inventory_module.inventory_model.MaterialListItem;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import timber.log.Timber;


/**
 * A simple {@link Fragment} subclass.
 */
public class AssetMaintainaceListFragment extends Fragment {


    @BindView(R.id.rv_asset_maintaianance_list)
    RecyclerView rvAssetMaintaiananceList;
    @BindView(R.id.mainRelativeMaintList)
    RelativeLayout mainRelativeMaintList;
    private View mParentView;


    public AssetMaintainaceListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mParentView= inflater.inflate(R.layout.fragment_asset_maintainace_list, container, false);

        ButterKnife.bind(this, mParentView);
        return mParentView;

    }

    public class MaterialListAdapter extends RealmRecyclerViewAdapter<MaterialListItem, MaterialListAdapter.MyViewHolder> {
        private OrderedRealmCollection<MaterialListItem> materialListItemCollection;
        private DecimalFormat df;

        public MaterialListAdapter(@Nullable OrderedRealmCollection<MaterialListItem> data, boolean autoUpdate, boolean updateOnModification) {
            super(data, autoUpdate, updateOnModification);
            setHasStableIds(true);
            Timber.d(String.valueOf(data));
            materialListItemCollection = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_material_list, parent, false);
            df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final MaterialListItem materialListItem = materialListItemCollection.get(position);
            holder.textview_material_name.setText(materialListItem.getMaterialName());
            holder.textview_quantity_in.setText(df.format(Float.parseFloat(materialListItem.getQuantityIn())) + "");
            holder.textview_quantity_out.setText(df.format(Float.parseFloat(materialListItem.getQuantityOut())) + "");
            holder.textview_quantity_current.setText(df.format(Float.parseFloat(materialListItem.getQuantityAvailable())) + "");
        }

        @Override
        public long getItemId(int index) {
            return materialListItemCollection.get(index).getId();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.textview_material_name)
            TextView textview_material_name;
            @BindView(R.id.textview_quantity_in)
            TextView textview_quantity_in;
            @BindView(R.id.textview_quantity_out)
            TextView textview_quantity_out;
            @BindView(R.id.textview_quantity_current)
            TextView textview_quantity_current;
            private Context context;

            private MyViewHolder(View itemView) {
                super(itemView);
                context = itemView.getContext();
                ButterKnife.bind(this, itemView);
            }
        }

    }
}
