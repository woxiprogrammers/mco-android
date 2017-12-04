package com.android.utils;

/**
 * Created by Sharvari on 2/12/17.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.constro360.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


/**
 * <b>public class ImageZoomDialogFragment extends DialogFragment</b>
 * <p>This class is used as Dialog Fragment to show Account Image with Zoom Support</p>
 * Created by Rohit.
 */
public class ImageZoomDialogFragment extends DialogFragment {
    public static String strFinalImageUrl = "";
    //    private AccountDetailsItem accountDetailsItem;
    private String strEventImageUrl;
    private Context mContext;

    public static ImageZoomDialogFragment newInstance(String strImageUrl) {
        Bundle args = new Bundle();
        args.putString("eventImageUrl", strImageUrl);
        ImageZoomDialogFragment fragment = new ImageZoomDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        if (getDialog().getWindow() != null) {
            ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
            // Assign window properties to fill the parent
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
        }
        // Call super onResume after sizing
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mParentView = inflater.inflate(R.layout.fragment_account_image_zoom, container, false);
        mContext = getActivity();
        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey("eventImageUrl")) {
                strEventImageUrl = args.getString("eventImageUrl");
            }
        }
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Toast.makeText(getActivity().getBaseContext(),"Double Touch or Pinch In/Out To Zoom", Toast.LENGTH_LONG).show();
        ImageView mIvAccountImage = (ImageView) mParentView.findViewById(R.id.ivAccountImage);
        ImageView mIvDialogClose = (ImageView) mParentView.findViewById(R.id.ivDialogClose);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mIvDialogClose.setElevation(4);
        }
        mIvDialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        strFinalImageUrl = strEventImageUrl;
        //Loading image from url.
        Glide.with(mContext)
                .load(strFinalImageUrl)
                .crossFade()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round)
                .into(mIvAccountImage);
        return mParentView;
    }
}
