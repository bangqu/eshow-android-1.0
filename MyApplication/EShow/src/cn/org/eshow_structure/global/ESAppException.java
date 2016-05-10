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
package cn.org.eshow_structure.global;

import cn.org.eshow_structure.util.ESStrUtil;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

// TODO: Auto-generated Javadoc

/**
 * 公共异常类.
 *
 */
public class ESAppException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1;

	
	/** 异常消息. */
	private String msg = null;

	/**
	 * 构造异常类.
	 *
	 * @param e 异常
	 */
	public ESAppException(Exception e) {
		super();

		try {
			if( e instanceof HttpHostConnectException) {  
				msg = ESAppConfig.UNKNOWN_HOST_EXCEPTION;
			}else if (e instanceof ConnectException) {
				msg = ESAppConfig.CONNECT_EXCEPTION;
			}else if (e instanceof ConnectTimeoutException) {
				msg = ESAppConfig.CONNECT_EXCEPTION;
			}else if (e instanceof UnknownHostException) {
				msg = ESAppConfig.UNKNOWN_HOST_EXCEPTION;
			}else if (e instanceof SocketException) {
				msg = ESAppConfig.SOCKET_EXCEPTION;
			}else if (e instanceof SocketTimeoutException) {
				msg = ESAppConfig.SOCKET_TIMEOUT_EXCEPTION;
			}else if( e instanceof NullPointerException) {  
				msg = ESAppConfig.NULL_POINTER_EXCEPTION;
			}else if( e instanceof ClientProtocolException) {  
				msg = ESAppConfig.CLIENT_PROTOCOL_EXCEPTION;
			}else {
				if (e == null || ESStrUtil.isEmpty(e.getMessage())) {
					msg = ESAppConfig.NULL_MESSAGE_EXCEPTION;
				}else{
				    msg = e.getMessage();
				}
			}
		} catch (Exception e1) {
		}
		
	}

	/**
	 * 用一个消息构造异常类.
	 *
	 * @param message 异常的消息
	 */
	public ESAppException(String message) {
		super(message);
		msg = message;
	}

	/**
	 * 描述：获取异常信息.
	 *
	 * @return the message
	 */
	@Override
	public String getMessage() {
		return msg;
	}

}
