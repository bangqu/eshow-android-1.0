package com.bangqu.base.activity;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.WindowManager;

import com.bangqu.lib.R;
import com.longtu.base.debug.SystemCrashLog;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;


public class BangquApplication extends Application {

	public static int width;
	public static int height;

	@Override
	public void onCreate() {
		SystemCrashLog.init(getApplicationContext(), true, false, false);

		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		width = wm.getDefaultDisplay().getWidth();
		height = wm.getDefaultDisplay().getHeight();
		
		
		 DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
			        .showImageOnLoading(R.mipmap.bg) //设置图片在下载期间显示的图片
			        .showImageForEmptyUri(R.mipmap.bg)//设置图片Uri为空或是错误的时候显示的图片
			        .showImageOnFail(R.mipmap.bg)  //设置图片加载/解码过程中错误时候显示的图片
			        .cacheInMemory(true)//设置下载的图片是否缓存在内存中  
			        .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中   
			        .imageScaleType(ImageScaleType.EXACTLY)//设置图片以如何的编码方式显示
			        .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
			        .resetViewBeforeLoading(true)
			        .build();//构建完成

			        /*DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
			                .showImageForEmptyUri(R.drawable.lb_bg) //
			                .showImageOnFail(R.drawable.lb_bg) //
			                .cacheInMemory(true) //
			                .cacheOnDisk(true) //
			                .build();//
			*/      ImageLoaderConfiguration config = new ImageLoaderConfiguration//
			        .Builder(getApplicationContext())//
			                .defaultDisplayImageOptions(defaultOptions)//
			                .discCacheSize(50 * 1024 * 1024)//
			                .discCacheFileCount(100)// 缓存一百张图片
			                .writeDebugLogs()//
			                .memoryCache(new WeakMemoryCache())
			                .threadPoolSize(3) //线程池内加载的数量   
			                .build();//  
			        ImageLoader.getInstance().init(config);
	}

}
