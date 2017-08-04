package com.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.constro360.R;
import com.android.models.ModulesItem;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class ModulesAdapter extends RealmRecyclerViewAdapter<ModulesItem, ModulesAdapter.MyViewHolder> {
    private OrderedRealmCollection<ModulesItem> modulesItemOrderedRealmCollection;

    public ModulesAdapter(OrderedRealmCollection<ModulesItem> modulesItemOrderedRealmCollection) {
        super(modulesItemOrderedRealmCollection, true);
        this.modulesItemOrderedRealmCollection = modulesItemOrderedRealmCollection;
//        setHasStableIds(true);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_listing, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ModulesItem obj = modulesItemOrderedRealmCollection.get(position);
//        holder.data = obj;
        //noinspection ConstantConditions
        holder.title.setText(obj.getModuleName());
        holder.moduleDescription.setText(modulesItemOrderedRealmCollection.get(position).getModule_description());
        if (position == 0) {
            holder.tvSubModule.setVisibility(View.VISIBLE);
        } else {
            holder.tvSubModule.setVisibility(View.GONE);
            holder.title.setText("Inventory");
            holder.moduleDescription.setText("Manage Inventory");
        }
    }

    /*@Override
    public long getItemId(int index) {
        //noinspection ConstantConditions
        return 0;
    }*/

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title, moduleDescription, tvSubModule;
//        public ModulesItem data;

        MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_Name);
            moduleDescription = (TextView) view.findViewById(R.id.tv_Description);
            tvSubModule = (TextView) view.findViewById(R.id.tvSubModule);
        }
    }
}
/*
public class ModulesAdapter extends RecyclerView.Adapter<ModulesAdapter.ItemHolder> {
    private ArrayList<AssignedTaskItem> mArrAssignedTaskItem;

    public ModulesAdapter(ArrayList<AssignedTaskItem> mArrAssignedTaskItem) {
        this.mArrAssignedTaskItem = mArrAssignedTaskItem;
    }

    @Override
    public ModulesAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemViewMain = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_listing, parent, false);
        return new ItemHolder(itemViewMain);
    }

    @Override
    public void onBindViewHolder(ModulesAdapter.ItemHolder holder, int position) {
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
}*/
