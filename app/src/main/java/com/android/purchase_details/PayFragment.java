package com.android.purchase_details;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class PayFragment extends Fragment implements FragmentInterface {

    @BindView(R.id.spinner_select_material)
    Spinner spinner;

    @BindView(R.id.edittextQuantity)
    EditText edittextQuantity;

    @BindView(R.id.editTextUnits)
    EditText editTextUnits;

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

    @BindView(R.id.textview_tapToAddNote)
    TextView textviewTapToAddNote;

    @BindView(R.id.buttonAction)
    Button buttonAction;
    @BindView(R.id.radio_Group)
    RadioGroup radioGroup;
    @BindView(R.id.textView_capture_images)
    TextView textViewCaptureImages;
    @BindView(R.id.textView_pick_images)
    TextView textViewPickImages;

    private RadioButton radioPayButton;
    private Unbinder unbinder;
    private Realm realm;
    private Context mContext;
    private RealmResults<MaterialNamesItem> availableMaterialRealmResults;
    private List<MaterialNamesItem> availableMaterialArray;
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
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                RadioButton radioButton = radioGroup.findViewById(i);
                String st = radioButton.getText().toString();
                if (st.equals("Create Ammetment")) {
                    buttonAction.setText("Create Ammetment");
                } else {
                    buttonAction.setText("Upload Bill");
                }
            }
        });
        requestForMaterialNames();
        setUpSpinnerValueChangeListener();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    @Override
    public void fragmentBecameVisible() {
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

    @OnClick(R.id.textview_tapToAddNote)
    void addNote(View view) {
        if (view.getId() == R.id.textview_tapToAddNote) {
            if (textviewTapToAddNote.getText().toString().equalsIgnoreCase("Show")) {
                openAddToNoteDialog("Show");
            } else {
                openAddToNoteDialog(getString(R.string.tap_too_add_note));
            }
        }
    }

    private void validateEntries() {
        strQuantity = edittextQuantity.getText().toString();
        strUnit = editTextUnits.getText().toString();
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
        //For Unit
        if (TextUtils.isEmpty(strUnit)) {
            editTextUnits.setFocusableInTouchMode(true);
            editTextUnits.requestFocus();
            editTextUnits.setError("Please " + getString(R.string.edittext_hint_units));
            return;
        } else {
            editTextUnits.setError(null);
            editTextUnits.requestFocus();
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

    }

    private void requestForMaterialNames() {
        realm = Realm.getDefaultInstance();
        AndroidNetworking.post(AppURL.API_PURCHASE_MATERIAL_UNITS_IMAGES_URL)
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
//                        AppUtils.getInstance().logRealmExecutionError(anError);
                        anError.printStackTrace();
                    }
                });
    }

    private void setUpSpinnerValueChangeListener() {
        realm = Realm.getDefaultInstance();
        availableMaterialRealmResults = realm.where(MaterialNamesItem.class).findAll();
        setUpSpinnerAdapter(availableMaterialRealmResults);
        if (availableMaterialRealmResults != null) {
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
        }
    }

    private void setUpSpinnerAdapter(RealmResults<MaterialNamesItem> availableUsersItems) {
        availableMaterialArray = realm.copyFromRealm(availableUsersItems);
        ArrayList<String> arrayOfUsers = new ArrayList<String>();
        for (MaterialNamesItem currentUser : availableMaterialArray) {
            String strMaterialName = currentUser.getMaterialName();
            arrayOfUsers.add(strMaterialName);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrayOfUsers);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    private void openAddToNoteDialog(String strMessage) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_tap_to_add_note, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        final EditText editText_add_note = ButterKnife.findById(dialogView, R.id.edit_text_add_note);
        final TextView textViewShowNote = ButterKnife.findById(dialogView, R.id.textViewShowNote);
        Button buttonOk = ButterKnife.findById(dialogView, R.id.button_ok);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_add_note = editText_add_note.getText().toString();
                if (TextUtils.isEmpty(str_add_note)) {
                    editText_add_note.setError("Please" + " " + getString(R.string.add_your_note));
                    editText_add_note.requestFocus();
                    editText_add_note.requestFocus();
                    return;
                } else {
                    editText_add_note.setError(null);
                    editText_add_note.requestFocus();
                    Toast.makeText(mContext, "Note Added", Toast.LENGTH_SHORT).show();
                    textViewShowNote.setVisibility(View.VISIBLE);
                    textViewShowNote.setText(str_add_note);
                    textviewTapToAddNote.setText("Show");
                    alertDialog.dismiss();

                }
            }

        });
        if (strMessage.equalsIgnoreCase("Show")) {
            editText_add_note.setFocusable(true);
            editText_add_note.setSelection(editText_add_note.getText().length());
            editText_add_note.requestFocus();
            editText_add_note.setText(str_add_note);
            textViewShowNote.setVisibility(View.VISIBLE);
        }
        alertDialog.show();
    }
}
