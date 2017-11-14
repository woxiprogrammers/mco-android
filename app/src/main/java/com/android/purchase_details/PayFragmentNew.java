package com.android.purchase_details;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.Realm;
import io.realm.RealmResults;
import timber.log.Timber;

public class PayFragmentNew extends Fragment implements FragmentInterface {

    @BindView(R.id.radioButtonUploadBills)
    RadioButton radioButtonUploadBills;
    @BindView(R.id.radioButtonCreateAmmetments)
    RadioButton radioButtonCreateAmmetments;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
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
    @BindView(R.id.editTextInDate)
    EditText editTextInDate;
    @BindView(R.id.editTextInTime)
    EditText editTextInTime;
    @BindView(R.id.editTextOutDate)
    EditText editTextOutDate;
    @BindView(R.id.editTextOutTime)
    EditText editTextOutTime;
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
    @BindView(R.id.linearLayoutToVisible)
    LinearLayout linearLayoutToVisible;
    Unbinder unbinder;

    private static int orderId;
    @BindView(R.id.linearLayoutInflateNames)
    LinearLayout linearLayoutInflateNames;
    private Realm realm;
    private Context mContext;
    private RealmResults<MaterialNamesItem> materialNamesItems;

    public PayFragmentNew() {
        // Required empty public constructor
    }

    public static PayFragmentNew newInstance(int purchaseOrderId) {

        Bundle args = new Bundle();

        PayFragmentNew fragment = new PayFragmentNew();
        fragment.setArguments(args);
        orderId = purchaseOrderId;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pay_new_design, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        requestForMaterialNames();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.textViewEdit, R.id.textViewCaptureMatImg, R.id.textViewPickMatImg, R.id.buttonActionGenerateGrn, R.id.textViewCaptureTransImg, R.id.textViewPickTransImg, R.id.buttonActionSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textViewEdit:
                break;
            case R.id.textViewCaptureMatImg:
                break;
            case R.id.textViewPickMatImg:
                break;
            case R.id.buttonActionGenerateGrn:
                requestToGenerateGrn();
                break;
            case R.id.textViewCaptureTransImg:
                break;
            case R.id.textViewPickTransImg:
                break;
            case R.id.buttonActionSubmit:
                break;
        }
    }

    @Override
    public void fragmentBecameVisible() {

    }

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
        JSONObject params = new JSONObject();
        try {
            params.put("", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //ToDO Add API URL.................
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
                            linearLayoutToVisible.setVisibility(View.VISIBLE);
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

    private void inflateViews(){
        realm=Realm.getDefaultInstance();
        materialNamesItems=realm.where(MaterialNamesItem.class).findAll();
        View inflatedView = getActivity().getLayoutInflater().inflate(R.layout.inflate_multiple_material_names, linearLayoutInflateNames, false);
        CheckBox checkBox=inflatedView.findViewById(R.id.checkboxMaterials);
        TextView textViewEdit=inflatedView.findViewById(R.id.textViewEdit);
        linearLayoutInflateNames.addView(inflatedView);

    }
}
