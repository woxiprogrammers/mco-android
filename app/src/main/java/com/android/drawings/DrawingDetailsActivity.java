package com.android.drawings;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import com.android.utils.ImageZoomDialogFragment;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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

    public static String imageUrl;
    private Context mContext;
    private AlertDialog alert_Dialog;
    public static int drawingVersionId,subCatId;
    public static String imageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_drawing_details);
        initializeViews();

    }

    private void initializeViews() {
        mContext = DrawingDetailsActivity.this;
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        setTitle();
        call(drawingVersionId, imageUrl, true);
        if (bundle != null) {
            imageUrl = bundle.getString("url");
            drawingVersionId = bundle.getInt("getDrawingImageVersionId");
            imageName = bundle.getString("imageName");
            subCatId=bundle.getInt("subId");
        }
        imageViewPreview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                openImageZoomFragment("http://test.mconstruction.co.in" + imageUrl);
                return true;
            }
        });
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
                call(drawingVersionId, imageUrl, false);
                break;
            case R.id.textviewVersions:
                textviewVersions.setTextColor(getColor(R.color.colorAccent));
                textviewComments.setTextColor(getColor(R.color.black));
                getFragmentVersions();
                break;
        }
    }

    public void setTitle() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(imageName);
        }
    }

    private void openDialogToAddComment() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_add_comment_for_image_drawing, null);
        Button btnDismiss = dialogView.findViewById(R.id.button_dismiss_drawing_dialog);
        Button btnAddComment = dialogView.findViewById(R.id.button_assign_drawing_dialog);
        final EditText editTextAddComment = dialogView.findViewById(R.id.editTextAddComment);

        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert_Dialog.dismiss();
            }
        });
        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(editTextAddComment.getText().toString())) {
                    editTextAddComment.setError("Please add comment");
                    return;
                } else {
                    requestToAddComment(editTextAddComment.getText().toString());
                    alert_Dialog.dismiss();
                }
            }
        });
        alertDialogBuilder.setView(dialogView);
        alert_Dialog = alertDialogBuilder.create();
        alert_Dialog.show();
    }

    private void openImageZoomFragment(String url) {
        ImageZoomDialogFragment imageZoomDialogFragment = ImageZoomDialogFragment.newInstance(url);
        imageZoomDialogFragment.setCancelable(true);
        imageZoomDialogFragment.show(getSupportFragmentManager(), "imageZoomDialogFragment");
    }

    public void call(int drawingId, String imageUrl, boolean isLoadImage) {
        if (isLoadImage) {
            AppUtils.getInstance().loadImageViaGlide(imageUrl, imageViewPreview, mContext);
        }
        textviewVersions.setTextColor(getColor(R.color.black));
        textviewComments.setTextColor(getColor(R.color.colorAccent));
        getFragment(drawingId);
    }

    private void getFragment(int drawingVersionId) {
        DrawingCommentFragment drawingCommentFragment = DrawingCommentFragment.newInstance(drawingVersionId);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, drawingCommentFragment, "drawingCommentFragment");
        fragmentTransaction.commit();

    }

    private void getFragmentVersions() {
        DrawingVersionsFragment drawingVersionsFragment = DrawingVersionsFragment.newInstance(drawingVersionId,subCatId);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, drawingVersionsFragment, "drawingVersionsFragment");
        fragmentTransaction.commit();

    }

    private void requestToAddComment(String strComment) {
        JSONObject params = new JSONObject();
        try {
            params.put("drawing_image_version_id", drawingVersionId);
            params.put("comment", strComment);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_DRAWING_ADD_COMMENT + AppUtils.getInstance().getCurrentToken())
                .setTag("requestToAddComment")
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                            DrawingCommentFragment drawingCommentFragment = (DrawingCommentFragment) getSupportFragmentManager().findFragmentByTag("drawingCommentFragment");
                            if (drawingCommentFragment != null) {
                                drawingCommentFragment.requestToGetComments();
                            }
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
