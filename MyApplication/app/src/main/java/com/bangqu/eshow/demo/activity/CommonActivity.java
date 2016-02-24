package com.bangqu.eshow.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import com.bangqu.eshow.global.ESActivityManager;

/**
 * Created by daikting on 16/1/12.
 */
public class CommonActivity  extends Activity {
    private Context mContext = CommonActivity.this;

    public static Typeface kaitiTTF ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ESActivityManager.getInstance().addActivity(this);

        if(kaitiTTF == null){
            kaitiTTF = Typeface.createFromAsset(getAssets(), "fonts/kai.ttf");
        }
    }
}