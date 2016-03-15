package com.bangqu.eshow.demo.common;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bangqu.eshow.demo.service.UpdateViewReveiver;
import com.bangqu.eshow.global.ESActivityManager;

/**
 * Created by daikting on 16/1/12.
 */
public class CommonActivity  extends AppCompatActivity {
    private Context mContext = CommonActivity.this;
    private UpdateViewReveiver updateViewReveiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ESActivityManager.getInstance().addActivity(this);

        rigisterReceiver();
    }

    private void rigisterReceiver(){
        updateViewReveiver = new UpdateViewReveiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Global.EShow_Broadcast_Action.ACTION_EXIT);
        intentFilter.addAction(Global.EShow_Broadcast_Action.ACTION_MISSING_TOKEN);
        intentFilter.addAction(Global.EShow_Broadcast_Action.ACTION_EXIT_REPASSWORD);
        registerReceiver(updateViewReveiver,intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(updateViewReveiver != null){
            unregisterReceiver(updateViewReveiver);
        }
    }
}