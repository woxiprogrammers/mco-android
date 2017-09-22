package com.android.material_request_approve;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.constro360.R;

import java.util.ArrayList;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class AutoSuggestAdapter extends RecyclerView.Adapter<AutoSuggestAdapter.ViewHolder> {
    ArrayList<String> list;
    //    AGPostOfficeName postOfficeName;
    Context context;

    public AutoSuggestAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result_list, parent, false);
        itemView.setOnClickListener(AutoSuggestActivity.postOfficeClickListner);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPinList;

        ViewHolder(View itemView) {
            super(itemView);
            tvPinList = (TextView) itemView.findViewById(R.id.tvPinList);
        }
    }
}
