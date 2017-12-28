package com.android.inventory;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;

/**
 * Created by Sharvari on 28/12/17.
 */

class AutoSuggectInventoryComponent extends BaseActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.peticash_form);
    }
}
