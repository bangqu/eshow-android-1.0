package cn.org.eshow.network;

import android.content.Context;

import cn.org.eshow.common.Global;
import cn.org.eshow_structure.http.ESHttpUtil;
import cn.org.eshow_structure.http.ESRequestParams;

/**
 * 网络请求公共模块
 * Created by daikting on 15/9/28.
 */
public class MyHttpUtil {
    private ESHttpUtil abHttpUtil;

    public MyHttpUtil(Context context) {
        abHttpUtil = ESHttpUtil.getInstance(context);


    }

    public void post(String interfaceName, ESRequestParams parameters, ESResponseListener responseListener) {
        parameters.put("from", "android");
        String url = Global.SERVER_URL + interfaceName + ".json";
        abHttpUtil.post(url, parameters, responseListener);
    }

}
