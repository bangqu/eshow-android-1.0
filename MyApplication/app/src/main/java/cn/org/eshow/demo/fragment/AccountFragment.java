package cn.org.eshow.demo.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.org.eshow.demo.R;
import cn.org.eshow.demo.bean.Enum_ThirdType;
import cn.org.eshow.demo.common.Global;
import cn.org.eshow.demo.network.ESResponseListener;
import cn.org.eshow.demo.network.NetworkInterface;
import cn.org.eshow.demo.view.ConfirmDialog;
import cn.org.eshow.fragment.ESProgressDialogFragment;
import cn.org.eshow.util.ESDialogUtil;
import cn.org.eshow.util.ESToastUtil;
import cn.org.eshow.util.ESViewUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 账号绑定修改分页
 * Created by daikting on 16/2/19.
 */
public class AccountFragment extends Fragment implements View.OnClickListener {
    private Context mContext;
    RelativeLayout rlWechat;
    TextView tvWechatState;
    RelativeLayout rlQQ;
    TextView tvQQState;

    ImageView ivQQProgress;
    ImageView ivWechatProgress;

    ESProgressDialogFragment progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = this.getActivity();
        View view = inflater.inflate(R.layout.fragment_account, null);
        ESViewUtil.scaleContentView((LinearLayout) view.findViewById(R.id.llParent));
        rlWechat = (RelativeLayout) view.findViewById(R.id.rlWechat);
        rlWechat.setOnClickListener(this);
        tvWechatState = (TextView) view.findViewById(R.id.tvWechatState);
        tvWechatState.setVisibility(View.GONE);
        ivWechatProgress = (ImageView) view.findViewById(R.id.ivWechatProgress);
        ivWechatProgress.setVisibility(View.VISIBLE);
        rlQQ = (RelativeLayout) view.findViewById(R.id.rlQQ);
        rlQQ.setOnClickListener(this);
        tvQQState = (TextView) view.findViewById(R.id.tvQQState);
        tvQQState.setVisibility(View.GONE);
        ivQQProgress = (ImageView) view.findViewById(R.id.ivQQProgress);
        ivQQProgress.setVisibility(View.VISIBLE);
        NetworkInterface.checkThirdBound(mContext, checkThirdResListener);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlWechat:
                try {
                    boolean isWechatBounded = (boolean) tvWechatState.getTag();
                    if (isWechatBounded) {
                        ConfirmDialog confirmDialog = new ConfirmDialog(mContext, "提示", "确定取消绑定？", "确定", "取消", onCancelWechatDialogListener);
                        confirmDialog.show();
                    } else {
                        Intent wechatAuthorize = new Intent();
                        wechatAuthorize.setAction(Global.EShow_Broadcast_Action.ACTION_WECHAT_AUTHORIZE);
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(wechatAuthorize);
                    }
                } catch (Exception e) {//未获取到状态，重新加载
                    e.printStackTrace();
                    NetworkInterface.checkThirdBound(mContext, checkThirdResListener);
                }
                break;
            case R.id.rlQQ:
                try {
                    boolean isQQBounded = (boolean) tvQQState.getTag();
                    if (isQQBounded) {
                        ConfirmDialog confirmDialog = new ConfirmDialog(mContext, "提示", "确定取消绑定？", "确定", "取消", onCancelQQDialogListener);
                        confirmDialog.show();
                    } else {
                        Intent qqAuthorize = new Intent();
                        qqAuthorize.setAction(Global.EShow_Broadcast_Action.ACTION_QQ_AUTHORIZE);
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(qqAuthorize);
                    }
                } catch (Exception e) {//未获取到状态，重新加载
                    e.printStackTrace();
                    NetworkInterface.checkThirdBound(mContext, checkThirdResListener);
                }
                break;
        }
    }

    /**
     * 检测第三方登录是否绑定接口回调
     */
    ESResponseListener checkThirdResListener = new ESResponseListener(mContext) {
        @Override
        public void onBQSucess(String esMsg, JSONObject resultJson) {
            try {
                JSONObject thirdParty = resultJson.getJSONObject("thirdPartyResponse");
                boolean isQQBound = thirdParty.getBoolean("qq");
                setQQStateUI(isQQBound);
                boolean isWechatBound = thirdParty.getBoolean("weixin");
                setWechatStateUI(isWechatBound);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onBQNoData() {

        }

        @Override
        public void onBQNotify(String bqMsg) {
            ESToastUtil.showToast(mContext, bqMsg);
        }

        @Override
        public void onStart() {

        }

        @Override
        public void onFinish() {
            tvQQState.setVisibility(View.VISIBLE);
            tvWechatState.setVisibility(View.VISIBLE);
            ivQQProgress.setVisibility(View.GONE);
            ivWechatProgress.setVisibility(View.GONE);
        }

        @Override
        public void onFailure(int statusCode, String content, Throwable error) {
            tvQQState.setVisibility(View.VISIBLE);
            tvWechatState.setVisibility(View.VISIBLE);
            ivQQProgress.setVisibility(View.GONE);
            ivWechatProgress.setVisibility(View.GONE);
            ESToastUtil.showToast(mContext, "请求失败，错误码：" + statusCode);
        }
    };

    ConfirmDialog.OnCustomDialogListener onCancelQQDialogListener = new ConfirmDialog.OnCustomDialogListener() {
        @Override
        public void OnCustomDialogConfim(String str) {
            Enum_ThirdType thirdType = Enum_ThirdType.QQ;
            NetworkInterface.clearnThirdBound(mContext, thirdType, new ESResponseListener(mContext) {
                @Override
                public void onBQSucess(String esMsg, JSONObject resultJson) {
                    setQQStateUI(false);
                }

                @Override
                public void onBQNoData() {

                }

                @Override
                public void onBQNotify(String bqMsg) {
                    ESToastUtil.showToast(mContext, bqMsg);
                }

                @Override
                public void onStart() {
                    progressDialog = ESDialogUtil.showProgressDialog(mContext, Global.LOADING_PROGRESSBAR_ID, "取消绑定中...");
                    progressDialog.setCancelable(false);

                }

                @Override
                public void onFinish() {
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(int statusCode, String content, Throwable error) {
                    progressDialog.dismiss();
                    ESToastUtil.showToast(mContext, "请求失败，错误码：" + statusCode);
                }
            });
        }

        @Override
        public void OnCustomDialogCancel(String str) {

        }
    };

    ConfirmDialog.OnCustomDialogListener onCancelWechatDialogListener = new ConfirmDialog.OnCustomDialogListener() {
        @Override
        public void OnCustomDialogConfim(String str) {
            Enum_ThirdType thirdType = Enum_ThirdType.WeChat;
            NetworkInterface.clearnThirdBound(mContext, thirdType, new ESResponseListener(mContext) {
                @Override
                public void onBQSucess(String esMsg, JSONObject resultJson) {
                    setWechatStateUI(false);
                }

                @Override
                public void onBQNoData() {

                }

                @Override
                public void onBQNotify(String bqMsg) {
                    ESToastUtil.showToast(mContext, bqMsg);
                }

                @Override
                public void onStart() {
                    progressDialog = ESDialogUtil.showProgressDialog(mContext, Global.LOADING_PROGRESSBAR_ID, "取消绑定中...");
                    progressDialog.setCancelable(false);

                }

                @Override
                public void onFinish() {
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(int statusCode, String content, Throwable error) {
                    progressDialog.dismiss();
                    ESToastUtil.showToast(mContext, "请求失败，错误码：" + statusCode);
                }
            });
        }

        @Override
        public void OnCustomDialogCancel(String str) {

        }
    };

    public void setQQStateUI(boolean isBounded) {
        if (isBounded) {
            tvQQState.setText("已绑定");
            tvQQState.setTag(true);
        } else {
            tvQQState.setText("未绑定");
            tvQQState.setTag(false);

        }
    }

    public void setWechatStateUI(boolean isBounded) {
        if (isBounded) {
            tvWechatState.setText("已绑定");
            tvWechatState.setTag(true);
        } else {
            tvWechatState.setText("未绑定");
            tvWechatState.setTag(false);

        }
    }
}
