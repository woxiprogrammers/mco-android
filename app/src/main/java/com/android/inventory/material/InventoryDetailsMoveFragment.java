package com.android.inventory.material;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.inventory.SelectedMaterialListAdapter;
import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;
import com.android.utils.ImageUtilityHelper;
import com.android.inventory.InventoryDetails;
import com.android.models.inventory.MaterialListItem;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;

import static com.android.inventory.InventoryDetails.IMAGE_CHOOSER_CODE;

/**
 * A simple {@link Fragment} subclass.
 */
public class InventoryDetailsMoveFragment extends Fragment implements View.OnClickListener, FragmentInterface {

    @BindView(R.id.textview_materialCount)
    TextView text_view_materialCount;

    @BindView(R.id.destination_spinner)
    Spinner spinnerDestinations;

    @BindView(R.id.text_view_name)
    TextView text_ViewSetSelectedTextName;

    @BindView(R.id.edit_text_selected_dest_name)
    EditText edit_text_selected_dest_name;

    @BindView(R.id.ll_forSite)
    LinearLayout ll_forsite;

    @BindView(R.id.ll_forSupplierVehicle)
    LinearLayout ll_forSupplierVehicle;

    @BindView(R.id.ll_forSupplierInOutTime)
    LinearLayout ll_forSupplierInOutTime;

    @BindView(R.id.checkbox_moveInOut)
    CheckBox checkboxMoveInOut;

    @BindView(R.id.text_view_project_name)
    Spinner textViewProjectName;

    @BindView(R.id.edit_text_vehicleNumber)
    EditText editTextVehicleNumber;

    @BindView(R.id.edit_text_ChallanNumber)
    EditText editTextChallanNumber;

    @BindView(R.id.ll_challanNumber)
    LinearLayout llChallanNumber;

    @BindView(R.id.editText_addNote)
    EditText editTextAddNote;

    @BindView(R.id.editText_Date)
    EditText editText_Date;

    @BindView(R.id.button_move)
    Button buttonMove;

    @BindView(R.id.edit_text_inTime)
    EditText editTextInTime;

    @BindView(R.id.edit_text_outTime)
    EditText editTextOutTime;

    @BindView(R.id.ivChooseImage)
    ImageView selectImage;

    @BindView(R.id.ll_uploadImage)
    LinearLayout llUploadImage;

    private View mParentView;
    private int intMaterialCount;
    private String strSourceName, strDate, strVehicleNumber, strInTime, strOutTime, strBillNumber;
    private boolean isChecked;
    private ImageUtilityHelper imageUtilityHelper;

    private String str;

    private Context mContext;
    private SelectedMaterialListAdapter selectedMaterialListAdapter;
    private ArrayList<Integer> integerArrayList = new ArrayList<Integer>();

