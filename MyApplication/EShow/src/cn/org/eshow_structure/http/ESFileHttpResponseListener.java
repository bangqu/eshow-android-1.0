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

import cn.org.eshow_structure.util.ESFileUtil;

import java.io.File;

// TODO: Auto-generated Javadoc
/**
 * Http文件响应监听器
 *
 */
public abstract class ESFileHttpResponseListener extends ESHttpResponseListener {
	
    /** 当前缓存文件. */
    private File mFile;
    
    /**
     * 下载文件的构造,用默认的缓存方式.
     *
     * @param url the url
     */
	public ESFileHttpResponseListener(String url) {
		super();
	}
	
	/**
	 * 默认的构造.
	 */
	public ESFileHttpResponseListener() {
		super();
	}
	
	/**
     * 下载文件的构造,指定缓存文件名称.
     * @param file 缓存文件名称
     */
    public ESFileHttpResponseListener(File file) {
        super();
	    this.mFile = file;
    }
	
	/**
	 * 描述：下载文件成功会调用这里.
	 *
	 * @param statusCode the status code
	 * @param file the file
	 */
    public void onSuccess(int statusCode,File file){};
    
    /**
     * 描述：多文件上传成功调用.
     *
     * @param statusCode the status code
     */
    public void onSuccess(int statusCode){};
    
   
   /**
    * 成功消息.
    *
    * @param statusCode the status code
    */
    public void sendSuccessMessage(int statusCode){
    	sendMessage(obtainMessage(ESHttpClient.SUCCESS_MESSAGE, new Object[]{statusCode}));
    }
    
    /**
     * 失败消息.
     *
     * @param statusCode the status code
     * @param error the error
     */
    public void sendFailureMessage(int statusCode,Throwable error){
    	sendMessage(obtainMessage(ESHttpClient.FAILURE_MESSAGE, new Object[]{statusCode, error}));
    }
    

	/**
	 * Gets the file.
	 *
	 * @return the file
	 */
	public File getFile() {
		return mFile;
	}

	/**
	 * Sets the file.
	 *
	 * @param file the new file
	 */
	public void setFile(File file) {
		this.mFile = file;
		try {
			if(!file.getParentFile().exists()){
			      file.getParentFile().mkdirs();
			}
			if(!file.exists()){
			      file.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Sets the file.
	 *
	 * @param context the context
	 * @param name the name
	 */
	public void setFile(Context context,String name) {
		//生成缓存文件
        if(ESFileUtil.isCanUseSD()){
	    	File file = new File(ESFileUtil.getFileDownloadDir(context) + name);
	    	setFile(file);
        }
	}
    
}
