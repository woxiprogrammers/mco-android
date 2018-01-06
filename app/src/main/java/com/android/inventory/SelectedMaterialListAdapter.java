package com.android.inventory;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.constro360.R;
import com.android.inventory.inventory_model.MaterialListItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Sharvari on 22/8/17.
 */
public class SelectedMaterialListAdapter extends RealmRecyclerViewAdapter<MaterialListItem, SelectedMaterialListAdapter.MyViewHolder> {
    private OrderedRealmCollection<MaterialListItem> materialListItemCollection;
    private Context context;

    public SelectedMaterialListAdapter(@Nullable OrderedRealmCollection<MaterialListItem> data, boolean autoUpdate) {
        super(data, autoUpdate);
        materialListItemCollection = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_open_dialog_on_materials, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final MaterialListItem materialListItem = materialListItemCollection.get(position);
        holder.text_view_MaterialName.setText(materialListItem.getMaterialName());
        holder.editText_Quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

    @Override
    public long getItemId(int index) {
        return materialListItemCollection.get(index).getId();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_view_MaterialName)
        TextView text_view_MaterialName;
        @BindView(R.id.button_RemoveMaterial)
        Button button_RemoveMaterial;
        @BindView(R.id.editText_Quantity)
        EditText editText_Quantity;
        @BindView(R.id.editText_Units)
        EditText editText_Units;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
