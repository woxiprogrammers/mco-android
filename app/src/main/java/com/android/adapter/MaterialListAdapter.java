package com.android.adapter;

import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.android.models.MaterialListItem;
import com.android.models.ModulesItem;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Sharvari on 18/8/17.
 */

public class MaterialListAdapter extends RealmRecyclerViewAdapter<MaterialListItem, ModulesAdapter.MyViewHolder> {
    public MaterialListAdapter(@Nullable OrderedRealmCollection<MaterialListItem> data, boolean autoUpdate) {
        super(data, autoUpdate);
    }

    @Override
    public ModulesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ModulesAdapter.MyViewHolder holder, int position) {

    }
}
