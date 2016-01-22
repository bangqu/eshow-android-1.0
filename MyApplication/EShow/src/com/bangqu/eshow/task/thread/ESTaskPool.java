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
package com.bangqu.eshow.task.thread;

import android.os.Handler;
import android.os.Message;

import com.bangqu.eshow.task.ESTaskItem;
import com.bangqu.eshow.task.ESTaskListListener;
import com.bangqu.eshow.task.ESTaskObjectListener;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
// TODO: Auto-generated Javadoc
/**
 * 用ESHOW   线程池
 *
 */

public class ESTaskPool {
	
	/** 单例对象 The http pool. */
	private static ESTaskPool abTaskPool = null;
	
	/** 线程执行器. */
	public static Executor mExecutorService = null;
	
	/**  存放返回的任务结果. */
    private static HashMap<String,Object> result;
	
	/** 下载完成后的消息句柄. */
    private static Handler handler = new Handler() { 
        @Override 
        public void handleMessage(Message msg) { 
        	ESTaskItem item = (ESTaskItem)msg.obj;
        	if(item.getListener() instanceof ESTaskListListener){
        		((ESTaskListListener)item.getListener()).update((List<?>)result.get(item.toString()));
        	}else if(item.getListener() instanceof ESTaskObjectListener){
        		((ESTaskObjectListener)item.getListener()).update(result.get(item.toString()));
        	}else{
        		item.getListener().update(); 
        	}
        	result.remove(item.toString());
        } 
    }; 
    
	
	/**
	 * 构造线程池.
	 */
    private ESTaskPool() {
        result = new HashMap<String,Object>();
        mExecutorService = ESThreadFactory.getExecutorService();
    } 
	
	/**
	 * 单例构造图片下载器.
	 *
	 * @return single instance of AbHttpPool
	 */
    public static ESTaskPool getInstance() {
    	if (abTaskPool == null) { 
    		abTaskPool = new ESTaskPool();
        } 
        return abTaskPool;
    } 
    
    /**
     * 执行任务.
     * @param item the item
     */
    public void execute(final ESTaskItem item) {
    	mExecutorService.execute(new Runnable() { 
    		public void run() {
    			try {
    				//定义了回调
                    if (item.getListener() != null) { 
                        if(item.getListener() instanceof ESTaskListListener){
                            result.put(item.toString(), ((ESTaskListListener)item.getListener()).getList());
                        }else if(item.getListener() instanceof ESTaskObjectListener){
                            result.put(item.toString(), ((ESTaskObjectListener)item.getListener()).getObject());
                        }else{
                        	item.getListener().get();
                            result.put(item.toString(), null);
                        }
                        
                    	//交由UI线程处理 
                        Message msg = handler.obtainMessage(); 
                        msg.obj = item; 
                        handler.sendMessage(msg); 
                    }                              
    			} catch (Exception e) { 
    				e.printStackTrace();
    			}                         
    		}                 
    	});                 
    	
    }
	
}
