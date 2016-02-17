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
package com.bangqu.eshow.view.sample;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

// TODO: Auto-generated Javadoc
/**
 * 有滚动事件监听的HorizontalScrollView
 *
 */
public class ESHorizontalScrollView extends HorizontalScrollView {

	/** The init position. */
	private int initPosition;
	
	/** The child width. */
	private int childWidth = 0;
	
	/** The on scroll listner. */
	private AbOnScrollListener onScrollListner;
	
	/**
	 * Instantiates a new ab horizontal scroll view.
	 *
	 * @param context the context
	 */
	public ESHorizontalScrollView(Context context) {
		this(context,null);
	}
	
	/**
	 * Instantiates a new ab horizontal scroll view.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public ESHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/* (non-Javadoc)
	 * @see android.view.View#onScrollChanged(int, int, int, int)
	 */
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		
		int newPosition = getScrollX();
		if (initPosition - newPosition == 0) {
			if (onScrollListner == null) {
				return;
			}
			onScrollListner.onScrollStoped();
			new Handler().postDelayed(new Runnable(){
				@Override
				public void run(){
					Rect outRect = new Rect();
					getDrawingRect(outRect);
					if (getScrollX() == 0) {
						onScrollListner.onScroll(0);
						onScrollListner.onScrollToLeft();
					} else if (childWidth + getPaddingLeft() + getPaddingRight() == outRect.right) {
						onScrollListner.onScroll(getScrollX());
						onScrollListner.onScrollToRight();
					} else {
						onScrollListner.onScroll(getScrollX());
					}
				}
			},200);
			
		} else {
			initPosition = getScrollX();
			checkTotalWidth();
		}
		super.onScrollChanged(l, t, oldl, oldt);
	}

	
	/**
	 * 描述：设置监听器.
	 *
	 * @param listner the new on scroll listener
	 */
	public void setOnScrollListener(AbOnScrollListener listner) {
		onScrollListner = listner;
	}
	
	/**
	 * 计算总宽.
	 */
	private void checkTotalWidth() {
		if (childWidth > 0) {
			return;
		}
		for (int i = 0; i < getChildCount(); i++) {
			childWidth += getChildAt(i).getWidth();
		}
	}
	
	/**
	 * 滚动.
	 *
	 * @see AbOnScrollEvent
	 */
    public interface AbOnScrollListener {
        
        /**
         * 滚动.
         * @param arg1 返回参数
         */
        public void onScroll(int arg1); 
        
        /**
         * 滚动停止.
         */
        public void onScrollStoped();

        /**
         * 滚到了最左边.
         */
        public void onScrollToLeft();

        /**
         * 滚到了最右边.
         */
        public void onScrollToRight();

    }
}
