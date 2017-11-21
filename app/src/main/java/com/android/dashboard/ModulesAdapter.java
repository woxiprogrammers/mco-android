package com.android.dashboard;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.constro360.R;
import com.android.models.login_acl.ModulesItem;
import com.android.models.login_acl.SubModulesItem;
import com.android.utils.SlideAnimationUtil;

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
    private OnItemClickListener clickListener;

    public ModulesAdapter(OrderedRealmCollection<ModulesItem> modulesItemOrderedRealmCollection) {
        super(modulesItemOrderedRealmCollection, true);
        this.modulesItemOrderedRealmCollection = modulesItemOrderedRealmCollection;
        setHasStableIds(true);
        int intMaxSize = 0;
        for (int index = 0; index < modulesItemOrderedRealmCollection.size(); index++) {
            int intMaxSizeTemp = modulesItemOrderedRealmCollection.get(index).getSubModules().size();
            if (intMaxSizeTemp > intMaxSize) intMaxSize = intMaxSizeTemp;
        }
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_listing, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ModulesItem modulesItem = modulesItemOrderedRealmCollection.get(position);
        RealmList<SubModulesItem> modulesItemRealmList = modulesItem.getSubModules();
        holder.moduleName.setText(modulesItem.getModuleName());
//        holder.moduleDescription.setText(modulesItem.getSubModules().get(size).getModuleDescription());
        int noOfTextViews = holder.ll_sub_modules.getChildCount();
        int noOfSubModules = modulesItemRealmList.size();
        if (noOfSubModules < noOfTextViews) {
            for (int index = noOfSubModules; index < noOfTextViews; index++) {
                TextView currentTextView = (TextView) holder.ll_sub_modules.getChildAt(index);
                currentTextView.setVisibility(View.GONE);
            }
        }
        for (int textViewIndex = 0; textViewIndex < noOfSubModules; textViewIndex++) {
            TextView currentTextView = (TextView) holder.ll_sub_modules.getChildAt(textViewIndex);
            currentTextView.setText(modulesItemRealmList.get(textViewIndex).getSubModuleName());
            currentTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        clickListener.onItemClick(view, holder.getAdapterPosition());
                    }
                }
            });
        }
        holder.ll_sub_modules.setVisibility(View.GONE);
        holder.fl_mainModuleFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.ll_sub_modules.getVisibility() == View.VISIBLE) {
//                    SlideAnimationUtil.slideOutToLeft(holder.context, holder.ll_sub_modules);
                    holder.ll_sub_modules.setVisibility(View.GONE);
                } else {
                    SlideAnimationUtil.slideInFromRight(holder.context, holder.ll_sub_modules);
                    holder.ll_sub_modules.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public long getItemId(int index) {
        //noinspection ConstantConditions
        return getItem(index).getId();
    }

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView moduleName/*, moduleDescription*/;
        private Context context;
        private LinearLayout ll_sub_modules;
        private FrameLayout fl_mainModuleFrame;

        MyViewHolder(View view) {
            super(view);
            context = view.getContext();
            moduleName = (TextView) view.findViewById(R.id.tv_moduleName);
//            moduleDescription = (TextView) view.findViewById(R.id.tv_Description);
            fl_mainModuleFrame = (FrameLayout) view.findViewById(R.id.fl_mainModuleFrame);
            ll_sub_modules = (LinearLayout) view.findViewById(R.id.ll_sub_modules);
            int intMaxSize = 0;
            for (int index = 0; index < modulesItemOrderedRealmCollection.size(); index++) {
                int intMaxSizeTemp = modulesItemOrderedRealmCollection.get(index).getSubModules().size();
                if (intMaxSizeTemp > intMaxSize) intMaxSize = intMaxSizeTemp;
            }
            for (int indexView = 0; indexView < intMaxSize; indexView++) {
                TextView textView = new TextView(context);
                textView.setId(indexView);
                textView.setPadding(0, 20, 0, 20);
                textView.setGravity(Gravity.CENTER);
                textView.setBackground(ContextCompat.getDrawable(context, R.drawable.background_sub_module_text));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                ll_sub_modules.addView(textView, layoutParams);
            }
        }
    }
}