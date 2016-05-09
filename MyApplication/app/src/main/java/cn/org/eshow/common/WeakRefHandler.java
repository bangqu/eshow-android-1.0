package cn.org.eshow.common;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * 使用弱引用的handler，防止出现内存泄露
 * Created by daikting on 16/2/19.
 */
public class WeakRefHandler extends Handler {
    private final WeakReference<Callback> mRef;
    private final int mLoopTime;
    private int NO_LOOP = -1;

    public WeakRefHandler(Callback loopAction, int loopTime) {
        super();
        mRef = new WeakReference<>(loopAction);
        mLoopTime = loopTime;
    }

    // 不循环
    public WeakRefHandler(Callback loopAction) {
        super();
        mRef = new WeakReference<>(loopAction);
        mLoopTime = NO_LOOP;
    }

    @Override
    public void handleMessage(Message msg) {
        Log.d("", "loophandle");
        Callback action = mRef.get();
        if (action != null) {
            action.handleMessage(msg);
            if (mLoopTime != NO_LOOP) {
                sendEmptyMessageDelayed(0, mLoopTime);
            }
        }
    }

    public void start() {
        removeMessages(0);
        sendEmptyMessageDelayed(0, 0);
    }

    public void start(int what, long delay) {
        removeMessages(0);
        sendEmptyMessageDelayed(what, delay);
    }

    public void stop() {
        removeMessages(0);
    }

    public void clear() {
        removeMessages(0);
        mRef.clear();
    }
}
