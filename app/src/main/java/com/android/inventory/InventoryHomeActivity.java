package com.android.inventory;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.adapter.MaterialListAdapter;
import com.android.constro360.R;
import com.android.models.inventory.InventoryDataResponse;
import com.android.models.inventory.InventoryResponse;
import com.android.models.inventory.MaterialListItem;
import com.android.utils.AppURL;
import com.android.utils.BaseActivity;
import com.android.utils.RecyclerItemClickListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import timber.log.Timber;

/**
 * Created by Sharvari on 18/8/17.
 */
public class InventoryHomeActivity extends BaseActivity {

    private MaterialListAdapter materialListAdapter;
    private Context mContext;
    @BindView(R.id.rv_material_list)
    RecyclerView rv_material_list;
    private ArrayList<String> strMaterialName=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_listing);
        mContext = InventoryHomeActivity.this;
        ButterKnife.bind(this);
        if(getSupportActionBar() !=null){
            getSupportActionBar().setHomeButtonEnabled(true);
            setTitle(getString(R.string.inventory));
        }
        requestInventoryResponse();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestInventoryResponse() {
        AndroidNetworking.get(AppURL.INVENTORY_DATA_URL)
                /*.addBodyParameter("email", "admin@mconstruction.co.in")
                .addBodyParameter("password", "mco@1234")*/
                .setTag("requestInventoryData")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.d(String.valueOf(response));
                        Gson gson = new GsonBuilder().create();
                        final String tempResponse = "{\n" +
                                "  \"data\": {\n" +
                                "    \"material_list\": [\n" +
                                "      {\n" +
                                "        \"material_name\": \"Electric Welding Machine Bits\",\n" +
                                "        \"quantity_in\": \"123\",\n" +
                                "        \"quantity_out\": \"567\",\n" +
                                "        \"quantity_available\": \"788\"\n" +
                                "      },\n" +
                                "      {\n" +
                                "        \"material_name\": \"Bits\",\n" +
                                "        \"quantity_in\": \"1030\",\n" +
                                "        \"quantity_out\": \"290\",\n" +
                                "        \"quantity_available\": \"10\"\n" +
                                "      },\n" +
                                "      {\n" +
                                "        \"material_name\": \"Bits-Two\",\n" +
                                "        \"quantity_in\": \"100\",\n" +
                                "        \"quantity_out\": \"200\",\n" +
                                "        \"quantity_available\": \"10\"\n" +
                                "      },\n" +
                                "      {\n" +
                                "        \"material_name\": \"Bits-Three\",\n" +
                                "        \"quantity_in\": \"180\",\n" +
                                "        \"quantity_out\": \"240\",\n" +
                                "        \"quantity_available\": \"10\"\n" +
                                "      },\n" +
                                "      {\n" +
                                "        \"material_name\": \"Bits-Four\",\n" +
                                "        \"quantity_in\": \"190\",\n" +
                                "        \"quantity_out\": \"200\",\n" +
                                "        \"quantity_available\": \"10\"\n" +
                                "      },\n" +
                                "      {\n" +
                                "        \"material_name\": \"Bits-Five\",\n" +
                                "        \"quantity_in\": \"1070\",\n" +
                                "        \"quantity_out\": \"200\",\n" +
                                "        \"quantity_available\": \"10\"\n" +
                                "      },\n" +
                                "      {\n" +
                                "        \"material_name\": \"Bits-Six\",\n" +
                                "        \"quantity_in\": \"10\",\n" +
                                "        \"quantity_out\": \"20\",\n" +
                                "        \"quantity_available\": \"890\"\n" +
                                "      },\n" +
                                "      {\n" +
                                "        \"material_name\": \"Bits-Seven\",\n" +
                                "        \"quantity_in\": \"900\",\n" +
                                "        \"quantity_out\": \"20\",\n" +
                                "        \"quantity_available\": \"10\"\n" +
                                "      }\n" +
                                "    ]\n" +
                                "    \n" +
                                "  },\n" +
                                "  \"next_url\": \"\",\n" +
                                "  \"message\": \"Sucess\"\n" +
                                "}";

                        final InventoryResponse inventoryResponse = gson.fromJson(String.valueOf(tempResponse), InventoryResponse.class);
                        Realm realm = null;
                        try {
                            realm = Realm.getDefaultInstance();
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.copyToRealm(inventoryResponse);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(InventoryHomeActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                    setAdapterForMaterialList();
                                }
                            }, new Realm.Transaction.OnError() {
                                @Override
                                public void onError(Throwable error) {

                                    Timber.d(String.valueOf(error));
                                }
                            });
                        } finally {
                            if (realm != null) {
                                realm.close();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Timber.d(String.valueOf(anError));

                    }
                });
    }

    private void setAdapterForMaterialList() {
        strMaterialName.add("1516");
        strMaterialName.add("1517");
        strMaterialName.add("1518");
        strMaterialName.add("1519");
        strMaterialName.add("1520");
        strMaterialName.add("1521");
        strMaterialName.add("1522");
        strMaterialName.add("1523");
        final Realm realm = Realm.getDefaultInstance();
        final OrderedRealmCollection<MaterialListItem> materialListItems = realm.where(InventoryDataResponse.class).findFirst().getMaterialList();
        materialListAdapter = new MaterialListAdapter(materialListItems, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_material_list.setLayoutManager(linearLayoutManager);
        rv_material_list.setAdapter(materialListAdapter);
        rv_material_list.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rv_material_list, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        materialListItems.get(position).setSelected(true);
                    }
                });
                View itemView= rv_material_list.getRootView();
                 LinearLayout cardView = ButterKnife.findById(itemView, R.id.cardView);
                cardView.setSelected(true);
                rv_material_list.getAdapter().notifyDataSetChanged();
                Snackbar snackbar = Snackbar
                        .make(view, "Select Materials", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent(InventoryHomeActivity.this, InventoryDetails.class);
                                intent.putExtra("Array",strMaterialName);
                               startActivity(intent);
                            }
                        });

                // Changing message text color
                snackbar.setActionTextColor(getColor(R.color.colorAccent));

                // Changing action button text color
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);

                snackbar.show();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

    }

}
