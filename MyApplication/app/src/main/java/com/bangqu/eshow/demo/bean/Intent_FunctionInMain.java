package com.bangqu.eshow.demo.bean;

import android.content.Context;
import android.content.Intent;

import com.bangqu.eshow.demo.activity.InfoFormActivity_;
import com.bangqu.eshow.demo.activity.MapActivity_;
import com.bangqu.eshow.demo.activity.PayWebViewActivity;
import com.bangqu.eshow.demo.common.SharedPrefUtil;
import com.bangqu.eshow.demo.view.ShareDialog;

/**
 * 首页上的功能列表选择性的跳转
 * Created by daikting on 16/3/17.
 */
public class Intent_FunctionInMain {
    public Intent_FunctionInMain(Context context, Enum_FunctionsInMain function) {
        switch (function) {
            case FROM:
                onFrom(context);
                break;
            case IMAGE:
                onImage(context);
                break;
            case DOWNLOAD:
                onDownload(context);
                break;
            case CITY:
                onCity(context);
                break;
            case MUSIC:
                onMusic(context);
                break;
            case MAP:
                onMap(context);
                break;
            case PAY:
                onPay(context);
                break;
            case SHARE:
                onShare(context);
                break;
            case CHAT:
                onChat(context);
                break;
        }
    }

    void onFrom(Context context) {
        InfoFormActivity_.intent(context).start();
    }

    void onImage(Context context) {

    }

    void onDownload(Context context) {

    }

    void onCity(Context context){
    }

    void onMusic(Context context){

    }

    void onMap(Context context){
        MapActivity_.intent(context).start();
    }

    void onPay(Context context){
        Intent intent = new Intent(context, PayWebViewActivity.class);
        String url = "http://api.eshow.org.cn/pingpay/pay.jsp?accessToken="+ SharedPrefUtil.getAccessToken(context);
        intent.putExtra(PayWebViewActivity.INTENT_URL_TAG, url);
        context.startActivity(intent);

    }

    void onShare(final Context context){
        new ShareDialog(context).show();
    }

    void onChat(Context context){

    }
}
