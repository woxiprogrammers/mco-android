package com.android.purchase_details;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;
import com.android.models.purchase_order.PurchaseOrderListItem;
import com.android.new_transaction_list.PurchaseOrderTransactionListingItem;
import com.android.new_transaction_list.TransactionDataItem;
import com.android.utils.AppConstants;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.ImageZoomDialogFragment;
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
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import id.zelory.compressor.Compressor;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;

public class PayFragmentNew extends Fragment implements FragmentInterface {
    @BindView(R.id.editTextBillumber)
    EditText editTextBillumber;
    @BindView(R.id.textViewCaptureMatImg)
    TextView textViewCaptureMatImg;
    @BindView(R.id.linearLayoutMatImg)
    LinearLayout linearLayoutMatImg;
    @BindView(R.id.llAddMatImg)
    LinearLayout llAddMatImg;
    @BindView(R.id.buttonActionGenerateGrn)
    Button buttonActionGenerateGrn;
    @BindView(R.id.editTextVehNum)
    EditText editTextVehNum;
    @BindView(R.id.editTextBillAmount)
    EditText editTextBillAmount;
    @BindView(R.id.editTextGrnNum)
    EditText editTextGrnNum;
    @BindView(R.id.linearLayoutGrnNum)
    LinearLayout linearLayoutGrnNum;
    @BindView(R.id.textViewCaptureTransImg)
    TextView textViewCaptureTransImg;
    @BindView(R.id.linearLayoutTransImg)
    LinearLayout linearLayoutTransImg;
    @BindView(R.id.ll_PaymentImageLayout)
    LinearLayout llPaymentImageLayout;
    @BindView(R.id.editextTransRemark)
    EditText editextTransRemark;
    @BindView(R.id.buttonActionSubmit)
    Button buttonActionSubmit;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.textViewVendor)
    TextView textViewVendor;
    @BindView(R.id.linearLayoutToVisible)
    LinearLayout linearLayoutToVisible;
    @BindView(R.id.linearLayoutInflateNames)
    LinearLayout linearLayoutInflateNames;
    @BindView(R.id.textViewSetVendor)
    TextView textViewSetVendor;
    @BindView(R.id.linearLayoutSetInflateNames)
    LinearLayout linearLayoutSetInflateNames;
    @BindView(R.id.llSetMatImg)
    LinearLayout llSetMatImg;
    @BindView(R.id.editTextSetGrnNum)
    EditText editTextSetGrnNum;
    @BindView(R.id.linearLayoutSetGrnNum)
    LinearLayout linearLayoutSetGrnNum;
    @BindView(R.id.editTextSetBillumber)
    EditText editTextSetBillumber;
    @BindView(R.id.editTextSetVehNum)
    EditText editTextSetVehNum;
    @BindView(R.id.editTextSetInTime)
    EditText editTextSetInTime;
    @BindView(R.id.editTextSetOutTime)
    EditText editTextSetOutTime;
    @BindView(R.id.editTextSetBillAmount)
    EditText editTextSetBillAmount;
    @BindView(R.id.linearLayoutSetBillAmount)
    LinearLayout linearLayoutSetBillAmount;
    @BindView(R.id.linearLayoutSetPaymentImageLayout)
    LinearLayout linearLayoutSetPaymentImageLayout;
    @BindView(R.id.editextSetTransRemark)
    EditText editextSetTransRemark;
    @BindView(R.id.cardViewTransRemark)
    CardView cardViewTransRemark;
    @BindView(R.id.linearLayoutFirstLayout)
    LinearLayout linearLayoutFirstLayout;
    @BindView(R.id.showImg)
    LinearLayout linearLayoutShowImg;
    @BindView(R.id.textViewShowMessage)
    TextView textViewShowMessage;
    private ArrayList<Integer> arrayList;
    Unbinder unbinder;
    private static int orderId;
    private ArrayList<File> arrayImageFileList;
    private Realm realm;
    private Context mContext;
    private DatePickerDialog.OnDateSetListener date;
    private RealmResults<MaterialNamesItem> materialNamesItems;
    private RealmList<TransactionDataItem> billDataItemRealmResults;
    private Calendar myCalendar;
    private boolean isForImage;
    private File currentImageFile;
    private JSONArray jsonImageNameArray = new JSONArray();
    private String strChallanNumber, strVehicleNumber;
    private TextView textViewIdDummy;
    private TextView textViewIdDummyView;
    private FrameLayout frameLayoutEdit;
    private AlertDialog alertDialog;
    private View inflatedView = null;
    private View viewData;
    private PurchaseOrderTransactionListingItem purchaseBIllDetailsItems;
    private int getId;
    private boolean isCheckedMaterial;
    private CheckBox checkBox;
    private static String strVendorName;
    private View layout;
    private PurchaseOrderListItem purchaseOrderListItem;

    public PayFragmentNew() {
        // Required empty public constructor
    }

    public static PayFragmentNew newInstance(int purchaseOrderId, String strVendor) {
        Bundle args = new Bundle();
        PayFragmentNew fragment = new PayFragmentNew();
        fragment.setArguments(args);
        orderId = purchaseOrderId;
        strVendorName = strVendor;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pay_new_design, container, false);
        unbinder = ButterKnife.bind(this, view);
        layout = view.findViewById(R.id.layoutView);
        mContext = getActivity();
        if (strVendorName != null) {
            textViewVendor.setText("Vendor Name : - " + strVendorName);
        }
        realm = Realm.getDefaultInstance();
        Timber.d(String.valueOf(orderId));
        purchaseOrderListItem = realm.where(PurchaseOrderListItem.class).equalTo("id", orderId).findFirst();
        int quantity = purchaseOrderListItem.getRemainingQuantity();
        if (quantity == 0) {
            textViewShowMessage.setVisibility(View.VISIBLE);
            linearLayoutFirstLayout.setVisibility(View.GONE);
        } else {
            String strGrnGenerated = purchaseOrderListItem.getGrnGenerated();
            Timber.d(strGrnGenerated);
            if (!TextUtils.isEmpty(strGrnGenerated)) {
                linearLayoutToVisible.setVisibility(View.VISIBLE);
                linearLayoutFirstLayout.setVisibility(View.GONE);
                editTextGrnNum.setText(purchaseOrderListItem.getGrnGenerated());
                requestForMaterialNames();
            } else {
                linearLayoutToVisible.setVisibility(View.GONE);
                linearLayoutFirstLayout.setVisibility(View.VISIBLE);
            }
        }

        if (purchaseOrderListItem.getListOfImages().size() > 0) {
            for (int index = 0; index < purchaseOrderListItem.getListOfImages().size(); index++) {
                ImageView imageView = new ImageView(getActivity());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
                layoutParams.setMargins(10, 10, 10, 10);
                imageView.setLayoutParams(layoutParams);
                linearLayoutShowImg.addView(imageView);
                final int finalIndex = index;
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openImageZoomFragment("http://test.mconstruction.co.in" + purchaseOrderListItem.getListOfImages().get(finalIndex).getImageUrl());
                    }
                });
                AppUtils.getInstance().loadImageViaGlide(purchaseOrderListItem.getListOfImages().get(index).getImageUrl(), imageView, mContext);

            }
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void fragmentBecameVisible() {
        if (PayAndBillsActivity.isForViewOnly) {
            setData(true);
        } else {
            setData(false);
        }
    }

    @OnClick({R.id.textViewCaptureMatImg, R.id.buttonActionGenerateGrn, R.id.textViewCaptureTransImg, R.id.buttonActionSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textViewCaptureMatImg:
                isForImage = true;
                captureImage();
                break;
            case R.id.buttonActionGenerateGrn:
                uploadImages_addItemToLocal("requestToGenerateGrn", "bill_transaction");

                break;
            case R.id.textViewCaptureTransImg:
                isForImage = false;
                captureImage();
                break;
            case R.id.buttonActionSubmit:
                validateEntries();
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
                if (isForImage)
                    addImages(imagesList, llAddMatImg);
                else
                    addImages(imagesList, llPaymentImageLayout);
                break;
        }
    }

    private void validateEntries() {
        strChallanNumber = editTextBillumber.getText().toString();
        strVehicleNumber = editTextVehNum.getText().toString();

        if (!isCheckedMaterial) {
            Toast.makeText(mContext, "Please Select At least One material", Toast.LENGTH_LONG).show();
            return;
        }
        //For Bill Number
        if (TextUtils.isEmpty(strChallanNumber)) {
            editTextBillumber.setFocusableInTouchMode(true);
            editTextBillumber.requestFocus();
            editTextBillumber.setError(getString(R.string.please_enter) + " " + getString(R.string.bill_number));
            return;
        } else {
            editTextBillumber.setError(null);
            editTextBillumber.requestFocus();
        }
        //For Vehicle Number
        if (TextUtils.isEmpty(strVehicleNumber)) {
            editTextVehNum.setFocusableInTouchMode(true);
            editTextVehNum.requestFocus();
            editTextVehNum.setError(getString(R.string.please_enter) + " " + getString(R.string.vehicle_number));
            return;
        } else {
            editTextVehNum.setError(null);
            editTextVehNum.requestFocus();
        }

        if (arrayImageFileList == null || arrayImageFileList.size() == 0) {
            Toast.makeText(mContext, "Please add at least one image", Toast.LENGTH_LONG).show();
            return;
        }
        uploadImages_addItemToLocal("requestToPayment", "bill_transaction");
    }

    //////////////API Calls///////////////////
    private void requestForMaterialNames() {
        JSONObject params = new JSONObject();
        try {
            params.put("purchase_order_id", orderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        realm = Realm.getDefaultInstance();
        AndroidNetworking.post(AppURL.API_PURCHASE_MATERIAL_UNITS_IMAGES_URL + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("API_PURCHASE_MATERIAL_UNITS_IMAGES_URL")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(MaterialUnitsImagesResponse.class, new ParsedRequestListener<MaterialUnitsImagesResponse>() {
                    @Override
                    public void onResponse(final MaterialUnitsImagesResponse response) {
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    Timber.d("Execute");
                                    //ToDo Check after multiple items in Purchase Order
                                    realm.delete(MaterialNamesItem.class);
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    inflateViews();
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
                        AppUtils.getInstance().logRealmExecutionError(anError);
                    }
                });
    }

    private void requestToGenerateGrn() {

        if (arrayImageFileList == null || arrayImageFileList.size() != 0) {
            Toast.makeText(mContext, "Please add at least one image", Toast.LENGTH_LONG).show();
            return;
        }
        JSONObject params = new JSONObject();
        /**/
        try {
            params.put("purchase_order_id", orderId);
            params.put("images", jsonImageNameArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_REQUEST_GENRATE_GRN_PURCHASE_ORDER_PAY + AppUtils.getInstance().getCurrentToken())
                .setTag("requestToGenerateGrn")
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            requestForMaterialNames();
                            Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                            JSONObject jsonObject = response.getJSONObject("data");
                            String grnNUm = jsonObject.getString("grn");
                            editTextGrnNum.setText(grnNUm);
                            buttonActionGenerateGrn.setVisibility(View.GONE);
                            linearLayoutToVisible.setVisibility(View.VISIBLE);
//                            checkBox.setEnabled(false);
//                            linearLayoutInflateNames.setEnabled(false);
//                            frameLayoutEdit.setEnabled(false);
                            linearLayoutMatImg.setVisibility(View.GONE);
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

    private void requestToPayment() {

        JSONObject params = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int intKey : arrayList) {
            for (MaterialNamesItem mItem : materialNamesItems) {
                if (mItem.getId() == intKey) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        if (mItem.getQuantity() == 0) {
                            Toast.makeText(mContext, "Please add Quantity for selected material", Toast.LENGTH_LONG).show();
                            return;
                        }
                        jsonObject.put("purchase_order_component_id", mItem.getId());
                        jsonObject.put("quantity", mItem.getQuantity());
                        jsonObject.put("unit_id", mItem.getMaterialUnits().get(0).getUnitId());
                        jsonArray.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        try {
            params.put("vehicle_number", strVehicleNumber);
            if (!editTextBillAmount.getText().toString().isEmpty()) {
                params.put("bill_amount", editTextBillAmount.getText().toString());
            } else {
                params.put("bill_amount", null);
            }
            params.put("remark", editextTransRemark.getText().toString());
            params.put("bill_number", strChallanNumber);
            params.put("grn", editTextGrnNum.getText().toString());
            params.put("images", jsonImageNameArray);
            params.put("item_list", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_PURCHASE_ORDER_PAYMENT_URL + AppUtils.getInstance().getCurrentToken())
                .setTag("requestToPayment")
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                            ((PayAndBillsActivity) mContext).moveFragments(true);
                            clearData();
                            nestedScrollView.setVisibility(View.GONE);
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

    private void captureImage() {
        Intent intent = new Intent(mContext, MultiCameraActivity.class);
        Params params = new Params();
        params.setCaptureLimit(AppConstants.IMAGE_PICK_CAPTURE_LIMIT);
        params.setToolbarColor(R.color.colorPrimaryLight);
        params.setActionButtonColor(R.color.colorAccentDark);
        params.setButtonTextColor(R.color.colorWhite);
        intent.putExtra(Constants.KEY_PARAMS, params);
        startActivityForResult(intent, Constants.TYPE_MULTI_CAPTURE);
    }

    private void addImages(ArrayList<Image> imagesList, LinearLayout layout) {
        layout.removeAllViews();
        arrayImageFileList = new ArrayList<File>();
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
                layout.addView(imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mContext, "Image Clicked", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void openDialog(int id, final boolean isForEdit) {
        getId = id;
        realm = Realm.getDefaultInstance();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_edit_purchase_order_for_material, null);
        alertDialogBuilder.setView(dialogView);
        EditText edittextMatUnit = dialogView.findViewById(R.id.edittextMatUnit);
        final EditText editTextMatQuantity = dialogView.findViewById(R.id.editTextMatQuantity);
        TextView textViewMaterialNameSelected = dialogView.findViewById(R.id.textViewMaterialNameSelected);
        final TextView TextViewExceedQuantity = dialogView.findViewById(R.id.TextViewExceedQuantity);
        final Button buttonToOk = dialogView.findViewById(R.id.buttonToOk);
        final MaterialNamesItem materialNamesItem = realm.where(MaterialNamesItem.class).equalTo("id", getId).findFirst();

        if (isForEdit) {
            if (materialNamesItem != null) {
                Timber.d(String.valueOf(materialNamesItem));
                edittextMatUnit.setText(materialNamesItem.getMaterialUnits().get(0).getUnit());
                textViewMaterialNameSelected.setText(materialNamesItem.getMaterialName());
                edittextMatUnit.setEnabled(false);

                final float floatMaterialComponentRemainingQuantity = Float.parseFloat(materialNamesItem.getMaterialComponentRemainingQuantity());
                editTextMatQuantity.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int arg1, int arg2, int arg3) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (!TextUtils.isEmpty(s.toString())) {
                            if (Float.parseFloat(s.toString()) > floatMaterialComponentRemainingQuantity) {
                                TextViewExceedQuantity.setText("Quantity should be less than " + String.valueOf(floatMaterialComponentRemainingQuantity));
                                TextViewExceedQuantity.setVisibility(View.VISIBLE);
                                buttonToOk.setVisibility(View.GONE);
                            } else {
                                TextViewExceedQuantity.setText("");
                                TextViewExceedQuantity.setVisibility(View.GONE);
                                buttonToOk.setVisibility(View.VISIBLE);

                            }
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {
                    }
                });
            }
        } else {
            TransactionDataItem billDataItem = realm.where(TransactionDataItem.class).equalTo("purchaseOrderTransactionComponentId", getId).findFirst();
            editTextMatQuantity.setText(billDataItem.getMaterialQuantity());
            editTextMatQuantity.setEnabled(false);
            edittextMatUnit.setText(billDataItem.getUnitName());
            edittextMatUnit.setEnabled(false);
            textViewMaterialNameSelected.setText(billDataItem.getMaterialName());
            LinearLayout linearLayout = dialogView.findViewById(R.id.llAddQuoImg);

        }
        buttonToOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(editTextMatQuantity.getText().toString())) {
                    editTextMatQuantity.setError("Please Enter Quantity");
                    return;
                }
                if (isForEdit) {
                    realm = Realm.getDefaultInstance();
                    try {
                        realm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                MaterialNamesItem materialNamesItem = realm.where(MaterialNamesItem.class).equalTo("id", getId).findFirst();
                                materialNamesItem.setQuantity(Float.parseFloat(editTextMatQuantity.getText().toString()));
                                realm.copyToRealmOrUpdate(materialNamesItem);
                            }
                        }, new Realm.Transaction.OnSuccess() {
                            @Override
                            public void onSuccess() {
                                Timber.d("Realm Execution Successful");
                                alertDialog.dismiss();
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
                } else {
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    //    bill_transaction
    private void uploadImages_addItemToLocal(final String strTag, final String imageFor) {
        if (arrayImageFileList != null && arrayImageFileList.size() > 0) {
            File sendImageFile = arrayImageFileList.get(0);
            File compressedImageFile = sendImageFile;
            try {
                compressedImageFile = new Compressor(mContext).compressToFile(sendImageFile);
            } catch (IOException e) {
                Timber.i("IOException", "uploadImages_addItemToLocal: image compression failed");
            }
            String strToken = AppUtils.getInstance().getCurrentToken();
            AndroidNetworking.upload(AppURL.API_IMAGE_UPLOAD_INDEPENDENT + strToken)
                    .setPriority(Priority.MEDIUM)
                    .addMultipartFile("image", compressedImageFile)
                    .addMultipartParameter("image_for", imageFor)
                    .addHeaders(AppUtils.getInstance().getApiHeaders())
                    .setTag("uploadImages_addItemToLocal")
                    .setPercentageThresholdForCancelling(50)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String fileName = response.getString("filename");
                                jsonImageNameArray.put(fileName);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            arrayImageFileList.remove(0);
                            uploadImages_addItemToLocal(strTag, imageFor);
                        }

                        @Override
                        public void onError(ANError anError) {
                            AppUtils.getInstance().logApiError(anError, "uploadImages_addItemToLocal");
                        }
                    });
        } else {
            if (strTag.equalsIgnoreCase("requestToGenerateGrn"))
                requestToGenerateGrn();
            else if (strTag.equalsIgnoreCase("requestToPayment")) {
                requestToPayment();
            }
        }
    }

    private void setData(boolean isFromClick) {
        if (isFromClick) {
            linearLayoutFirstLayout.setVisibility(View.GONE);
            linearLayoutToVisible.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
            nestedScrollView.setVisibility(View.VISIBLE);
            realm = Realm.getDefaultInstance();
            purchaseBIllDetailsItems = realm.where(PurchaseOrderTransactionListingItem.class).equalTo("grn", PayAndBillsActivity.idForBillItem).findFirst();
            if (purchaseBIllDetailsItems != null) {
                editTextSetBillumber.setText(purchaseBIllDetailsItems.getBillNumber());
                editTextSetVehNum.setText(purchaseBIllDetailsItems.getVehicleNumber());
                editTextSetOutTime.setText(purchaseBIllDetailsItems.getOutTime());
                editTextSetBillAmount.setText(purchaseBIllDetailsItems.getBillAmount());
                editTextSetGrnNum.setText(purchaseBIllDetailsItems.getGrn());
                editTextSetInTime.setText(purchaseBIllDetailsItems.getInTime());
                editextSetTransRemark.setText(purchaseBIllDetailsItems.getRemark());
                llSetMatImg.removeAllViews();
                linearLayoutSetPaymentImageLayout.removeAllViews();
                for (int i = 0; i < purchaseBIllDetailsItems.getImages().size(); i++) {
                    if (purchaseBIllDetailsItems.getImages().get(i).getImage_status().equalsIgnoreCase("Pre-GRN")) {
                        loadImage(purchaseBIllDetailsItems.getImages().get(i).getImageUrl(), llSetMatImg);

                    } else {
                        loadImage(purchaseBIllDetailsItems.getImages().get(i).getImageUrl(), linearLayoutSetPaymentImageLayout);

                    }
                }
                setViewData();
            } else {
                linearLayoutFirstLayout.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
            }
        }
    }

    private void clearData() {
        editTextBillumber.setText("");
        editTextVehNum.setText("");
        editTextBillAmount.setText("");
    }

    private void loadImage(final String strUrl, LinearLayout linearLayout) {
        ImageView imageView = new ImageView(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(150, 150);
        layoutParams.setMargins(10, 10, 10, 10);
        imageView.setLayoutParams(layoutParams);
        linearLayout.addView(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageZoomFragment("http://test.mconstruction.co.in" + strUrl);
            }
        });
        AppUtils.getInstance().loadImageViaGlide(strUrl, imageView, mContext);

    }

    private void inflateViews() {
        realm = Realm.getDefaultInstance();
        arrayList = new ArrayList<>();
        materialNamesItems = realm.where(MaterialNamesItem.class).notEqualTo("materialComponentRemainingQuantity", "0").or().notEqualTo("materialComponentRemainingQuantity", "0.0").findAll();
        for (int i = 0; i < materialNamesItems.size(); i++) {
            final MaterialNamesItem materialNamesItem = materialNamesItems.get(i);
            inflatedView = getActivity().getLayoutInflater().inflate(R.layout.inflate_multiple_material_names, null, false);
            inflatedView.setId(i);
            checkBox = inflatedView.findViewById(R.id.checkboxMaterials);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        isCheckedMaterial = true;
                        arrayList.add(materialNamesItem.getId());
                    } else {
                        isCheckedMaterial = false;
                        try {
                            arrayList.remove(materialNamesItem.getId());
                        } catch (Exception e) {
                            Log.i("PayFragmentNew", "onCheckedChanged: key does not exist");
                        }
                    }
                }
            });
            checkBox.setChecked(false);
            frameLayoutEdit = inflatedView.findViewById(R.id.frameLayoutEdit);
            textViewIdDummy = inflatedView.findViewById(R.id.textViewIdDummy);
            textViewIdDummy.setText(materialNamesItem.getId() + "");
            checkBox.setText(materialNamesItem.getMaterialName());
            frameLayoutEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView textViewId = view.findViewById(R.id.textViewIdDummy);
                    int intTemp = Integer.parseInt(textViewId.getText().toString());
                    MaterialNamesItem materialNamesItem = realm.where(MaterialNamesItem.class).equalTo("id", intTemp).findFirst();
                    int index = materialNamesItems.indexOf(materialNamesItem);
                    View currentView = linearLayoutInflateNames.findViewById(index);
                    CheckBox currentCheckbox = currentView.findViewById(R.id.checkboxMaterials);
                    if (currentCheckbox.isChecked()) {
                        openDialog(intTemp, true);
                    } else {
                        Toast.makeText(getActivity(), "Please Select Material", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            linearLayoutInflateNames.addView(inflatedView);
        }
    }

    private void setViewData() {
        realm = Realm.getDefaultInstance();
        arrayList = new ArrayList<>();
        billDataItemRealmResults = realm.where(PurchaseOrderTransactionListingItem.class).equalTo("grn", PayAndBillsActivity.idForBillItem).findFirst().getTransactionData();
        linearLayoutSetInflateNames.removeAllViews();
        for (int i = 0; i < billDataItemRealmResults.size(); i++) {
            final TransactionDataItem transactionDataItem = billDataItemRealmResults.get(i);
            viewData = getActivity().getLayoutInflater().inflate(R.layout.linear_view_multiple_material_purchase_order, null, false);
            viewData.setId(i);
            TextView name = viewData.findViewById(R.id.name);
            name.setText(transactionDataItem.getMaterialName());
            FrameLayout frameLayoutView = viewData.findViewById(R.id.frameLayoutView);
            textViewIdDummyView = viewData.findViewById(R.id.textViewIdDummyView);
            textViewIdDummyView.setText(transactionDataItem.getPurchaseOrderTransactionComponentId() + "");
            frameLayoutView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView textViewId = view.findViewById(R.id.textViewIdDummyView);
                    int intTemp = Integer.parseInt(textViewId.getText().toString());
                    openDialog(intTemp, false);
                }
            });
            linearLayoutSetInflateNames.addView(viewData);
        }
    }

    private void openImageZoomFragment(String url) {
        ImageZoomDialogFragment imageZoomDialogFragment = ImageZoomDialogFragment.newInstance(url);
        imageZoomDialogFragment.setCancelable(true);
        imageZoomDialogFragment.show(getActivity().getSupportFragmentManager(), "imageZoomDialogFragment");
    }
}
