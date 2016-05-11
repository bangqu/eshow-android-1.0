package cn.org.eshow.demo.common;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import cn.org.eshow.demo.bean.UserBean;
import cn.org.eshow_framwork.util.AbJsonUtil;
import cn.org.eshow_framwork.util.AbStrUtil;

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

    private static final String TEMP_TEL = "TempTel";

    //存储QiniuToken
    private static final String QINIUTOKEN = "QiniuToken";
    //存储QiniuKey
    private static final String QINIUKEY = "QiniuKey";
    //存储蓝牙设备地址 address
    private static final String BLUETOOTH_ADDR = "BluetoothAddr";
    //存储蓝牙设备名称 name
    private static final String BLUETOOTH_NAME = "BluetoothName";
    //存储刚才点击的音乐position
    private static final String SELTETPOSITION = "selectPosition";
    //存储刚才点击的音乐position
    private static final String DOWNEDMUSIC = "downedMusic";

    /**
     * 存储selectPosition
     *
     * @param context
     * @param selectPosition
     */
    public static void setSelectPosition(Context context, String selectPosition) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor e = sp.edit();
        e.putString(SELTETPOSITION, selectPosition);
        e.commit();
    }

    /**
     * 获取selectPosition
     *
     * @param context
     * @return
     */
    public static String getSeltetposition(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String seltetposition = sp.getString(SELTETPOSITION, null);
        return seltetposition;
    }
    /**
     * 存储下载列表
     *
     * @param context
     * @param downedMusic
     */
    public static void setDowmedMusic(Context context, String downedMusic) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor e = sp.edit();
        e.putString(DOWNEDMUSIC, downedMusic);
        e.commit();
    }

    /**
     * 获取下载列表
     *
     * @param context
     */
    public static String getDownedmusic(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String downedMusic = sp.getString(DOWNEDMUSIC, null);
        return downedMusic;
    }
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

        Intent intent = new Intent(Global.EShow_Broadcast_Action.ACTION_LOGIN_SUCESS);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
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
            userBean = (UserBean) AbJsonUtil.fromJson(user, UserBean.class);
        }
        return userBean;
    }

    /**
     * 存储临时用户输入的手机号码
     *
     * @param context
     * @param tel
     */
    public static void setTempTel(Context context, String tel) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor e = sp.edit();
        e.putString(TEMP_TEL, tel);
        e.commit();
    }

    /**
     * 临时用户输入的手机号码
     *
     * @param context
     * @return
     */
    public static String getTempTel(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String accessToken = sp.getString(TEMP_TEL, null);
        return accessToken;
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
        if (userBean != null && !AbStrUtil.isEmpty(token)) {
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

    //--------保存之前已连接的蓝牙设备信息-----
    /**
     * 存储 addr
     * @param context
     * @param addr
     * @param name
     */
    public static void setBluetooth(Context context, String addr,String name) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor e = sp.edit();
        e.putString(BLUETOOTH_ADDR, addr);
        e.putString(BLUETOOTH_NAME,name);
        e.commit();
    }
    /**
     * 获取 addr
     * @param context
     * @return
     */
    public static String getBluetoothAddr(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String addr = sp.getString(BLUETOOTH_ADDR, null);
        return addr;
    }
    /**
     * 获取 name
     * @param context
     * @return
     */
    public static String getBluetoothName(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String name = sp.getString(BLUETOOTH_NAME, null);
        return name;
    }
}
