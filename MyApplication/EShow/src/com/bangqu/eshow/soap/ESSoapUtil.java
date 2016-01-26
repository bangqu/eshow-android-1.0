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
package com.bangqu.eshow.soap;

import android.content.Context;

// TODO: Auto-generated Javadoc

/**
 * Soap请求工具类
 * 
 */
public class ESSoapUtil {

	/** 实例话对象. */
	private ESSoapClient mClient = null;

	/** AbSoapUtil实例. */
	private static ESSoapUtil mAbSoapUtil;

	/**
	 * 单例 获取AbSoapUtil实例.
	 * 
	 * @param context
	 *            the context
	 * @return single instance of AbSoapUtil
	 */
	public static ESSoapUtil getInstance(Context context) {
		if (null == mAbSoapUtil) {
			mAbSoapUtil = new ESSoapUtil(context);
		}
		return mAbSoapUtil;
	}

	/**
	 * AbSoapUtil构造方法.
	 * 
	 * @param context
	 *            the context
	 */
	private ESSoapUtil(Context context) {
		super();
		this.mClient = new ESSoapClient(context);
	}

	/**
	 * Call.
	 * 
	 * @param url
	 *            the url
	 * @param nameSpace
	 *            the name space
	 * @param methodName
	 *            the method name
	 * @param params
	 *            the params
	 * @param listener
	 *            the listener
	 */
	public void call(String url, String nameSpace, String methodName,
			ESSoapParams params, ESSoapListener listener) {
		mClient.call(url, nameSpace, methodName, params, listener);
	}

	/**
	 * 描述：设置连接超时时间(第一次请求前设置).
	 * 
	 * @param timeout
	 *            毫秒
	 */
	public void setTimeout(int timeout) {
		mClient.setTimeout(timeout);
	}
	
	/**
	 * Sets the dot net.
	 *
	 * @param dotNet the new dot net
	 */
	public void setDotNet(boolean dotNet) {
		mClient.setDotNet(dotNet);
	}
	
	
}
