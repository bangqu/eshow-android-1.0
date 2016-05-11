package cn.org.eshow.demo.network;

import android.content.Context;

import cn.org.eshow.framwork.http.AbHttpUtil;
import cn.org.eshow.framwork.http.AbRequestParams;

/**
 * 高德地图网络请求公共模块
 * Created by daikting on 15/9/28.
 */
public class AMapHttpUtil {
    private AbHttpUtil abHttpUtil;

    public AMapHttpUtil(Context context) {
        abHttpUtil = AbHttpUtil.getInstance(context);


    }

    public void post(String interfaceName, AbRequestParams parameters, AMapResponseListener responseListener) {
        String url = "http://restapi.amap.com/" + interfaceName;
        abHttpUtil.get(url, parameters, responseListener);
    }

}
