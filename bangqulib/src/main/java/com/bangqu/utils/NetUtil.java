package com.bangqu.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;

/**
 * 网络工具类
 * 
 * @author aizhimin
 */
public class NetUtil {
	private static final String TAG = "NetUtil";

	public static boolean isBreak = false;

	/**
	 * 检查网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkNet(Context context) {// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Check network error !");
			return false;
		}
		return false;
	}

	/**
	 * 判断是否有wifi或3g网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkWifiOr3gNet(Context context) {
		ConnectivityManager mConnectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mTelephony = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		// 检查网络连接，如果无网络可用，就不需要进行连网操作等
		NetworkInfo info = mConnectivity.getActiveNetworkInfo();

		if (info == null || !mConnectivity.getBackgroundDataSetting()) {
			return false;
		}

		// 判断网络连接类型，只有在3G或wifi里进行一些数据更新。
		int netType = info.getType();
		int netSubtype = info.getSubtype();

		if (netType == ConnectivityManager.TYPE_WIFI) {
			return info.isConnected();
		} else if (netType == ConnectivityManager.TYPE_MOBILE
				&& netSubtype == TelephonyManager.NETWORK_TYPE_UMTS
				&& !mTelephony.isNetworkRoaming()) {
			return info.isConnected();
		} else {
			return false;
		}
	}

	/**
	 * 判断是否为2G网络：gprs
	 * 
	 * @param context
	 * @return
	 */
	public static boolean is2gNet(Context context) {
		ConnectivityManager mConnectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 检查网络连接，如果无网络可用，就不需要进行连网操作等
		NetworkInfo info = mConnectivity.getActiveNetworkInfo();
		if (info == null || !mConnectivity.getBackgroundDataSetting()) {
			return false;
		}
		// 判断网络连接类型，只有在3G或wifi里进行一些数据更新。
		int netType = info.getType();
		int netSubtype = info.getSubtype();
		if (netType == ConnectivityManager.TYPE_MOBILE
				&& netSubtype != TelephonyManager.NETWORK_TYPE_UMTS) {
			return info.isConnected();
		} else {
			return false;
		}
	}

	/**
	 * 判断是否为3g网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean is3gNet(Context context) {
		ConnectivityManager mConnectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 检查网络连接，如果无网络可用，就不需要进行连网操作等
		NetworkInfo mWifi = mConnectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		return mWifi.isConnected();
	}

	/**
	 * 检查是否有wifi
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifi(Context context) {
		ConnectivityManager mConnectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 检查网络连接，如果无网络可用，就不需要进行连网操作等
		// NetworkInfo info = mConnectivity.getActiveNetworkInfo();
		// if (info == null || !mConnectivity.getBackgroundDataSetting()) {
		// return false;
		// }
		// //判断网络连接类型，只有在3G或wifi里进行一些数据更新。
		// int netType = info.getType();
		// if (netType == ConnectivityManager.TYPE_WIFI) {
		// return info.isConnected();
		// } else {
		// return false;
		// }

		NetworkInfo mWifi = mConnectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return mWifi.isConnected();
	}

//	/**
//	 * 获得mac地址
//	 *
//	 * @param context
//	 * @return
//	 */
//	public static String getLocalMacAddress(Context context) {
//		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//		WifiInfo info = wifi.getConnectionInfo();
//		return info.getMacAddress();
//	}

	/**
	 * 获取手机的ip地址
	 * 
	 * @return
	 */
	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
					.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
						.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e(TAG, ex.toString());
		}
		return null;
	}

	/**
	 * 判断端口是否可用
	 * 
	 * @param host
	 * @param port
	 * @return
	 */
	public static boolean isAvailable(String host, int port) {
		try {
			bindPort("0.0.0.0", port);
			bindPort(InetAddress.getLocalHost().getHostAddress(), port);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private static void bindPort(String host, int port) throws Exception {
		Socket s = new Socket();
		s.bind(new InetSocketAddress(host, port));
		s.close();
	}

	/**
	 * 从某端口开始，活动一个未被占用，可使用的端口。
	 * 
	 * @param host
	 * @param startPort
	 * @return
	 */
	public static int getAvailablePort(String host, int startPort) {
		for (int i = 80; i < 65535; i++) {
			if (NetUtil.isAvailable(host, i)) {
				startPort = i;
				break;
			}
		}
		return startPort;
	}

	public static int breakpointDownload(int startposition, String fileName, String filePath,
			String netUrl) {

		isBreak = false;
		String localPath = filePath + "/" + fileName;

		try {
			File file = new File(localPath);
			if (!file.exists()) {
				startposition = 0;
			}

			URL url = new URL(netUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept-Encoding", "gzip");
			conn.setRequestProperty("Range", "bytes=" + startposition + "-");
			conn.setRequestProperty(
					"Accept",
					"image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			conn.setRequestProperty("Accept-Language", "zh-CN");
			conn.setRequestProperty(
					"User-Agent",
					"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Connection", "Keep-Alive");

			InputStream inStream = conn.getInputStream();
			int length = conn.getContentLength();

			if (!file.exists()) {
				RandomAccessFile rfile = new RandomAccessFile(localPath, "rw");
				rfile.setLength(length);
				;
				rfile.close();
			}
			RandomAccessFile rfile = new RandomAccessFile(localPath, "rw");
			rfile.writeUTF("utf-8");
			rfile.seek(startposition);
			int downPosition = startposition;

			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				rfile.write(buffer, 0, len);
				downPosition += len;
				Log.i(TAG, "downPosition:" + downPosition);
				if (isBreak) {
					break;
				}
			}
			inStream.close();
			rfile.close();
			return downPosition;
		} catch (Exception e) {
			Log.i(TAG, e.getMessage().toString());
		}
		return 0;
	}

}
