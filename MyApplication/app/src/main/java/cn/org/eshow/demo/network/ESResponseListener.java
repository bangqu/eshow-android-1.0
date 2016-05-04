package cn.org.eshow.demo.network;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import cn.org.eshow.demo.common.Global;
import cn.org.eshow.http.ESStringHttpResponseListener;
import cn.org.eshow.util.ESToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 自定义网络请求回调接口
 * Created by daikting on 15/9/29.
 */
public abstract class ESResponseListener extends ESStringHttpResponseListener {
    private Context mContext;

    public ESResponseListener(Context context) {
        super();
        mContext = context;
    }

    @Override
    public void onSuccess(final int statusCode, String content) {

        try {
            final JSONObject jsonObject = new JSONObject(content);
            final String bqMsg = jsonObject.getString("msg");

            final int bqStatus = jsonObject.getInt("status");
            new SwitchNetworkType(bqStatus) {
                @Override
                public void onSucess() {
                    onBQSucess(bqMsg, jsonObject);
                }

                @Override
                public void onNotify() {
                    onBQNotify(bqMsg);
                }

                @Override
                public void onNOData() {
                    onBQNoData();
                }

                @Override
                public void onErrorParame() {
                    if (onErrorListener != null) {
                        onErrorListener.onErrorListener(bqMsg);
                    } else {
                        ESToastUtil.showToast(mContext, bqMsg);
                    }
                }

                @Override
                public void onFailedRequest() {
                    if (onErrorListener != null) {
                        onErrorListener.onErrorListener(bqMsg);
                    } else {
                        ESToastUtil.showToast(mContext, bqMsg);
                    }
                }

                @Override
                public void onPermissionDenied() {
                    if (onErrorListener != null) {
                        onErrorListener.onErrorListener(bqMsg);
                    } else {
                        ESToastUtil.showToast(mContext, bqMsg);
                    }
                }

                @Override
                public void onMissingToken() {
                    String action = Global.EShow_Broadcast_Action.ACTION_MISSING_TOKEN;
                    Intent brocast = new Intent(action);
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(brocast);
                }

                @Override
                public void onPayFail() {
                    onBQPayFail();
                }
            };

        } catch (JSONException e) {
            onBQNotify("接口数据出错JSONException");
            e.printStackTrace();
        }
    }

    /**
     * 接口请求成功
     *
     * @param resultJson
     */
    public abstract void onBQSucess(String esMsg, JSONObject resultJson);

    /**
     * 没有请求到数据
     */
    public abstract void onBQNoData();

    /**
     * 网络请求成功，由于请求数据不对，接口数据请求失败
     *
     * @param bqMsg
     */
    public abstract void onBQNotify(String bqMsg);

    OnErrorListener onErrorListener;

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    public interface OnErrorListener {
        void onErrorListener(String bgMsg);
    }


    /**
     * 支付失败处理
     */
    public void onBQPayFail() {

    }

}
