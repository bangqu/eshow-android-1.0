package com.bangqu.utils;

import android.text.format.Time;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * 
 * 说明：处理一下字符串的常用操作，字符串校验等
 */
public class StringUtil {
	/**
	 * 判断字符串是否为空或者空字符串 如果字符串是空或空字符串则返回true，否则返回false
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		if (str == null || "".equals(str) || "null".equals(str)) {
			return true;
		} else {
			return false;
		}
	}

	public static String null2(String str) {
		if (str.equals("null")) {
			str = "";
		}
		return str;
	}

	public static String removeSpace(String str) {
		str = str.replace(" ", "");
		return str;
	}

	public static int str22Int(String str) {
		int i = Integer.parseInt(str);
		return i;
	}

	/**
	 * 验证邮箱输入是否合法
	 * 
	 * @param strEmail
	 * @return
	 */
	public static boolean isEmail(String strEmail) {
		// String strPattern =
		// "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		String strPattern = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";

		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail);
		return m.matches();
	}

	/*
	 * 验证号码 手机号 固话均可
	 */
	public static boolean isPhoneOrPhoneValid(String phoneNumber) {
		boolean isValid = false;

		// String expression =
		// "((^(13|15|18)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";
		// 手机号
		String expression = "(^(0|86|17951)?(13([0-9])|15([012356789])|17([678])|18([0-9])|14([57]))([0-9]){8})";
		// 400 800
		// String expression2 = "(^([48])00\\d)";
		// 固话
		// String expression3 = "((\\d{2,5}\\d{7,8}))";
		// String expression3 = "((\\d{2,5}-\\d{7,8}))";
		CharSequence inputStr = phoneNumber;

		Pattern pattern = Pattern.compile(expression);
		// Pattern pattern2 = Pattern.compile(expression2);
		// Pattern pattern3 = Pattern.compile(expression3);

		Matcher matcher = pattern.matcher(inputStr);
		// Matcher matcher2 = pattern2.matcher(inputStr);
		// Matcher matcher3 = pattern3.matcher(inputStr);

		// if (matcher.matches() || matcher2.matches() || matcher3.matches()) {
		if (matcher.matches()) {
			isValid = true;
		}

		return isValid;

	}

	/**
	 * 验证手机号码手机号是否合法
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isMobile(String str) {
		Pattern pattern = Pattern.compile("1[0-9]{10}");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 验证电话号码是否合法
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isPhone(String str) {
		Pattern p1 = null, p2 = null;
		Matcher m = null;
		boolean b = false;
		p1 = Pattern.compile("^[48]00$"); // 验证带区号的
		p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$"); // 验证没有区号的
		if (str.length() > 9) {
			m = p1.matcher(str);
			b = m.matches();
		} else {
			m = p2.matcher(str);
			b = m.matches();
		}
		return b;
	}

	/**
	 * 验证电话号码是否合法
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public static boolean isPhoneNumberValid(String phoneNumber) {
		boolean isValid = false;
		String expression = "((^(13|15|18)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";
		CharSequence inputStr = phoneNumber;
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	/**
	 * MD5加密（多数用于密码加密、一般都在后台操作）
	 * 
	 * @param secret_key
	 * @return
	 */
	public static String createSign(String secret_key) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(secret_key.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
		}

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		return md5StrBuff.toString();
	}

	/**
	 * 判断是否是中文
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	/**
	 * 判断字符串是否超过指定字符数
	 * 
	 * @param content
	 * @param stringNum
	 *            指定字符数 如：140
	 * @return
	 */
	public static boolean countStringLength(String content, int stringNum) {
		int result = 0;
		if (content != null && !"".equals(content)) {
			char[] contentArr = content.toCharArray();
			if (contentArr != null) {
				for (int i = 0; i < contentArr.length; i++) {
					char c = contentArr[i];
					if (isChinese(c)) {
						result += 3;
					} else {
						result += 1;
					}
				}
			}
		}
		if (result > stringNum * 3) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 将网络图片路径md5加密作为文件名
	 * 
	 * @param imageUrl
	 * @return
	 */
	public static String createImageName(String imageUrl) {
		return createSign(imageUrl) + ".jpg";
	}

	/**
	 * 将网络图片路径md5加密作为文件名,可以设置图片类型
	 * 
	 * @param imageUrl
	 * @param imgSuffix
	 * @return
	 */
	public static String createImageName(String imageUrl, String imgSuffix) {
		return createSign(imageUrl) + imgSuffix;
	}

	/**
	 * 将null转换为""
	 * 
	 * @param str
	 * @return
	 */
	public static String trimNull(String str) {
		if (str == null || "null".equalsIgnoreCase(str))
			return "";
		else
			return str;
	}

	/**
	 * 把异常信息打印出来
	 * 
	 * @param e
	 * @return
	 */
	public static String getExceptionInfo(Exception e) {
		String result = "";
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		result = e.getMessage() + "/r/n" + sw.toString();
		pw.close();
		try {
			sw.close();
		} catch (IOException e1) {

		}
		return result;
	}

	/**
	 * 用当前时间给取得的图片命名
	 * 
	 */
	public static String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmsss");
		return dateFormat.format(date) + ".jpg";
	}

	/**
	 * 头像文件名
	 * 
	 * @param sid
	 * @return
	 */
	public static String createAvatarFileName(String sid) {
		return "avatar_" + sid + ".jpg";
	}

	/**
	 * 获取当前系统时间
	 * 
	 * @return
	 */
	public static String getTime() {
		Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。

		t.setToNow(); // 取得系统时间。
		int year = t.year;
		int month = t.month;
		int date = t.monthDay;
		int hour = t.hour; // 0-23
		int minute = t.minute;
		int second = t.second;

		return hour + ":" + minute + ":" + second;
	}

	/**
	 * 获取系统当前小时
	 * 
	 * @return
	 */
	public static int getHour() {
		Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
		t.setToNow(); // 取得系统时间。
		return t.hour; // 0-23
	}

	// 保留一位小数
	public static String getDoubleOne(String str) {
		Double c1 = Double.parseDouble(str);

		int b = (int) Math.round(c1 * 10); // 小数点后两位前移，并四舍五入
		double c = ((double) b / 10.0); // 还原小数点后两位
		if ((c * 10) % 5 != 0) {
			int d = (int) Math.round(c); // 小数点前移，并四舍五入
			c = ((double) d); // 还原小数点
		}
		String cao = c + "";
		return cao;
	}

	// 保留两位小数
	public static String getDoubleTwo(String str) {
		Double c1 = Double.parseDouble(str);

		return String.format("%.2f", c1);

		// int b = (int) Math.round(c1 * 10); // 小数点后两位前移，并四舍五入
		// double c = ((double) b / 10.0); // 还原小数点后两位
		// // if ((c * 10) % 5 != 0) {
		// // int d = (int) Math.round(c); // 小数点前移，并四舍五入
		// // c = ((double) d); // 还原小数点
		// // }
		// String cao = c + "";
		// return cao;
	}

	/**
	 * 小于十数字补零
	 * 
	 * @param i
	 * @return
	 */
	public static String addzero(int i) {

		String f;
		if (i < 10) {
			f = "0" + i;
		} else {
			f = i + "";
		}
		return f;
	}

	/**
	 * 字符全角化
	 * 
	 * @param input
	 * @return
	 */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	private static long lastClickTime;

	public static boolean isFastDoubleClick() {
		long currentTime = System.currentTimeMillis();
		long timeDiffer = currentTime - lastClickTime;
		if (0 < timeDiffer && timeDiffer < 500) {
			return true;
		}
		lastClickTime = currentTime;
		return false;
	}
}
