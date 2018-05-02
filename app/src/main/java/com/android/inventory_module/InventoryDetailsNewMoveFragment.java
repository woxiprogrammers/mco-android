package com.android.inventory_module;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.R;
import com.android.purchase_module.material_request.material_request_model.UnitQuantityItem;
import com.android.purchase_module.purchase_request.purchase_request_model.bill_model.UnitsResponse;
import com.android.purchase_module.purchase_request.purchase_request_model.bill_model.UnitsResponseData;
import com.android.utils.AppConstants;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.FragmentInterface;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.vlk.multimager.activities.MultiCameraActivity;
import com.vlk.multimager.utils.Constants;
import com.vlk.multimager.utils.Image;
import com.vlk.multimager.utils.Params;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import id.zelory.compressor.Compressor;
import io.realm.Realm;
import io.realm.RealmResults;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;

/**
 * Created by woxi-007 on 22/12/17.
 */
public class InventoryDetailsNewMoveFragment extends Fragment implements FragmentInterface {
    private static int inventoryComponentId;
    @BindView(R.id.edt_userName)
    AutoCompleteTextView edtUserName;
    @BindView(R.id.edt_quantity)
    EditText edtQuantity;
    @BindView(R.id.spinnerUnits)
    Spinner spinnerUnits;
    @BindView(R.id.textView_capture)
    TextView textViewCapture;
    @BindView(R.id.linearLayoutCaptImage)
    LinearLayout linearLayoutCaptImage;
    @BindView(R.id.editText_addNote)
    EditText editTextAddNote;
    @BindView(R.id.btnMoveOut)
    Button btnMoveOut;
    Unbinder unbinder;
    RealmResults<UnitQuantityItem> unitQuantityItemRealmResults;
    @BindView(R.id.mainRelative)
    RelativeLayout mainRelative;
    private ArrayList<File> arrayImageFileList;
    private JSONArray jsonImageNameArray = new JSONArray();
    private Context mContext;
    private Realm realm;
    private JSONArray jsonArrayForName;
    private ArrayList<String> userNameArray;
    private ArrayAdapter<String> adapter;

    @Override
    public void fragmentBecameVisible() {
    }

