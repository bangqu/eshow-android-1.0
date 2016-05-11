package cn.org.eshow.demo.bean;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import cn.org.eshow.demo.activity.InfoFormActivity_;
import cn.org.eshow.demo.activity.LoginActivity_;
import cn.org.eshow.demo.activity.MapActivity_;
import cn.org.eshow.demo.activity.PayWebViewActivity;
import cn.org.eshow.demo.activity.ShareActivity_;
import cn.org.eshow.demo.bluetooth.BluetoothActivity_;
import cn.org.eshow.demo.common.SharedPrefUtil;
import cn.org.eshow.demo.music.MusicListActivity_;
import cn.org.eshow.framwork.util.AbLogUtil;
import cn.org.eshow.framwork.util.AbToastUtil;

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
                Toast.makeText(context,"该功能正在开发中 ...",Toast.LENGTH_SHORT).show();
                //onImage(context);
                break;
            case DOWNLOAD:
                Toast.makeText(context,"该功能正在开发中 ...",Toast.LENGTH_SHORT).show();
                //onDownload(context);
                break;
            case CITY:
                Toast.makeText(context,"该功能正在开发中 ...",Toast.LENGTH_SHORT).show();
                //onCity(context);
                break;
            case MUSIC:
                //Toast.makeText(context,"该功能正在开发中 ...",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(context,"该功能正在开发中 ...",Toast.LENGTH_SHORT).show();
                //onChat(context);
                break;
            case BLUETOOTH:
                Toast.makeText(context,"该功能正在完善开发中 ...",Toast.LENGTH_SHORT).show();
                onBluetooth(context);
                break;
        }
    }

    private void onBluetooth(Context context) {
        BluetoothActivity_.intent(context).start();
    }

    void onFrom(Context context) {
        if(SharedPrefUtil.isLogin(context)){
            InfoFormActivity_.intent(context).start();

        }else{
            AbToastUtil.showToast(context, "请先登录再尝试使用此功能！");
            LoginActivity_.intent(context).start();
        }
    }

    void onImage(Context context) {

    }

    void onDownload(Context context) {

    }

    void onCity(Context context){
    }

    void onMusic(Context context){
        MusicListActivity_.intent(context).start();

    }

    void onMap(Context context){
        MapActivity_.intent(context).start();
    }

    void onPay(Context context){
        if(SharedPrefUtil.isLogin(context)){
            Intent intent = new Intent(context, PayWebViewActivity.class);

            String url = "http://api.eshow.org.cn/pingpay/pay.jsp?accessToken="+ SharedPrefUtil.getAccessToken(context);
            AbLogUtil.i(context, "支付请求地址：" + url);

            intent.putExtra(PayWebViewActivity.INTENT_URL_TAG, url);
            context.startActivity(intent);
        }else{
            AbToastUtil.showToast(context,"请先登录再尝试使用此功能！");
            LoginActivity_.intent(context).start();
        }
    }

    void onShare(final Context context){
        ShareActivity_.intent(context).start();
    }

    void onChat(Context context){

    }
}
