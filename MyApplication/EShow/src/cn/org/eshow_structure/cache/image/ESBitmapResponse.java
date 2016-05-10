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
package cn.org.eshow_structure.cache.image;

import android.graphics.Bitmap;

// TODO: Auto-generated Javadoc
/**
 * 
 * 响应实体
 */
public class ESBitmapResponse {
	
	/** Bitmap实体. */
	private Bitmap bitmap;
	
	/** 请求URL. */
	private String requestURL;

	/**
	 * 
	 * 构造.
	 * @param requestURL
	 */
	public ESBitmapResponse(String requestURL) {
		super();
		this.requestURL = requestURL;
	}

	/**
	 * 获取Bitmap.
	 *
	 * @return the bitmap
	 */
	public Bitmap getBitmap() {
		return bitmap;
	}

	/**
	 * 设置Bitmap.
	 *
	 * @param bitmap the new bitmap
	 */
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	/**
	 * 获取请求的URL.
	 *
	 * @return the request url
	 */
	public String getRequestURL() {
		return requestURL;
	}

	/**
	 * 设置请求的URL.
	 *
	 * @param requestURL the new request url
	 */
	public void setRequestURL(String requestURL) {
		this.requestURL = requestURL;
	}

}
