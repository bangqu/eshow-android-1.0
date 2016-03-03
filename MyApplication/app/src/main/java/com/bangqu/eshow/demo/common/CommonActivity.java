package com.bangqu.eshow.demo.common;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bangqu.eshow.global.ESActivityManager;

/**
 * Created by daikting on 16/1/12.
 */
public class CommonActivity  extends AppCompatActivity {
    private Context mContext = CommonActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ESActivityManager.getInstance().addActivity(this);
    }
}