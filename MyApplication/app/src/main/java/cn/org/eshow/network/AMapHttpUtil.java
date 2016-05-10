package cn.org.eshow.network;

import android.content.Context;

import cn.org.eshow_structure.http.ESHttpUtil;
import cn.org.eshow_structure.http.ESRequestParams;

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
