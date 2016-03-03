package com.bangqu.eshow.demo;

import android.support.multidex.MultiDexApplication;

import com.bangqu.eshow.global.ESAppConfig;
import com.umeng.socialize.PlatformConfig;

/**
 * Created by daikting on 16/2/19.
 */
public class MyApp extends MultiDexApplication {
    public static float sScale;

    @Override
    public void onCreate() {
        super.onCreate();
        ESAppConfig.UI_WIDTH = 750;
        ESAppConfig.UI_HEIGHT = 1334;

        sScale = getResources().getDisplayMetrics().density;
        //微信 appid appsecret
        PlatformConfig.setWeixin("wx747d053fa471eb15", "95072c38637c0dc19256cc968165ff6a");
        //新浪微博 appkey appsecret
        PlatformConfig.setSinaWeibo("","");
        // QQ和Qzone appid appkey
        PlatformConfig.setQQZone("1105134763", "rexsau2NHZYARCJw");
    }
}
