package com.android.dashboard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.constro360.R;
import com.android.firebase.counts_model.NotificationCountData;
import com.android.login_mvp.login_model.ModulesItem;
import com.android.login_mvp.login_model.SubModulesItem;
import com.android.utils.SlideAnimationUtil;

import io.realm.OrderedRealmCollection;
import io.realm.RealmList;
import io.realm.RealmRecyclerViewAdapter;
import timber.log.Timber;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class ModulesAdapter extends RealmRecyclerViewAdapter<ModulesItem, ModulesAdapter.MyViewHolder> {
    private OrderedRealmCollection<ModulesItem> modulesItemOrderedRealmCollection;
    private NotificationCountData notificationCountData;
    // Define listener member variable
    private OnItemClickListener clickListener;

    ModulesAdapter(OrderedRealmCollection<ModulesItem> modulesItemOrderedRealmCollection,
                   NotificationCountData notificationCountData) {
        super(modulesItemOrderedRealmCollection, true, true);
        this.modulesItemOrderedRealmCollection = modulesItemOrderedRealmCollection;
        this.notificationCountData = notificationCountData;
        setHasStableIds(true);
        int intMaxSize = 0;
        for (int index = 0; index < modulesItemOrderedRealmCollection.size(); index++) {
            int intMaxSizeTemp = modulesItemOrderedRealmCollection.get(index).getSubModules().size();
            if (intMaxSizeTemp > intMaxSize) intMaxSize = intMaxSizeTemp;
        }
    }

    // Define the method that allows the parent activity or fragment to define the listener
    void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_listing, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ModulesItem modulesItem = modulesItemOrderedRealmCollection.get(position);
        RealmList<SubModulesItem> modulesItemRealmList = modulesItem.getSubModules();
        holder.moduleName.setText(modulesItem.getModuleName());
        String moduleName = modulesItem.getModuleName();
        if (moduleName.equalsIgnoreCase("Purchase")) {
            int intCount = notificationCountData.getMaterialRequestCreateCount()
                    + notificationCountData.getMaterialRequestDisapprovedCount()
                    + notificationCountData.getPurchaseRequestCreateCount()
                    + notificationCountData.getPurchaseRequestDisapprovedCount()
                    +notificationCountData.getPurchaseOrderBillCreateCount()
                    +notificationCountData.getPurchaseOrderCreateCount()
                    +notificationCountData.getPurchaseOrderRequestCreateCount()
                    +notificationCountData.getPurchaseRequestApprovedCount();
//            holder.moduleCount.setText(String.valueOf(intCount));
            if (intCount > 0) {
                Timber.d("Purchase Count: " + intCount);
                holder.moduleCount.setText(String.valueOf(intCount));
            } else {
                Timber.d("Purchase Count: " + intCount);
                holder.moduleCount.setVisibility(View.GONE);
            }
            holder.imageViewModule.setBackgroundResource(R.drawable.ic_purchase);
        } else if (moduleName.equalsIgnoreCase("Peticash")) {
            int intCount = notificationCountData.getSalaryApprovedCount()
                    + notificationCountData.getSalaryRequestCount();
            if (intCount > 0) {
                Timber.d("Peticash Count: " + intCount);
                holder.moduleCount.setText(String.valueOf(intCount));
            } else {
                Timber.d("Peticash Count: " + intCount);
                holder.moduleCount.setVisibility(View.GONE);
            }
            holder.imageViewModule.setBackgroundResource(R.drawable.ic_peticash);
        } else if (moduleName.equalsIgnoreCase("Inventory")) {

            int intCount = notificationCountData.getMaterialSiteOutTransferApproveCount()
                    + notificationCountData.getMaterialSiteOutTransferCreateCount()
                    + notificationCountData.getAssetMaintenanceRequestCount();
            if (intCount > 0) {
                Timber.d("Purchase Count: " + intCount);
                holder.moduleCount.setText(String.valueOf(intCount));
            } else {
                Timber.d("Purchase Count: " + intCount);
                holder.moduleCount.setVisibility(View.GONE);
            }
            holder.imageViewModule.setBackgroundResource(R.drawable.ic_inventory2);
        } else if (moduleName.equalsIgnoreCase("Checklist")) {
            int intCount = notificationCountData.getChecklistAssignedCount()
                    + notificationCountData.getReviewChecklistCount();
            if (intCount > 0) {
                Timber.d("Checklist Count: " + intCount);
                holder.moduleCount.setText(String.valueOf(intCount));
            } else {
                Timber.d("Checklist Count: " + intCount);
                holder.moduleCount.setVisibility(View.GONE);
            }
            holder.imageViewModule.setBackgroundResource(R.drawable.ic_checklist);
        } else {
            holder.imageViewModule.setBackgroundResource(R.drawable.ic_purchase);
            holder.moduleCount.setVisibility(View.GONE);
        }
//       holder.moduleDescription.setText(modulesItem.getSubModules().get(size).getModuleDescription());
        int noOfChildViews = holder.ll_sub_modules.getChildCount();
        int noOfSubModules = modulesItemRealmList.size();
        if (noOfSubModules < noOfChildViews) {
            for (int index = noOfSubModules; index < noOfChildViews; index++) {
                LinearLayout currentChildView = (LinearLayout) holder.ll_sub_modules.getChildAt(index);
                currentChildView.setVisibility(View.GONE);
            }
        }
        for (int textViewIndex = 0; textViewIndex < noOfSubModules; textViewIndex++) {
            LinearLayout currentChildView = (LinearLayout) holder.ll_sub_modules.getChildAt(textViewIndex);
            TextView currentTextView_Name = currentChildView.findViewById(R.id.textView_submodule_name);
            String strSumModuleName = modulesItemRealmList.get(textViewIndex).getSubModuleName();
            currentTextView_Name.setText(strSumModuleName);
            TextView currentTextView_Count = currentChildView.findViewById(R.id.tv_submoduleCount);
            currentTextView_Count.setText("");
            if (moduleName.equalsIgnoreCase("Purchase")) {
                if (strSumModuleName.equalsIgnoreCase("material request")) {
                    int intCount = notificationCountData.getMaterialRequestCreateCount()
                            + notificationCountData.getMaterialRequestDisapprovedCount();
                    if (intCount > 0) {
                        Timber.d("material request: " + intCount);
                        currentTextView_Count.setText(String.valueOf(intCount));
                    } else {
                        Timber.d("material request: " + intCount);
                        currentTextView_Count.setVisibility(View.GONE);
                    }
                } else if (strSumModuleName.equalsIgnoreCase("purchase request")) {
                    int intCount = notificationCountData.getPurchaseRequestCreateCount()
                            + notificationCountData.getPurchaseRequestDisapprovedCount()
                            +notificationCountData.getPurchaseRequestApprovedCount();
                    if (intCount > 0) {
                        Timber.d("purchase request: " + intCount);
                        currentTextView_Count.setText(String.valueOf(intCount));
                    } else {
                        Timber.d("purchase request: " + intCount);
                        currentTextView_Count.setVisibility(View.GONE);
                    }
                } else if (strSumModuleName.equalsIgnoreCase("Purchase Order")) {
                    int intCount = notificationCountData.getPurchaseOrderCreateCount();
                    if (intCount > 0) {
                        Timber.d("Purchase Order: " + intCount);
                        currentTextView_Count.setText(String.valueOf(intCount));
                    } else {
                        Timber.d("Purchase Order: " + intCount);
                        currentTextView_Count.setVisibility(View.GONE);
                    }
                } else if (strSumModuleName.equalsIgnoreCase("GRN")) {
                    int intCount = notificationCountData.getPurchaseOrderBillCreateCount();
                    if (intCount > 0) {
                        Timber.d("Purchase Bill: " + intCount);
                        currentTextView_Count.setText(String.valueOf(intCount));
                    } else {
                        Timber.d("Purchase Bill: " + intCount);
                        currentTextView_Count.setVisibility(View.GONE);
                    }

                }else if(strSumModuleName.equalsIgnoreCase("Purchase Order Request")){
                    int intCount = notificationCountData.getPurchaseOrderRequestCreateCount();
                    if (intCount > 0) {
                        Timber.d("Purchase Order Request: " + intCount);
                        currentTextView_Count.setText(String.valueOf(intCount));
                    } else {
                        Timber.d("Purchase Order Request: " + intCount);
                        currentTextView_Count.setVisibility(View.GONE);
                    }
                }
                else {
                    currentTextView_Count.setVisibility(View.GONE);

                }
            } else if (moduleName.equalsIgnoreCase("Peticash")) {
                if (strSumModuleName.equalsIgnoreCase("Peticash Management")) {
                    int intCount = notificationCountData.getSalaryApprovedCount()
                            + notificationCountData.getSalaryRequestCount();
                    if (intCount > 0) {
                        Timber.d("Peticash Management: " + intCount);
                        currentTextView_Count.setText(String.valueOf(intCount));
                    } else {
                        Timber.d("Peticash Management: " + intCount);
                        currentTextView_Count.setVisibility(View.GONE);
                    }
                } else {
                    currentTextView_Count.setVisibility(View.GONE);
                }
            } else if (moduleName.equalsIgnoreCase("Inventory")) {
                if (strSumModuleName.equalsIgnoreCase("Component Transfer")) {
                    int intCount = notificationCountData.getMaterialSiteOutTransferApproveCount()
                            + notificationCountData.getMaterialSiteOutTransferCreateCount();
                    if (intCount > 0) {
                        Timber.d("Component Transfer: " + intCount);
                        currentTextView_Count.setText(String.valueOf(intCount));
                    } else {
                        Timber.d("Component Transfer: " + intCount);
                        currentTextView_Count.setVisibility(View.GONE);
                    }
                } else if(strSumModuleName.equalsIgnoreCase("Asset Maintainance")){
                    int intCount = notificationCountData.getAssetMaintenanceRequestCount();
                    if (intCount > 0) {
                        currentTextView_Count.setText(String.valueOf(intCount));
                    } else {
                        currentTextView_Count.setVisibility(View.GONE);
                    }
                }
                else {
                    currentTextView_Count.setVisibility(View.GONE);
                }

            } else if (moduleName.equalsIgnoreCase("Checklist")) {
                if (strSumModuleName.equalsIgnoreCase("Checklist User Assignment")) {
                    int intCount = notificationCountData.getChecklistAssignedCount();
                    if (intCount > 0) {
                        Timber.d("Checklist User Assignment: " + intCount);
                        currentTextView_Count.setText(String.valueOf(intCount));
                    } else {
                        Timber.d("Checklist User Assignment: " + intCount);
                        currentTextView_Count.setVisibility(View.GONE);
                    }
                } else if (strSumModuleName.equalsIgnoreCase("Checklist Recheck")) {
                    int intCount = notificationCountData.getReviewChecklistCount();
                    if (intCount > 0) {
                        Timber.d("Checklist Recheck: " + intCount);
                        currentTextView_Count.setText(String.valueOf(intCount));
                    } else {
                        Timber.d("Checklist Recheck: " + intCount);
                        currentTextView_Count.setVisibility(View.GONE);
                    }
                } else {
                    currentTextView_Count.setVisibility(View.GONE);
                }
            } else {
                currentTextView_Count.setVisibility(View.GONE);
            }

            /*TextView currentTextView = (TextView) holder.ll_sub_modules.getChildAt(textViewIndex);
            currentTextView.setText(modulesItemRealmList.get(textViewIndex).getSubModuleName());*/
            currentChildView.setOnClickListener(new View.OnClickListener() {
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
        private TextView moduleName, moduleCount;
        private Context context;
        private LinearLayout ll_sub_modules;
        private LinearLayout fl_mainModuleFrame;
        private ImageView imageViewModule;

        MyViewHolder(View view) {
            super(view);
            context = view.getContext();
            moduleName = view.findViewById(R.id.tv_moduleName);
            moduleCount = view.findViewById(R.id.tv_moduleCount);
            fl_mainModuleFrame = view.findViewById(R.id.fl_mainModuleFrame);
            ll_sub_modules = view.findViewById(R.id.ll_sub_modules);
            imageViewModule = view.findViewById(R.id.imageViewModule);
            int intMaxSize = 0;
            for (int index = 0; index < modulesItemOrderedRealmCollection.size(); index++) {
                int intMaxSizeTemp = modulesItemOrderedRealmCollection.get(index).getSubModules().size();
                if (intMaxSizeTemp > intMaxSize) intMaxSize = intMaxSizeTemp;
            }
            /*for (int indexView = 0; indexView < intMaxSize; indexView++) {
                TextView textView = new TextView(context);
                textView.setId(indexView);
                textView.setPadding(0, 20, 0, 20);
                textView.setGravity(Gravity.CENTER);
                textView.setBackground(ContextCompat.getDrawable(context, R.drawable.background_sub_module_text));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                ll_sub_modules.addView(textView, layoutParams);
            }*/
            for (int indexView = 0; indexView < intMaxSize; indexView++) {
                View taskLayout = LayoutInflater.from(context).inflate(R.layout.item_submodule_dashboard, null);
                taskLayout.setId(indexView);
                ll_sub_modules.addView(taskLayout);
            }
        }
    }
}