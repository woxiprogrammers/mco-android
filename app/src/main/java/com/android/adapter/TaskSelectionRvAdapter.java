package com.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.android.models.AssignedTaskItem;

import java.util.ArrayList;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class TaskSelectionRvAdapter extends RecyclerView.Adapter<TaskSelectionRvAdapter.ItemHolder> {
    ArrayList<AssignedTaskItem> mArrAssignedTaskItem;

    public TaskSelectionRvAdapter(ArrayList<AssignedTaskItem> mArrAssignedTaskItem) {
        this.mArrAssignedTaskItem = mArrAssignedTaskItem;
    }

    @Override
    public TaskSelectionRvAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(TaskSelectionRvAdapter.ItemHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        Context mContext;

        public ItemHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
        }
    }
}
