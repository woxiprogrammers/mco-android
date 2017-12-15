package com.android.dpr_module;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.constro360.R;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DPRHomeActivity extends Activity {

    @BindView(R.id.spinner_client)
    Spinner spinnerClient;
    @BindView(R.id.spinner_site)
    Spinner spinnerSite;
    @BindView(R.id.spinner_project)
    Spinner spinnerProject;
    @BindView(R.id.edittextNoOfLAbours)
    EditText edittextNoOfLAbours;
    @BindView(R.id.button_submit)
    Button buttonSubmit;
    private JSONArray jsonArray;
    private ArrayList<String> siteNameArray;
    private Context mContext;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dprhome);
        ButterKnife.bind(this);
        mContext=DPRHomeActivity.this;
        requestToGetSystemSites();
    }

    private void requestToGetSystemSites() {
        AndroidNetworking.get(AppURL.API_GET_SYSTEM_SITES)
                .setTag("requestToGetSystemSites")
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            jsonArray = response.getJSONArray("data");
                            siteNameArray = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                siteNameArray.add(jsonObject.getString("project_name"));
                            }
                            adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, siteNameArray);
                            spinnerClient.setAdapter(adapter);
//                            setProjectNameFromIndex(edit_text_selected_dest_name.getListSelection());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logApiError(anError, "requestToGetSystemSites");
                    }
                });
    }

}
