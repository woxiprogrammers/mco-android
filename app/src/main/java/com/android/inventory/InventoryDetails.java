package com.android.inventory;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;
import com.android.inventory.material.InventoryDetailsMoveFragment;
import com.android.inventory.material.MaterialHistoryFragment;
import com.android.utils.BaseActivity;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InventoryDetails extends BaseActivity {
    public static final int IMAGE_CHOOSER_CODE = 2612;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottom_navigation;

    @BindView(R.id.view_pager)
    ViewPager viewPagerInventory;
    private MenuItem prevMenuItem;
    private ArrayList<Integer> arrayList = new ArrayList<Integer>();
    private ImageUtilityHelper imageUtilityHelper;
    private Context mContext;
    public static final int WRITE_PERMISSION_CODE = 5;
    private InventoryDetailsMoveFragment inventoryDetailsMoveFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_view);
        initializeViews();
        callFragment();

    }

    private void initializeViews() {
        ButterKnife.bind(this);
        mContext = InventoryDetails.this;
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Details");
        }
        Intent intent = getIntent();
        if (intent != null) {
            arrayList = intent.getIntegerArrayListExtra("Array");
        }
        viewPagerInventory.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottom_navigation.getMenu().getItem(0).setChecked(false);
                }
                bottom_navigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottom_navigation.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_move:
                        viewPagerInventory.setCurrentItem(0);
                        break;
                    case R.id.action_history:
                        viewPagerInventory.setCurrentItem(1);
                        break;

                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void callFragment() {
        final InventoryDetailsViewPagerAdapter inventoryViewPagerAdapter = new InventoryDetailsViewPagerAdapter(getSupportFragmentManager());
        viewPagerInventory.setAdapter(inventoryViewPagerAdapter);
        viewPagerInventory.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                FragmentInterface fragment = (FragmentInterface) inventoryViewPagerAdapter.instantiateItem(viewPagerInventory, position);
                if (fragment != null) {
                    fragment.fragmentBecameVisible();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageUtilityHelper.onSelectionResult(requestCode, resultCode, data);
        imageUtilityHelper.deleteLocalImage();
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                inventoryDetailsMoveFragment = (InventoryDetailsMoveFragment) viewPagerInventory.getAdapter().instantiateItem(viewPagerInventory, 0);
                inventoryDetailsMoveFragment.addImageViewObject(mContext);
            }
        }
    }

    private class InventoryDetailsViewPagerAdapter extends FragmentPagerAdapter {
        private String[] arrBottomTitle = {"Bottom1", "Bottom2"};

        public InventoryDetailsViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return InventoryDetailsMoveFragment.newInstance(arrayList);
                case 1:
                    return MaterialHistoryFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return arrBottomTitle.length;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //TODO: Function that requires permission
                inventoryDetailsMoveFragment = (InventoryDetailsMoveFragment) viewPagerInventory.getAdapter().instantiateItem(viewPagerInventory, 0);
                inventoryDetailsMoveFragment.getImageChooser();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show permission explanation dialog...
                    Snackbar.make(findViewById(android.R.id.content), "Permission required for the function to work properly.", Snackbar.LENGTH_LONG)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ActivityCompat.requestPermissions(InventoryDetails.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_CODE);
                                }
                            })
                            .show();
                } else {
                    //Never ask again selected, or device policy prohibits the app from having that permission.
                    //So, disable that feature, or fall back to another situation...
                    //Open App Settings Page
                    Snackbar.make(findViewById(android.R.id.content), "You have denied this permission. Please allow this permission.", Snackbar.LENGTH_LONG)
                            .setAction("Settings", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intentSettings = new Intent();
                                    intentSettings.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intentSettings.addCategory(Intent.CATEGORY_DEFAULT);
                                    intentSettings.setData(Uri.parse("package:" + mContext.getPackageName()));
                                    intentSettings.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intentSettings.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    intentSettings.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                    mContext.startActivity(intentSettings);
                                }
                            }).show();
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void createObject(ImageView imageView) {
        imageUtilityHelper = new ImageUtilityHelper(mContext, imageView);
    }



}
