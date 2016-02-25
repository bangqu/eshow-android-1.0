package com.bangqu.eshow.demo.common;

import com.bangqu.eshow.demo.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;

/**
 * 带返回按钮的公共Activity
 * Created by daikting on 16/2/24.
 */
@EActivity(R.layout.activity_base_annotation)
public class BackActivity extends CommonActivity {
    @AfterViews
    protected final void annotationDispayHomeAsUp() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OptionsItem(android.R.id.home)
    protected final void annotaionClose() {
        onBackPressed();
    }
}
