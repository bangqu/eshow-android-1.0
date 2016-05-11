package cn.org.eshow.demo.common;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.org.eshow.demo.service.UpdateViewReveiver;
import cn.org.eshow.framwork.global.AbActivityManager;
import cn.org.eshow.framwork.util.AbLogUtil;

/**
 * Created by daikting on 16/1/12.
 */
public class CommonActivity  extends AppCompatActivity {
    private Context mContext = CommonActivity.this;
    private UpdateViewReveiver updateViewReveiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AbLogUtil.i(mContext, "onCreate");

        super.onCreate(savedInstanceState);
        AbActivityManager.getInstance().addActivity(this);

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
        AbLogUtil.i(mContext, "onDestroy");

        super.onDestroy();
        if(updateViewReveiver != null){
            unregisterReceiver(updateViewReveiver);
        }
    }
}