package cn.org.eshow.network;

import android.content.Context;

import cn.org.eshow_structure.http.ESStringHttpResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 自定义网络请求回调接口
 * Created by daikting on 15/9/29.
 */
public abstract class AMapResponseListener extends ESStringHttpResponseListener {
    private Context mContext;

    public AMapResponseListener(Context context) {
        super();
        mContext = context;
    }

    @Override
    public void onSuccess(final int statusCode, String content) {

        try {
            final JSONObject jsonObject = new JSONObject(content);
            final int status = jsonObject.getInt("status");
            if(status == 1){
                int count = jsonObject.getInt("count");
                if(count > 0){
                    onAMapSucess(jsonObject);
                }else{
                    onAMapNoData();
                }
            }else{
                onAMapNotify("获取失败，错误码：" + jsonObject.getInt("infocode"));
            }
        } catch (JSONException e) {
            onAMapNotify("接口数据出错JSONException");
            e.printStackTrace();
        }
    }

    /**
     * 接口请求成功
     *
     * @param resultJson
     */
    public abstract void onAMapSucess(JSONObject resultJson);

    /**
     * 没有请求到数据
     */
    public abstract void onAMapNoData();

    /**
     * 网络请求成功，由于请求数据不对，接口数据请求失败
     *
     */
    public abstract void onAMapNotify(String notify);

    OnErrorListener onErrorListener;

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    public interface OnErrorListener {
        void onErrorListener(String bgMsg);
    }

}
