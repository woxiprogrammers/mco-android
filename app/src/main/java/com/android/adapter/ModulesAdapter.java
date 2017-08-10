package com.android.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.constro360.R;
import com.android.models.ModulesItem;
import com.android.models.SubModulesItem;

import io.realm.OrderedRealmCollection;
import io.realm.RealmList;
import io.realm.RealmRecyclerViewAdapter;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class ModulesAdapter extends RealmRecyclerViewAdapter<ModulesItem, ModulesAdapter.MyViewHolder> {
    private OrderedRealmCollection<ModulesItem> modulesItemOrderedRealmCollection;
    // Define listener member variable
    private OnItemClickListener listener;

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ModulesAdapter(OrderedRealmCollection<ModulesItem> modulesItemOrderedRealmCollection) {
        super(modulesItemOrderedRealmCollection, true);
        this.modulesItemOrderedRealmCollection = modulesItemOrderedRealmCollection;
        setHasStableIds(true);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_listing, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ModulesItem modulesItem = modulesItemOrderedRealmCollection.get(position);
        RealmList<SubModulesItem> modulesItemRealmList = modulesItem.getSubModules();
        for (int size = 0; size < modulesItemRealmList.size(); size++) {
            if (size == 0) {
                //noinspection ConstantConditions
                holder.moduleName.setText(modulesItem.getSubModules().get(size).getSubModuleName());
                holder.moduleDescription.setText(modulesItem.getSubModules().get(size).getModuleDescription());
            } else {
                /*View dividerVIew = new View(holder.context);
                ViewGroup.LayoutParams layoutParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 8);
                dividerVIew.setBackgroundColor(ContextCompat.getColor(holder.context, R.color.colorDivider));
                holder.linearLayout_Modules.addView(dividerVIew, layoutParam);
                final TextView textView = new TextView(holder.context);
                textView.setPadding(0, 20, 0, 20);
                textView.setCompoundDrawables(null,null,ContextCompat.getDrawable(holder.context, R.drawable.ic_arrow_up),null);
                textView.setGravity(Gravity.CENTER);
//                textView.setBackground(ContextCompat.getDrawable(holder.context, R.drawable.background_sub_module_text));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                holder.linearLayout_Modules.addView(textView, layoutParams);
                textView.setText(modulesItem.getSubModules().get(size).getSubModuleName());
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View itemView) {
                        // Triggers click upwards to the adapter on click
                        if (listener != null) {
//                           int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                listener.onItemClick(textView, position);
                            }
                        }
                    }
                });*/
            }
        }
    }

    @Override
    public long getItemId(int index) {
        //noinspection ConstantConditions
        return getItem(index).getId();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView moduleName, moduleDescription;
        private Context context;
        private LinearLayout ll_sub_modules;

        MyViewHolder(View view) {
            super(view);
            context = view.getContext();
            moduleName = (TextView) view.findViewById(R.id.tv_moduleName);
            moduleDescription = (TextView) view.findViewById(R.id.tv_Description);
            ll_sub_modules = (LinearLayout) view.findViewById(R.id.ll_sub_modules);
            int intMaxSize = 0;
            for (int index = 0; index < modulesItemOrderedRealmCollection.size(); index++) {
                int intMaxSizeTemp = modulesItemOrderedRealmCollection.get(index).getSubModules().size();
                if (intMaxSizeTemp > intMaxSize) intMaxSize = intMaxSizeTemp;
            }
            for (int indexView = 0; indexView < intMaxSize; indexView++) {
                View dividerView = new View(context);
                ViewGroup.LayoutParams layoutParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 8);
                dividerView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorDivider));
                ll_sub_modules.addView(dividerView, layoutParam);
                TextView textView = new TextView(context);
                textView.setPadding(0, 20, 0, 20);
                textView.setGravity(Gravity.CENTER);
//                textView.setBackground(ContextCompat.getDrawable(holder.context, R.drawable.background_sub_module_text));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                ll_sub_modules.addView(textView, layoutParams);
            }
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
