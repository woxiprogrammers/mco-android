package com.android.inventory.assets;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.constro360.R;
import com.android.utils.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityAssetsReadings extends BaseActivity {

    @BindView(R.id.ll_add_readings)
    LinearLayout llAddReadings;

    @BindView(R.id.iamgeButton_open_readings_menu)
    ImageButton iamgeButtonOpenReadingsMenu;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets_readings);
        ButterKnife.bind(this);
        initializeViews();
        inflateReadingLayout();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeViews() {
        mContext = ActivityAssetsReadings.this;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void inflateReadingLayout() {
        View child = getLayoutInflater().inflate(R.layout.item_add_asset_readings, null);
        llAddReadings.addView(child);
        ButterKnife.bind(this,child);

    }

    @OnClick(R.id.iamgeButton_open_readings_menu)
    public void onViewClicked() {
        Toast.makeText(mContext,"Hello",Toast.LENGTH_SHORT).show();
    }
}
