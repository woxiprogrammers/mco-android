package com.android.material_request_approve;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.constro360.R;

public class AutoSuggestActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEtPincode, mEtEnterPostoffice;
    private RelativeLayout mRNoSeaarchFound;
    private LinearLayout llEnterPostOffice;
    private TextView llSuggestions;
    private Context mContext;
    private String mStrSearch;
    //    private ArrayList<AGPostOfficeName> mPostNameList;
    private AutoSuggestAdapter mPinCodeAdapter;
    private RecyclerView recyclerViewPincodeList;
    public static View.OnClickListener postOfficeClickListner;
    private String mStrState, mStrDistrict, mStrTaluka, mStrAtPost, mStrPinCOde;
    private ImageView ivClickOkPin, ivClickOkPost;
    private Button btnSendEnquiry;
    private Dialog dialog;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        /*getMenuInflater().inflate(R.menu.menu_logout, menu);
        menu.findItem(R.id.actionShare).setVisible(false);
        menu.findItem(R.id.actionHome).setVisible(false);
        menu.findItem(R.id.actionLogin).setVisible(false);
        menu.findItem(R.id.actionCart).setVisible(false);
        menu.findItem(R.id.actionMyAccount).setVisible(false);
        menu.findItem(R.id.actionMyOrders).setVisible(false);
        menu.findItem(R.id.actionMyReturns).setVisible(false);
        menu.findItem(R.id.actionTermsCond).setVisible(false);
        menu.findItem(R.id.actionretrnPolicy).setVisible(false);
        menu.findItem(R.id.actionContactUs).setVisible(false);
        if (mEtPincode.isSelected() && mEtEnterPostoffice.isSelected()) {
            menu.findItem(R.id.actionConfirm).setVisible(true);
        } else {
            menu.findItem(R.id.actionConfirm).setVisible(false);
        }*/
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_suggest);
        initializaViews();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle("");
        postOfficeClickListner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*int position = recyclerViewPincodeList.getChildLayoutPosition(v);
                mStrAtPost = mPostNameList.get(position).getAtPostName();
                mStrState = mPostNameList.get(position).getState();
                mStrDistrict = mPostNameList.get(position).getDistrict();
                mStrTaluka = mPostNameList.get(position).getTaluka();
                llEnterPostOffice.setVisibility(View.VISIBLE);
                mStrPinCOde = mEtPincode.getText().toString();
                mEtEnterPostoffice.setText(mStrAtPost);
                ivClickOkPost.setVisibility(View.VISIBLE);
                mEtEnterPostoffice.setSelected(true);
                invalidateOptionsMenu();*/
            }
        };
    }

    private void initializaViews() {
        mContext = AutoSuggestActivity.this;
//        mEtPincode = (EditText) findViewById(R.id.etPincode);
//        mEtEnterPostoffice = (EditText) findViewById(R.id.etEnterPostoffice);
//        mRNoSeaarchFound = (RelativeLayout) findViewById(R.id.rlNoSeaarchFound);
//        llSuggestions = (TextView) findViewById(R.id.llSuggestions);
//        llEnterPostOffice = (LinearLayout) findViewById(R.id.llEnterPostOffice);
        recyclerViewPincodeList = (RecyclerView) findViewById(R.id.recyclerViewPincodeList);
//        ivClickOkPin = (ImageView) findViewById(R.id.ivClickOk);
//        ivClickOkPost = (ImageView) findViewById(R.id.ivClickOkPost);
        btnSendEnquiry = (Button) findViewById(R.id.btnSendEnquiry);
        btnSendEnquiry.setOnClickListener(this);
        dialog = new Dialog(mContext);
        mEtEnterPostoffice.setEnabled(false);
        /*mEtPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mStrSearch = s.toString();
                if (mStrSearch.length() == 6) {
                    getResposeOfPincode(mStrSearch);
                    ivClickOkPin.setVisibility(View.VISIBLE);
                    mEtPincode.setSelected(true);
                    llEnterPostOffice.setVisibility(View.VISIBLE);
//                    new AGCommonMethods(mContext).hideKeyBoard(mEtPincode);
                } else {
                    invalidateOptionsMenu();
                    llEnterPostOffice.setVisibility(View.GONE);
                    ivClickOkPin.setVisibility(View.GONE);
                    recyclerViewPincodeList.setVisibility(View.GONE);
                    llSuggestions.setVisibility(View.GONE);
                    mRNoSeaarchFound.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });*/
    }

    private void getResposeOfPincode(String searchString) {
        /*String url = AGURLsAndConstants.API_GET_POST_OFFICE_FROM_PINCODE + searchString;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url
                , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Object object = new AGParser(mContext).parsePostData(response.toString());
                            if (object instanceof Boolean) {
                                mRNoSeaarchFound.setVisibility(View.VISIBLE);
                                recyclerViewPincodeList.setVisibility(View.GONE);
                                llEnterPostOffice.setVisibility(View.GONE);
                                llSuggestions.setVisibility(View.GONE);
                                ivClickOkPin.setVisibility(View.GONE);
                            } else if (object instanceof ArrayList) {
                                mRNoSeaarchFound.setVisibility(View.GONE);
                                recyclerViewPincodeList.setVisibility(View.VISIBLE);
                                llSuggestions.setVisibility(View.VISIBLE);
                                mPostNameList = (ArrayList<AGPostOfficeName>) object;
                                mPinCodeAdapter = new AGPinCodeSearchAdapter(mContext, mPostNameList);
                                recyclerViewPincodeList.setLayoutManager(new LinearLayoutManager(mContext));
                                recyclerViewPincodeList.setAdapter(mPinCodeAdapter);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        AppController.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, "getSearchResult");*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirmAddress() {
        /*AlertDialog.Builder alertDialog = new AlertDialog.Builder(AutoSuggestActivity.this);
        alertDialog.setTitle(getString(R.string.confirm));
        alertDialog.setMessage(getString(R.string.are_you_sure_you_want_to_confirm));
        alertDialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intentData = getIntent();
                Bundle bundleExtras = new Bundle();
                intentData.putExtra(getString(R.string.key_state), mStrState);
                intentData.putExtra(getString(R.string.key_district), mStrDistrict);
                intentData.putExtra(getString(R.string.key_taluka), mStrTaluka);
                intentData.putExtra(getString(R.string.key_at_post), mStrAtPost);
                intentData.putExtra(getString(R.string.key_at_pincode), mStrPinCOde);
                intentData.putExtras(bundleExtras);
                setResult(RESULT_OK, intentData);
                finish();
            }
        });
        alertDialog.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSendEnquiry:
                /*dialog.setContentView(R.layout.address_send_enquiry);
                final TextView pincode = (TextView) dialog.findViewById(R.id.tvProductPincodeEnquiry);
                final EditText mobileNumber = (EditText) dialog.findViewById(R.id.etProductMobileEnquiry);
                pincode.setText(mEtPincode.getText().toString().trim());
                Button tvCancelEnquiry = (Button) dialog.findViewById(R.id.btCancelEnquiry);
                Button tvSubmitEnquiry = (Button) dialog.findViewById(R.id.btSubmitEnquiry);
                final EditText etRemark = (EditText) dialog.findViewById(R.id.etRemark);
                mobileNumber.setText(AGAppSettings.getStringPref(PREFS_USER_MOBILE, mContext));
                dialog.setCancelable(false);
                mobileNumber.setSelection(mobileNumber.getText().length());
                mobileNumber.requestFocus();
                tvCancelEnquiry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                tvSubmitEnquiry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mobileNumber.length() > 10 || mobileNumber.length() < 10) {
                            mobileNumber.setError(getString(R.string.valid_mobile_number));
                            return;
                        } else if (etRemark.getText().toString().equals("")) {
                            etRemark.setError(getString(R.string.enter_comments));
                            return;
                        } else {
                            enquiryProduct(mEtPincode.getText().toString().trim(), mobileNumber.getText().toString().trim());
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();*/
                break;
        }
    }

    /*public void enquiryProduct(String pincode, String mobile_number) {
        JSONObject params = new JSONObject();
        try {
            params.put("product_id", 9);
            params.put("pincode", pincode);
            params.put("mobile", mobile_number);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = AGURLsAndConstants.API_ENQUIRY + "/" + AGAppSettings.getStringPref(PREFS_LANGUAGE_APPLIED, mContext);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.toString());
                            if (jsonObject.has("message")) {
                                String message = jsonObject.getString("message");
                                new AGAppSettings().showToastUserNotification(mContext, message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    try {
                        if (response.statusCode == STATUS_AUTH_ERROR) {
                            new AGCommonMethods(mContext).showMessage("" + (new JSONObject(new String(response.data))).getString("message"));
                        } else if (response.statusCode == STATUS_NOT_FOUND) {
                            new AGCommonMethods(mContext).showMessage("" + (new JSONObject(new String(response.data))).getString("message"));
                        } else if (response.statusCode == STATUS_INT_SERVER_ERROR) {
                            new AGCommonMethods(mContext).showMessage("" + (new JSONObject(new String(response.data))).getString("message"));
                        } else {
                            new AGCommonMethods(mContext).showMessage("" + (new JSONObject(new String(response.data))).getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json; charset=UTF-8");
                return headers;
            }
        };
        AppController.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest, "requestEnquiry");
    }*/
}

