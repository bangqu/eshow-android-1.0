package com.bangqu.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 设计院 on 2016/6/30.
 */
public class SharedUtils {
    private static SharedPreferences sharedPreferences;

    /**
     * 设置是否第一次启动
     * @param context
     * @param welcome
     */
    public static void setWelcome(Context context, boolean welcome){
        sharedPreferences=context.getSharedPreferences(Contact.USERINFO,  Context.MODE_APPEND);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("welcome",welcome);
        editor.commit();
    }

    /**
     * 获取是否第一次启动
     * @param context
     * @return
     */
    public static boolean getWelcome(Context context){
        sharedPreferences=context.getSharedPreferences(Contact.USERINFO,  Context.MODE_APPEND);
        return sharedPreferences.getBoolean("welcome",false);
    }


    /***
     * 本地保存
     * @param context
     * @param name
     * @param pwd
     */
    public static void setUserNamePwd(Context context, String name, String pwd,String userid){
        sharedPreferences=context.getSharedPreferences(Contact.USERINFO,  Context.MODE_APPEND);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("name",name);
        editor.putString("pwd",pwd);
        editor.putString("userid",userid);
        editor.commit();
    }

    /**
     * 获取本地用户名
     * @param context
     * @return
     */
    public static String getUserName(Context context){
        sharedPreferences=context.getSharedPreferences(Contact.USERINFO,  Context.MODE_APPEND);
        return sharedPreferences.getString("name","");
    }

    /**
     * 获取本地密码
     * @param context
     * @return
     */
    public static String getUserPwd(Context context){
        sharedPreferences=context.getSharedPreferences(Contact.USERINFO,  Context.MODE_APPEND);
        return sharedPreferences.getString("pwd","");
    }

    /**
     * 获取本地UserId
     * @param context
     * @return
     */
    public static String getUserId(Context context){
        sharedPreferences=context.getSharedPreferences(Contact.USERINFO,  Context.MODE_APPEND);
        return sharedPreferences.getString("userid","");
    }

    /***
     * 本地保存
     * @param context
     */
    public static void setLocation(Context context, String locaname, String locaid){
        sharedPreferences=context.getSharedPreferences(Contact.USERINFO,  Context.MODE_APPEND);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("locaname",locaname);
        editor.putString("locaid",locaid);
        editor.commit();
    }

    /**
     * 获取小区名称
     * @param context
     * @return
     */
    public static String getLocaName(Context context){
        sharedPreferences=context.getSharedPreferences(Contact.USERINFO,  Context.MODE_APPEND);
        return sharedPreferences.getString("locaname","");
    }

    /**
     * 获取小区ID
     * @param context
     * @return
     */
    public static String getLocaId(Context context){
        sharedPreferences=context.getSharedPreferences(Contact.USERINFO,  Context.MODE_APPEND);
        return sharedPreferences.getString("locaid","");
    }

    /**
     * 设置
     * @param context
     */
    public static void setCity(Context context, String city){
        sharedPreferences=context.getSharedPreferences(Contact.USERINFO,  Context.MODE_APPEND);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("city",city);
        editor.commit();
    }

    /**
     * 获取默认城市
     * @param context
     * @return
     */
    public static String getCity(Context context){
        sharedPreferences=context.getSharedPreferences(Contact.USERINFO,  Context.MODE_APPEND);
        return sharedPreferences.getString("city","");
    }
}
