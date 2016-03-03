package com.bangqu.eshow.demo.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.bangqu.eshow.demo.R;
import com.bangqu.eshow.demo.common.CommonActivity;
import com.bangqu.eshow.demo.common.Global;
import com.bangqu.eshow.fragment.ESProgressDialogFragment;
import com.bangqu.eshow.util.ESDialogUtil;
import com.bangqu.eshow.util.ESToastUtil;
import com.umeng.socialize.Config;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Map;

/**
 * 登录页面
 * Created by daikting on 16/2/24.
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends CommonActivity {
    private Context mContext = LoginActivity.this;
    @ViewById(R.id.rlBack)
    RelativeLayout mRlMenu;
    @ViewById(R.id.material_back_button)
    MaterialMenuView mMaterialBackButton;
    @ViewById(R.id.tvTitle)
    TextView mTvTitle;

//    @ViewById(R.id.etTel)
//    LoginAutoCompleteEdit mEtTel;
//    @ViewById(R.id.etPassword)
//    LoginAutoCompleteEdit mEtPassword;
    @ViewById(R.id.btnLogin)
    Button mBtnLogin;
    @ViewById(R.id.tvForgetPW)
    TextView mTvForgetPW;
    @ViewById(R.id.llQQLogin)
    LinearLayout mLlQQLogin;
    @ViewById(R.id.llWechatLogin)
    LinearLayout mLlWechatLogin;
    //友盟分享
    UMShareAPI umShareAPI;

    ESProgressDialogFragment progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void init() {
        mTvTitle.setText(getTitle());
        mMaterialBackButton.setState(MaterialMenuDrawable.IconState.ARROW);
        mMaterialBackButton.setVisibility(View.GONE);
    }
    @Click(R.id.btnLogin)
    void onLogin(){
        MainActivity_.intent(mContext).start();
    }
    @Click(R.id.tvForgetPW)
    void onForgetPW(){
//        InputTelActivity_.intent(mContext).extra(InputTelActivity.INTENT_ISREGISTER,false).start();
        progressDialog = ESDialogUtil.showProgressDialog(mContext, Global.LOADING_PROGRESSBAR_ID,"正在请求登录中...");
    }

    /**
     * 微信授权登录
     */
    @Click(R.id.llWechatLogin)
    void onWechatLogin(){
        Config.dialog = ProgressDialog.show(mContext,"提示","正在请求跳转....");

        SHARE_MEDIA platform = SHARE_MEDIA.WEIXIN;
        umShareAPI = UMShareAPI.get(mContext);
        umShareAPI.doOauthVerify(this, platform, umAuthListener);
    }

    /**
     * QQ授权登录
     */
    @Click(R.id.llQQLogin)
    void onQQLogin(){
        Config.dialog = ProgressDialog.show(mContext,"提示","正在请求跳转....");
        SHARE_MEDIA platform = SHARE_MEDIA.QQ;
        UMShareAPI umShareAPI = UMShareAPI.get(mContext);
        umShareAPI.doOauthVerify(this, platform, umAuthListener);

    }

    /**
     * 授权回调监听
     */
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            ESToastUtil.showToast(mContext,  platform.name()+"Authorize succeed");

        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            ESToastUtil.showToast(mContext,  platform.name()+"Authorize fail");

        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            ESToastUtil.showToast(mContext, platform.name()+"Authorize cancel");
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(umShareAPI != null){
            umShareAPI.onActivityResult(requestCode, resultCode, data);
        }else{
            //应用未审核
        }
    }
}
