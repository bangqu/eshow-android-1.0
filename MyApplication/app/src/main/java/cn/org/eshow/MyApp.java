package cn.org.eshow;

import android.app.ActivityManager;
import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.tencent.android.tpush.XGNotifaction;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.XGPushNotifactionCallback;
import com.umeng.socialize.PlatformConfig;

import java.util.List;

import cn.org.eshow_structure.global.ESAppConfig;
import cn.org.eshow_structure.util.ESFontsOverride;

/**
 * Created by daikting on 16/2/19.
 */
public class MyApp extends MultiDexApplication {
    private static final String tag="TPush_App";
    public static float sScale;

    private boolean isMainProcess() {
        Log.i(tag, "isMainProcess");
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 在主进程设置信鸽相关的内容
        if (isMainProcess()) {
            Log.i(tag,"在主进程设置信鸽相关的内容");
            // 为保证弹出通知前一定调用本方法，需要在application的onCreate注册
            // 收到通知时，会调用本回调函数。
            // 相当于这个回调会拦截在信鸽的弹出通知之前被截取
            // 一般上针对需要获取通知内容、标题，设置通知点击的跳转逻辑等等
            XGPushManager.setNotifactionCallback(

                    new XGPushNotifactionCallback() {

                        @Override
                        public void handleNotify(XGNotifaction xGNotifaction) {
                            Log.i(tag, "处理信鸽通知：" + xGNotifaction);
                            // 获取标签、内容、自定义内容
                            String title = xGNotifaction.getTitle();
                            String content = xGNotifaction.getContent();
                            String customContent = xGNotifaction.getCustomContent();
                            // 其它的处理
                            // 如果还要弹出通知，可直接调用以下代码或自己创建Notifaction，否则，本通知将不会弹出在通知栏中。
                            xGNotifaction.doNotify();
                        }
                    }
            );
        }

        //通过反射机制替换系统Monospace字体
        ESFontsOverride.setDefaultFont(this, "MONOSPACE", "yahei.ttf");

        ESAppConfig.UI_WIDTH = 750;
        ESAppConfig.UI_HEIGHT = 1334;

        sScale = getResources().getDisplayMetrics().density;
        //微信 appid appsecret
        PlatformConfig.setWeixin("wx747d053fa471eb15", "95072c38637c0dc19256cc968165ff6a");
        //新浪微博 appkey appsecret
        PlatformConfig.setSinaWeibo("", "");
        // QQ和Qzone appid appkey
        PlatformConfig.setQQZone("1105134763", "rexsau2NHZYARCJw");
    }
}
