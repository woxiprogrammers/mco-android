package com.android.utils;

import android.view.View;

public interface SectionedRecyclerViewClickListener {
    void onSectionItemClick(View view, int position, int primaryKey);
}