    public static InventoryDetailsMoveFragment newInstance(ArrayList<Integer> arrayMaterialCount) {
        Bundle args = new Bundle();
        args.putIntegerArrayList("arrayMaterialCount", arrayMaterialCount);
        InventoryDetailsMoveFragment fragment = new InventoryDetailsMoveFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public InventoryDetailsMoveFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.fragment_inventory_details_move, container, false);
        initializeViews();
        return mParentView;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initializeViews() {
        ButterKnife.bind(this, mParentView);
        mContext = getActivity();
        text_view_materialCount.setOnClickListener(this);
        buttonMove.setOnClickListener(this);
        selectImage.setOnClickListener(this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            integerArrayList = bundle.getIntegerArrayList("arrayMaterialCount");
        }
        intMaterialCount = integerArrayList.size();
        text_view_materialCount.setText(intMaterialCount + " " + "Item(s) selected");
        checkboxMoveInOut.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                } else {
                    llChallanNumber.setVisibility(View.GONE);
                }

            }
        });
        spinnerDestinations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int selectedItemIndex, long l) {
                switch (selectedItemIndex) {
                    //For Site
                    case 0:
                        text_ViewSetSelectedTextName.setText(getString(R.string.site_name));
                        ll_forsite.setVisibility(View.VISIBLE);
                        ll_forSupplierInOutTime.setVisibility(View.GONE);
                        ll_forSupplierVehicle.setVisibility(View.GONE);
                        str = getString(R.string.site_name);
                        break;
                    //For CLient
                    case 1:
                        text_ViewSetSelectedTextName.setText(getString(R.string.client_name));
                        ll_forsite.setVisibility(View.GONE);
                        ll_forSupplierInOutTime.setVisibility(View.GONE);
                        ll_forSupplierVehicle.setVisibility(View.GONE);
                        str = getString(R.string.client_name);
                        break;
                    //For Labour
                    case 2:
                        text_ViewSetSelectedTextName.setText(getString(R.string.labour_name));
                        ll_forsite.setVisibility(View.GONE);
                        ll_forSupplierInOutTime.setVisibility(View.GONE);
                        ll_forSupplierVehicle.setVisibility(View.GONE);
                        str = getString(R.string.labour_name);
                        break;
                    //For SubContracter
                    case 3:
                        text_ViewSetSelectedTextName.setText(getString(R.string.sub_contracter_name));
                        ll_forsite.setVisibility(View.GONE);
                        ll_forSupplierInOutTime.setVisibility(View.GONE);
                        ll_forSupplierVehicle.setVisibility(View.GONE);
                        str = getString(R.string.sub_contracter_name);
                        break;
                    //For Supplier
                    case 4:
                        text_ViewSetSelectedTextName.setText(getString(R.string.supplier_name));
                        ll_forSupplierInOutTime.setVisibility(View.VISIBLE);
                        ll_forSupplierVehicle.setVisibility(View.VISIBLE);
                        str = getString(R.string.supplier_name);
                        isChecked = true;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textview_materialCount:
                openMaterialListDialog();
                Toast.makeText(mContext, "onClick", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_move:
                validateEntries();
                break;
            case R.id.ivChooseImage:
                pickAndCropImage(view);
                break;
        }
    }

    private void openMaterialListDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext,R.style.DialogTheme);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_common_recycler_view_listing, null);
        dialogBuilder.setView(dialogView);

        RecyclerView rv_material_list = ButterKnife.findById(dialogView, R.id.rv_material_list);
        final Realm realm = Realm.getDefaultInstance();
        /*for (int i = 0; i < arrayList.size(); i++) {
            int strId = arrayList.get(i);
            final SearchMaterialListItem materialListItem = realm.where(SearchMaterialListItem.class).equalTo("id", strId).findFirst();
            materialListItems.add(materialListItem);
        }*/
        Integer[] integers = {1516, 1517, 1518, 1519, 1520, 1521, 1522, 1523};
        OrderedRealmCollection<MaterialListItem> materialListItems1 = realm.where(MaterialListItem.class).in("id", integers).findAll();
        selectedMaterialListAdapter = new SelectedMaterialListAdapter(materialListItems1, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_material_list.setLayoutManager(linearLayoutManager);
        rv_material_list.setAdapter(selectedMaterialListAdapter);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.show();
    }

    @Override
    public void fragmentBecameVisible() {

    }

    private void validateEntries() {

        strSourceName = edit_text_selected_dest_name.getText().toString();
        if (TextUtils.isEmpty(strSourceName)) {
            edit_text_selected_dest_name.setError(getString(R.string.please_enter) + " " + str);
            return;
        } else {
            edit_text_selected_dest_name.requestFocus();
            edit_text_selected_dest_name.setError(null);
        }

        //Date
        strDate = editText_Date.getText().toString();
        if (TextUtils.isEmpty(strDate)) {
            editText_Date.setError(getString(R.string.please_enter) + getString(R.string.date));
            return;
        } else {
            editText_Date.requestFocus();
            editText_Date.setError(null);
        }

        //Bill
        strBillNumber = editTextChallanNumber.getText().toString();
        if (TextUtils.isEmpty(strBillNumber)) {
            editTextChallanNumber.setError(getString(R.string.please_enter) + getString(R.string.bill_number));
            return;
        } else {
            editTextChallanNumber.setError(null);
            editTextChallanNumber.requestFocus();
        }

        if (isChecked) {
            //Vehicle Number
            strVehicleNumber = editTextVehicleNumber.getText().toString();
            if (TextUtils.isEmpty(strVehicleNumber)) {
                editTextVehicleNumber.setError(getString(R.string.please_enter) + getString(R.string.vehicle_number));
                return;
            } else {
                editTextVehicleNumber.setError(null);
                editTextVehicleNumber.requestFocus();
            }

            //In Time
            strInTime = editTextInTime.getText().toString();
            if (TextUtils.isEmpty(strInTime)) {
                editTextInTime.setError(getString(R.string.please_enter) + getString(R.string.in_time));
                return;
            } else {
                editTextInTime.setError(null);
                editTextInTime.requestFocus();
            }

            //Out Time
            strOutTime = editTextOutTime.getText().toString();

            if (TextUtils.isEmpty(strOutTime)) {
                editTextOutTime.setError(getString(R.string.please_enter) + getString(R.string.out_time));
                return;
            } else {
                editTextOutTime.setError(null);
                editTextOutTime.requestFocus();
            }
        }
        Toast.makeText(mContext, "Succes", Toast.LENGTH_SHORT).show();

    }

    String str_add_note;


    public void pickAndCropImage(View view) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, InventoryDetails.WRITE_PERMISSION_CODE);
        } else {
            //Permission allowed
            getImageChooser();
        }
    }

    public void getImageChooser() {
        imageUtilityHelper = new ImageUtilityHelper(mContext, selectImage);
        ((InventoryDetails) mContext).createObject(selectImage);
        Intent imageChooserIntent = imageUtilityHelper.getPickImageChooserIntent();
        startActivityForResult(imageChooserIntent, IMAGE_CHOOSER_CODE);
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imageChooserIntent = imageUtilityHelper.getPickImageChooserIntent();
                startActivityForResult(imageChooserIntent, IMAGE_CHOOSER_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageUtilityHelper.onSelectionResult(requestCode, resultCode, data);
        imageUtilityHelper.deleteLocalImage();

    }

    public void addImageViewObject(Context context) {
        ImageView imageView = new ImageView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
        layoutParams.setMargins(10,10,10,10);
        imageView.setLayoutParams(layoutParams);
        imageView.setBackgroundResource(R.drawable.ic_plus);
        llUploadImage.addView(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imageChooserIntent = imageUtilityHelper.getPickImageChooserIntent();
                startActivityForResult(imageChooserIntent, IMAGE_CHOOSER_CODE);
                ((InventoryDetails) mContext).createObject((ImageView) view);
            }
        });
    }
}
