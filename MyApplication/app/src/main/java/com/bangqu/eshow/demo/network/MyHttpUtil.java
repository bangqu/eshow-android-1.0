package com.bangqu.eshow.demo.network;

import android.content.Context;

import com.bangqu.eshow.demo.common.Global;
import com.bangqu.eshow.http.ESRequestParams;

/**
 * 网络请求公共模块
 * Created by daikting on 15/9/28.
 */
public class MyHttpUtil {
    private com.bangqu.eshow.http.ESHttpUtil abHttpUtil;

    public MyHttpUtil(Context context) {
        abHttpUtil = com.bangqu.eshow.http.ESHttpUtil.getInstance(context);


    }

    public void post(String interfaceName, ESRequestParams parameters, ESResponseListener responseListener) {
        parameters.put("from", "android");
        String url = Global.SERVER_URL + interfaceName + ".json";
        abHttpUtil.post(url, parameters, responseListener);
    }

}
