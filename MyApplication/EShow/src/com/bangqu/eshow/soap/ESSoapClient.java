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

import com.bangqu.eshow.global.ESAppConfig;
import com.bangqu.eshow.global.ESAppException;
import com.bangqu.eshow.http.ESHttpStatus;
import com.bangqu.eshow.task.thread.ESThreadFactory;
import com.bangqu.eshow.util.ESAppUtil;
import com.bangqu.eshow.util.ESLogUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.List;
import java.util.concurrent.Executor;

// TODO: Auto-generated Javadoc

/**
 * Soap客户端
 * 
 */
public class ESSoapClient {

	/** 上下文. */
	private static Context mContext;

	/** 线程执行器. */
	public static Executor mExecutorService = null;

	/**  WebService dotNet属性. */
	private boolean mDotNet = true;

	/**  soap参数. */
	private ESSoapParams mParams = null;
	
	/** 超时时间. */
    public static final int DEFAULT_SOCKET_TIMEOUT = 10000;
    
    /** 超时时间. */
	private int mTimeout = DEFAULT_SOCKET_TIMEOUT;


	/**
	 * 初始化.
	 * 
	 * @param context
	 *            the context
	 */
	public ESSoapClient(Context context) {
		mContext = context;
		mExecutorService = ESThreadFactory.getExecutorService();
	}

	/**
	 * Call.
	 *
	 * @param url the url
	 * @param nameSpace the name space
	 * @param methodName the method name
	 * @param Params the params
	 * @param listener the listener
	 */
	public void call(final String url,final String nameSpace,final String methodName, ESSoapParams Params,
			final ESSoapListener listener) {
		this.mParams = Params;

		if (!ESAppUtil.isNetworkAvailable(mContext)) {
			listener.sendFailureMessage(ESHttpStatus.CONNECT_FAILURE_CODE,
					ESAppConfig.CONNECT_EXCEPTION, new ESAppException(
							ESAppConfig.CONNECT_EXCEPTION));
			return;
		}

		listener.sendStartMessage();

		mExecutorService.execute(new Runnable() {
			@Override
			public void run() {
				try {
					doCall(url,nameSpace,methodName,mParams,listener);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * Do call.
	 *
	 * @param url the url
	 * @param nameSpace the name space
	 * @param methodName the method name
	 * @param params the params
	 * @param listener the listener
	 */
	public void doCall(String url,String nameSpace,String methodName,ESSoapParams params, ESSoapListener listener) {
		try {
			SoapObject request = new SoapObject(nameSpace, methodName);
			// 传递参数
			List<BasicNameValuePair> paramsList = params.getParamsList();
			for (NameValuePair nameValuePair : paramsList) {
				request.addProperty(nameValuePair.getName(), nameValuePair.getValue());
			}
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.bodyOut = request;
			envelope.dotNet = mDotNet;
			envelope.setOutputSoapObject(request);
			HttpTransportSE httpTransportSE = new HttpTransportSE(url,mTimeout);
			httpTransportSE.debug = true;

			ESLogUtil.d(ESSoapClient.class, "--call--");
			httpTransportSE.call(nameSpace+methodName, envelope);
			Object object = envelope.bodyIn;
			if(object instanceof SoapObject){
				SoapObject bodyIn = (SoapObject) envelope.bodyIn;
				if (bodyIn != null) {
					listener.sendSuccessMessage(ESHttpStatus.SUCCESS_CODE, bodyIn);
				}
			}else if(object instanceof SoapFault){
				SoapFault fault = (SoapFault) envelope.bodyIn;
				if (fault != null) {
				    listener.sendFailureMessage(ESHttpStatus.SERVER_FAILURE_CODE,fault );
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			listener.sendFailureMessage(ESHttpStatus.UNTREATED_CODE, ESAppConfig.UNTREATED_EXCEPTION, new ESAppException(ESAppConfig.UNTREATED_EXCEPTION));
		}finally{
			listener.sendFinishMessage();
		}
	}
	
	/**
     * 描述：设置连接超时时间.
     *
     * @param timeout 毫秒
     */
    public void setTimeout(int timeout) {
    	this.mTimeout = timeout;
	}

	/**
	 * Checks if is dot net.
	 *
	 * @return true, if is dot net
	 */
	public boolean isDotNet() {
		return mDotNet;
	}

	/**
	 * Sets the dot net.
	 *
	 * @param dotNet the new dot net
	 */
	public void setDotNet(boolean dotNet) {
		this.mDotNet = dotNet;
	}
    
    

}
