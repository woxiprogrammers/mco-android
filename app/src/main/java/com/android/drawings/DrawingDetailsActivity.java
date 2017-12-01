package com.android.drawings;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.purchase_details.PayAndBillsActivity;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DrawingDetailsActivity extends BaseActivity {

    @BindView(R.id.imageViewPreview)
    ImageView imageViewPreview;
    @BindView(R.id.textviewComments)
    TextView textviewComments;
    @BindView(R.id.textviewVersions)
    TextView textviewVersions;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;

    private String imageUrl;
    private Context mContext;
    private AlertDialog alert_Dialog;
    private int drawingVersionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_drawing_details);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Drawing Details");
        }
        mContext = DrawingDetailsActivity.this;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            imageUrl = bundle.getString("url");
            drawingVersionId=bundle.getInt("getDrawingImageVersionId");
        }
        AppUtils.getInstance().loadImageViaGlide("http://test.mconstruction.co.in" +imageUrl, imageViewPreview,mContext);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.imageViewPreview, R.id.textviewComments, R.id.textviewVersions})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageViewPreview:
                openDialogToAddComment();
                break;
            case R.id.textviewComments:
                textviewComments.setTextColor(getColor(R.color.colorAccent));
                textviewVersions.setTextColor(getColor(R.color.black));
                getFragment();
                break;
            case R.id.textviewVersions:
                textviewVersions.setTextColor(getColor(R.color.colorAccent));
                textviewComments.setTextColor(getColor(R.color.black));
                getFragmentVersions();
                break;
        }
    }

    private void openDialogToAddComment() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_add_comment_for_image_drawing, null);
        Button btnDismiss=dialogView.findViewById(R.id.button_dismiss_drawing_dialog);
        Button btnAddComment=dialogView.findViewById(R.id.button_assign_drawing_dialog);
        final EditText editTextAddComment=dialogView.findViewById(R.id.editTextAddComment);

        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert_Dialog.dismiss();
            }
        });
        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(editTextAddComment.getText().toString())){
                    editTextAddComment.setError("Please add comment");
                    return;
                }else {
                    requestToAddComment(editTextAddComment.getText().toString());
                    alert_Dialog.dismiss();
                }
            }
        });
        alertDialogBuilder.setView(dialogView);
        alert_Dialog = alertDialogBuilder.create();
        alert_Dialog.show();
    }

    private void getFragment() {
        DrawingCommentFragment drawingCommentFragment = DrawingCommentFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle bundleArgs = new Bundle();
        bundleArgs.putInt("drawingVersionId", drawingVersionId);
        drawingCommentFragment.setArguments(bundleArgs);
        fragmentTransaction.replace(R.id.frameLayout, drawingCommentFragment, "Fragment");
        fragmentTransaction.commit();

    }

    private void getFragmentVersions() {
        DrawingVersionsFragment drawingVersionsFragment = DrawingVersionsFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, drawingVersionsFragment, "Fragment");
        fragmentTransaction.commit();

    }

    private void requestToAddComment(String strComment){
        JSONObject params=new JSONObject();
        try {
            params.put("drawing_image_version_id",1);
            params.put("comment",strComment);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_DRAWING_ADD_COMMENT + AppUtils.getInstance().getCurrentToken())
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
}
