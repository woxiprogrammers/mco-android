package com.android.peticash;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.constro360.R;
import com.android.peticash.peticash_models.DatewiseTransactionsListItem;
import com.android.peticash.peticash_models.TransactionListItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmList;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
class PeticashTransactionsListAdapter extends RealmRecyclerViewAdapter<DatewiseTransactionsListItem, PeticashTransactionsListAdapter.MyViewHolder> {
    private OrderedRealmCollection<DatewiseTransactionsListItem> modulesItemOrderedRealmCollection;
    // Define listener member variable
    private PeticashTransactionsListAdapter.OnItemClickListener clickListener;

    PeticashTransactionsListAdapter(@Nullable RealmResults<DatewiseTransactionsListItem> data, boolean autoUpdate, boolean updateOnModification) {
        super(data, true, true);
        this.modulesItemOrderedRealmCollection = data;
        setHasStableIds(true);
        int intMaxSize = 0;
        for (int index = 0; index < modulesItemOrderedRealmCollection.size(); index++) {
            int intMaxSizeTemp = modulesItemOrderedRealmCollection.get(index).getTransactionList().size();
            if (intMaxSizeTemp > intMaxSize) intMaxSize = intMaxSizeTemp;
        }
    }

    // Define the method that allows the parent activity or fragment to define the listener
    void setOnItemClickListener(PeticashTransactionsListAdapter.OnItemClickListener listener) {
        this.clickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_peticash_transactions_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final DatewiseTransactionsListItem modulesItem = modulesItemOrderedRealmCollection.get(position);
        final RealmList<TransactionListItem> modulesItemRealmList = modulesItem.getTransactionList();
        holder.mTvTransactionDate.setText(modulesItem.getDate());
        holder.mTvNoOfTransactions.setText("Total no. of transactions: " + modulesItem.getTransactionList().size());
        int noOfChildViews = holder.mLlRemainingTransactionsExpandable.getChildCount();
        final int noOfSubModules = modulesItemRealmList.size();
        if (noOfSubModules < noOfChildViews) {
            for (int index = noOfSubModules; index < noOfChildViews; index++) {
                LinearLayout currentChildView = (LinearLayout) holder.mLlRemainingTransactionsExpandable.getChildAt(index);
                currentChildView.setVisibility(View.GONE);
            }
        }
        holder.mLlRemainingTransactionsExpandable.setVisibility(View.GONE);
        holder.mFlMainTransactionFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.mLlRemainingTransactionsExpandable.getVisibility() == View.VISIBLE) {
//                    SlideAnimationUtil.slideOutToLeft(holder.context, holder.ll_sub_modules);
                    holder.mLlRemainingTransactionsExpandable.setVisibility(View.GONE);
                } else {
                    for (int viewIndex = 0; viewIndex < noOfSubModules; viewIndex++) {
                        LinearLayout currentChildView = (LinearLayout) holder.mLlRemainingTransactionsExpandable.getChildAt(viewIndex);
                        TextView currentTextView_Name = currentChildView.findViewById(R.id.textView_transactionName);
                        currentTextView_Name.setText(modulesItemRealmList.get(viewIndex).getName());
                        TextView currentTextView_Status = currentChildView.findViewById(R.id.textView_transactionStatus);
                        currentTextView_Status.setText(modulesItemRealmList.get(viewIndex).getPaymentStatus());
                        TextView currentTextView_Type = currentChildView.findViewById(R.id.textView_transactionType);
                        currentTextView_Type.setText(modulesItemRealmList.get(viewIndex).getPeticashTransactionType() + ":  ");
                        TextView currentTextView_Amount = currentChildView.findViewById(R.id.textView_transactionAmount);
                        currentTextView_Amount.setText(modulesItemRealmList.get(viewIndex).getPeticashTransactionAmount());
                        currentChildView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (clickListener != null) {
                                    clickListener.onItemClick(view, holder.getAdapterPosition());
                                }
                            }
                        });
                    }
                    holder.mLlRemainingTransactionsExpandable.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public long getItemId(int index) {
        //noinspection ConstantConditions
        return getItem(index).getPrimaryKey();
    }

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        @BindView(R.id.tv_transaction_date)
        TextView mTvTransactionDate;
        @BindView(R.id.tv_no_of_transactions)
        TextView mTvNoOfTransactions;
        @BindView(R.id.fl_mainTrasactionFrame)
        FrameLayout mFlMainTransactionFrame;
        @BindView(R.id.ll_remaining_transactions_expandable)
        LinearLayout mLlRemainingTransactionsExpandable;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            context = view.getContext();
            int intMaxSize = 0;
            for (int index = 0; index < modulesItemOrderedRealmCollection.size(); index++) {
                int intMaxSizeTemp = modulesItemOrderedRealmCollection.get(index).getTransactionList().size();
                if (intMaxSizeTemp > intMaxSize) intMaxSize = intMaxSizeTemp;
            }
            for (int indexView = 0; indexView < intMaxSize; indexView++) {
                View transactionDetailLayout = LayoutInflater.from(context).inflate(R.layout.item_transaction_detail, null);
                transactionDetailLayout.setId(indexView);
                mLlRemainingTransactionsExpandable.addView(transactionDetailLayout);
            }
        }
    }
}
