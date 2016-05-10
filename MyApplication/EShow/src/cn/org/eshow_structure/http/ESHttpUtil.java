/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.org.eshow_structure.http;

import android.content.Context;

// TODO: Auto-generated Javadoc
/**
 * Http执行工具类，可处理get，post，以及异步处理文件的上传下载
 *
 */
public class ESHttpUtil {
	
	/** 实例化单例对象. */
	private ESHttpClient mClient = null;
	
	/** 工具类单例. */
	private static ESHttpUtil mAbHttpUtil = null;
	
	
	/**
	 * 描述：获取实例.
	 *
	 * @param context the context
	 * @return single instance of AbHttpUtil
	 */
	public static ESHttpUtil getInstance(Context context){
	    if (null == mAbHttpUtil){
	    	mAbHttpUtil = new ESHttpUtil(context);
	    }
	    
	    return mAbHttpUtil;
	}
	
	/**
	 * 初始化AbHttpUtil.
	 *
	 * @param context the context
	 */
	private ESHttpUtil(Context context) {
		super();
		this.mClient = new ESHttpClient(context);
	}
	
	/**
	 * 描述：无参数的get请求.
	 *
	 * @param url the url
	 * @param responseListener the response listener
	 */
	public void get(String url, ESHttpResponseListener responseListener) {
		mClient.get(url,null,responseListener);
	}
	
	/**
	 * 描述：带参数的get请求.
	 *
	 * @param url the url
	 * @param params the params
	 * @param responseListener the response listener
	 */
	public void get(String url, ESRequestParams params,
			ESHttpResponseListener responseListener) {
		mClient.get(url, params, responseListener);
	}
	
	/**
	 *  
	 * 描述：下载数据使用，会返回byte数据(下载文件或图片).
	 *
	 * @param url the url
	 * @param responseListener the response listener
	 */
	public void get(String url, ESBinaryHttpResponseListener responseListener) {
		mClient.get(url,null,responseListener);
	}
	
	/**
	 * 描述：文件下载的get.
	 *
	 * @param url the url
	 * @param params the params
	 * @param responseListener the response listener
	 */
	public void get(String url, ESRequestParams params,
			ESFileHttpResponseListener responseListener) {
		mClient.get(url, params, responseListener);
	}
	
	/**
	 * 描述：无参数的get请求.
	 *
	 * @param url the url
	 * @param responseListener the response listener
	 */
	public void getWithCache(String url, ESHttpResponseListener responseListener) {
		mClient.getWithCache(url,null,responseListener);
	}
	
	/**
	 * 描述：带参数的get请求.
	 *
	 * @param url the url
	 * @param params the params
	 * @param responseListener the response listener
	 */
	public void getWithCache(String url, ESRequestParams params,
			ESHttpResponseListener responseListener) {
		mClient.getWithCache(url, params, responseListener);
	}
	
	/**
	 *  
	 * 描述：下载数据使用，会返回byte数据(下载文件或图片).
	 *
	 * @param url the url
	 * @param responseListener the response listener
	 */
	public void getWithCache(String url, ESBinaryHttpResponseListener responseListener) {
		mClient.getWithCache(url,null,responseListener);
	}
	
	/**
	 * 描述：文件下载的get.
	 *
	 * @param url the url
	 * @param params the params
	 * @param responseListener the response listener
	 */
	public void getWithCache(String url, ESRequestParams params,
			ESFileHttpResponseListener responseListener) {
		mClient.getWithCache(url, params, responseListener);
	}
	
	/**
	 * 描述：无参数的post请求.
	 *
	 * @param url the url
	 * @param responseListener the response listener
	 */
	public void post(String url, ESHttpResponseListener responseListener) {
		mClient.post(url,null, responseListener);
	}
	
	/**
	 * 描述：带参数的post请求.
	 *
	 * @param url the url
	 * @param params the params
	 * @param responseListener the response listener
	 */
	public void post(String url, ESRequestParams params, ESHttpResponseListener responseListener) {
		mClient.post(url, params, responseListener);
	}
	
	/**
	 * 描述：文件下载的post.
	 *
	 * @param url the url
	 * @param params the params
	 * @param responseListener the response listener
	 */
	public void post(String url, ESRequestParams params,
			ESFileHttpResponseListener responseListener) {
		mClient.post(url, params, responseListener);
	}
	
	/**
	 * 描述：一般通用请求.
	 *
	 * @param url the url
	 * @param responseListener the response listener
	 */
	public void request(String url,ESStringHttpResponseListener responseListener) {
		request(url,null,responseListener);
	}
	
	/**
	 * 描述：一般通用请求.
	 *
	 * @param url the url
	 * @param params the params
	 * @param responseListener the response listener
	 */
	public void request(String url, ESRequestParams params,
			ESStringHttpResponseListener responseListener) {
		mClient.doRequest(url, params, responseListener);
	}
	
	/**
	 * 描述：设置连接超时时间(第一次请求前设置).
	 *
	 * @param timeout 毫秒
	 */
	public void setTimeout(int timeout) {
		mClient.setTimeout(timeout);
	}
	
    /**
     * 打开ssl 自签名(第一次请求前设置).
     * @param enabled
     */
    public void setEasySSLEnabled(boolean enabled){
    	mClient.setOpenEasySSL(enabled);
    }

    /**
	 * 设置编码(第一次请求前设置).
	 * @param encode
	 */
	public void setEncode(String encode) {
		mClient.setEncode(encode);
	}
	
	/**
     * 设置用户代理(第一次请求前设置).
     * @param userAgent
     */
	public void setUserAgent(String userAgent) {
		mClient.setUserAgent(userAgent);
	}
	
	/**
	 * 关闭HttpClient
	 * 当HttpClient实例不再需要是，确保关闭connection manager，以释放其系统资源  
	 */
	public void shutdownHttpClient(){
	    if(mClient != null){
	    	mClient.shutdown();
	    }
	}
	
	/**
	 * 
	 * 设置缓存的最大时间.
	 * @return
	 */
	public long getCacheMaxAge() {
		return mClient.getCacheMaxAge();
	}


	/**
	 * 
	 * 获取缓存的最大时间.
	 * @param cacheMaxAge
	 */
	public void setCacheMaxAge(long cacheMaxAge) {
		mClient.setCacheMaxAge(cacheMaxAge);
	}
	
}
