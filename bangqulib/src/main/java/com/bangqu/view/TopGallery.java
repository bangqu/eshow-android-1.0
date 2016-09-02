package com.bangqu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

public class TopGallery extends Gallery implements OnGestureListener {




    /**
     * @param context
     */
    public TopGallery(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public TopGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
    
    private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {  
        return e2.getX() > e1.getX();  
    }  
  
    @Override  
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {  
        // e1是按下的事件，e2是抬起的事件  
        int keyCode;   
        if (isScrollingLeft(e1, e2)) {  
            keyCode = KeyEvent.KEYCODE_DPAD_LEFT;  
        } else {  
            keyCode = KeyEvent.KEYCODE_DPAD_RIGHT;  
        }  
      
        onKeyDown(keyCode, null);   
        return true;  
    }  

}