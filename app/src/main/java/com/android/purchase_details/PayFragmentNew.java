package com.android.purchase_details;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.constro360.R;
import com.android.dummy.BillDataItem;
import com.android.dummy.PurchaseOrderBillListingItem;
import com.android.interfaces.FragmentInterface;
import com.android.utils.AppConstants;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vlk.multimager.activities.GalleryActivity;
import com.vlk.multimager.activities.MultiCameraActivity;
import com.vlk.multimager.utils.Constants;
import com.vlk.multimager.utils.Image;
import com.vlk.multimager.utils.Params;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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
    @BindView(R.id.textViewPickMatImg)
    TextView textViewPickMatImg;
    @BindView(R.id.linearLayoutMatImg)
    LinearLayout linearLayoutMatImg;
    @BindView(R.id.llAddMatImg)
    LinearLayout llAddMatImg;
    @BindView(R.id.buttonActionGenerateGrn)
    Button buttonActionGenerateGrn;
    @BindView(R.id.editTextVehNum)
    EditText editTextVehNum;
    @BindView(R.id.editTextInTime)
    EditText editTextInTime;
    @BindView(R.id.editTextBillAmount)
    EditText editTextBillAmount;
    @BindView(R.id.editTextGrnNum)
    EditText editTextGrnNum;
    @BindView(R.id.linearLayoutGrnNum)
    LinearLayout linearLayoutGrnNum;
    @BindView(R.id.textViewCaptureTransImg)
    TextView textViewCaptureTransImg;
    @BindView(R.id.textViewPickTransImg)
    TextView textViewPickTransImg;
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
    private ArrayList<Integer> arrayList;
    Unbinder unbinder;
    private static int orderId;
    private ArrayList<File> arrayImageFileList;
    private Realm realm;
    private Context mContext;
    private DatePickerDialog.OnDateSetListener date;
    private RealmResults<MaterialNamesItem> materialNamesItems;
    private RealmResults<BillDataItem> billDataItemRealmResults;
    private Calendar myCalendar;
    private boolean isForImage;
    private File currentImageFile;
    private JSONArray jsonImageNameArray = new JSONArray();
    private String strChallanNumber, strVehicleNumber;
    private TextView textViewIdDummy;
    private TextView textViewIdDummyView;
    private FrameLayout frameLayoutEdit;
    private AlertDialog alertDialog;
    private RealmList<MaterialUnitsData> materialUnitsData;
    private View inflatedView = null;
    private View viewData;
    private PurchaseOrderBillListingItem purchaseBIllDetailsItems;
    private int getId;
    private boolean isCheckedMaterial;
    private CheckBox checkBox;
    private static String strVendorName;
    View layout;

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
        requestForMaterialNames();
        if (strVendorName != null) {
            textViewVendor.setText("Vendor Name : - " + strVendorName);
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

    @OnClick({R.id.textViewCaptureMatImg, R.id.textViewPickMatImg, R.id.buttonActionGenerateGrn, R.id.editTextInTime, R.id.textViewCaptureTransImg, R.id.textViewPickTransImg, R.id.buttonActionSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textViewCaptureMatImg:
                isForImage = true;
                captureImage();
                break;
            case R.id.textViewPickMatImg:
                isForImage = true;
                pickImage();
                break;
            case R.id.buttonActionGenerateGrn:
                uploadImages_addItemToLocal("requestToGenerateGrn", "bill_transaction");
                break;
            case R.id.textViewCaptureTransImg:
                isForImage = false;
                captureImage();
                break;
            case R.id.textViewPickTransImg:
                isForImage = false;
                pickImage();
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
            case Constants.TYPE_MULTI_PICKER:
                ArrayList<Image> imagesList2 = intent.getParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST);
                if (isForImage)
                    addImages(imagesList2, llAddMatImg);
                else
                    addImages(imagesList2, llPaymentImageLayout);
                break;
        }
    }

    private void validateEntries() {
        strChallanNumber = editTextBillumber.getText().toString();
        strVehicleNumber = editTextVehNum.getText().toString();

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
        uploadImages_addItemToLocal("requestToPayment", "post_grn_bill_transaction");
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
        if (!isCheckedMaterial) {
            Toast.makeText(mContext, "Please Select At least One material", Toast.LENGTH_LONG).show();
            return;
        }
        if (arrayImageFileList == null || arrayImageFileList.size() != 0) {
            Toast.makeText(mContext, "Please add at least one image", Toast.LENGTH_LONG).show();
            return;
        }
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
            params.put("images", jsonImageNameArray);
            params.put("item_list", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Timber.d(String.valueOf(jsonArray));
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
                            Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                            JSONObject jsonObject = response.getJSONObject("data");
                            String grnNUm = jsonObject.getString("grn");
                            editTextGrnNum.setText(grnNUm);
                            buttonActionGenerateGrn.setVisibility(View.GONE);
                            linearLayoutToVisible.setVisibility(View.VISIBLE);
                            checkBox.setEnabled(false);
                            linearLayoutInflateNames.setEnabled(false);
                            frameLayoutEdit.setEnabled(false);
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
        try {
            params.put("vehicle_number", strVehicleNumber);
            if (!editTextBillAmount.getText().toString().isEmpty()) {
                params.put("bill_amount", editTextBillAmount.getText().toString());
            }
            params.put("remark", editextTransRemark.getText().toString());
            params.put("bill_number", strChallanNumber);
            params.put("type", "upload-bill");
            params.put("grn", editTextGrnNum.getText().toString());
            params.put("images", jsonImageNameArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("@@Check", params.toString());
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

    private void pickImage() {
        Intent intent = new Intent(mContext, GalleryActivity.class);
        Params params = new Params();
        params.setCaptureLimit(AppConstants.IMAGE_PICK_CAPTURE_LIMIT);
        params.setPickerLimit(AppConstants.IMAGE_PICK_CAPTURE_LIMIT);
        params.setToolbarColor(R.color.colorPrimaryLight);
        params.setActionButtonColor(R.color.colorAccentDark);
        params.setButtonTextColor(R.color.colorWhite);
        intent.putExtra(Constants.KEY_PARAMS, params);
        startActivityForResult(intent, Constants.TYPE_MULTI_PICKER);
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
        Button buttonToOk = dialogView.findViewById(R.id.buttonToOk);
        MaterialNamesItem materialNamesItem = realm.where(MaterialNamesItem.class).equalTo("id", getId).findFirst();
        if (isForEdit) {
            if (materialNamesItem != null) {
                Timber.d(String.valueOf(materialNamesItem));
                edittextMatUnit.setText(materialNamesItem.getMaterialUnits().get(0).getUnit());
                textViewMaterialNameSelected.setText(materialNamesItem.getMaterialName());
                edittextMatUnit.setEnabled(false);
            }
        } else {
            BillDataItem billDataItem = realm.where(BillDataItem.class).equalTo("id", getId).findFirst();
            editTextMatQuantity.setText(billDataItem.getMaterialQuantity());
            editTextMatQuantity.setEnabled(false);
            edittextMatUnit.setText(billDataItem.getUnitName());
            edittextMatUnit.setEnabled(false);
            textViewMaterialNameSelected.setText(billDataItem.getMaterialName());
            LinearLayout linearLayout = dialogView.findViewById(R.id.llAddQuoImg);
            for (int i = 0; i < billDataItem.getImages().size(); i++) {
                loadImage(billDataItem.getImages().get(i).getImageUrl(), linearLayout);
            }
        }
        buttonToOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isForEdit) {
                    realm = Realm.getDefaultInstance();
                    try {
                        realm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                MaterialNamesItem materialNamesItem = realm.where(MaterialNamesItem.class).equalTo("id", getId).findFirst();
                                materialNamesItem.setQuantity(Integer.parseInt(editTextMatQuantity.getText().toString()));
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
            layout.setVisibility(View.VISIBLE);
            realm = Realm.getDefaultInstance();
            purchaseBIllDetailsItems = realm.where(PurchaseOrderBillListingItem.class).equalTo("grn", PayAndBillsActivity.idForBillItem).findFirst();
            if (purchaseBIllDetailsItems != null) {
//              textViewSetVendor.setText("Vendor Name :- " + purchaseBIllDetailsItems.getVendor());
                editTextSetBillumber.setText(purchaseBIllDetailsItems.getBillData().get(0).getBillNumber());
                editTextSetVehNum.setText(purchaseBIllDetailsItems.getBillData().get(0).getVehicleNumber());
                editTextSetOutTime.setText(purchaseBIllDetailsItems.getBillData().get(0).getOutTime());
                editTextSetBillAmount.setText(purchaseBIllDetailsItems.getBillData().get(0).getBillAmount());
                editTextSetGrnNum.setText(purchaseBIllDetailsItems.getBillData().get(0).getPurchaseBillGrn());
                editTextSetInTime.setText(purchaseBIllDetailsItems.getBillData().get(0).getInTime());
                editextSetTransRemark.setText(purchaseBIllDetailsItems.getBillData().get(0).getRemark());
                llSetMatImg.removeAllViews();
                setViewData();
                /*if (purchaseBIllDetailsItems.getBillData().get(0).getImages().size() > 0) {
                    for (int index = 0; index < purchaseBIllDetailsItems.getBillData().get(0).getImages().size(); index++) {
                        loadImage(purchaseBIllDetailsItems.getBillData().get(0).getImages().get(index).getImageUrl(),llSetMatImg);
                    }
                }*/
            } else {
                linearLayoutFirstLayout.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
           /* linearLayoutToVisible.setVisibility(View.GONE);
            editTextBillumber.setEnabled(true);
            editTextVehNum.setEnabled(true);
            editTextInTime.setEnabled(true);
            editTextOutTime.setEnabled(true);
            editTextBillAmount.setEnabled(true);
            editTextGrnNum.setEnabled(false);*/
            }
        }
    }

    private void clearData() {
        editTextBillumber.setText("");
        editTextVehNum.setText("");
        editTextInTime.setText("");
        editTextBillAmount.setText("");
    }

    private void loadImage(String strUrl, LinearLayout linearLayout) {
        ImageView imageView = new ImageView(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(150, 150);
        layoutParams.setMargins(10, 10, 10, 10);
        imageView.setLayoutParams(layoutParams);
        linearLayout.addView(imageView);
        Glide.with(mContext).load("http://test.mconstruction.co.in" + strUrl)
                .thumbnail(0.1f)
                .crossFade()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    private void inflateViews() {
        realm = Realm.getDefaultInstance();
        arrayList = new ArrayList<>();
        materialNamesItems = realm.where(MaterialNamesItem.class).findAll();
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
        billDataItemRealmResults = realm.where(BillDataItem.class).equalTo("purchaseBillGrn", PayAndBillsActivity.idForBillItem).findAll();
        linearLayoutSetInflateNames.removeAllViews();
        for (int i = 0; i < billDataItemRealmResults.size(); i++) {
            final BillDataItem billDataItem = billDataItemRealmResults.get(i);
            viewData = getActivity().getLayoutInflater().inflate(R.layout.linear_view_multiple_material_purchase_order, null, false);
            viewData.setId(i);
            TextView name = viewData.findViewById(R.id.name);
            name.setText(billDataItem.getMaterialName());
            FrameLayout frameLayoutView = viewData.findViewById(R.id.frameLayoutView);
            textViewIdDummyView = viewData.findViewById(R.id.textViewIdDummyView);
            textViewIdDummyView.setText(billDataItem.getId() + "");
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
}
