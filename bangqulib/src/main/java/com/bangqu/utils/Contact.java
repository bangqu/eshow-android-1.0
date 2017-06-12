package com.bangqu.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.bangqu.bean.UserLoginBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 设计院 on 2016/6/30.
 * 通用信息
 */
public class Contact {

    public final static String USERINFO="userinfo";
    public static UserLoginBean userLoginBean;
    public static String Openid;
    public static String access_token;


    /**
     * 将图片联合路径处理成单个
     *
     * @param imgs
     * @return
     */
    public static List<String> getPhotos(String imgs) {
        List<String> listphotos = new ArrayList<String>();
        int index = imgs.indexOf(",");
        String imgurl;
        while (index > -1) {
            imgurl = imgs.substring(0, index);
            listphotos.add(imgurl);
            Log.e("imgUrl==>", imgurl);
            imgs = imgs.substring(index + 1);
            index = imgs.indexOf(",");
        }
        Log.e("imgs==>", imgs);
        listphotos.add(imgs);
        return listphotos;
    }

    /**
     * 判断是否是手机号
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((11[0-9])|(12[0-9])|(13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static  String getNetworkStateName(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager
                .getActiveNetworkInfo();
        if(mNetworkInfo.isAvailable()){
            //获取网络类型
            int netWorkType =mNetworkInfo.getType();
            if(netWorkType==ConnectivityManager.TYPE_WIFI){
                return "WIFI";
            }else if(netWorkType==ConnectivityManager.TYPE_MOBILE){
                return "2G/3G/4G";
            }else{
                return "其它方式";
            }
        }else{
            return "当前无网络";
        }

    }

    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }
}
