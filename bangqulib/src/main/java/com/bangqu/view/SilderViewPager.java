package com.bangqu.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 唯图 on 2016/7/28.
 */
public class SilderViewPager extends ViewPager {
    public SilderViewPager(Context context) {
        super(context);
    }

    public SilderViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try{
            return super.onTouchEvent(ev);
        } catch(IllegalArgumentException ex) {
        }
        return true;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        try{
            return super.onInterceptTouchEvent(ev);
        } catch(IllegalArgumentException ex) {
        }
        return true;
    }
}
