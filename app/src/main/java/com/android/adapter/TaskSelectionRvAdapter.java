package com.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.constro360.R;
import com.android.models.AssignedTaskItem;

import java.util.ArrayList;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class TaskSelectionRvAdapter extends RecyclerView.Adapter<TaskSelectionRvAdapter.ItemHolder> {
    private ArrayList<AssignedTaskItem> mArrAssignedTaskItem;

    public TaskSelectionRvAdapter(ArrayList<AssignedTaskItem> mArrAssignedTaskItem) {
        this.mArrAssignedTaskItem = mArrAssignedTaskItem;
    }

    @Override
    public TaskSelectionRvAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemViewMain = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_listing, parent, false);
        return new ItemHolder(itemViewMain);
    }

    @Override
    public void onBindViewHolder(TaskSelectionRvAdapter.ItemHolder holder, int position) {
        holder.tvName.setText(mArrAssignedTaskItem.get(position).getStrName());
        holder.tvDescription.setText(mArrAssignedTaskItem.get(position).getStrDescription());
    }

    @Override
    public int getItemCount() {
        return mArrAssignedTaskItem.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        Context mContext;
        TextView tvName, tvDescription;

        ItemHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            tvName = (TextView) itemView.findViewById(R.id.tv_Name);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_Description);
        }
    }
}
