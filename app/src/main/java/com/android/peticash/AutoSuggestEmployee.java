package com.android.peticash;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.material_request_approve.AutoSuggestActivity;
import com.android.material_request_approve.SearchAssetListItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Sharvari on 31/10/17.
 */

public class AutoSuggestEmployee extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_suggest);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @SuppressWarnings("WeakerAccess")
    protected class EmployeeAutoSuggestAdapter extends RealmRecyclerViewAdapter<SearchAssetListItem, EmployeeAutoSuggestAdapter.MyViewHolder> {

        EmployeeAutoSuggestAdapter(@Nullable OrderedRealmCollection<SearchAssetListItem> data, boolean autoUpdate, boolean updateOnModification) {
            super(data, autoUpdate, updateOnModification);

        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        @Override
        public EmployeeAutoSuggestAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result_list, parent, false);
            return new EmployeeAutoSuggestAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(EmployeeAutoSuggestAdapter.MyViewHolder holder, int position) {
        }

        @Override
        public int getItemCount() {
//            return arrSearchAssetListItem == null ? 0 : arrSearchAssetListItem.size();
            return 0;
        }

    }
}
