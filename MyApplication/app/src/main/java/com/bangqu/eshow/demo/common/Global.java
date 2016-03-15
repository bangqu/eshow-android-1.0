package com.bangqu.eshow.demo.common;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.bangqu.eshow.demo.MyApp;
import com.bangqu.eshow.demo.R;

import java.text.SimpleDateFormat;

/**
 * 放一些公共的全局方法
 * Created by daikting on 16/2/19.
 */
public class Global {
    /**
     * 请求服务器的Url地址
     */
    public static final String SERVER_URL = "http://api.eshow.org.cn/";
    /**
     * 短信验证码发送周期60s
     */
    public static final long SEND_CODE_CYCLE = 60 * 1000;

    public static final SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd EEE");
    public static final SimpleDateFormat mDateYMDHH = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final SimpleDateFormat sFormatToday = new SimpleDateFormat("今天 HH:mm");
    private static final SimpleDateFormat sFormatThisYear = new SimpleDateFormat("MM/dd HH:mm");
    private static final SimpleDateFormat sFormatOtherYear = new SimpleDateFormat("yy/MM/dd HH:mm");
    private static final SimpleDateFormat sFormatMessageToday = new SimpleDateFormat("今天");
    private static final SimpleDateFormat sFormatMessageThisYear = new SimpleDateFormat("MM/dd");
    private static final SimpleDateFormat sFormatMessageOtherYear = new SimpleDateFormat("yy/MM/dd");

    public static int dpToPx(int dpValue) {
        return (int) (dpValue * MyApp.sScale + 0.5f);
    }

    public static int dpToPx(double dpValue) {
        return (int) (dpValue * MyApp.sScale + 0.5f);
    }

    public static int pxToDp(float pxValue) {
        return (int) (pxValue / MyApp.sScale + 0.5f);
    }

    public static String makeSmallUrl(ImageView view, String url) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        int max = Math.max(lp.height, lp.width);
        if (max > 0) {
            return makeSmallUrlSquare(url, max);
        }

        return url;
    }

    public static String makeSmallUrlSquare(String url, int widthPix) {
        if (canCrop(url)) {
            String parma = "";
            if (!url.contains("?imageMogr2/")) {
                parma = "?imageMogr2/";
            }
            return String.format("%s%s/!%dx%d", url, parma, widthPix, widthPix);
        } else {
            return url;
        }
    }
    public static boolean canCrop(String url) {
        return url.startsWith("http") && (!url.contains("/thumbnail/"));
    }

    /**
     * 网络请求过程中加载等待时的进度滚轮资源id
     */
    public static final int LOADING_PROGRESSBAR_ID = R.drawable.icon_progressbar;

    public static class EShow_Broadcast_Action {
        /**
         * 退出程序
         */
        public static final String ACTION_EXIT = "bangqu.exit";
        /**
        /**
         * 登录信息过期
         */
        public static final String ACTION_MISSING_TOKEN = "bangqu.missing.token";

        /**
         *
         * 由于密码修改，需要退出
         * */
        public static final String ACTION_EXIT_REPASSWORD = "bangqu.exit.repassword";
    }

}
