package com.android.drawings;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DrawingDetailsActivity extends BaseActivity {

    @BindView(R.id.imageViewPreview)
    ImageView imageViewPreview;
    @BindView(R.id.textviewComments)
    TextView textviewComments;
    @BindView(R.id.textviewVersions)
    TextView textviewVersions;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_drawing_details);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Drawing Details");
        }
    }
}
