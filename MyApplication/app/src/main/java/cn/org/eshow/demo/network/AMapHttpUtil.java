package cn.org.eshow.demo.network;

import android.content.Context;

import cn.org.eshow.http.ESHttpUtil;
import cn.org.eshow.http.ESRequestParams;

/**
 * 高德地图网络请求公共模块
 * Created by daikting on 15/9/28.
 */
public class AMapHttpUtil {
    private ESHttpUtil abHttpUtil;

    public AMapHttpUtil(Context context) {
        abHttpUtil = ESHttpUtil.getInstance(context);


    }

    public void post(String interfaceName, ESRequestParams parameters, AMapResponseListener responseListener) {
        String url = "http://restapi.amap.com/" + interfaceName;
        abHttpUtil.get(url, parameters, responseListener);
    }

}
