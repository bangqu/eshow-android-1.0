package com.bangqu.eshow.demo.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.bangqu.eshow.demo.bean.UserBean;
import com.bangqu.eshow.util.ESJsonUtil;
import com.bangqu.eshow.util.ESStrUtil;

/**
 * 程序配置保存到SharedPreferances
 * Created by daikting on 16/2/24.
 */
public class SharedPrefUtil {
    // 用户第一次登陆
    private static final String IS_FIRST_IN = "IsFirstIn";
    //存储发送短信验证码的时刻
    private static final String SEND_CODE_TIME = "SendCodeTime";
    //存储登录时获取的user对象
    private static final String USER = "User";
    //存储accesstoken
    private static final String ACCESSTOKEN = "AccessToken";
    //存储QiniuToken
    private static final String QINIUTOKEN = "QiniuToken";
    //存储QiniuKey
    private static final String QINIUKEY = "QiniuKey";
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
    /**
     * 存储发送短信验证码的时间
     *
     * @param context
     */
    public static void setSendCodeTime(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor e = sp.edit();
        long currentTime = System.currentTimeMillis();
        e.putLong(SEND_CODE_TIME, currentTime);
        e.commit();
    }
    /**
     * 获取发送短信验证的时间
     *
     * @param context
     * @return
     */
    public static long getSendCodeTime(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getLong(SEND_CODE_TIME, 0l);
    }

    /**
     * 存储用户实体类
     *
     * @param context
     * @param user
     */
    public static void setUser(Context context, String user) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor e = sp.edit();
        e.putString(USER, user);
        e.commit();
    }

    /**
     * 获取缓存的店铺信息实体类对象
     *
     * @param context
     * @return
     */
    public static UserBean getUser(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String user = sp.getString(USER, null);
        UserBean userBean = null;
        if (user != null) {
            userBean = (UserBean) ESJsonUtil.fromJson(user, UserBean.class);
        }
        return userBean;
    }

    /**
     * 存储accesstoken
     *
     * @param context
     * @param accesstoken
     */
    public static void setAccesstoken(Context context, String accesstoken) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor e = sp.edit();
        e.putString(ACCESSTOKEN, accesstoken);
        e.commit();
    }

    /**
     * 获取accesstoken
     *
     * @param context
     * @return
     */
    public static String getAccessToken(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String accessToken = sp.getString(ACCESSTOKEN, null);
        return accessToken;
    }

    /**
     * 判断是否已经登录
     *
     * @return
     */
    public static boolean isLogin(Context context) {
        UserBean userBean = getUser(context);
        String token = getAccessToken(context);
        if (userBean != null && !ESStrUtil.isEmpty(token)) {
            return true;
        }
        return false;
    }
    /**
     * 登出时清空缓存信息
     *
     * @param context
     * @return
     */
    public static void logout(Context context) {
        setAccesstoken(context, null);
        setUser(context, null);
    }

    /**
     * 获取qiniutoken
     *
     * @param context
     * @param qiniuToken
     */
    public static void setQiniuToken(Context context, String qiniuToken) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor e = sp.edit();
        e.putString(QINIUTOKEN, qiniuToken);
        e.commit();
    }

    /**
     * 获取qiniutoken
     *
     * @param context
     * @return
     */
    public static String getQiniuToken(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String accessToken = sp.getString(QINIUTOKEN, "");
        return accessToken;
    }

    /**
     * 获取qiniuKey
     *
     * @param context
     * @param qiniuKey
     */
    public static void setQiniuKey(Context context, String qiniuKey) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor e = sp.edit();
        e.putString(QINIUKEY, qiniuKey);
        e.commit();
    }

    /**
     * 获取qiniuKey
     *
     * @param context
     * @return
     */
    public static String getQiniuKey(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String accessToken = sp.getString(QINIUKEY, "");
        return accessToken;
    }

}
