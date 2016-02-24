package com.bangqu.eshow.demo.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 程序配置保存到SharedPreferances
 * Created by daikting on 16/2/24.
 */
public class SharedPrefUtil {
    // 用户第一次登陆
    private static final String IS_FIRST_IN = "IsFirstIn";
    /**
     * 如果已经进入应用，则设置第一次登录为false
     *
     * @param context
     */
    public static void setSecondIn(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor e = sp.edit();
        e.putBoolean(IS_FIRST_IN, false);
        e.commit();
    }

    /**
     * 判断是否是第一次进入应用
     *
     * @param context
     * @return
     */
    public static boolean isFirstIn(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(IS_FIRST_IN, true);
    }

}
