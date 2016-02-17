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

package com.bangqu.eshow.view.wheel;

// TODO: Auto-generated Javadoc

/**
 * 轮子适配器（对象）
 *
 */
public class ESObjectWheelAdapter<T> implements ESWheelAdapter {
	
	/** The default items length. */
	public static final int DEFAULT_LENGTH = -1;
	
	// items
	/** The items. */
	private T items[];
	// length
	/** The length. */
	private int length;

	/**
	 * Constructor.
	 *
	 * @param items the items
	 * @param length the max items length
	 */
	public ESObjectWheelAdapter(T items[], int length) {
		this.items = items;
		this.length = length;
	}
	
	/**
	 * Contructor.
	 *
	 * @param items the items
	 */
	public ESObjectWheelAdapter(T items[]) {
		this(items, DEFAULT_LENGTH);
	}

	/**
	 * 描述：TODO.
	 *
	 * @version v1.0
	 * @param index the index
	 * @return the item
	 * @see ESWheelAdapter#getItem(int)
	 * @author: amsoft.cn
	 * @date：2013-6-17 上午9:04:48
	 */
	@Override
	public String getItem(int index) {
		if (index >= 0 && index < items.length) {
			return items[index].toString();
		}
		return null;
	}

	/**
	 * 描述：TODO.
	 *
	 * @version v1.0
	 * @return the items count
	 * @see ESWheelAdapter#getItemsCount()
	 * @author: amsoft.cn
	 * @date：2013-6-17 上午9:04:48
	 */
	@Override
	public int getItemsCount() {
		return items.length;
	}

	/**
	 * 描述：TODO.
	 *
	 * @version v1.0
	 * @return the maximum length
	 * @see ESWheelAdapter#getMaximumLength()
	 * @author: amsoft.cn
	 * @date：2013-6-17 上午9:04:48
	 */
	@Override
	public int getMaximumLength() {
		return length;
	}

}
