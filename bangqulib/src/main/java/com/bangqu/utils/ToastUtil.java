/**
 * Toast工具类
 * 
 */
package com.bangqu.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	private static Toast toast = null;

	/**
	 * Toast提示(短)
	 * 
	 * @param context
	 * @param info
	 */
	public static void showShort(Context context, String info) {
		try {
			if (toast == null) {
				toast = Toast.makeText(context, info, Toast.LENGTH_SHORT);
			} else {
				toast.setText(info);
			}
			toast.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Toast提示(长)
	 * 
	 * @param context
	 * @param info
	 */
	public static void showLong(Context context, String info) {
		Toast.makeText(context, info, Toast.LENGTH_LONG).show();
	}

	/**
	 * Toast提示(int 短)
	 * 
	 * @param context
	 * @param info
	 */
	public static void showInt(Context context, int info) {
		Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Toast提示(int 长)
	 * 
	 * @param context
	 * @param info
	 */
	public static void showIntLong(Context context, int info) {
		Toast.makeText(context, info, Toast.LENGTH_LONG).show();
	}

}
