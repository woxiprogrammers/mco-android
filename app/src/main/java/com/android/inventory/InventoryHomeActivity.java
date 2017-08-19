package com.android.inventory;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.adapter.MaterialListAdapter;
import com.android.constro360.R;
import com.android.models.inventory.InventoryDataResponse;
import com.android.models.inventory.InventoryResponse;
import com.android.models.inventory.MaterialListItem;
import com.android.models.login_acl.LoginResponse;
import com.android.utils.AppConstants;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import timber.log.Timber;

import static android.support.v7.widget.LinearLayoutManager.*;

/**
 * Created by Sharvari on 18/8/17.
 */
public class InventoryHomeActivity extends AppCompatActivity {

    private MaterialListAdapter materialListAdapter;
    private Context mContext;
    @BindView(R.id.rv_material_list) RecyclerView rv_material_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_listing);
        ButterKnife.bind(this);
        mContext=InventoryHomeActivity.this;
        requestInventoryResponse();

    }

    private void requestInventoryResponse(){
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
                        final String tempResponse="{\n" +
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

                        final InventoryResponse inventoryResponse=gson.fromJson(String.valueOf(tempResponse), InventoryResponse.class);
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
                                    Toast.makeText(InventoryHomeActivity.this,"Success",Toast.LENGTH_SHORT).show();
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

    private void setAdapterForMaterialList(){
        Realm realm=Realm.getDefaultInstance();
        OrderedRealmCollection<MaterialListItem> materialListItems=realm.where(InventoryDataResponse.class).findFirst().getMaterialList();
        materialListAdapter=new MaterialListAdapter(materialListItems,false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_material_list.setLayoutManager(linearLayoutManager);
        rv_material_list.setAdapter(materialListAdapter);

    }

}