    public static InventoryDetailsNewMoveFragment newInstance(int inventoryCompId) {
        Bundle args = new Bundle();
        args.putInt("inventoryCompid", inventoryCompId);
        InventoryDetailsNewMoveFragment fragment = new InventoryDetailsNewMoveFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick({R.id.textView_capture, R.id.btnMoveOut})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textView_capture:
                chooseAction(Constants.TYPE_MULTI_CAPTURE, MultiCameraActivity.class);
                break;
            case R.id.btnMoveOut:
                if (AppUtils.getInstance().checkNetworkState()) {
                    validateEntries();
                } else {
                    AppUtils.getInstance().showOfflineMessage("InventoryDetailsNewMoveFragment");
                }
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constants.TYPE_MULTI_CAPTURE:
                ArrayList<Image> imagesList = intent.getParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST);
                Timber.d(String.valueOf(imagesList));
                linearLayoutCaptImage.removeAllViews();
                arrayImageFileList = new ArrayList<File>();
                File currentImageFile;
                for (Image currentImage : imagesList) {
                    if (currentImage.imagePath != null) {
                        currentImageFile = new File(currentImage.imagePath);
                        arrayImageFileList.add(currentImageFile);
                        Bitmap myBitmap = BitmapFactory.decodeFile(currentImage.imagePath);
                        ImageView imageView = new ImageView(mContext);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
                        layoutParams.setMargins(10, 10, 10, 10);
                        imageView.setLayoutParams(layoutParams);
                        imageView.setImageBitmap(myBitmap);
                        linearLayoutCaptImage.addView(imageView);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(mContext, "Image Clicked", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mParentView = inflater.inflate(R.layout.fragment_new_inventory_details_move, container, false);
        unbinder = ButterKnife.bind(this, mParentView);
        initializeViews();
        return mParentView;
    }

    private void initializeViews() {
        mContext = getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            inventoryComponentId = bundle.getInt("inventoryCompid");
        }
        checkAvailability(inventoryComponentId);
        edtUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString() != null){
                    if(AppUtils.getInstance().checkNetworkState())
                        requestToGetUserName(charSequence.toString());
                    else
                        AppUtils.getInstance().showOfflineMessage("GetUserName");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void checkAvailability(int materialRequestComponentId) {
        JSONObject params = new JSONObject();
        try {
            params.put("inventory_component_id", materialRequestComponentId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_INVENTORY_CHECK_AVAILABLE_FOR_UNITS + AppUtils.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("checkAvailability")
                .build()
                .getAsObject(UnitsResponse.class, new ParsedRequestListener<UnitsResponse>() {
                    @Override
                    public void onResponse(final UnitsResponse response) {
                        realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(UnitsResponse.class);
                                    realm.delete(UnitQuantityItem.class);
                                    realm.delete(UnitsResponseData.class);
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    setUpUnitQuantityChangeListener();
                                }
                            }, new Realm.Transaction.OnError() {
                                @Override
                                public void onError(Throwable error) {
                                    AppUtils.getInstance().logRealmExecutionError(error);
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
                        AppUtils.getInstance().logApiError(anError, "checkAvailability");
                    }
                });
    }

    private void setUpUnitQuantityChangeListener() {
        realm = Realm.getDefaultInstance();
        unitQuantityItemRealmResults = realm.where(UnitQuantityItem.class).findAll();
        setUpSpinnerUnitAdapterForDialogUnit(unitQuantityItemRealmResults);
    }

    private void setUpSpinnerUnitAdapterForDialogUnit(RealmResults<UnitQuantityItem> unitQuantityItemRealmResults) {
        List<UnitQuantityItem> unitQuantityItems = realm.copyFromRealm(unitQuantityItemRealmResults);
        ArrayList<String> arrayOfUsers = new ArrayList<String>();
        for (UnitQuantityItem currentUser : unitQuantityItems) {
            String strUserName = currentUser.getUnitName();
            arrayOfUsers.add(strUserName);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrayOfUsers);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnits.setAdapter(arrayAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void validateEntries() {
        btnMoveOut.setEnabled(false);
        String strSourceName = edtUserName.getText().toString();
        if (TextUtils.isEmpty(strSourceName)) {
            edtUserName.setError(getString(R.string.please_enter) + " " + " User Name");
            return;
        } else {
            edtUserName.requestFocus();
            edtUserName.setError(null);
        }
        //Quantity
        String strQuantity = edtQuantity.getText().toString();
        if (TextUtils.isEmpty(strQuantity)) {
            edtQuantity.setError("Please " + getString(R.string.edittext_hint_quantity));
            return;
        } else {
            edtQuantity.requestFocus();
            edtQuantity.setError(null);
        }
        uploadImages_addItemToLocal();
    }

    private void uploadImages_addItemToLocal() {
        if (arrayImageFileList != null && arrayImageFileList.size() > 0) {
            File sendImageFile = arrayImageFileList.get(0);
            File compressedImageFile = sendImageFile;
            try {
                compressedImageFile = new Compressor(getActivity()).compressToFile(sendImageFile);
            } catch (IOException e) {
                Timber.i("IOException", "uploadImages_addItemToLocal: image compression failed");
            }
            String strToken = AppUtils.getInstance().getCurrentToken();
            AndroidNetworking.upload(AppURL.API_IMAGE_UPLOAD_INDEPENDENT + strToken)
                    .setPriority(Priority.MEDIUM)
                    .addMultipartFile("image", compressedImageFile)
                    .addMultipartParameter("image_for", "inventory_transfer")
                    .addHeaders(AppUtils.getInstance().getApiHeaders())
                    .setTag("uploadImages_addItemToLocal")
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            arrayImageFileList.remove(0);
                            try {
                                String fileName = response.getString("filename");
                                jsonImageNameArray.put(fileName);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            uploadImages_addItemToLocal();
                        }

                        @Override
                        public void onError(ANError anError) {
                            AppUtils.getInstance().logApiError(anError, "uploadImages_addItemToLocal");
                        }
                    });
        } else {
            requestToMoveOut();
        }
    }

    private void requestToMoveOut() {
        AppUtils.getInstance().initializeProgressBar(mainRelative,mContext);
        AppUtils.getInstance().showProgressBar(mainRelative,true);
        JSONObject params = new JSONObject();
        try {
            params.put("project_site_id_from", AppUtils.getInstance().getCurrentSiteId());
            params.put("name", "user");
            params.put("source_name", edtUserName.getText().toString());
            params.put("type", "OUT");
            params.put("inventory_component_id", inventoryComponentId);
            params.put("quantity", edtQuantity.getText().toString());
            if (unitQuantityItemRealmResults != null && !unitQuantityItemRealmResults.isEmpty()) {
                int unitId = unitQuantityItemRealmResults.get(spinnerUnits.getSelectedItemPosition()).getUnitId();
                params.put("unit_id", unitId);
            }
            params.put("remark", editTextAddNote.getText().toString());
            params.put("images", jsonImageNameArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_MATERIAL_MOVE_IN_OUT + AppUtils.getInstance().getCurrentToken())
                .setTag("materialCreateTransfer")
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                            AppUtils.getInstance().showProgressBar(mainRelative,false);
                            getActivity().finish();
                            btnMoveOut.setEnabled(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logRealmExecutionError(anError);
                    }
                });
    }

    private void chooseAction(int type, Class aClass) {
        Intent intent = new Intent(mContext, aClass);
        Params params = new Params();
        params.setCaptureLimit(AppConstants.IMAGE_PICK_CAPTURE_LIMIT);
        params.setToolbarColor(R.color.colorPrimaryLight);
        params.setActionButtonColor(R.color.colorAccentDark);
        params.setButtonTextColor(R.color.colorWhite);
        intent.putExtra(Constants.KEY_PARAMS, params);
        startActivityForResult(intent, type);
    }

    private void requestToGetUserName(String strSearch) {
        JSONObject params=new JSONObject();
        try {
            params.put("employee_name",strSearch);
            params.put("project_site_id",AppUtils.getInstance().getCurrentSiteId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_AUTO_SUGGEST_FOR_PETICASH_EMPLOYEE)
                .setPriority(Priority.MEDIUM)
                .addJSONObjectBody(params)
                .setTag("requestToGetUserName")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("@@",response.toString());
                            jsonArrayForName = response.getJSONArray("data");
                            userNameArray = new ArrayList<>();
                            for (int i = 0; i < jsonArrayForName.length(); i++) {
                                JSONObject jsonObject = jsonArrayForName.getJSONObject(i);
                                userNameArray.add(jsonObject.getString("employee_name") + " - " + jsonObject.getString("format_employee_id"));
                            }
                            adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, userNameArray);
                            edtUserName.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logApiError(anError, "requestToGetUserName");
                    }
                });
    }
}
