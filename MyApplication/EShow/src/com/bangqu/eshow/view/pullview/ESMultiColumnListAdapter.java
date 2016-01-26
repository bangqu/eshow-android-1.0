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
package com.bangqu.eshow.view.pullview;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.ViewGroup;




// TODO: Auto-generated Javadoc
/**
 * AbViewInfo的基础类
 *
 */
public abstract class ESMultiColumnListAdapter {
	
	/** The m data set observable. */
	private final DataSetObservable mDataSetObservable = new DataSetObservable();
    
    /**
     * Gets the count.
     *
     * @return the count
     */
    public abstract int getCount();
    
    /**
     * Gets the item.
     *
     * @param position the position
     * @return the item
     */
    public abstract Object getItem(int position);

    /**
     * Gets the item id.
     *
     * @param position the position
     * @return the item id
     */
    public abstract long getItemId(int position);
    
    /**
     * Gets the view.
     *
     * @param position the position
     * @param convertView the convert view
     * @param parent the parent
     * @return the view
     */
    public abstract ESViewInfo getView(int position, ESViewInfo convertView, ViewGroup parent);
    
    /**
     * Register data set observer.
     *
     * @param observer the observer
     */
    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    /**
     * Unregister data set observer.
     *
     * @param observer the observer
     */
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }
    
    /**
     * Notify data set changed.
     */
    public void notifyDataSetChanged(){
    	mDataSetObservable.notifyChanged();
    };    
}
