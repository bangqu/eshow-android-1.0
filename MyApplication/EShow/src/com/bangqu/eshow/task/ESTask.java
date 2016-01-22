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
package com.bangqu.eshow.task;
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
import android.os.AsyncTask;

import java.util.List;
// TODO: Auto-generated Javadoc

/**
 * 下载数据的任务实现
 *
 */
public class ESTask extends AsyncTask<ESTaskItem, Integer, ESTaskItem> {
	
	/** 监听器. */
	private ESTaskListener listener;
	
	/** 结果. */
	private Object result;
	
	/**
	 * 初始化Task.
	 */
	public ESTask() {
		super();
	}
	
	/**
	 * 实例化.
	 */
	public static ESTask newInstance() {
		ESTask mAbTask = new ESTask();
		return mAbTask;
	}
	
	/**
	 * 
	 * 执行任务.
	 * @param items
	 * @return
	 */
	@Override
	protected ESTaskItem doInBackground(ESTaskItem... items) {
		ESTaskItem item = items[0];
		this.listener = item.getListener();
		if (this.listener != null) { 
			if(this.listener instanceof ESTaskListListener){
				result = ((ESTaskListListener)this.listener).getList();
        	}else if(this.listener instanceof ESTaskObjectListener){
        		result = ((ESTaskObjectListener)this.listener).getObject();
        	}else{
        		this.listener.get(); 
        	}
        } 
		return item;
	}

	/**
	 * 
	 * 取消.
	 */
	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	/**
	 * 
	 * 执行完成.
	 * @param item
	 */
	@Override
	protected void onPostExecute(ESTaskItem item) {
		if (this.listener != null) {
			if(this.listener instanceof ESTaskListListener){
        		((ESTaskListListener)this.listener).update((List<?>)result);
        	}else if(this.listener instanceof ESTaskObjectListener){
        		((ESTaskObjectListener)this.listener).update(result);
        	}else{
        		this.listener.update(); 
        	}
		}
	}

	/**
	 * 
	 * 执行前.
	 */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	/**
	 * 
	 * 进度更新.
	 * @param values
	 */
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		if (this.listener != null) { 
			this.listener.onProgressUpdate(values);
		}
	}

}
