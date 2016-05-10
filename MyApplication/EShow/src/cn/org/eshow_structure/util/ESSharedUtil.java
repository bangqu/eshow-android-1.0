package cn.org.eshow_structure.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import cn.org.eshow_structure.global.ESAppConfig;

//TODO: Auto-generated Javadoc

/**
* 保存到 SharedPreferences 的数据.
*
*/
public class ESSharedUtil {

	private static final String SHARED_PATH = ESAppConfig.SHARED_PATH;
	private static SharedPreferences sharedPreferences;

	public static SharedPreferences getDefaultSharedPreferences(Context context) {
		if(sharedPreferences==null){
			sharedPreferences = context.getSharedPreferences(SHARED_PATH, Context.MODE_PRIVATE);
		}
		return sharedPreferences;
	}
	
	public static void putInt(Context context,String key, int value) {
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		Editor edit = sharedPreferences.edit();
		edit.putInt(key, value);
		edit.commit();
	}

	public static int getInt(Context context,String key) {
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		return sharedPreferences.getInt(key, 0);
	}
	
	public static void putString(Context context,String key, String value) {
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		Editor edit = sharedPreferences.edit();
		edit.putString(key, value);
		edit.commit();
	}

	public static String getString(Context context,String key) {
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		return sharedPreferences.getString(key,null);
	}
	
	public static void putBoolean(Context context,String key, boolean value) {
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		Editor edit = sharedPreferences.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}

	public static boolean getBoolean(Context context,String key,boolean defValue) {
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		return sharedPreferences.getBoolean(key,defValue);
	}
	
	public static void remove(Context context,String key) {
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		Editor edit = sharedPreferences.edit();
		edit.remove(key);
		edit.commit();
	}

}
