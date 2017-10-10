package com.android.purchase_details;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;
import com.android.models.purchase_bill.PurchaseBillListItem;
import com.android.utils.AppConstants;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vlk.multimager.activities.GalleryActivity;
import com.vlk.multimager.activities.MultiCameraActivity;
import com.vlk.multimager.utils.Constants;
import com.vlk.multimager.utils.Image;
import com.vlk.multimager.utils.Params;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PayFragment extends Fragment implements FragmentInterface {
    @BindView(R.id.spinner_select_material)
    Spinner spinner;
    @BindView(R.id.edittextQuantity)
    EditText edittextQuantity;
    @BindView(R.id.editText_Challan_Number)
    EditText editTextChallanNumber;
    @BindView(R.id.editText_VehicleNumber)
    EditText editTextVehicleNumber;
    @BindView(R.id.editText_InTime)
    EditText editTextInTime;
    @BindView(R.id.editText_OutTime)
    EditText editTextOutTime;
    @BindView(R.id.editText_BillAmount)
    EditText editTextBillAmount;
    @BindView(R.id.editText_PayableAmount)
    EditText editTextPayableAmount;
    @BindView(R.id.ll_PayableAmount)
    LinearLayout llPayableAmount;
    @BindView(R.id.editText_grnNumber)
    EditText editTextGrnNumber;
    @BindView(R.id.llgrnNumber)
    LinearLayout llgrnNumber;
    @BindView(R.id.editext_tapToAddNote)
    EditText editext_tapToAddNote;
    @BindView(R.id.buttonAction)
    Button buttonAction;
    @BindView(R.id.radio_Group)
    RadioGroup radioGroup;
    TextView textViewCaptureImages;
    TextView textViewPickImages;
    @BindView(R.id.spinner_select_units)
    Spinner spinnerSelectUnits;
    @BindView(R.id.llIm)
    LinearLayout llIm;
    @BindView(R.id.edittext_setNameOfMaterial)
    EditText edittextSetNameOfMaterial;
    @BindView(R.id.ll_materialName)
    LinearLayout llMaterialName;
    @BindView(R.id.edittext_setUnit)
    EditText edittextSetUnit;
    @BindView(R.id.ll_unit)
    LinearLayout llUnit;
    @BindView(R.id.frameLayout_materialSpinner)
    FrameLayout frameLayoutMaterialSpinner;
    @BindView(R.id.frameLayout_UnitSpinner)
    FrameLayout frameLayoutUnitSpinner;
    @BindView(R.id.ll_addImage)
    LinearLayout llAddImage;
    @BindView(R.id.ll_PaymentImageLayout)
    LinearLayout llPaymentImageLayout;
    @BindView(R.id.layoutBillImage)
    LinearLayout layoutBill_Image;
    @BindView(R.id.billPaymentImageLayout)
    LinearLayout billPayment_ImageLayout;
    private RadioButton radioPayButton;
    private Unbinder unbinder;
    private Realm realm;
    private Context mContext;
    private RealmResults<MaterialNamesItem> availableMaterialRealmResults;
    private RealmList<MaterialUnitsItem> unitsRealmResults;
    private RealmList<MaterialImagesItem> materialImagesItemRealmList;
    private List<MaterialNamesItem> availableMaterialArray;
    private List<MaterialUnitsItem> unitsArray;
    PurchaseBillListItem purchaseBIllDetailsItems = new PurchaseBillListItem();
    private List<MaterialImagesItem> materialImagesItemList;
    private String strQuantity, strUnit, strChallanNumber, strVehicleNumber, strInTime, strOutTime, strBillAmount, str_add_note, str;

    public PayFragment() {
        // Required empty public constructor
    }

    public static PayFragment newInstance() {
        Bundle args = new Bundle();
        PayFragment fragment = new PayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pay, container, false);
        textViewPickImages = view.findViewById(R.id.textView_pick);
        textViewCaptureImages = view.findViewById(R.id.textView_capture);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setUpSpinnerValueForUnits(availableMaterialRealmResults.get(i).getId());
                getMaterialImages(availableMaterialRealmResults.get(i).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        requestForMaterialNames();
        textViewPickImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });
        textViewCaptureImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseAction(Constants.TYPE_MULTI_CAPTURE, MultiCameraActivity.class);
            }
        });
        return view;
    }

    @Override
    public void fragmentBecameVisible() {
        if (PayAndBillsActivity.isForViewOnly) {
            setData(true);
        } else {
            setData(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.buttonAction)
    void onClickButtonAction(View view) {
        if (view.getId() == R.id.buttonAction) {
            validateEntries();
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
                if (PayAndBillsActivity.isForViewOnly) {
                    setImageToLayout(imagesList, Constants.TYPE_MULTI_CAPTURE, MultiCameraActivity.class, llPaymentImageLayout);
                } else {
                    setImageToLayout(imagesList, Constants.TYPE_MULTI_CAPTURE, MultiCameraActivity.class, llAddImage);
                }
                break;
            case Constants.TYPE_MULTI_PICKER:
                ArrayList<Image> imagesList2 = intent.getParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST);
                Timber.d(String.valueOf(imagesList2));
                if (PayAndBillsActivity.isForViewOnly) {
                    setImageToLayout(imagesList2, Constants.TYPE_MULTI_CAPTURE, GalleryActivity.class, llPaymentImageLayout);
                } else {
                    setImageToLayout(imagesList2, Constants.TYPE_MULTI_CAPTURE, GalleryActivity.class, llAddImage);
                }
                break;
        }
    }

    private void validateEntries() {
        String name = spinner.getSelectedItem().toString();
        strQuantity = edittextQuantity.getText().toString();
        strChallanNumber = editTextChallanNumber.getText().toString();
        strVehicleNumber = editTextVehicleNumber.getText().toString();
        strInTime = editTextInTime.getText().toString();
        strOutTime = editTextOutTime.getText().toString();
        strBillAmount = editTextBillAmount.getText().toString();
        //For Quantity
        if (TextUtils.isEmpty(strQuantity)) {
            edittextQuantity.setFocusableInTouchMode(true);
            edittextQuantity.requestFocus();
            edittextQuantity.setError("Please " + getString(R.string.edittext_hint_quantity));
            return;
        } else {
            edittextQuantity.setError(null);
            edittextQuantity.requestFocus();
        }
        //For Challan Number
        if (TextUtils.isEmpty(strChallanNumber)) {
            editTextChallanNumber.setFocusableInTouchMode(true);
            editTextChallanNumber.requestFocus();
            editTextChallanNumber.setError(getString(R.string.please_enter) + " " + getString(R.string.bill_number));
            return;
        } else {
            editTextChallanNumber.setError(null);
            editTextChallanNumber.requestFocus();
        }
        //For Vehicle Number
        if (TextUtils.isEmpty(strVehicleNumber)) {
            editTextVehicleNumber.setFocusableInTouchMode(true);
            editTextVehicleNumber.requestFocus();
            editTextVehicleNumber.setError(getString(R.string.please_enter) + " " + getString(R.string.vehicle_number));
            return;
        } else {
            editTextVehicleNumber.setError(null);
            editTextVehicleNumber.requestFocus();
        }
        //For In Time
        if (TextUtils.isEmpty(strInTime)) {
            editTextInTime.setFocusableInTouchMode(true);
            editTextInTime.requestFocus();
            editTextInTime.setError(getString(R.string.please_enter) + " " + getString(R.string.in_time));
            return;
        } else {
            editTextInTime.setError(null);
            editTextInTime.requestFocus();
        }
        //For Out Time
        if (TextUtils.isEmpty(strOutTime)) {
            editTextOutTime.setFocusableInTouchMode(true);
            editTextOutTime.requestFocus();
            editTextOutTime.setError(getString(R.string.please_enter) + " " + getString(R.string.out_time));
            return;
        } else {
            editTextOutTime.setError(null);
            editTextOutTime.requestFocus();
        }
        //For Bill Amount
        if (TextUtils.isEmpty(strBillAmount)) {
            editTextBillAmount.setFocusableInTouchMode(true);
            editTextBillAmount.requestFocus();
            editTextBillAmount.setError(getString(R.string.please_enter) + "Bill Amount ");
            return;
        } else {
            editTextBillAmount.setError(null);
            editTextBillAmount.requestFocus();
        }
        purchaseBIllDetailsItems.setId(1);
        purchaseBIllDetailsItems.setMaterialName(name);
        purchaseBIllDetailsItems.setMaterialUnit(strUnit);
        purchaseBIllDetailsItems.setPurchaseOrderId(strQuantity);
        purchaseBIllDetailsItems.setChallanNumber(strChallanNumber);
        purchaseBIllDetailsItems.setVehicleNumber(strVehicleNumber);
        purchaseBIllDetailsItems.setInTime(strInTime);
        purchaseBIllDetailsItems.setOutTime(strOutTime);
        purchaseBIllDetailsItems.setMaterialQuantity(strQuantity);
        purchaseBIllDetailsItems.setBillAmount(Integer.parseInt(strBillAmount));
        realm = Realm.getDefaultInstance();
        try {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Timber.d("Execute");
                    realm.insertOrUpdate(purchaseBIllDetailsItems);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    clearData();
                    ((PayAndBillsActivity) mContext).moveFragments(true);
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

    private void requestForMaterialNames() {
        JSONObject params=new JSONObject();
        try {
            params.put("purchase_order_id",1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        realm = Realm.getDefaultInstance();
        AndroidNetworking.post(AppURL.API_PURCHASE_MATERIAL_UNITS_IMAGES_URL + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("requestInventoryData")
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
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
                                    setUpSpinnerValueChangeListener();
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

    private void getMaterialImages(int selectedId) {
        realm = Realm.getDefaultInstance();
        MaterialNamesItem materialNamesItem = realm.where(MaterialNamesItem.class).equalTo("id", selectedId).findFirst();
        if (materialNamesItem != null) {
            materialImagesItemRealmList = materialNamesItem.getMaterialImages();
        }
        setImage(materialImagesItemRealmList);
    }

    private void setImage(RealmList<MaterialImagesItem> image) {
        llIm.removeAllViews();
        materialImagesItemList = realm.copyFromRealm(image);
        for (MaterialImagesItem currentUser : materialImagesItemList) {
            String strMaterialImageUrl = currentUser.getImageUrl();
            ImageView imageView = new ImageView(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
            layoutParams.setMargins(10, 10, 10, 10);
            imageView.setLayoutParams(layoutParams);
            llIm.addView(imageView);
            Log.i("##Url",strMaterialImageUrl);
            Glide.with(mContext).load("http://test.mconstruction.co.in" +strMaterialImageUrl)
                    .thumbnail(0.1f)
                    .crossFade()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageView);
        }
    }

    private void setUpSpinnerValueChangeListener() {
        realm = Realm.getDefaultInstance();
        availableMaterialRealmResults = realm.where(MaterialNamesItem.class).findAll();
        setUpSpinnerAdapter(availableMaterialRealmResults);
        /*if (availableMaterialRealmResults != null) {
            Timber.d("availableUsersRealmResults change listener added.");
            availableMaterialRealmResults.addChangeListener(new RealmChangeListener<RealmResults<MaterialNamesItem>>() {
                @Override
                public void onChange(RealmResults<MaterialNamesItem> availableUsersItems) {
                    Timber.d("Size of availableUsersItems: " + String.valueOf(availableUsersItems.size()));
                    setUpSpinnerAdapter(availableMaterialRealmResults);
                }
            });
        } else {
            AppUtils.getInstance().showOfflineMessage("PurchaseMaterialListActivity");
        }*/
    }

    private void setUpSpinnerValueForUnits(int selectedId) {
        realm = Realm.getDefaultInstance();
        MaterialNamesItem materialNamesItem = realm.where(MaterialNamesItem.class).equalTo("id", selectedId).findFirst();
        if (materialNamesItem != null) {
            unitsRealmResults = materialNamesItem.getMaterialUnits();
        }
        setUpSpinnerAdapterForUnits(unitsRealmResults);
    }

    private void setUpSpinnerAdapter(RealmResults<MaterialNamesItem> availableUsersItems) {
        availableMaterialArray = realm.copyFromRealm(availableUsersItems);
        ArrayList<String> arrayOfUsers = new ArrayList<String>();
        for (MaterialNamesItem currentUser : availableMaterialArray) {
            String strMaterialName = currentUser.getMaterialName();
            arrayOfUsers.add(strMaterialName);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrayOfUsers);
        if (arrayAdapter != null) {
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);
        }
    }

    private void setUpSpinnerAdapterForUnits(RealmList<MaterialUnitsItem> unitsItems) {
        unitsArray = realm.copyFromRealm(unitsItems);
        ArrayList<String> arrayOfUsers = new ArrayList<String>();
        for (MaterialUnitsItem currentUser : unitsArray) {
            String strMaterialUnit = currentUser.getUnit();
            arrayOfUsers.add(strMaterialUnit);
        }
        ArrayAdapter<String> arrayAdapterUnits = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrayOfUsers);
        if (arrayAdapterUnits != null) {
            arrayAdapterUnits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSelectUnits.setAdapter(arrayAdapterUnits);
        }
    }

    private void clearData() {
        edittextQuantity.setText("");
        editTextChallanNumber.setText("");
        editTextVehicleNumber.setText("");
        editTextInTime.setText("");
        editTextOutTime.setText("");
        editTextBillAmount.setText("");
    }

    private void setData(boolean isFromClick) {
        if (isFromClick) {
            llgrnNumber.setVisibility(View.VISIBLE);
            llPayableAmount.setVisibility(View.VISIBLE);
            llMaterialName.setVisibility(View.VISIBLE);
            llUnit.setVisibility(View.VISIBLE);
            frameLayoutMaterialSpinner.setVisibility(View.GONE);
            frameLayoutUnitSpinner.setVisibility(View.GONE);
            radioGroup.setVisibility(View.GONE);
            llAddImage.setVisibility(View.GONE);
            layoutBill_Image.setVisibility(View.GONE);
            billPayment_ImageLayout.setVisibility(View.VISIBLE);
            billPayment_ImageLayout.setVisibility(View.VISIBLE);
            //Non Editable Fields
            editTextChallanNumber.setEnabled(false);
            editTextVehicleNumber.setEnabled(false);
            editTextInTime.setEnabled(false);
            editTextOutTime.setEnabled(false);
            editTextBillAmount.setEnabled(false);
            edittextQuantity.setEnabled(false);
            editTextGrnNumber.setEnabled(false);
            spinner.setEnabled(false);
            realm = Realm.getDefaultInstance();
            PurchaseBillListItem purchaseBIllDetailsItems = realm.where(PurchaseBillListItem.class).equalTo("id", PayAndBillsActivity.idForBillItem).findFirst();
            if (purchaseBIllDetailsItems != null) {
                edittextSetNameOfMaterial.setText(purchaseBIllDetailsItems.getMaterialName());
                edittextSetUnit.setText(purchaseBIllDetailsItems.getMaterialUnit());
                edittextQuantity.setText(purchaseBIllDetailsItems.getMaterialQuantity());
                editTextChallanNumber.setText(purchaseBIllDetailsItems.getChallanNumber());
                editTextVehicleNumber.setText(purchaseBIllDetailsItems.getVehicleNumber());
                editTextInTime.setText(purchaseBIllDetailsItems.getInTime());
                editTextOutTime.setText(purchaseBIllDetailsItems.getOutTime());
                editTextBillAmount.setText(purchaseBIllDetailsItems.getBillAmount());
                editTextGrnNumber.setText(purchaseBIllDetailsItems.getPurchaseBillGrn());
                buttonAction.setText("Pay");
            }
        } else {
            buttonAction.setText("Submit");
            llgrnNumber.setVisibility(View.GONE);
            radioGroup.setVisibility(View.VISIBLE);
            llMaterialName.setVisibility(View.GONE);
            llPayableAmount.setVisibility(View.GONE);
            llUnit.setVisibility(View.GONE);
            frameLayoutMaterialSpinner.setVisibility(View.VISIBLE);
            frameLayoutUnitSpinner.setVisibility(View.VISIBLE);
            llAddImage.setVisibility(View.VISIBLE);
            layoutBill_Image.setVisibility(View.VISIBLE);
            billPayment_ImageLayout.setVisibility(View.GONE);
            billPayment_ImageLayout.setVisibility(View.GONE);
            editTextChallanNumber.setEnabled(true);
            editTextVehicleNumber.setEnabled(true);
            editTextInTime.setEnabled(true);
            editTextOutTime.setEnabled(true);
            editTextBillAmount.setEnabled(true);
            edittextQuantity.setEnabled(true);
            editTextGrnNumber.setEnabled(false);
            spinner.setEnabled(true);
        }
    }

    @OnClick(R.id.editText_InTime)
    public void onViewClicked() {
        setInOutTime(editTextInTime);
    }

    @OnClick(R.id.editText_OutTime)
    public void onViewClickedOutTime() {
        setInOutTime(editTextOutTime);
    }

    private void setInOutTime(final EditText currentEditText) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                currentEditText.setText(selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void chooseAction(int type, Class aClass) {
        Intent intent = new Intent(mContext, aClass);
        Params params = new Params();
        params.setCaptureLimit(10);
        params.setToolbarColor(R.color.colorPrimaryLight);
        params.setActionButtonColor(R.color.colorAccentDark);
        params.setButtonTextColor(R.color.colorWhite);
        intent.putExtra(Constants.KEY_PARAMS, params);
        startActivityForResult(intent, type);
    }

    private void setImageToLayout(ArrayList<Image> imageArrayList, final int type, final Class aClass, LinearLayout layout) {
        for (Image currentImage : imageArrayList) {
            if (currentImage.imagePath != null) {
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
}
