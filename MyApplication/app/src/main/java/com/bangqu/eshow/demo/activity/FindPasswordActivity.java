package com.bangqu.eshow.demo.activity;

import android.os.Bundle;

import com.bangqu.eshow.demo.R;
import com.bangqu.eshow.demo.common.BackActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

/**
 * 找回密码的修改密码页面
 * Created by daikting on 16/2/24.
 */
@EActivity(R.layout.activity_findpassword)
public class FindPasswordActivity extends BackActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @AfterViews
    void init() {

    }
}
