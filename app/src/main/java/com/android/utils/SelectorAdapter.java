package com.android.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.constro360.R;

import java.util.ArrayList;
import java.util.List;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class SelectorAdapter extends RecyclerView.Adapter<SelectorAdapter.ViewHolder> {
    private List<String> data;

    public SelectorAdapter() {
        this.data = getListData();
    }

    private List<String> getListData() {
        List<String> selectorItems = new ArrayList<>();
        selectorItems.add("Jan");
        selectorItems.add("Feb");
        selectorItems.add("Mar");
        selectorItems.add("Apr");
        selectorItems.add("May");
        selectorItems.add("Jun");
        selectorItems.add("Jul");
        selectorItems.add("Aug");
        selectorItems.add("Sept");
        selectorItems.add("Oct");
        selectorItems.add("Nov");
        selectorItems.add("Dec");
        return selectorItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_shop_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.text.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView text;

        ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
        }
    }
}