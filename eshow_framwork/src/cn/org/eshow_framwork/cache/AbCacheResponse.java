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
package cn.org.eshow_framwork.cache;

import org.apache.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * 
 * 缓存响应实体
 */
public class AbCacheResponse {
	
	/** 响应码. */
	public final int statusCode;

	/** 响应数据. */
	public final byte[] data;

	/** 响应头. */
	public final Map<String, String> headers;

	/**
	 * 构造.
	 *
	 * @param statusCode the status code
	 * @param data the data
	 * @param headers the headers
	 */
	public AbCacheResponse(int statusCode, byte[] data,
                           Map<String, String> headers) {
		this.statusCode = statusCode;
		this.data = data;
		this.headers = headers;
	}

	/**
	 * 构造.
	 *
	 * @param data the data
	 */
	public AbCacheResponse(byte[] data) {
		this(HttpStatus.SC_OK, data, Collections.<String, String> emptyMap());
	}

	/**
	 * 构造.
	 *
	 * @param data the data
	 * @param headers the headers
	 */
	public AbCacheResponse(byte[] data, Map<String, String> headers) {
		this(HttpStatus.SC_OK, data, headers);
	}
}