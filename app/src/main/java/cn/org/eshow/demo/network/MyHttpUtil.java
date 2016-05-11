package cn.org.eshow.demo.network;

import android.content.Context;

import cn.org.eshow.demo.common.Global;
import cn.org.eshow.framwork.http.AbHttpUtil;
import cn.org.eshow.framwork.http.AbRequestParams;

/**
 * 网络请求公共模块
 * Created by daikting on 15/9/28.
 */
public class MyHttpUtil {
    private AbHttpUtil abHttpUtil;

    public MyHttpUtil(Context context) {
        abHttpUtil = AbHttpUtil.getInstance(context);


    }

    public void post(String interfaceName, AbRequestParams parameters, ESResponseListener responseListener) {
        parameters.put("from", "android");
        String url = Global.SERVER_URL + interfaceName + ".json";
        abHttpUtil.post(url, parameters, responseListener);
    }

}
