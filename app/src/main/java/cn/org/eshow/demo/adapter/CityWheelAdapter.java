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

package cn.org.eshow.demo.adapter;

import java.util.List;

import cn.org.eshow.demo.bean.CityBean;
import cn.org.eshow_framwork.util.AbStrUtil;
import cn.org.eshow_framwork.view.wheel.AbWheelAdapter;

// TODO: Auto-generated Javadoc

/**
 * 轮子适配器（字符串）
 *
 */

public class CityWheelAdapter implements AbWheelAdapter {

	/** The default items length. */
	public static final int DEFAULT_LENGTH = -1;
	// items
	/** The items. */
	private List<CityBean> items;
	// length
	/** The length. */
	private int length = -1;

	/**
	 * Constructor.
	 *
	 * @param items the items
	 * @param length the max items length
	 */
	public CityWheelAdapter(List<CityBean> items, int length) {
		this.items = items;
		this.length = length;
	}

	/**
	 * Constructor.
	 *
	 * @param items the items
	 */
	public CityWheelAdapter(List<CityBean> items) {
		this(items, DEFAULT_LENGTH);
	}
	

	/**
	 * 描述：TODO.
	 *
	 * @version v1.0
	 * @param index the index
	 * @return the item
	 * @see AbWheelAdapter#getItem(int)
	 * @author: amsoft.cn
	 * @date：2013-6-17 上午9:04:49
	 */
	@Override
	public String getItem(int index) {
		if (index >= 0 && index < items.size()) {
			return items.get(index).getName();
		}
		return null;
	}

	/**
	 * 描述：TODO.
	 *
	 * @version v1.0
	 * @return the items count
	 * @see AbWheelAdapter#getItemsCount()
	 * @author: amsoft.cn
	 * @date：2013-6-17 上午9:04:49
	 */
	@Override
	public int getItemsCount() {
		return items.size();
	}

	/**
	 * 描述：TODO.
	 *
	 * @version v1.0
	 * @return the maximum length
	 * @see AbWheelAdapter#getMaximumLength()
	 * @author: amsoft.cn
	 * @date：2013-6-17 上午9:04:49
	 */
	@Override
	public int getMaximumLength() {
		if(length!=-1){
			return length;
		}
		int maxLength = 0;
		for(int i=0;i<items.size();i++){
			String cur = items.get(i).getName();
			int l = AbStrUtil.strLength(cur);
			if(maxLength<l){
				maxLength = l;
			}
		}
		return maxLength;
	}

}
