package com.bangqu.eshow.demo.network;

/**
 * 网络请求返回的状态
 * Created by daikting on 15/11/9.
 */
public abstract class SwitchNetworkType {
    public SwitchNetworkType(int state){
        switch (state){
            case 1://获取成功
                onSucess();
                break;
            case 0://暂无数据
                onNOData();
                break;
            case -1://缺少参数
                onErrorParame();
                break;
            case -2://非法请求
                onFailedRequest();
                break;
            case -3://余额不足
                onPayFail();
                break;
            case -4://权限不足
                onPermissionDenied();
                break;
            case -5://请求失败，提示用户，提示内容为接口返回
                onNotify();
                break;
            case -9://账户信息过期
                onMissingToken();
                break;

        }

    }
    public abstract void onSucess();
    public abstract void onNotify();

    public abstract void onNOData();

    public abstract void onErrorParame();

    public abstract void onFailedRequest();

    public abstract void onPermissionDenied();

    public abstract void onMissingToken();

    /**
     * 订单支付的时候调用
     */
    public abstract void onPayFail();
}
