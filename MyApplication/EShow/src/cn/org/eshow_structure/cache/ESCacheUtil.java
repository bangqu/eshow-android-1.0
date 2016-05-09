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
package cn.org.eshow_structure.cache;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.org.eshow_structure.util.ESImageUtil;
import cn.org.eshow_structure.util.ESLogUtil;
import cn.org.eshow_structure.util.ESStrUtil;
import cn.org.eshow_structure.util.ESStreamUtil;

// TODO: Auto-generated Javadoc
/**
 * 
 * 缓存工具类
 */
public class ESCacheUtil {
	
	/**
	 * 从连接中获取响应信息.
	 *
	 * @param url the url
	 * @param expiresTime the expires time
	 * @return the cache response
	 */
	public static ESCacheResponse getCacheResponse(String url){
		URLConnection con = null;
		InputStream is = null;
		ESCacheResponse response = null;
		try {
			URL imageURL = new URL(url);
			con = imageURL.openConnection();
			con.setDoInput(true);
			con.connect();
			is = con.getInputStream();
			byte [] data = ESStreamUtil.stream2bytes(is);
			Map<String, List<String>> headers = con.getHeaderFields();
			Map<String,String> mapHeaders = new HashMap<String,String>();
			for (Map.Entry<String, List<String>> entry : headers.entrySet()) { 
				
				String key = entry.getKey();
				List<String> values = entry.getValue();
				if(ESStrUtil.isEmpty(key)){
					key = "andbase";
				}
				mapHeaders.put(key, values.get(0));
				
		    }  
			
			/*key = null and value = [HTTP/1.1 200 OK]
		    key = Accept-Ranges and value = [bytes]
			key = Connection and value = [Keep-Alive]
			key = Content-Length and value = [4357]
			key = Content-Type and value = [image/png]
			key = Date and value = [Thu, 02 Apr 2015 10:42:54 GMT]
			key = ETag and value = ["620e07-1105-4f5d6331a2300"]
			key = Keep-Alive and value = [timeout=15, max=97]
			key = Last-Modified and value = [Sun, 30 Mar 2014 17:23:56 GMT]
			key = Server and value = [Apache]
			key = X-Android-Received-Millis and value = [1427971373392]
			key = X-Android-Sent-Millis and value = [1427971373356]*/

			response = new ESCacheResponse(data, mapHeaders);
			
		} catch (Exception e) {
			//e.printStackTrace();
			ESLogUtil.d(ESImageUtil.class, "" + e.getMessage());
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return response;
	}
	
}
