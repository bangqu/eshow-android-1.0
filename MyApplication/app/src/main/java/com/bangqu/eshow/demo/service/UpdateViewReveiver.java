package com.bangqu.eshow.demo.service;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bangqu.eshow.demo.activity.LoginActivity_;
import com.bangqu.eshow.demo.common.Global;
import com.bangqu.eshow.demo.common.SharedPrefUtil;
import com.bangqu.eshow.demo.view.ConfirmDialog;
import com.bangqu.eshow.util.ESLogUtil;

/**
 * 用于通知界面更新、界面操作、退出程序等的通知接收
 * Created by daikting on 16/3/15.
 */
public class UpdateViewReveiver extends BroadcastReceiver
{
    @Override
    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();
        ESLogUtil.d(context, "broadcast action:" + action);
        if(action.equals(Global.EShow_Broadcast_Action.ACTION_EXIT)){
            try {
                final Activity activity = (Activity) context;
                ConfirmDialog.OnCustomDialogListener onCustomDialogListener = new ConfirmDialog.OnCustomDialogListener() {
                    @Override
                    public void OnCustomDialogConfim(String str) {
                        SharedPrefUtil.logout(context);
                        LoginActivity_.intent(context).start();
                        activity.finish();
                    }

                    @Override
                    public void OnCustomDialogCancel(String str) {

                    }
                };
                ConfirmDialog confirmDialog = new ConfirmDialog(context,"退出","确定退出？","确定","取消",onCustomDialogListener);
                confirmDialog.show();
            }catch (Exception e){
                e.printStackTrace();
            }

        }else if (action.equals(Global.EShow_Broadcast_Action.ACTION_EXIT_REPASSWORD)) {//修改密码成功后退出
            try {
                final Activity activity = (Activity) context;
                ConfirmDialog.OnCustomDialogListener onCustomDialogListener = new ConfirmDialog.OnCustomDialogListener() {
                    @Override
                    public void OnCustomDialogConfim(String str) {
                        SharedPrefUtil.logout(context);
                        LoginActivity_.intent(context).start();
                        activity.finish();
                    }

                    @Override
                    public void OnCustomDialogCancel(String str) {
                        SharedPrefUtil.logout(context);
                        LoginActivity_.intent(context).start();
                        activity.finish();
                    }
                };
                ConfirmDialog confirmDialog = new ConfirmDialog(context,"退出","由于密码被修改，需要重新登录！","确定","取消",onCustomDialogListener);
                confirmDialog.show();
            }catch (Exception e){
                e.printStackTrace();
            }

        }else if(action.equals(Global.EShow_Broadcast_Action.ACTION_MISSING_TOKEN)){
            //token过期

        }
    }
}
